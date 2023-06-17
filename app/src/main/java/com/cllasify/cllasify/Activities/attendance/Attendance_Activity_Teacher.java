package com.cllasify.cllasify.Activities.attendance;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Activities.Attendance_History;
import com.cllasify.cllasify.Adapters.TeacherAttendanceAdapter;
import com.cllasify.cllasify.ModelClasses.User;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
import com.cllasify.cllasify.databinding.AttendanceActivityBinding;
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
import java.util.List;
import java.util.Locale;

public class Attendance_Activity_Teacher extends AppCompatActivity {

    private AttendanceActivityBinding binding;
    private BroadcastReceiver broadcastReceiver;

    private String groupPushId = "";
    private String subGroupPushId = "";
    private String classPushId = "";

    private AttendanceViewModel attendanceViewModel;

    private TeacherAttendanceAdapter teacherAttendanceAdapter;

    private List<User> teachers = new ArrayList<>();

    private Calendar myCalendar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AttendanceActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        attendanceViewModel = new ViewModelProvider(this, getDefaultViewModelProviderFactory()).get(AttendanceViewModel.class);
        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        groupPushId = getIntent().getStringExtra("groupPushId");
        subGroupPushId = getIntent().getStringExtra("subGroupPushId");
        classPushId = getIntent().getStringExtra("classPushId");
        attendanceViewModel.setBasicData(groupPushId, subGroupPushId, classPushId);
        myCalendar = Calendar.getInstance();
        initUi();
        observeFields();
    }

    private void initUi() {
        teacherAttendanceAdapter = new TeacherAttendanceAdapter(this);
        binding.rvGrpMemberAttendance.setLayoutManager(new LinearLayoutManager(this));
        binding.rvGrpMemberAttendance.setAdapter(teacherAttendanceAdapter);
        addRecyclerViewSwipeListener();
        binding.btnDone.setOnClickListener(v -> {
            if (!teachers.isEmpty()) {
                Toast.makeText(this, "Please marked all teachers", Toast.LENGTH_SHORT).show();
                return;
            }
            attendanceViewModel.startAttendance();
        });
        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        binding.btnAttendance.setOnClickListener(v -> {
            showmenu();
        });
    }

    private void showmenu() {

        PopupMenu popupMenu = new PopupMenu(Attendance_Activity_Teacher.this, binding.btnAttendance);

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

    private void addRecyclerViewSwipeListener() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                User user = teachers.get(position);
                boolean isPresent = direction == ItemTouchHelper.RIGHT;
                attendanceViewModel.markAttendance(user, position, isPresent);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                Paint p = new Paint();
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
        itemTouchHelper.attachToRecyclerView(binding.rvGrpMemberAttendance);
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

    private void observeFields() {
        attendanceViewModel.get_isLoading().observe(this, isLoading -> {
            if (isLoading) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.btnDone.setVisibility(View.GONE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnDone.setVisibility(View.VISIBLE);
            }
        });

        attendanceViewModel.getTeachers().observe(this, teacher -> {
            if (teacher != null) {
                this.teachers = teacher;
                teacherAttendanceAdapter.submitUsers(teacher);
            }
        });

        attendanceViewModel.get_error().observe(this, error -> {
            Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
        });
        attendanceViewModel.getAttendanceMarked().observe(this, attendanceMarked -> {
            if (attendanceMarked) {
                Toast.makeText(this, "Attendance marked", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        attendanceViewModel.get_attendanceStatus().observe(this, status -> {
            switch (status) {
                case 1:
                    Toast.makeText(this, "Someone already started the attendance", Toast.LENGTH_SHORT).show();
                    return;
                case 2:
                    Toast.makeText(this, "Today's attendance already marked", Toast.LENGTH_SHORT).show();
                    return;
                default:

            }
        });
    }

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
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        unregisterReceiver(broadcastReceiver);
        broadcastReceiver = null;
    }
}