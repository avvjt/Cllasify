package com.cllasify.cllasify.Profile;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.cllasify.cllasify.Home.Notification_Activity;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Register.getStarted;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AccountSetting_Activity extends AppCompatActivity {


    Button btn_Back,btn_Submit,btn_Cancel,btn_ProfileSetting,btnDefault,btnDark,btnLight;
    TextView tv_SignOut,tv_setTheme,tv_ProfileSetting,tv_User_Name , tv_rateUs;
    SwitchCompat allNotifySwitch;
//    Spinner spinnerUserStatus;
//    String[] userStatus = {
//            "Select Status",
//            "Online",
//            "Offline",
//            "Busy",
//            "Unavailable",
//    };

    Boolean showTheme=false,showStatus=false,showFeedback=false;
    FirebaseUser currentUser;
    String userID,userName,userEmail;
    DatabaseReference refUserStatus;
    AlertDialog.Builder builder;

    RadioGroup rg_Theme;

    TextView tv_notiConfig,tv_Feedback;
    LinearLayout ll_showFeedback;
    EditText et_Feedback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting_activity);

        btn_Back=findViewById(R.id.btn_Back);
        tv_SignOut =findViewById(R.id.tv_SignOut);
        et_Feedback =findViewById(R.id.et_Feedback);
        tv_User_Name =findViewById(R.id.tv_User_Name);
        tv_rateUs = findViewById(R.id.tv_rateUs);

//        spinnerUserStatus=view.findViewById(R.id.spinnerUserStatus);
//        allNotifySwitch=view.findViewById(R.id.allNotifySwitch);
        tv_notiConfig=findViewById(R.id.tv_notiConfig);
        tv_setTheme=findViewById(R.id.tv_setTheme);
        tv_Feedback=findViewById(R.id.tv_Feedback);
        tv_ProfileSetting =findViewById(R.id.tv_ProfileSetting);
        btn_ProfileSetting =findViewById(R.id.btn_ProfileSetting);
        tv_notiConfig =findViewById(R.id.tv_notiConfig);
        builder  = new AlertDialog.Builder(this);

        btn_Submit=findViewById(R.id.btn_Submit);
        btn_Cancel=findViewById(R.id.btn_Cancel);

        rg_Theme=findViewById(R.id.rg_Theme);
        btnDefault=findViewById(R.id.btnDefault);
        btnDark=findViewById(R.id.btnDark);
        btnLight=findViewById(R.id.light);

        ll_showFeedback=findViewById(R.id.ll_showFeedback);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        userName = currentUser.getDisplayName();
        userEmail = currentUser.getEmail();
//        userPhoto = currentUser.getPhotoUrl();
        tv_User_Name.setText(userName);




        //RateUs in playStore
        tv_rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //when playStore is available
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //when playStore is Unavailable
                    Uri uri = Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });


        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentTransaction transaction = getSupportFragmentManager()FragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, new ProfileFragment());
//                //transaction.addToBackStack(null);
//                transaction.commit();

                finish();
//                Intent i = new Intent(AccountSetting_Activity.this, ProfileFragment.class);
//                startActivity(i);
//                (AccountSetting_Activity.this).overridePendingTransition(0, 0);

            }
        });
        tv_ProfileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, new ProfileSettingFragment());
//                //transaction.addToBackStack(null);
//                transaction.commit();

                Intent i = new Intent(AccountSetting_Activity.this, ProfileSetting_Activity.class);
                startActivity(i);
                (AccountSetting_Activity.this).overridePendingTransition(0, 0);
            }
        });

        btn_ProfileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountSetting_Activity.this, ProfileSetting_Activity.class);
                startActivity(i);
                (AccountSetting_Activity.this).overridePendingTransition(0, 0);

            }
        });


        tv_notiConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBtmNotification();
            }
        });




        tv_setTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBtmTheme();

            }
        });

        tv_Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editSetting();

/*
                    if (!showFeedback){
                        ll_showFeedback.setVisibility(View.VISIBLE);
                        showFeedback=true;
                    }else{
                        ll_showFeedback.setVisibility(View.GONE);
                        showFeedback=false;
                    }
*/
            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String feedback = et_Feedback.getText().toString();
//                String to = "cllasify@gmail.com";
//                String subject = "A feedback for Cllasify App";
//                String gmailPackage = "com.google.android.gm";
//
//                Intent email = new Intent(Intent.ACTION_SEND);
//                email.setPackage(gmailPackage);
//                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
//                email.putExtra(Intent.EXTRA_SUBJECT, subject);
//                email.putExtra(Intent.EXTRA_TEXT, feedback);
//                email.setType("message/rfc822");
//                startActivity(Intent.createChooser(email, "Choose an Email client :"));

            }
        });


        //
//        allNotifySwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (allNotifySwitch.isChecked()){
//                    Toast.makeText(getContext(), "Notification On", Toast.LENGTH_SHORT).show();
////                    FirebaseDatabase.getInstance().getReference()
////                            .child("Admin").child("Token").child("All Notifications").child(userId).setValue(true);
////                    FirebaseMessaging.getInstance().subscribeToTopic("All_Notify");
//                }
//                else{
//                    Toast.makeText(getContext(), "Notification Off", Toast.LENGTH_SHORT).show();
////                    FirebaseDatabase.getInstance().getReference()
////                            .child("Admin").child("Token").child("All Notifications").child(userId).removeValue();
////                    FirebaseMessaging.getInstance().unsubscribeFromTopic("All_Notify");
//                }
//            }
//        });



//        ArrayAdapter<String> adapterSex = new ArrayAdapter<String>(getContext(), android.
//                R.layout.simple_spinner_dropdown_item, userStatus);
//        spinnerUserStatus.setAdapter(adapterSex);
//        spinnerUserStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view,
//                                       int position, long id) {
//                // Get select item
//                int sexPosition = spinnerUserStatus.getSelectedItemPosition();
//                if (sexPosition != 0) {
//                    String Sex = userStatus[sexPosition];
//                    refUserStatus= FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
//                    refUserStatus.child("userStatus").setValue(Sex);
//
//                } else {
////                        Toast.makeText(getBaseContext(), "Please Select Sex Category",
////                                Toast.LENGTH_LONG).show();
//                    ((TextView) spinnerUserStatus.getChildAt(0)).setError("Please Select Status");
//
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });


        tv_SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                 builder.setTitle("Sign out")
                        .setMessage("Do you want to logout?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                HashMap<String,Object> map= new HashMap<>();
                                map.put("token","");

                                FirebaseDatabase.getInstance().getReference()
                                        .child("Admin").child("Login Status").child(userID).updateChildren(map)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                FirebaseAuth.getInstance().signOut();
                                                Intent i = new Intent(AccountSetting_Activity.this, getStarted.class);
                                                startActivity(i);
                                                (AccountSetting_Activity.this).overridePendingTransition(0, 0);

                                                Toast.makeText(AccountSetting_Activity.this, "You are Logged Out", Toast.LENGTH_LONG).show();

                                            }
                                        });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();



            }
        });


    }

    private void showBtmTheme() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheet_theme);

         rg_Theme = dialog.findViewById(R.id.rg_Theme);
         btnDefault = dialog.findViewById(R.id.btnDefault);
         btnDark = dialog.findViewById(R.id.btnDark);
         btnLight = dialog.findViewById(R.id.btnLight);

        SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn",false);
        if (isDarkModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            rg_Theme.check(R.id.btnDark);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            rg_Theme.check(R.id.btnLight);
        }

        rg_Theme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.btnDefault:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                        rg_Theme.check(R.id.btnDefault);
                        Toast.makeText(AccountSetting_Activity.this, "System Default Mode Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnDark:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        rg_Theme.check(R.id.btnDark);
                        editor.putBoolean("isDarkModeOn",true);
                        editor.apply();
                        Toast.makeText(AccountSetting_Activity.this, "Dark Mode selected", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btnLight:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        rg_Theme.check(R.id.btnLight);
                        editor.putBoolean("isDarkModeOn",false);
                        editor.apply();
                        Toast.makeText(AccountSetting_Activity.this, "Light Mode selected", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }


    private void editSetting() {

        BottomSheetDialog bottomSheetDialoglogin = new BottomSheetDialog(this);
        bottomSheetDialoglogin.setCancelable(false);
        bottomSheetDialoglogin.setContentView(R.layout.btmdialog_setting);

        Button btn_Cancel = bottomSheetDialoglogin.findViewById(R.id.btn_Cancel);
        TextView tv_subTitle = bottomSheetDialoglogin.findViewById(R.id.tv_subTitle);
        TextView tv_SettingTitle = bottomSheetDialoglogin.findViewById(R.id.tv_SettingTitle);
        EditText et_NewDetails = bottomSheetDialoglogin.findViewById(R.id.et_NewDetails);
        Button btn_Submit = bottomSheetDialoglogin.findViewById(R.id.btn_Submit);


        tv_subTitle.setText("Please enter the details of the feedback");
        tv_SettingTitle.setText("Share Feedback");

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialoglogin.dismiss();
            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback = et_NewDetails.getText().toString().trim();
//                String feedback = et_Feedback.getText().toString();
                String to = "cllasify@gmail.com";
                String subject = "A feedback for Cllasify App";
                String gmailPackage = "com.google.android.gm";

                Intent email = new Intent(Intent.ACTION_SEND);
                email.setPackage(gmailPackage);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, feedback);
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));


                bottomSheetDialoglogin.dismiss();
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialoglogin.dismiss();
            }
        });

        bottomSheetDialoglogin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialoglogin.show();

    }


    private void showBtmNotification() {

        BottomSheetDialog bottomSheetDialoglogin = new BottomSheetDialog(AccountSetting_Activity.this);
        bottomSheetDialoglogin.setCancelable(false);
        bottomSheetDialoglogin.setContentView(R.layout.btmfialog_notifysetting);

        SwitchMaterial sw_Direct = bottomSheetDialoglogin.findViewById(R.id.sw_Direct);
        SwitchMaterial sw_Mention = bottomSheetDialoglogin.findViewById(R.id.sw_Mention);
        SwitchMaterial sw_Server=bottomSheetDialoglogin.findViewById(R.id.sw_Server);


        refUserStatus= FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        refUserStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
                    if (snapshot.child("Direct_Notify").exists()){
                        String Direct_Notify=snapshot.child("Direct_Notify").getValue().toString();
                        if (Direct_Notify.equals(true)){
                            sw_Direct.setChecked(true);
                        }else{
                            sw_Direct.setChecked(false);
                        }
                    }if (snapshot.child("Server_Notify").exists()){
                        String Server_Notify=snapshot.child("Server_Notify").getValue().toString();
                        if (Server_Notify.equals(true)){
                            sw_Server.setChecked(true);
                        }else{
                            sw_Server.setChecked(false);
                        }
                    }if (snapshot.child("Mention_Notify").exists()){
                        String Mention_Notify=snapshot.child("Mention_Notify").getValue().toString();
                        if (Mention_Notify.equals(true)){
                            sw_Mention.setChecked(true);
                        }else{
                            sw_Mention.setChecked(false);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        sw_Direct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_Direct.isChecked()){
                    refUserStatus.child("Direct_Notify").setValue(false);
                }
                else{
                    refUserStatus.child("Direct_Notify").setValue(true);
                }
            }
        });
        sw_Mention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_Direct.isChecked()){
                    refUserStatus.child("Mention_Notify").setValue(false);
                }else{
                    refUserStatus.child("Mention_Notify").setValue(true);
                }
            }
        });
        sw_Server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_Direct.isChecked()){
                    refUserStatus.child("Server_Notify").setValue(true);
                }else{
                    refUserStatus.child("Server_Notify").setValue(false);
                }
            }
        });

        bottomSheetDialoglogin.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        bottomSheetDialoglogin.show();

    }
}