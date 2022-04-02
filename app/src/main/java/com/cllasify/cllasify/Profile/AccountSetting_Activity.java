package com.cllasify.cllasify.Profile;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.Home.Profile_Activity;
import com.cllasify.cllasify.Privacy;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Register.getStarted;
import com.cllasify.cllasify.Terms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSetting_Activity extends AppCompatActivity {


    Button btn_Submit, btn_Cancel;
    ImageView btn_Back;
    TextView tv_SignOut, tv_setTheme, tv_User_Name, tv_privacy, tv_terms, tv_rateUs;
    SwitchCompat allNotifySwitch;
//    Spinner spinnerUserStatus;
//    String[] userStatus = {
//            "Select Status",
//            "Online",
//            "Offline",
//            "Busy",
//            "Unavailable",
//    };

    SwitchCompat switchCompat;
    SharedPreferences sharedPreferences = null;

    Boolean showTheme = false, showStatus = false, showFeedback = false;
    FirebaseUser currentUser;
    String userID, userName, userEmail;
    DatabaseReference refUserStatus, refUserProfPic;
    AlertDialog.Builder builder;

    RadioGroup rg_Theme;

    TextView tv_notiConfig, tv_Feedback;
    LinearLayout ll_showFeedback, ll_profileSetting;
    EditText et_Feedback;
    CircleImageView prof_pic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefault();
        setContentView(R.layout.account_setting_activity);

        btn_Back=findViewById(R.id.btn_Back);
        tv_SignOut =findViewById(R.id.tv_SignOut);
        et_Feedback =findViewById(R.id.et_Feedback);
        tv_User_Name =findViewById(R.id.tv_User_Name);
        tv_rateUs = findViewById(R.id.tv_rateUs);
        prof_pic = findViewById(R.id.prof_pic);

//        spinnerUserStatus=view.findViewById(R.id.spinnerUserStatus);
//        allNotifySwitch=view.findViewById(R.id.allNotifySwitch);
        tv_notiConfig=findViewById(R.id.tv_notiConfig);
        tv_setTheme=findViewById(R.id.tv_setTheme);
        tv_Feedback=findViewById(R.id.tv_Feedback);

        tv_notiConfig =findViewById(R.id.tv_notiConfig);
        builder  = new AlertDialog.Builder(this);

        btn_Submit=findViewById(R.id.btn_Submit);
        btn_Cancel=findViewById(R.id.btn_Cancel);

        rg_Theme=findViewById(R.id.rg_Theme);


        ll_showFeedback=findViewById(R.id.ll_showFeedback);
        ll_profileSetting = findViewById(R.id.ll_profileSetting);
        tv_privacy = findViewById(R.id.tv_privacyPolicy);
        tv_terms = findViewById(R.id.tv_termsServices);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        userName = currentUser.getDisplayName();
        userEmail = currentUser.getEmail();
//        userPhoto = currentUser.getPhotoUrl();






        refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);

        refUserProfPic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Name").exists()){
                    tv_User_Name.setText(snapshot.child("Name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Set Profile Pic
        refUserProfPic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("profilePic").exists()){
                    String profilePic=snapshot.child("profilePic").getValue().toString();
                    if (!(AccountSetting_Activity.this).isFinishing()) {
                        Glide.with(getApplicationContext()).load(profilePic).into(prof_pic);
                    }
                }else{
                    if (!(AccountSetting_Activity.this).isFinishing()) {
                        Glide.with(getApplicationContext()).load(R.drawable.maharaji).into(prof_pic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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
                Intent i = new Intent(AccountSetting_Activity.this, Profile_Activity.class);
                startActivity(i);
                (AccountSetting_Activity.this).overridePendingTransition(0, 0);

            }
        });

        ll_profileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountSetting_Activity.this, ProfileSetting_Activity.class);
                startActivity(i);
                (AccountSetting_Activity.this).overridePendingTransition(0, 0);

            }
        });

        tv_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountSetting_Activity.this, Privacy.class);
                startActivity(i);
                (AccountSetting_Activity.this).overridePendingTransition(0, 0);

            }
        });

        tv_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AccountSetting_Activity.this, Terms.class);
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

    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply(); // or editor.commit() in case you want to write data instantly
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "Default");
    }

    private void showBtmTheme() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.bottomsheet_theme);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RadioButton btnDefault, btnDark, btnLight;

        rg_Theme = dialog.findViewById(R.id.rg_Theme);
        btnDefault = dialog.findViewById(R.id.btnDefault);
        btnDark = dialog.findViewById(R.id.btnDark);
        btnLight = dialog.findViewById(R.id.btnLight);

        DatabaseReference setDarkLightDefault = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);

        rg_Theme.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.btnDefault:
                    btnDefault.setChecked(true);
                    Log.d("DLD", "onCreate: " + btnDefault);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    setDarkLightDefault.child("DarkLightDefault").setValue("Default");
                    setDefaults("DefaultDarkLight", "Default", AccountSetting_Activity.this);
                    dialog.dismiss();
                    break;

                case R.id.btnDark:
                    btnDark.setChecked(true);
                    Log.d("DLD", "onCreate: " + btnDark);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    setDarkLightDefault.child("DarkLightDefault").setValue("Dark");
                    setDefaults("DefaultDarkLight", "Dark", AccountSetting_Activity.this);
                    dialog.dismiss();
                    break;

                case R.id.btnLight:
                    btnLight.setChecked(true);
                    Log.d("DLD", "onCreate: " + btnLight);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    setDarkLightDefault.child("DarkLightDefault").setValue("Light");
                    setDefaults("DefaultDarkLight", "Light", AccountSetting_Activity.this);
                    dialog.dismiss();
                    break;
            }
        });

        String darkLightDefaultVal = getDefaults("DefaultDarkLight", AccountSetting_Activity.this);

        if (darkLightDefaultVal != null) {
            if (darkLightDefaultVal.equals("Dark")) {
                btnDark.setChecked(true);
            }
            if (darkLightDefaultVal.equals("Light")) {
                btnLight.setChecked(true);
            }
            if (darkLightDefaultVal.equals("Default")) {
                btnDefault.setChecked(true);
            }
        } else {
            btnDefault.setChecked(true);
        }

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        moveTaskToBack(false);
    }

    private void checkDarkLightDefault() {

        String darkLightDefaultVal = getDefaults("DefaultDarkLight", AccountSetting_Activity.this);
        Log.d("USERIDSA", "onCreate: " + getDefaults("DefaultDarkLight", AccountSetting_Activity.this));
        if (darkLightDefaultVal != null) {
            if (darkLightDefaultVal.equals("Dark")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            if (darkLightDefaultVal.equals("Light")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            if (darkLightDefaultVal.equals("Default")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }


    private void editSetting() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.bottomsheet_feedback);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_Cancel = dialog.findViewById(R.id.btn_cancel);
        TextView tv_subTitle = dialog.findViewById(R.id.editTitle);
        EditText et_NewDetails = dialog.findViewById(R.id.et_NewDetails);
        Button btn_Submit = dialog.findViewById(R.id.btn_submit);


        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback = et_NewDetails.getText().toString().trim();
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

                dialog.dismiss();
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        moveTaskToBack(false);

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