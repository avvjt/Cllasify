package com.cllasify.cllasify.Server;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adaptor.Adaptor_Attendance;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMemberAttendanceRollNumberList;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class_Student_Details;
import com.cllasify.cllasify.Home.Attendance_History;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.SwipeToDeleteCallback;
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

public class Attendance_Activity extends AppCompatActivity {

    String remarks = null;
    List<Class_Student_Details> listGrpMemberList, mDatalistNew;
    List<Class_Group> list_showAttend;

    DatabaseReference refGrpMemberList, refAttendance;
    RecyclerView rv_GrpMemberList;
    Adaptor_ShowGrpMemberAttendanceRollNumberList showGrpMemberList;
    Adaptor_Attendance showAttendanceStatus;
    Class_Group userAddGroupClass;
    Button btn_ShowAttendStatus, btn_CheckAttendHistory;
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

    Paint p = new Paint();
    TextView tv_groupName, tv_subGroupName;


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
    String currUserId,groupPushId,subGroupPushId,classPushId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_activity);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        currUserId = currentUser.getUid();


        groupPushId = getIntent().getStringExtra("groupPushId");
        subGroupPushId = getIntent().getStringExtra("subGroupPushId");
        classPushId = getIntent().getStringExtra("classPushId");


        Toast.makeText(Attendance_Activity.this,groupPushId+"_"+subGroupPushId+"_"+classPushId,Toast.LENGTH_SHORT).show();
        btn_ShowAttendStatus=findViewById(R.id.btn_ShowAttendStatus);
        btn_CheckAttendHistory=findViewById(R.id.btn_CheckAttendHistory);

        tv_groupName=findViewById(R.id.tv_groupName);
        tv_subGroupName=findViewById(R.id.tv_subGroupName);

        rv_GrpMemberList=findViewById(R.id.rv_GrpMemberAttendance);


        tv_groupName.setText(groupPushId);
        tv_subGroupName.setText(subGroupPushId);

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String btnData = sdf.format(myCalendar.getTime());
                dialog_AttendanceStatus(btnData);
            }

        };

        btn_CheckAttendHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(Attendance_Activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        btn_ShowAttendStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                btn_ShowAttendStatus.setText("Check Attendance Status");
                String btnData=sdf.format(myCalendar.getTime());

                dialog_AttendanceStatus(btnData);
            }
        });


        show_GrpMemberList(groupPushId,subGroupPushId);
        initSwipe(groupPushId,subGroupPushId);
//        enableSwipeToDeleteAndUndo();
    }


    private void dialog_AttendanceStatus(String currentDate) {

        Intent intent = new Intent(Attendance_Activity.this, Attendance_History.class);
        intent.putExtra("currentDate",currentDate);
        intent.putExtra("groupPushId",groupPushId);
        intent.putExtra("subGroupPushId",subGroupPushId);
        intent.putExtra("classPushId",classPushId);

        startActivity(intent);

        /*
        btn_ShowAttendStatus.setText("Check Attendance Status");

        final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(this).create();
        dialogBuilder.setCanceledOnTouchOutside(true);
        //dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.dialog_attendancehistory, null);



        RecyclerView rv_ShowAttend=dialogView.findViewById(R.id.rv_ShowAttend);
        TextView tv_titleAttendance=dialogView.findViewById(R.id.tv_titleAttendance);
        rv_ShowAttend.setLayoutManager(new LinearLayoutManager(this));
        list_showAttend = new ArrayList<>();
        showAttendanceStatus = new Adaptor_Attendance(this, list_showAttend);
        rv_ShowAttend.setAdapter(showAttendanceStatus);


        tv_titleAttendance.setText("Attendance-"+currentDate+"\n");

        DatabaseReference refChildGroup1 = FirebaseDatabase.getInstance().getReference().child("Groups").child("Attendance").child(groupPushId).child(subGroupPushId).child(classPushId).child(currentDate);
        ChildEventListener childEventListenerAttend= new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Class_Group class_userDashBoard = snapshot.getValue(Class_Group.class);
                list_showAttend.add(class_userDashBoard);
                showAttendanceStatus.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                myArrayAdaptor.notifyDataSetChanged();
                showAttendanceStatus.notifyDataSetChanged();
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
        };
        refChildGroup1.orderByChild("userName").addChildEventListener(childEventListenerAttend);


        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
*/
    }

    private void show_GrpMemberList(String groupPushId,String subGroupPushId) {
        initSwipe(groupPushId, subGroupPushId);
//        enableSwipeToDeleteAndUndo();
        rv_GrpMemberList.setLayoutManager(new LinearLayoutManager(this));
        listGrpMemberList = new ArrayList<>();
        showGrpMemberList = new Adaptor_ShowGrpMemberAttendanceRollNumberList(this, listGrpMemberList);
        rv_GrpMemberList.setAdapter(showGrpMemberList);

        final String[] classPosition = new String[1];

        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(currUserId);

        refSaveCurrentData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    if (snapshot.child("clickedClassName").exists() && snapshot.child("clickedSubjectName").exists() && snapshot.child("clickedGroupPushId").exists()) {
                        classPosition[0] = snapshot.child("uniPushClassId").getValue().toString().trim();


                        refGrpMemberList = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_GRPs").child(groupPushId).child(classPosition[0]).child("classStudentList");

                        refGrpMemberList.orderByChild("rollNumber").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                Class_Student_Details class_student_details = snapshot.getValue(Class_Student_Details.class);
                                listGrpMemberList.add(class_student_details);

                                rv_GrpMemberList.setAdapter(showGrpMemberList);
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
                        listGrpMemberList.clear();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        refGrpMemberList.orderByChild("userName").addChildEventListener(childEventListener);
    }


    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Class_Student_Details item = showGrpMemberList.getData().get(position);

                showGrpMemberList.removeItem(position);

/*
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mAdapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
*/
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(rv_GrpMemberList);
    }


    private void initSwipe(String groupPushId, String subGroupPushId) {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

//                listGrpMemberList.get(position)

                Class_Student_Details Answers = listGrpMemberList.get(position);
                String userName = Answers.getUserName();
                String userID = Answers.getUserId();
                String push = String.valueOf(1);
                String groupName = "Expptt Grpp";

                refAttendance = FirebaseDatabase.getInstance().getReference().child("Groups").child("Attendance")
                        .child(groupPushId).child(subGroupPushId).child(classPushId).child(formattedDate);

                if (direction == ItemTouchHelper.LEFT) {
                    userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, currUserId, "Absent", subGroupPushId, push, groupName, groupPushId);
                    refAttendance.child(userID).setValue(userAddGroupClass);
                    Toast.makeText(Attendance_Activity.this, "Absent" + userName, Toast.LENGTH_SHORT).show();

//                    swipeLeft(userName, userID, currUserId, "Absent", subGroupPushId, push, groupName, groupPushId);

                } else {
                    userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, currUserId, "Present", subGroupPushId, push, groupName, groupPushId);
                    refAttendance.child(userID).setValue(userAddGroupClass);
                    Toast.makeText(Attendance_Activity.this, "Present" + userName, Toast.LENGTH_SHORT).show();

                }

                showGrpMemberList.removeItem(position);


            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;

                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

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

    private void swipeLeft(String userName, String userID, String currUserId, String absent, String subGroupPushId, String push, String groupName, String groupPushId) {

        String[] listItems={"Absent without Leave",
                "Abset with Leave less than 3 days",
                "Absent with Leave more than 3 days,",
                "Others"};

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();

        dialogBuilder.setCanceledOnTouchOutside(false);

        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_attendance, null);


        RadioButton rb_PoPAbsentWOL=dialogView.findViewById(R.id.rb_PoPAbsentWOL);
        RadioButton rb_POPAbsentWLL3D=dialogView.findViewById(R.id.rb_POPAbsentWLL3D);
        RadioButton rb_POPAbsentWLM3D=dialogView.findViewById(R.id.rb_POPAbsentWLM3D);
        RadioButton rb_Others=dialogView.findViewById(R.id.rb_Others);
        EditText et_OtherDetails=dialogView.findViewById(R.id.et_OtherDetails);


        rb_PoPAbsentWOL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarks="Absent Without Leave";
            }
        });
        rb_POPAbsentWLL3D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarks="Absent With Leave less then 3 days";
            }
        });
        rb_POPAbsentWLM3D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarks="Absent With Leave More then 3 days";

            }
        });
        rb_Others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_OtherDetails.setVisibility(View.VISIBLE);
                remarks="other"+et_OtherDetails.getText().toString();

            }
        });


        Button btn_Cancel=dialogView.findViewById(R.id.btn_Cancel);
        Button btn_Submit=dialogView.findViewById(R.id.btn_Submit);

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialogBuilder.dismiss();
            }
        });
        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remarks.equals("other")) {
                    remarks = et_OtherDetails.getText().toString();
                }
                userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, currUserId, remarks, subGroupPushId, push, groupName, groupPushId);
                refAttendance.child(userID).setValue(userAddGroupClass);
                Toast.makeText(Attendance_Activity.this, remarks + userName, Toast.LENGTH_SHORT).show();
                dialogBuilder.dismiss();

            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }



}