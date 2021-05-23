package com.cllasify.cllasify;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cllasify.cllasify.Adaptor.Adaptor_JoinGroupReq;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMember;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Fragment.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class AttendanceFragment extends Fragment {


    List<Class_Group> listGrpMemberList,mDatalistNew;
    DatabaseReference refGrpMemberList,refAttendance;
    RecyclerView rv_GrpMemberList;
    Adaptor_ShowGrpMember showGrpMemberList;
    Class_Group userAddGroupClass;
    Button btn_ShowAttendStatus;
    ArrayList<HashMap<String, String>> arrayList=new ArrayList<HashMap<String, String>>();

    Adaptor_JoinGroupReq showSubChild_Adaptor;
    List<Class_Group> list_SubChild;
    DatabaseReference refChildGroup;
    ListAdapter adapter;

    Paint p = new Paint();
    TextView tv_groupName,tv_subGroupName;


    Calendar calenderCC = Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());

    Date c = Calendar.getInstance().getTime();
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    String formattedDate = df.format(c);
    Button btn_date;
    Calendar myCalendar;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String currUserId,groupPushId,subGroupPushId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        currUserId = currentUser.getUid();

        btn_ShowAttendStatus=view.findViewById(R.id.btn_ShowAttendStatus);
        tv_groupName=view.findViewById(R.id.tv_groupName);
        tv_subGroupName=view.findViewById(R.id.tv_subGroupName);

       
        groupPushId = getArguments().getString("groupPushId");
        subGroupPushId = getArguments().getString("subGroupPushId");

        tv_groupName.setText(groupPushId);
        tv_subGroupName.setText(subGroupPushId);

        rv_GrpMemberList=view.findViewById(R.id.rv_GrpMemberAttendance);
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//                _birthYear = year;
//                _month = monthOfYear;
//                _day = dayOfMonth;
                dialog_AttendanceStatus();
            }

        };
        btn_ShowAttendStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

//                dialog_AttendanceStatus();
            }
        });



        show_GrpMemberList();
        initSwipe();
        return view;
    }


    private void dialog_AttendanceStatus() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
//        btn_date.setText(new StringBuilder()
//                // Month is 0 based so add 1
//                .append(_day).append("/").append(_month + 1).append("/").append(_birthYear).append(" "));
        btn_ShowAttendStatus.setText("Check Attendance Status");
        String btnData=sdf.format(this.myCalendar.getTime());
        Toast.makeText(getContext(), "check+"+btnData, Toast.LENGTH_SHORT).show();

        final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(getContext()).create();
        dialogBuilder.setCanceledOnTouchOutside(true);
        //dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_creategroup1, null);
        TextView tapCancel=dialogView.findViewById(R.id.tv_Cancel);
        TextView tapSubmit=dialogView.findViewById(R.id.tv_Submit);
        RecyclerView rv_JoinGroupReq=dialogView.findViewById(R.id.rv_JoinGroupReq);
        ListView lv_showStudents=dialogView.findViewById(R.id.lv_showStudents);
        rv_JoinGroupReq.setLayoutManager(new LinearLayoutManager(getContext()));

        list_SubChild = new ArrayList<>();
//            listGroupSTitle=new ArrayList<>();
//

        DatabaseReference refChildGroup1 = FirebaseDatabase.getInstance().getReference().child("Groups").child("Attendance").child(groupPushId).child(subGroupPushId);

        refChildGroup1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    if (snapshot.child(btnData).exists()){
                        rv_GrpMemberList.setVisibility(View.VISIBLE);
                    }else{
                        dialogBuilder.dismiss();
                        Toast.makeText(getContext(), "No Attendance taken on this date,\n Please select correct date", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "No Attendance taken yet, Please take attendance first", Toast.LENGTH_LONG).show();
//                    ll_SubChild.setVisibility(View.GONE);
                    dialogBuilder.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        refChildGroup = FirebaseDatabase.getInstance().getReference().child("Groups").child("Attendance").child(groupPushId).child(subGroupPushId).child(btnData);

        showSubChild_Adaptor = new Adaptor_JoinGroupReq(getContext(), list_SubChild);
        rv_JoinGroupReq.setAdapter(showSubChild_Adaptor);

//        ArrayList arrayList=new ArrayList<String>();

        HashMap<String, String> item = new HashMap<>();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

//                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                String key=dataSnapshot.getKey();
                String value=dataSnapshot.getValue().toString();
//                    Toast.makeText(getContext(), "key+value"+key+value, Toast.LENGTH_SHORT).show();
//

                item.put("key", key);
                item.put("value", value);


                arrayList.add(item);
//                }




//                Class_Group userQuestions = dataSnapshot.getValue(Class_Group.class);
//                list_SubChild.add(userQuestions);
//                showSubChild_Adaptor.notifyDataSetChanged();

            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        refChildGroup.addChildEventListener(childEventListener);
        adapter = new SimpleAdapter(getContext(),arrayList,R.layout.list_group_dashboard,
                new String[]{item.get("key"),item.get("value")},new int[]{R.id.tv_OtherMsz,R.id.tv_MyMsz});
        lv_showStudents.setAdapter(adapter);

        tapCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }

        });
        tapSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    private void show_GrpMemberList() {
        initSwipe();
        rv_GrpMemberList.setLayoutManager(new LinearLayoutManager(getContext()));
        listGrpMemberList = new ArrayList<>();
        showGrpMemberList = new Adaptor_ShowGrpMember(getContext(), listGrpMemberList);
        rv_GrpMemberList.setAdapter(showGrpMemberList);
        refGrpMemberList = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Universal_Group" ).child(groupPushId).child("User_Subscribed_Groups");

        refGrpMemberList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

//                String value=snapshot.getValue(String.class);
//                Toast.makeText(getContext(), "c"+value, Toast.LENGTH_SHORT).show();
////                arrayList.add(value);
////                arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList);
////                listView.setAdapter(arrayAdapter);

                Class_Group class_userDashBoard = snapshot.getValue(Class_Group.class);
                listGrpMemberList.add(class_userDashBoard);
                showGrpMemberList.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Class_Group Answers=listGrpMemberList.get(position);
                String userName=Answers.getUserName();
                String userID=Answers.getUserId();
                String push=Answers.getPosition();
                String groupName=Answers.getGroupName();

                refAttendance = FirebaseDatabase.getInstance().getReference().child("Groups").child("Attendance").child(groupPushId).child(subGroupPushId).child(formattedDate);
//                userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, currUserId,groupName, position);
//                refAttendance.child(push).setValue(userAddGroupClass);

                if (direction == ItemTouchHelper.LEFT){
//                    adapter.removeItem(position);
                    userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, currUserId,groupName, position);
                    refAttendance.child(userID).setValue("Absent");
                    Toast.makeText(getContext(), "Absent"+userName, Toast.LENGTH_SHORT).show();

                } else {
                    userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, push, currUserId,groupName, position);
                    refAttendance.child(userID).setValue("Present");
                    Toast.makeText(getContext(), "Present"+userName, Toast.LENGTH_SHORT).show();

//                    removeView();
//                    edit_position = position;
//                    alertDialog.setTitle("Edit Country");
//                    et_country.setText(countries.get(position));
//                    alertDialog.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

//                    viewHolder.

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);

                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rv_GrpMemberList);
    }

}
