package com.cllasify.cllasify.Activities.Profile;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Activities.Notification_Activity;
import com.cllasify.cllasify.Activities.RightPanel.Discover_Activity;
import com.cllasify.cllasify.Activities.Server.Server_Activity;
import com.cllasify.cllasify.Activities.getStarted;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Activity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    CircleImageView prof_pic;
    BottomNavigationView bottom_nav;
    LinearLayout ll_bio, ll_Institution, ll_location;
    RelativeLayout ll_UserName;


    TextView tv_Name, tv_UserBio, tv_UserInstitute, tv_UserUserName, tv_UserLocation, tv_CountFollowing, tv_CountFollowers;

    DatabaseReference refUserStatus, refUserFollowers, refUserFollowing;

    ImageButton ib_ShareApp, ib_Settings;


    FirebaseUser currentUser;
    String userID, userName, userEmail;
    Uri userPhoto;
    ProgressDialog notifyPB;
    private AdManagerInterstitialAd mAdManagerInterstitialAd;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Profile_Activity.this, Server_Activity.class);
        startActivity(intent);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.profile_activity);

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        AdManagerAdRequest adRequest = new AdManagerAdRequest.Builder().build();
        //Add the adUnitId -> ca-app-pub-3940256099942544/1033173712
        AdManagerInterstitialAd.load(this, "", adRequest,
                new AdManagerInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                        // The mAdManagerInterstitialAd reference will be null until
                        // an ad is loaded.
                        mAdManagerInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mAdManagerInterstitialAd = null;
                    }
                });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAdManagerInterstitialAd != null) {
                    mAdManagerInterstitialAd.show(Profile_Activity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }

            }
        }, 4000);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            bottom_nav = findViewById(R.id.bottom_nav);
            bottom_nav.setSelectedItemId(R.id.bottom_nav_profile);


            notifyPB = new ProgressDialog(this);
            notifyPB.setTitle("Govt Jobs");
            notifyPB.setMessage("Loading All Jobs");
            notifyPB.setCanceledOnTouchOutside(true);

            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            userID = currentUser.getUid();
            userName = currentUser.getDisplayName();
            userEmail = currentUser.getEmail();
            userPhoto = currentUser.getPhotoUrl();


            tv_Name = findViewById(R.id.tv_Name);
            prof_pic = findViewById(R.id.prof_pic);

            tv_UserBio = findViewById(R.id.tv_UserBio);
            tv_UserInstitute = findViewById(R.id.tv_UserInstitute);
            tv_UserUserName = findViewById(R.id.tv_UserUserName);
            tv_UserLocation = findViewById(R.id.tv_userLocation);

            ll_bio = findViewById(R.id.ll_bio);
            ll_Institution = findViewById(R.id.ll_Institution);
            ll_location = findViewById(R.id.ll_location);
            ll_UserName = findViewById(R.id.ll_UserName);


            tv_CountFollowers = findViewById(R.id.tv_CountFollowers);
            tv_CountFollowing = findViewById(R.id.tv_CountFollowing);

            ib_Settings = findViewById(R.id.btn_Settings);
            ib_ShareApp = findViewById(R.id.btn_ShareApp);


            ib_Settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Pair[] pair = new Pair[2];
                    pair[0] = new Pair<View, String>(prof_pic, "pic_shared");
                    pair[1] = new Pair<View, String>(tv_Name, "name_shared");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Profile_Activity.this, pair);

                    Intent i = new Intent(Profile_Activity.this, AccountSetting_Activity.class);
                    startActivity(i, options.toBundle());

                }
            });

            ib_ShareApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //    void appShare(){
                    Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String body = "Cllasify is the best App to Discuss Study Material with Classmates using Servers," +
                            "\nPlease Click on Below Link to Install:";
                    String subject = "Install Classify App";
                    String app_url = " https://play.google.com/store/apps/details?id=in.dreamworld.fillformonline";
                    shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, body + app_url);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    startActivity(Intent.createChooser(shareIntent, "Share via"));
//    }
                }
            });

            DatabaseReference refUserName = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);

            refUserName.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("Name").exists()) {
                        tv_Name.setText(snapshot.child("Name").getValue().toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            showProfile();
            bottomMenu();

        } else {
            startActivity(new Intent(this, getStarted.class));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void showProfile() {
        refUserStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        refUserStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    if (snapshot.child("Bio").exists()) {
                        String bio = snapshot.child("Bio").getValue().toString();
                        if (bio.isEmpty()) {
                            ll_bio.setVisibility(View.GONE);
                        } else {
                            ll_bio.setVisibility(View.VISIBLE);
                            tv_UserBio.setText(bio);
                        }
                        notifyPB.dismiss();
                    } else {
                        ll_bio.setVisibility(View.GONE);

                    }

                    if (snapshot.child("Insitution").exists()) {
                        String insitution = snapshot.child("Insitution").getValue().toString();
                        if (insitution.isEmpty()) {
                            ll_Institution.setVisibility(View.GONE);
                        } else {
                            ll_Institution.setVisibility(View.VISIBLE);
                            tv_UserInstitute.setText(insitution);
                        }
                        notifyPB.dismiss();
                    } else {
                        ll_Institution.setVisibility(View.GONE);

                    }


                    if (snapshot.child("uniqueUserName").exists()) {
                        String UserName = snapshot.child("uniqueUserName").getValue().toString();
                        tv_UserUserName.setVisibility(View.VISIBLE);
                        tv_UserUserName.setText(UserName);
                        notifyPB.dismiss();
                    } else {
                        tv_UserUserName.setVisibility(View.GONE);


                    }
                    if (snapshot.child("Location").exists()) {
                        String Location = snapshot.child("Location").getValue().toString();
                        if (Location.isEmpty()) {
                            ll_location.setVisibility(View.GONE);
                        } else {
                            ll_location.setVisibility(View.VISIBLE);
                            tv_UserLocation.setText(Location);
                        }
                        notifyPB.dismiss();
                    } else {
                        ll_location.setVisibility(View.GONE);

                    }


                    if (snapshot.child("profilePic").exists()) {
                        String profilePic = snapshot.child("profilePic").getValue().toString();
                        if (!(Profile_Activity.this).isFinishing()) {
                            Glide.with(getApplicationContext()).load(profilePic).into(prof_pic);
                        }
                    } else {
                        if (!(Profile_Activity.this).isFinishing()) {
                            Glide.with(getApplicationContext()).load(R.drawable.maharaji).into(prof_pic);
                        }


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refUserFollowers = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child(userID);
        refUserFollowers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    long count = snapshot.getChildrenCount();
                    if (count < 2) {
                        tv_CountFollowers.setText(" Following - " + (int) count);
                    } else {
                        tv_CountFollowers.setText(" Following - " + (int) count);
                    }
                    notifyPB.dismiss();
                } else {
                    tv_CountFollowers.setText("Following - 0");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
/*
        refUserFollowing = FirebaseDatabase.getInstance().getReference().child("Users").child("Following").child(userID);
        refUserFollowing.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    long count = snapshot.getChildrenCount();
                    tv_CountFollowing.setText((int) count + " Following");
                    notifyPB.dismiss();
                } else {
                    tv_CountFollowing.setText("0 Following");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
*/

    }

    private void bottomMenu() {

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.bottom_nav_home:
                        Intent intent = new Intent(Profile_Activity.this, Server_Activity.class);
                        intent.putExtra("panelState", "close");
                        startActivity(intent);

                        break;
                    case R.id.bottom_nav_discover:
                        startActivity(new Intent(Profile_Activity.this, Discover_Activity.class));
                        Profile_Activity.this.overridePendingTransition(0, 0);

                        break;
                    case R.id.bottom_nav_notification:
                        startActivity(new Intent(Profile_Activity.this, Notification_Activity.class));
                        Profile_Activity.this.overridePendingTransition(0, 0);

                        break;
                    case R.id.bottom_nav_profile:
//                        startActivity(new Intent(Profile_Activity.this, Profile_Activity.class));
                        Profile_Activity.this.overridePendingTransition(0, 0);

                        break;
                }
                return true;
            }
        });
    }

}