package com.cllasify.cllasify;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cllasify.cllasify.Fragment.HomeFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link ProfileSettingFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class ProfileSettingFragment extends Fragment {

    LinearLayout ll_UserName,ll_Name,ll_PhoneNo,ll_Instituion,ll_Email,ll_Location,ll_Bio;
    DatabaseReference refUserStatus;
    FirebaseUser currentUser;
    String userID,userName,userEmail;
    TextView tv_UserName,tv_Name,tv_Email,tv_Institution,tv_Phone,tv_Location,tv_Bio,tv_ShowUserName,tv_ChangeProfileImage;

    Button btn_Cancel;

    CircleImageView prof_pic;
    StorageReference storageReference;
    int TAKE_IMAGE_CODE=1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.profile_setting_fragment, container, false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentUser.getUid();
        userName = currentUser.getDisplayName();
        userEmail = currentUser.getEmail();
//        userPhoto = currentUser.getPhotoUrl();

        tv_ShowUserName=view.findViewById(R.id.tv_ShowUserName);
        tv_Bio=view.findViewById(R.id.tv_Bio);
        tv_UserName=view.findViewById(R.id.tv_UserName);
        tv_Name=view.findViewById(R.id.tv_Name);
        tv_Location=view.findViewById(R.id.tv_Location);
        tv_Institution=view.findViewById(R.id.tv_Institution);
        tv_Phone=view.findViewById(R.id.tv_Phone);
        tv_Email=view.findViewById(R.id.tv_Email);
        tv_ChangeProfileImage=view.findViewById(R.id.tv_ChangeProfileImage);
        prof_pic=view.findViewById(R.id.prof_pic);

        btn_Cancel=view.findViewById(R.id.btn_Cancel);

        ll_UserName=view.findViewById(R.id.ll_UserName);
        ll_Name=view.findViewById(R.id.ll_Name);
        ll_Location=view.findViewById(R.id.ll_Location);
        ll_Instituion=view.findViewById(R.id.ll_Institution);
        ll_PhoneNo=view.findViewById(R.id.ll_Phone);
        ll_Email=view.findViewById(R.id.ll_Email);
        ll_Bio=view.findViewById(R.id.ll_Bio);

        storageReference=FirebaseStorage.getInstance().getReference();


        tv_ChangeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openGalleryIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (openGalleryIntent.resolveActivity(getContext().getPackageManager())!=null){

                    startActivityForResult(openGalleryIntent,TAKE_IMAGE_CODE);
                }
//                startActivityForResult(openGalleryIntent,1000);
            }
        });

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container, new AccountSettingFragment());
                                //transaction.addToBackStack(null);
                                transaction.commit();

            }
        });

        tv_ShowUserName.setText(userName);
//        tv_ShowUserName.setText(userEmail);

        ll_UserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=tv_UserName.getText().toString();
                editSetting("NickName",userName);
            }
        });
        ll_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name=tv_Name.getText().toString();
                editSetting("Name",Name);

            }
        });
        ll_Instituion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Institution=tv_Institution.getText().toString();
                editSetting("Insitution",Institution);

            }
        });
        ll_Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Location=tv_Location.getText().toString();
                editSetting("Location",Location);

            }
        });
        ll_Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email=tv_Email.getText().toString();
                editSetting("Email",Email);
            }
        });
        ll_PhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Phone=tv_Phone.getText().toString();
                editSetting("Phone",Phone);
            }
        });
        ll_Bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Bio=tv_Bio.getText().toString();
                editSetting("Bio",Bio);
            }
        });



        showProfile();
        return view;
    }

    private void showProfile() {
        refUserStatus= FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        refUserStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){

                    if (snapshot.child("Bio").exists()){
                        String bio=snapshot.child("Bio").getValue().toString();
                        tv_Bio.setText(bio);
                    }else{
                        tv_Bio.setText("add");
                    }
                    if (snapshot.child("Insitution").exists()){
                      String Institute=snapshot.child("Insitution").getValue().toString();
                      tv_Institution.setText(Institute);
                    }else{
                        tv_Institution.setText("add");
                    }
                    if (snapshot.child("NickName").exists()){
                      String NickName=snapshot.child("NickName").getValue().toString();
                      tv_UserName.setText(NickName);
                    }else{
                        tv_UserName.setText("add");
                    }
                    if (snapshot.child("Name").exists()){
                        String Name=snapshot.child("Name").getValue().toString();
                        tv_Name.setText(Name);
                    }else{
                        tv_Name.setText("add");
                    }
                    if (snapshot.child("Phone").exists()){
                      String Phone=snapshot.child("Phone").getValue().toString();
                      tv_Phone.setText(Phone);
                   }else{
                        tv_Phone.setText("add");
                    }
                    if (snapshot.child("Location").exists()){
                        String Location=snapshot.child("Location").getValue().toString();
                        tv_Location.setText(Location);
                    }else{
                        tv_Location.setText("add");
                    }
                    if (snapshot.child("userEmailId").exists()){
                        String email=snapshot.child("userEmailId").getValue().toString();
                        tv_Email.setText(email);
                    }else{
                        tv_Location.setText("add");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
//
//        refUserFollowers= FirebaseDatabase.getInstance().getReference().child("Users").child("Followers").child(userID);
//        refUserFollowers.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getChildrenCount()>0){
//                    long count=snapshot.getChildrenCount();
//                    tv_CountFollowers.setText((int) count+" Followers");
//                    notifyPB.dismiss();
////                        notifyPB.show();
//                }else{
//                    tv_CountFollowers.setText("No Followers");
//
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//        refUserFollowing= FirebaseDatabase.getInstance().getReference().child("Users").child("Following").child(userID);
//        refUserFollowing.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.getChildrenCount()>0){
//                    long count=snapshot.getChildrenCount();
//                    tv_CountFollowing.setText((int) count+" Following");
//                    notifyPB.dismiss();
//                }else {
//                    tv_CountFollowing.setText("No Following");
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });


    }

    private void editSetting(String title,String userData) {

        BottomSheetDialog bottomSheetDialoglogin=new BottomSheetDialog(getContext());
        bottomSheetDialoglogin.setCancelable(false);
        bottomSheetDialoglogin.setContentView(R.layout.btmdialog_setting);

        Button btn_Cancel=bottomSheetDialoglogin.findViewById(R.id.btn_Cancel);
        TextView tv_subTitle=bottomSheetDialoglogin.findViewById(R.id.tv_subTitle);
        TextView tv_SettingTitle=bottomSheetDialoglogin.findViewById(R.id.tv_SettingTitle);
        EditText et_NewDetails=bottomSheetDialoglogin.findViewById(R.id.et_NewDetails);
        Button btn_Submit=bottomSheetDialoglogin.findViewById(R.id.btn_Submit);


        tv_subTitle.setText("Enter new "+userData);
        et_NewDetails.setText(userData);
        tv_SettingTitle.setText("Please update"+userData);

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            bottomSheetDialoglogin.dismiss();
            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.equals("NickName")){
                    String username=et_NewDetails.getText().toString().trim();
                    refUserStatus.child("NickName").setValue(username);

                }else if(title.equals("Name")){
                    String name=et_NewDetails.getText().toString().trim();
                    refUserStatus.child("Name").setValue(name);

                }else if(title.equals("Insitution")){
                    String institution=et_NewDetails.getText().toString().trim();
                    refUserStatus.child("Insitution").setValue(institution);

                }else if(title.equals("Bio")){
                    String bio=et_NewDetails.getText().toString().trim();
                    refUserStatus.child("Bio").setValue(bio);

                }else if(title.equals("Phone")){
                    String phone=et_NewDetails.getText().toString().trim();
                    refUserStatus.child("Phone").setValue(phone);

                }else if(title.equals("Email")){
                    String email=et_NewDetails.getText().toString().trim();
                    refUserStatus.child("userEmailId").setValue(email);

                }else if (title.equals("Location")){
                    String location=et_NewDetails.getText().toString().trim();
                    refUserStatus.child("Location").setValue(location);

                }

            bottomSheetDialoglogin.dismiss();
            }
        });
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode==TAKE_IMAGE_CODE){
            if (requestCode== Activity.RESULT_OK){
                Bitmap bitmap= (Bitmap) data.getExtras().get("data");
                prof_pic.setImageBitmap(bitmap);

//                Uri galleryImageUri=data.getData();
//                prof_pic.setImageURI(galleryImageUri);

//                uploadImagetoFirebaseStorage(galleryImageUri);

            }

        }

    }

    private void uploadImagetoFirebaseStorage(Uri galleryImageUri) {

        StorageReference imageRef=storageReference.child(userID).child("ProfilePic.jpg");
        imageRef.putFile(galleryImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "Image Successfully updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Image Upload Failure", Toast.LENGTH_SHORT).show();
            }
        });

    }
}