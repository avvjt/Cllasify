package com.cllasify.cllasify.Activities.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.LayoutTransition;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;

import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class About extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    LinearLayout cardView , cardView1;
    LinearLayout ll_details , ll_details1;
    LinearLayout Layout , Layout1 ;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.activity_about);

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        cardView = findViewById(R.id.cardView);
        cardView1 = findViewById(R.id.cardView1);
        ll_details = findViewById(R.id.details);
        ll_details1 = findViewById(R.id.details1);
        Layout = findViewById(R.id.layout);
        Layout1 = findViewById(R.id.layout1);

        Layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        Layout1.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int v = (ll_details.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
                TransitionManager.beginDelayedTransition(Layout, new AutoTransition());
                ll_details.setVisibility(v);
            }
        });
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int x = (ll_details1.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
                TransitionManager.beginDelayedTransition(Layout1, new AutoTransition());
                ll_details1.setVisibility(x);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

}