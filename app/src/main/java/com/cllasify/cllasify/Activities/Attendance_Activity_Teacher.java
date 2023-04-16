package com.cllasify.cllasify.Activities;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adapters.Adaptor_Attendance;
import com.cllasify.cllasify.Adapters.Adaptor_ShowGrpMemberAttendanceRollNumberList;
import com.cllasify.cllasify.ModelClasses.Class_Group;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class Attendance_Activity_Teacher extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    String remarks = null;
    List<Class_Student_Details> listGrpMemberList, mDatalistNew;
    List<Class_Group> list_showAttend;

    DatabaseReference refGrpMemberList, refAttendance;
    RecyclerView rv_GrpMemberList;
    Adaptor_ShowGrpMemberAttendanceRollNumberList showGrpMemberList;
    Adaptor_Attendance showAttendanceStatus;
    Class_Group userAddGroupClass;
    ImageButton btn_ShowAttendStatus, btn_Back, btn_CheckAttendHistory, btn_attendance;
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
    String currUserId, groupPushId, subGroupPushId, classPushId;


    public void checkDarkLightDefaultStatusBar() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {

            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().setStatusBarColor(Color.parseColor("#17181c"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                // edited here
                getWindow().setStatusBarColor(Color.parseColor("#ffffff"));

                break;


        }
    }

    private void checkOnlineStatus(String status) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();

        DatabaseReference setStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        setStatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("userStatus").exists()) {
                    setStatus.child("userStatus").setValue(status);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    protected void onStart() {
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {

        String timestamp = String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timestamp);
        super.onPause();
    }

    @Override
    protected void onResume() {

        checkOnlineStatus("online");
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.attendance_activity);

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        showToast();

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        currUserId = currentUser.getUid();

        groupPushId = getIntent().getStringExtra("groupPushId");
        subGroupPushId = getIntent().getStringExtra("subGroupPushId");
        classPushId = getIntent().getStringExtra("classPushId");


        tv_groupName = findViewById(R.id.tv_groupName);
        tv_subGroupName = findViewById(R.id.tv_subGroupName);
        btn_attendance = findViewById(R.id.btn_attendance);
        btn_Back = findViewById(R.id.btn_Back);

        rv_GrpMemberList = findViewById(R.id.rv_GrpMemberAttendance);


        tv_groupName.setText(groupPushId);
        tv_subGroupName.setText(subGroupPushId);

        myCalendar = Calendar.getInstance();


        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showmenu();

            }
        });


        show_GrpMemberList(groupPushId, subGroupPushId);
        initSwipe(groupPushId, subGroupPushId);
//        enableSwipeToDeleteAndUndo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }


    public static Bitmap getBitmapFromVectorDrawable(Attendance_Activity_Teacher context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    //for attendances menu
    private void showmenu() {

        PopupMenu popupMenu = new PopupMenu(Attendance_Activity_Teacher.this, btn_attendance);

        popupMenu.getMenuInflater().inflate(R.menu.attendance_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.Recent_attendance:
                        recent_attendance();
                        break;
                    case R.id.Attendance_history:
                        calenderDialog();
                        break;


                }
                return true;

            }
        });
        popupMenu.show();

    }


    private void recent_attendance() {

        String myFormat = "dd-MM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        //btn_ShowAttendStatus.setText("Check Attendance Status");
        String btnData = sdf.format(myCalendar.getTime());

        dialog_AttendanceStatus(btnData);
    }

    private void calenderDialog() {
        new DatePickerDialog(Attendance_Activity_Teacher.this, R.style.calender_style, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //do something with the date
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd-MM-yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String btnData = sdf.format(myCalendar.getTime());
                dialog_AttendanceStatus(btnData);
            }
        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void dialog_AttendanceStatus(String currentDate) {

        Intent intent = new Intent(Attendance_Activity_Teacher.this, Attendance_History.class);
        intent.putExtra("currentDate", currentDate);
        intent.putExtra("groupPushId", groupPushId);
        intent.putExtra("subGroupPushId", subGroupPushId);
        intent.putExtra("classPushId", classPushId);

        startActivity(intent);

    }

    private void show_GrpMemberList(String groupPushId, String subGroupPushId) {
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

                        String grpPush = snapshot.child("clickedGroupPushId").getValue().toString();

                        DatabaseReference databaseReferenceGetTeachers = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins").child(grpPush);

                        databaseReferenceGetTeachers.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                listGrpMemberList.clear();

                                for (DataSnapshot dataSnapshot1 : snapshot.child("classAdminList").getChildren()) {
                                    Class_Student_Details class_student_details = dataSnapshot1.getValue(Class_Student_Details.class);
                                    listGrpMemberList.add(class_student_details);

                                    rv_GrpMemberList.setAdapter(showGrpMemberList);
                                    showGrpMemberList.notifyDataSetChanged();

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



/*
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
*/

    }


    //Toast for take attendance
    public void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_attendances, (ViewGroup) findViewById(R.id.toast));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    //Toast for complete attendance
    public void showToastComplete() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_attendance_complete, (ViewGroup) findViewById(R.id.toast));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
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
/*
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
//                    Toast.makeText(Attendance_Activity.this, "Absent" + userName, Toast.LENGTH_SHORT).show();

//                    swipeLeft(userName, userID, currUserId, "Absent", subGroupPushId, push, groupName, groupPushId);

                } else {
                    userAddGroupClass = new Class_Group(dateTimeCC, userName, userID, currUserId, "Present", subGroupPushId, push, groupName, groupPushId);
                    refAttendance.child(userID).setValue(userAddGroupClass);
//                    Toast.makeText(Attendance_Activity.this, "Present" + userName, Toast.LENGTH_SHORT).show();

                }

                showGrpMemberList.removeItem(position);

                //Showing toast
                if (listGrpMemberList.size() == 0) {
                    showToastComplete();
                }
*/

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;

                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if (dX > 0) {
                        p.setColor(Color.parseColor("#FF6200EE"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                        c.drawBitmap(getBitmapFromVectorDrawable(Attendance_Activity_Teacher.this, R.drawable.ic_present), null, icon_dest, p);

                    } else {
                        p.setColor(Color.parseColor("#333333"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                        c.drawBitmap(getBitmapFromVectorDrawable(Attendance_Activity_Teacher.this, R.drawable.ic_absent), null, icon_dest, p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rv_GrpMemberList);


    }


}