package com.cllasify.cllasify;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.cllasify.cllasify.Fragment.HomeFragment;
import com.cllasify.cllasify.Fragment.FriendsFragment;
import com.cllasify.cllasify.Fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class Dashboard extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;
    BottomNavigationView bottomNavigationView;
    boolean homeFrag = true;
    private DrawerLayout drawer;
    String tag = null;

    public static final String CHANNEL_ID="cllasify";
    private static final String CHANNEL_NAME="cllasify";
    private static final String CHANNEL_DESC="cllasify notification";


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
        setContentView(R.layout.activity_dashboard);

//        chipNavigationBar = findViewById(R.id.bottom_nav);
        bottomNavigationView = findViewById(R.id.bottom_nav);
//        chipNavigationBar.setItemSelected(R.id.bottom_nav_home,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment(),"feed").commit();
        bottomMenu();


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
                        fragment = new HomeFragment();
                        tag = "home";
                        break;
                    case R.id.bottom_nav_discover:
//                          fragment = new FeedFragment();
                        fragment = new JoinGroupFragment();
                        tag = "joingroup";
                        break;
                    case R.id.bottom_nav_notification:
                        fragment = new User_Notification_Frag();
                        tag = "notify";
                        break;
                    case R.id.bottom_nav_profile:
                        fragment = new ProfileFragment();
                        tag = "profile";
                        break;
//                    case R.id.action_Share:
//                        Toast.makeText(landing_Page.this, "Refer and Earn", Toast.LENGTH_LONG).show();
//                        generateLink();
//                        break;
                }
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragment,tag);
                transaction.addToBackStack(tag);
                transaction.commit();


                return true;
            }
        });
    }

    @Override
   public void onBackPressed() {

        if (getFragmentManager().getBackStackEntryCount()!=0){
            getFragmentManager().popBackStack();
        }else{
            exitApp();

        }


//        Fragment fragment = getSupportFragmentManager().findFragmentByTag("home");
//        if (fragment != null && fragment.isVisible()) {
//            exitApp();
////            Toast.makeText(this, "vvvv", Toast.LENGTH_SHORT).show();
////            HomeFragment hm=new HomeFragment();
////            hm.onDetach();
//
//        }
//        int count = getSupportFragmentManager().getBackStackEntryCount();
//        if (count ==0){
//            super.onBackPressed();
//        }else{
//            getSupportFragmentManager().popBackStack();
//        }


//            homeFrag=false;


//        if (getFragmentManager().getBackStackEntryCount()>1)
//        {
//            android.app.Fragment f=getFragmentManager().findFragmentById(R.id.nav_host_fragment);
//        }



//        if (drawer.isDrawerOpen(Gravity.LEFT | GravityCompat.START)) {
////            super.onBackPressed();
//            drawer.closeDrawers();
//
//        }
//
//        FragmentTransaction transaction;
//        transaction=getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment(),"home");
//        transaction.commit();

//            else {
//                if (homeFrag || !(drawer.isDrawerOpen(Gravity.LEFT | GravityCompat.START))){
    }
    private void exitApp() {

        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(Dashboard.this);
        alertdialogbuilder.setTitle("Please confirm !!!")
                .setMessage("Are you want to close this application?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    Dashboard.this.finishAffinity();
                                    System.exit(0);
                                } else {
                                    Dashboard.this.finish();
                                    System.exit(0);
                                }
                                homeFrag=false;
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = alertdialogbuilder.create();
        alert.show();


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment(),"home").commit();
        }

    }
}