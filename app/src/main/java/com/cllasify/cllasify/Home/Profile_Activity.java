package com.cllasify.cllasify.Home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Friend_Chat_Activity;
import com.cllasify.cllasify.NestedRecyclerview.TestFirebaseActivity;
import com.cllasify.cllasify.Profile.AccountSetting_Activity;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Register.getStarted;
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
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Activity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    CircleImageView prof_pic;
    BottomNavigationView bottom_nav;
    LinearLayout ll_bio, ll_Institution, ll_location, ll_UserName;
    Boolean showSpinner = false, showUserName = false, showInstitute = false, showUserBio = false, showSpinnerCategory = false;


    TextView tv_Name, tv_addBio, tv_UserBio, tv_addInstitute, tv_UserInstitute, tv_addUserName, tv_UserUserName, tv_addLocation, tv_UserLocation, tv_CountFollowing, tv_CountFollowers;

    DatabaseReference refUserStatus, refUserFollowers, refUserFollowing;

    LinearLayout ll_AddBio,
            ll_AddInstitute,
            ll_AddUserName;

    Button btn_AddBio, btn_AddBioCancel,
            btn_AddInstitute, btn_AddInstituteCancel,
            btn_AddUserName, btn_AddUserNameCancel;

    ImageButton ib_ShareApp, ib_Settings;

    EditText et_AddBio,
            et_AddInstitute,
            et_AddUserName;

    FirebaseUser currentUser;
    String userID, userName, userEmail;
    Uri userPhoto;
    ProgressDialog notifyPB;
    private AdManagerInterstitialAd mAdManagerInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);


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

            refUserStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
            refUserStatus.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() > 0) {

                        if (snapshot.child("profilePic").exists()) {
                            String profilePic = snapshot.child("profilePic").getValue().toString();
                            Glide.with(Profile_Activity.this).load(profilePic).into(prof_pic);
                        } else {
                            Picasso.get().load(userPhoto).into(prof_pic);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

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
//                    Fragment fragment = null;
//                    FragmentTransaction transaction;
//                    fragment = new AccountSettingFragment();
//                    transaction = getFragmentManager().beginTransaction();
//                    transaction.replace(R.id.fragment_container, fragment, "AccountSettingFragment");
//                    transaction.addToBackStack("AccountSettingFragment");
//                    transaction.commit();

                    Intent i = new Intent(Profile_Activity.this, AccountSetting_Activity.class);
                    startActivity(i);
                    Profile_Activity.this.overridePendingTransition(0, 0);
//                    ((Activity) getActivity()).overridePendingTransition(0, 0);

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

            tv_Name.setText(userName);

            showProfile();
            bottomMenu();

        } else {
            startActivity(new Intent(this, getStarted.class));
        }

    }


    private void addInstitution() {

        tv_addInstitute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showInstitute) {
                    ll_AddInstitute.setVisibility(View.VISIBLE);
                    showInstitute = true;
                } else {
                    ll_AddInstitute.setVisibility(View.GONE);
                    showInstitute = false;
                }
            }
        });
        btn_AddInstituteCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_AddInstitute.setVisibility(View.GONE);
                tv_UserInstitute.setVisibility(View.GONE);
            }
        });

        btn_AddInstitute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_Institute = et_AddInstitute.getText().toString();
                if (!user_Institute.equals("")) {
                    ll_AddInstitute.setVisibility(View.GONE);
                    refUserStatus.child("Institute").setValue(user_Institute);
                    tv_UserInstitute.setVisibility(View.VISIBLE);
                    tv_UserInstitute.setText(user_Institute);
                } else {
                    et_AddInstitute.setError("Enter Institute");
                    tv_UserInstitute.setVisibility(View.GONE);


                }

            }
        });

    }

    private void addUserName() {

        tv_addUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showUserName) {
                    ll_AddUserName.setVisibility(View.VISIBLE);
                    showUserName = true;
                } else {
                    ll_AddUserName.setVisibility(View.GONE);
                    showUserName = false;
                }
            }
        });
        btn_AddUserNameCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_AddUserName.setVisibility(View.GONE);
                tv_UserUserName.setVisibility(View.GONE);
            }
        });

        btn_AddUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_UserName = et_AddUserName.getText().toString();
                if (!user_UserName.equals("")) {
                    ll_AddUserName.setVisibility(View.GONE);
                    refUserStatus.child("UserName").setValue(user_UserName);
                    tv_UserUserName.setVisibility(View.VISIBLE);
                    tv_UserUserName.setText(user_UserName);

                } else {
                    et_AddUserName.setError("Enter Institute");

                }

            }
        });


    }

    private void addBio() {

        tv_addBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showUserBio) {
                    ll_AddBio.setVisibility(View.VISIBLE);
                    showUserBio = true;
                } else {
                    ll_AddBio.setVisibility(View.GONE);
                    showUserBio = false;
                }
            }
        });
        btn_AddBioCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_AddBio.setVisibility(View.GONE);
                tv_UserBio.setVisibility(View.GONE);

            }
        });

        btn_AddBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_Bio = et_AddBio.getText().toString();
                if (!user_Bio.equals("")) {
                    ll_AddBio.setVisibility(View.GONE);
                    refUserStatus.child("Bio").setValue(user_Bio);
                } else {
                    et_AddBio.setError("Enter Bio");

                }

            }
        });


    }

    private void showProfile() {
        refUserStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        refUserStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    if (snapshot.child("Bio").exists()) {
//                            tv_addBio.setVisibility(View.GONE);
                        String bio = snapshot.child("Bio").getValue().toString();
//                            tv_UserBio.setVisibility(View.VISIBLE);
                        ll_bio.setVisibility(View.VISIBLE);
                        tv_UserBio.setText(bio);
                        notifyPB.dismiss();
                    } else {
                        ll_bio.setVisibility(View.GONE);
//                            tv_addBio.setVisibility(View.VISIBLE);

                    }

                    if (snapshot.child("Insitution").exists()) {
//                            tv_addInstitute.setVisibility(View.GONE);
                        String bio = snapshot.child("Insitution").getValue().toString();
//                            tv_UserInstitute.setVisibility(View.VISIBLE);
                        ll_Institution.setVisibility(View.VISIBLE);
                        tv_UserInstitute.setText(bio);

                        notifyPB.dismiss();
                    } else {
//                            tv_addInstitute.setVisibility(View.VISIBLE);
                        ll_Institution.setVisibility(View.GONE);

                    }


                    if (snapshot.child("NickName").exists()) {
//                            tv_addUserName.setVisibility(View.GONE);
                        String UserName = snapshot.child("NickName").getValue().toString();
                        tv_UserUserName.setVisibility(View.VISIBLE);
                        tv_UserUserName.setText(UserName);
                        ll_UserName.setVisibility(View.VISIBLE);
                        notifyPB.dismiss();
                    } else {
//                            tv_addUserName.setVisibility(View.VISIBLE);
                        ll_UserName.setVisibility(View.GONE);


                    }
                    if (snapshot.child("Location").exists()) {
//                            tv_addLocation.setVisibility(View.GONE);
//                            spinnerLocation.setVisibility(View.GONE);
                        ll_location.setVisibility(View.VISIBLE);
                        String Location = snapshot.child("Location").getValue().toString();
//                            tv_UserLocation.setVisibility(View.VISIBLE);
                        tv_UserLocation.setText(Location);
                        notifyPB.dismiss();
                    } else {
//                            tv_addLocation.setVisibility(View.VISIBLE);
                        ll_location.setVisibility(View.GONE);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refUserFollowers = FirebaseDatabase.getInstance().getReference().child("Users").child("Followers").child(userID);
        refUserFollowers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    long count = snapshot.getChildrenCount();
                    tv_CountFollowers.setText((int) count + " Followers");
                    notifyPB.dismiss();
//                        notifyPB.show();
                } else {
                    tv_CountFollowers.setText("No Followers");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        refUserFollowing = FirebaseDatabase.getInstance().getReference().child("Users").child("Following").child(userID);
        refUserFollowing.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount() > 0) {
                    long count = snapshot.getChildrenCount();
                    tv_CountFollowing.setText((int) count + " Following");
                    notifyPB.dismiss();
                } else {
                    tv_CountFollowing.setText("No Following");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    private void bottomMenu() {
//        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(int i) {
//                Fragment fragment = null;
//                switch (i) {
//                    case R.id.bottom_nav_home:
//                        fragment = new HomeFragment();
//                        tag="home";
//                        break;
//                    case R.id.bottom_nav_feed:
////                        fragment = new FeedFragment();
//                        fragment = new FeedFragment();
//                        tag="feed";
//                        break;
//                    case R.id.bottom_nav_notification:
//                        fragment = new NotificationFragment();
//                        tag="notify";
//                        break;
//                    case R.id.bottom_nav_profile:
//                        fragment = new ProfileFragment();
//                        tag="profile";
//                        break;
//                }
//                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container,fragment,tag);
//                transaction.addToBackStack(tag);
//                transaction.commit();
//
//            }
//        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.bottom_nav_home:
//                        fragment = new HomeFragment();
//                        tag = "home";
                        startActivity(new Intent(Profile_Activity.this, Server_Activity.class));
                        Profile_Activity.this.overridePendingTransition(0, 0);

                        break;
                    case R.id.bottom_nav_discover:
//                          fragment = new FeedFragment();
//                        fragment = new JoinGroupFragment();
//                        tag = "joingroup";
                        startActivity(new Intent(Profile_Activity.this, Discover_Activity.class));
                        Profile_Activity.this.overridePendingTransition(0, 0);

                        break;
                    case R.id.bottom_nav_notification:
//                        fragment = new User_Notification_Frag();
//                        tag = "notify";
                        startActivity(new Intent(Profile_Activity.this, Notification_Activity.class));
                        Profile_Activity.this.overridePendingTransition(0, 0);

                        break;
                    case R.id.bottom_nav_profile:
//                        fragment = new ProfileFragment();
//                        tag = "profile";
                        startActivity(new Intent(Profile_Activity.this, Profile_Activity.class));
                        Profile_Activity.this.overridePendingTransition(0, 0);

                        break;
//                    case R.id.action_Share:
//                        Toast.makeText(landing_Page.this, "Refer and Earn", Toast.LENGTH_LONG).show();
//                        generateLink();
//                        break;
                }
//                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container,fragment,tag);
//                transaction.addToBackStack(tag);
//                transaction.commit();


                return true;
            }
        });
    }

}