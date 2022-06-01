package com.cllasify.cllasify.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Service.NetworkBroadcast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Create_Server extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    Uri userPhoto;
    String userID, userEmailID, userName;

    public void checkDarkLightDefaultStatusBar() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {

            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().setStatusBarColor(Color.parseColor("#17181c"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                // edited here
                getWindow().setStatusBarColor(Color.parseColor("#ffffff"));

                break;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.activity_create_server);


        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID = currentUser.getUid();
        userEmailID = currentUser.getEmail();
        userPhoto = currentUser.getPhotoUrl();
        userName = currentUser.getDisplayName();

        Button btn_CreateGroup = findViewById(R.id.btn_CreateGroup);
        LinearLayout ll_CreatingServer = findViewById(R.id.ll_groupFamFrnds);
        EditText et_GroupName = findViewById(R.id.et_GroupName);
        ImageButton btn_Back = findViewById(R.id.btn_Back);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btn_CreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String GroupName = et_GroupName.getText().toString().trim();

                if (GroupName.isEmpty()) {
                    Toast.makeText(Create_Server.this, "Enter Group Name", Toast.LENGTH_SHORT).show();
                    et_GroupName.setError("Enter Group Name");
                }
                else {
                    Intent intent = new Intent(Create_Server.this,Create_Class.class);
                    intent.putExtra("GroupName",GroupName);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

}