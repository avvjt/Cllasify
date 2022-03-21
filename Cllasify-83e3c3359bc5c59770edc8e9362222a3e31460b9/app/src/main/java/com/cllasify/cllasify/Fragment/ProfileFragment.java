package com.cllasify.cllasify.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.AccountSettingFragment;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Register.getStarted;
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

public class ProfileFragment extends Fragment {

    ChipNavigationBar chipNavigationBar;
    CircleImageView prof_pic;
    BottomNavigationView bottom_nav;
    LinearLayout ll_bio,ll_Institution,ll_location,ll_UserName;
    Boolean showSpinner=false,showUserName=false,showInstitute=false,showUserBio=false,showSpinnerCategory=false;


    TextView tv_UserName,tv_Name
            ,tv_addBio,tv_UserBio
            ,tv_addInstitute,tv_UserInstitute
            ,tv_addUserName,tv_UserUserName
            ,tv_addLocation,tv_UserLocation
            ,tv_addCategory,tv_UserCategory
            ,tv_CountFollowing,tv_CountFollowers;

    DatabaseReference refUserStatus,refUserFollowers,refUserFollowing;

    LinearLayout ll_AddBio,
            ll_AddInstitute,
            ll_AddUserName,
    ll_AddCategory;

    Button btn_AddBio, btn_AddBioCancel,
            btn_AddInstitute, btn_AddInstituteCancel,
            btn_AddUserName, btn_AddUserNameCancel;

    ImageButton ib_ShareApp, ib_Settings;

    EditText et_AddBio,
            et_AddInstitute,
            et_AddUserName;

    FirebaseUser currentUser;
    String userID,userName,userEmail;
    Uri userPhoto;
    ProgressDialog notifyPB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            bottom_nav = getActivity().findViewById(R.id.bottom_nav);


            notifyPB = new ProgressDialog(getContext());
            notifyPB.setTitle("Govt Jobs");
            notifyPB.setMessage("Loading All Jobs");
            notifyPB.setCanceledOnTouchOutside(true);

            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            userID = currentUser.getUid();
            userName = currentUser.getDisplayName();
            userEmail = currentUser.getEmail();
            userPhoto = currentUser.getPhotoUrl();


            tv_UserName = view.findViewById(R.id.tv_UserName);
            tv_Name = view.findViewById(R.id.tv_Name);
            prof_pic = view.findViewById(R.id.prof_pic);

            refUserStatus= FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
            refUserStatus.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount()>0){

                        if (snapshot.child("profilePic").exists()){
                            String profilePic=snapshot.child("profilePic").getValue().toString();
                            Glide.with(getContext()).load(profilePic).into(prof_pic);
                        }else{
                            Picasso.get().load(userPhoto).into(prof_pic);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            tv_UserBio = view.findViewById(R.id.tv_UserBio);
            tv_UserInstitute = view.findViewById(R.id.tv_UserInstitute);
            tv_UserUserName = view.findViewById(R.id.tv_UserUserName);
            tv_UserLocation = view.findViewById(R.id.tv_userLocation);

            ll_bio = view.findViewById(R.id.ll_bio);
            ll_Institution = view.findViewById(R.id.ll_Institution);
            ll_location = view.findViewById(R.id.ll_location);
            ll_UserName = view.findViewById(R.id.ll_UserName);


            tv_UserCategory = view.findViewById(R.id.tv_userCategory);
            tv_addCategory = view.findViewById(R.id.tv_addCategory);
            ll_AddCategory = view.findViewById(R.id.ll_AddCategory);

            tv_CountFollowers = view.findViewById(R.id.tv_CountFollowers);
            tv_CountFollowing = view.findViewById(R.id.tv_CountFollowing);

            ib_Settings = view.findViewById(R.id.btn_Settings);
            ib_ShareApp = view.findViewById(R.id.btn_ShareApp);


            ib_Settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment fragment = null;
                    FragmentTransaction transaction;
                    fragment = new AccountSettingFragment();
                    transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment, "AccountSettingFragment");
                    transaction.addToBackStack("AccountSettingFragment");
                    transaction.commit();
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

        } else {
            startActivity(new Intent(getContext(), getStarted.class));
        }
        return view;
    }
//        private void addLocation() {
//            tv_addLocation.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (!showSpinner){
//                        spinnerLocation.setVisibility(View.VISIBLE);
//                        showSpinner=true;
//                    }else{
//                        spinnerLocation.setVisibility(View.GONE);
//                        showSpinner=false;
//                    }
//
//
//                }
//            });
//
//            ArrayAdapter<String> adapterSex = new ArrayAdapter<String>(getContext(), android.
//                    R.layout.simple_spinner_dropdown_item, Location);
//            spinnerLocation.setAdapter(adapterSex);
//            spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,
//                                           int position, long id) {
//                    // Get select item
//                    int sexPosition = spinnerLocation.getSelectedItemPosition();
//                    if (sexPosition != 0) {
//                        String Sex = Location[sexPosition];
//                        refUserStatus= FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
//                        refUserStatus.child("Location").setValue(Sex);
//                        tv_UserLocation.setText(Sex);
//                        tv_addLocation.setVisibility(View.GONE);
//                    } else {
////                        Toast.makeText(getBaseContext(), "Please Select Sex Category",
////                                Toast.LENGTH_LONG).show();
//                        ((TextView) spinnerLocation.getChildAt(0)).setError("Please Select Location");
//                    }
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });
//        }

//        private void addCategory() {
//            tv_addCategory.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (!showSpinnerCategory){
//                        ll_AddCategory.setVisibility(View.VISIBLE);
//                        spinnerCategory.setVisibility(View.VISIBLE);
//                        showSpinnerCategory=true;
//                    }else{
//                        ll_AddCategory.setVisibility(View.GONE);
//                        spinnerCategory.setVisibility(View.GONE);
//                        showSpinnerCategory=false;
//                    }
//                }
//            });
//
//            ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(getContext(), android.
//                    R.layout.simple_spinner_dropdown_item, Category);
//            spinnerCategory.setAdapter(adapterCategory);
//            spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view,
//                                           int position, long id) {
//                    // Get select item
//                    int sexPosition = spinnerCategory.getSelectedItemPosition();
//                    if (sexPosition != 0) {
//                        String category = Category[sexPosition];
//                        refUserStatus= FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
//                        refUserStatus.child("Category").setValue(category);
////                        Toast.makeText(getContext(),"selection"+category,Toast.LENGTH_SHORT).show();
//                        tv_UserCategory.setVisibility(View.VISIBLE);
//                        tv_UserCategory.setText(category);
//
//                        tv_addCategory.setVisibility(View.GONE);
//                        spinnerCategory.setVisibility(View.GONE);
//                    } else {
////                        Toast.makeText(getBaseContext(), "Please Select Sex Category",
////                                Toast.LENGTH_LONG).show();
//                        ((TextView) spinnerCategory  .getChildAt(0)).setError("Please Select Location");
//
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });
//
//        }


        private void addInstitution() {

            tv_addInstitute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!showInstitute){
                        ll_AddInstitute.setVisibility(View.VISIBLE);
                        showInstitute=true;
                    }else{
                        ll_AddInstitute.setVisibility(View.GONE);
                        showInstitute=false;
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
                    String user_Institute=et_AddInstitute.getText().toString();
                    if(!user_Institute.equals("")){
                        ll_AddInstitute.setVisibility(View.GONE);
                        refUserStatus.child( "Institute" ).setValue( user_Institute );
                        tv_UserInstitute.setVisibility(View.VISIBLE);
                        tv_UserInstitute.setText(user_Institute);
                    }else{
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
                    if (!showUserName){
                        ll_AddUserName.setVisibility(View.VISIBLE);
                        showUserName=true;
                    }else{
                        ll_AddUserName.setVisibility(View.GONE);
                        showUserName=false;
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
                    String user_UserName=et_AddUserName.getText().toString();
                    if(!user_UserName.equals("")){
                        ll_AddUserName.setVisibility(View.GONE);
                        refUserStatus.child( "UserName" ).setValue( user_UserName );
                        tv_UserUserName.setVisibility(View.VISIBLE);
                        tv_UserUserName.setText(user_UserName);

                    }else{
                        et_AddUserName.setError("Enter Institute");

                    }

                }
            });


        }
        private void addBio() {

            tv_addBio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!showUserBio){
                        ll_AddBio.setVisibility(View.VISIBLE);
                        showUserBio=true;
                    }else{
                        ll_AddBio.setVisibility(View.GONE);
                        showUserBio=false;
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
                    String user_Bio=et_AddBio.getText().toString();
                    if(!user_Bio.equals("")){
                        ll_AddBio.setVisibility(View.GONE);
                        refUserStatus.child( "Bio" ).setValue( user_Bio );
                    }else{
                        et_AddBio.setError("Enter Bio");

                    }

                }
            });


        }
        private void showProfile() {
            refUserStatus= FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
            refUserStatus.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount()>0){
                        if (snapshot.child("Bio").exists()){
//                            tv_addBio.setVisibility(View.GONE);
                            String bio=snapshot.child("Bio").getValue().toString();
//                            tv_UserBio.setVisibility(View.VISIBLE);
                            ll_bio.setVisibility(View.VISIBLE);
                            tv_UserBio.setText(bio);
                            notifyPB.dismiss();
                        }else{
                            ll_bio.setVisibility(View.GONE);
//                            tv_addBio.setVisibility(View.VISIBLE);

                        }

                        if (snapshot.child("Insitution").exists()){
//                            tv_addInstitute.setVisibility(View.GONE);
                            String bio=snapshot.child("Insitution").getValue().toString();
//                            tv_UserInstitute.setVisibility(View.VISIBLE);
                            ll_Institution.setVisibility(View.VISIBLE);
                            tv_UserInstitute.setText(bio);

                            notifyPB.dismiss();
                        }else{
//                            tv_addInstitute.setVisibility(View.VISIBLE);
                            ll_Institution.setVisibility(View.GONE);

                        }


                        if (snapshot.child("NickName").exists()){
//                            tv_addUserName.setVisibility(View.GONE);
                            String UserName=snapshot.child("NickName").getValue().toString();
                            tv_UserUserName.setVisibility(View.VISIBLE);
                            tv_UserUserName.setText(UserName);
                            ll_UserName.setVisibility(View.VISIBLE);
                            notifyPB.dismiss();
                        }else{
//                            tv_addUserName.setVisibility(View.VISIBLE);
                            ll_UserName.setVisibility(View.GONE);


                        }
                        if (snapshot.child("Location").exists()){
//                            tv_addLocation.setVisibility(View.GONE);
//                            spinnerLocation.setVisibility(View.GONE);
                            ll_location.setVisibility(View.VISIBLE);
                            String Location=snapshot.child("Location").getValue().toString();
//                            tv_UserLocation.setVisibility(View.VISIBLE);
                            tv_UserLocation.setText(Location);
                            notifyPB.dismiss();
                        }else{
//                            tv_addLocation.setVisibility(View.VISIBLE);
                            ll_location.setVisibility(View.GONE);

                        }

                        if (snapshot.child("Category").exists()){
                            tv_addCategory.setVisibility(View.GONE);
//                            spinnerCategory.setVisibility(View.GONE);

                            String Category=snapshot.child("Category").getValue().toString();
                            tv_UserCategory.setVisibility(View.VISIBLE);
                            tv_UserCategory.setText(Category);
                            notifyPB.dismiss();
                        }else{
                            tv_addCategory.setVisibility(View.VISIBLE);

                        }
//                        notifyPB.show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            refUserFollowers= FirebaseDatabase.getInstance().getReference().child("Users").child("Followers").child(userID);
            refUserFollowers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount()>0){
                        long count=snapshot.getChildrenCount();
                        tv_CountFollowers.setText((int) count+" Followers");
                        notifyPB.dismiss();
//                        notifyPB.show();
                    }else{
                        tv_CountFollowers.setText("No Followers");

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

            refUserFollowing= FirebaseDatabase.getInstance().getReference().child("Users").child("Following").child(userID);
            refUserFollowing.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount()>0){
                        long count=snapshot.getChildrenCount();
                        tv_CountFollowing.setText((int) count+" Following");
                        notifyPB.dismiss();
                    }else {
                        tv_CountFollowing.setText("No Following");
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


        }

    }
