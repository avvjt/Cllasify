package com.cllasify.cllasify;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cllasify.cllasify.Fragment.HomeFragment;
import com.cllasify.cllasify.Fragment.ProfileFragment;
import com.cllasify.cllasify.Profile.ProfileSetting_Activity;
import com.cllasify.cllasify.Register.getStarted;
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

//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link AccountSettingFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class AccountSettingFragment extends Fragment {

    Button btn_Back, btn_Premium ,btn_Submit,btn_Cancel,btn_ProfileSetting;
    TextView tv_SignOut,tv_setTheme,tv_setStatus, tv_ProfileSetting,tv_User_Name;
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

    RadioGroup rg_Status,rg_Theme;

    TextView tv_notiConfig,tv_Feedback;
    LinearLayout ll_showFeedback;
    EditText et_Feedback;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account_setting, container, false);

        btn_Back=view.findViewById(R.id.btn_Back);
        btn_Premium=view.findViewById(R.id.btn_Premium);
        tv_SignOut =view.findViewById(R.id.tv_SignOut);
        et_Feedback =view.findViewById(R.id.et_Feedback);
        tv_User_Name =view.findViewById(R.id.tv_User_Name);

//        spinnerUserStatus=view.findViewById(R.id.spinnerUserStatus);
//        allNotifySwitch=view.findViewById(R.id.allNotifySwitch);
        tv_notiConfig=view.findViewById(R.id.tv_notiConfig);
        tv_setStatus=view.findViewById(R.id.tv_setStatus);
        tv_setTheme=view.findViewById(R.id.tv_setTheme);
        tv_Feedback=view.findViewById(R.id.tv_Feedback);
        tv_ProfileSetting =view.findViewById(R.id.tv_ProfileSetting);
        btn_ProfileSetting =view.findViewById(R.id.btn_ProfileSetting);
        tv_notiConfig =view.findViewById(R.id.tv_notiConfig);

        btn_Submit=view.findViewById(R.id.btn_Submit);
        btn_Cancel=view.findViewById(R.id.btn_Cancel);

        rg_Status=view.findViewById(R.id.rg_Status);
        rg_Theme=view.findViewById(R.id.rg_Theme);

        ll_showFeedback=view.findViewById(R.id.ll_showFeedback);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        userName = currentUser.getDisplayName();
        userEmail = currentUser.getEmail();
//        userPhoto = currentUser.getPhotoUrl();
        tv_User_Name.setText(userName);



        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new ProfileFragment());
                //transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        tv_ProfileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, new ProfileSettingFragment());
//                //transaction.addToBackStack(null);
//                transaction.commit();

                Intent i = new Intent(getActivity(), ProfileSetting_Activity.class);
                startActivity(i);
                 getActivity().overridePendingTransition(0, 0);
            }
        });

        btn_ProfileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new ProfileSettingFragment());
                //transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        tv_notiConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBtmNotification();
            }
        });



        btn_Premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, new HomeFragment());
//                //transaction.addToBackStack(null);
//                transaction.commit();
                Toast.makeText(getContext(), "In Progress", Toast.LENGTH_SHORT).show();
            }
        });


        tv_notiConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new HomeFragment());
                transaction.addToBackStack(null);
                transaction.commit();
                //transaction.addToBackStack(null);

            }
        });
        tv_SignOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
        //                FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //                transaction.replace(R.id.fragment_container, new HomeFragment());
        //                //transaction.addToBackStack(null);
        //                transaction.commit();
                        Toast.makeText(getContext(), "In Progress", Toast.LENGTH_SHORT).show();
                    }
                });

        tv_setStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showStatus){
                    rg_Status.setVisibility(View.VISIBLE);
                    showStatus=true;
                }else{
                    rg_Status.setVisibility(View.GONE);
                    showStatus=false;
                }
            }
        });

        tv_setTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showTheme){
                    rg_Theme.setVisibility(View.VISIBLE);
                    showTheme=true;
                }else{
                    rg_Theme.setVisibility(View.GONE);
                    showTheme=false;
                }

            }
        });

        tv_Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!showFeedback){
                    ll_showFeedback.setVisibility(View.VISIBLE);
                    showFeedback=true;
                }else{
                    ll_showFeedback.setVisibility(View.GONE);
                    showFeedback=false;
                }

            }
        });







        RadioButton rb_Light=view.findViewById(R.id.rb_Light);
        RadioButton rb_Dark=view.findViewById(R.id.rb_Dark);
        rb_Light.setSelected(true);

        rb_Light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Light Mode selected", Toast.LENGTH_SHORT).show();
            }
        });
        rb_Dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Dark Mode selected", Toast.LENGTH_SHORT).show();
            }
        });



        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fedback=et_Feedback.getText().toString();


            }
        });

        RadioButton rb_Online=view.findViewById(R.id.rb_Online);
        RadioButton rb_Busy=view.findViewById(R.id.rb_Busy);
        RadioButton rb_Offline=view.findViewById(R.id.rb_Offline);
        rb_Light.setSelected(true);

        rb_Online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refUserStatus= FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                refUserStatus.child("Status").setValue("Online");

                Toast.makeText(getContext(), "Online selected", Toast.LENGTH_SHORT).show();
            }
        });
        rb_Busy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refUserStatus= FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                refUserStatus.child("Status").setValue("Busy");

                Toast.makeText(getContext(), "Busy selected", Toast.LENGTH_SHORT).show();
            }
        });
        rb_Offline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refUserStatus= FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                        refUserStatus.child("Status").setValue("Offline");

                        Toast.makeText(getContext(), "Offline selected", Toast.LENGTH_SHORT).show();
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
                HashMap<String,Object> map= new HashMap<>();
                map.put("token","");

                FirebaseDatabase.getInstance().getReference()
                        .child("Admin").child("Login Status").child(userID).updateChildren(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
//                        SessionManagement sessionManagement = new SessionManagement(user_Settings.this);
//                        //sessionManagement.removeSession();
//                        sessionManagement.removeSSession();
                                FirebaseAuth.getInstance().signOut();

                                Intent intent=new Intent(getContext(), getStarted.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                getActivity().finish();
                                Toast.makeText(getContext(), "You are Logged Out", Toast.LENGTH_LONG).show();

                            }
                        });

            }
        });

        return view;

    }

    private void showBtmNotification() {

        BottomSheetDialog bottomSheetDialoglogin=new BottomSheetDialog(getContext());
        bottomSheetDialoglogin.setCancelable(false);
        bottomSheetDialoglogin.setContentView(R.layout.btmfialog_notifysetting);

        SwitchMaterial sw_Direct=bottomSheetDialoglogin.findViewById(R.id.sw_Direct);
        SwitchMaterial sw_Mention=bottomSheetDialoglogin.findViewById(R.id.sw_Mention);
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