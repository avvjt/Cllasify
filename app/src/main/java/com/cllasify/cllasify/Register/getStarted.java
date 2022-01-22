package com.cllasify.cllasify.Register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.cllasify.cllasify.Home.Discover_Activity;
import com.cllasify.cllasify.Home.Server_Activity;
import com.cllasify.cllasify.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class getStarted extends AppCompatActivity {

    GoogleSignInClient googleSignInClient;
    Button btn_SignIn;
    ProgressDialog notifyPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Cllasify);
        setContentView(R.layout.activity_get_started);

        //initialize() AdMob
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs",MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn",false);
        if (isDarkModeOn){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        btn_SignIn=findViewById(R.id.btn_SignIn);
        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyPB = new ProgressDialog(getStarted.this);
                notifyPB.setTitle("Govt Jobs");
                notifyPB.setMessage("Loading All Jobs");
                notifyPB.setCanceledOnTouchOutside(true);
                notifyPB.show();

                // Configure Google Sign In
                GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("1085537073642-dq2djhhvidcgmb4c3a5ushet55jk6hf5.apps.googleusercontent.com")
                        .requestEmail()
                        .build();


                googleSignInClient= GoogleSignIn.getClient(getStarted.this,googleSignInOptions);

                Intent intent=googleSignInClient.getSignInIntent();

                startActivityForResult(intent,100);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getStarted.this, "on activity result called", Toast.LENGTH_SHORT).show();

        if (requestCode==100){
            Toast.makeText(getStarted.this, "100", Toast.LENGTH_SHORT).show();

            Task<GoogleSignInAccount> signInAccountTask= GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            Toast.makeText(getStarted.this, "checking", Toast.LENGTH_SHORT).show();


            if (signInAccountTask.isSuccessful()){
//                String s= "Google Signin is sucessful";
//                Toast.makeText(getStarted.this, s, Toast.LENGTH_SHORT).show();
                try {
                    GoogleSignInAccount googleSignInAccount=signInAccountTask.getResult(ApiException.class);
                    if (googleSignInAccount!=null){
                        AuthCredential authCredential= GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
                        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(getStarted.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    submitLoginData();
                                    Log.i("shitt0", "login successful,el");
//                                    showStudTeachBtmDialog();
//                                    chipNavigationBar.setVisibility(View.VISIBLE);
                                }else{
                                    Log.i("shitt22", task.getException().toString());
                                    Toast.makeText(getStarted.this, "Authentication Failed: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(getStarted.this, "google account null", Toast.LENGTH_SHORT).show();

                    }


                } catch (ApiException e) {
                    e.printStackTrace();
                    Log.i("shitt1", e.toString());
                }
            }else{
                Log.i("shitt2", signInAccountTask.getException().toString());
            }

        }
        else{
            Toast.makeText(getStarted.this, "wrong request code", Toast.LENGTH_SHORT).show();

        }
    }

    private void submitLoginData() {
//        rl_feed.setBackgroundColor(Color.GRAY);
//        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(getStarted.this);
//        bottomSheetDialog.setCancelable(false);
//        bottomSheetDialog.setContentView(R.layout.btmdialog_studteach);
//
//        LinearLayout student_ll=bottomSheetDialog.findViewById(R.id.student_ll);
//        LinearLayout teacher_ll=bottomSheetDialog.findViewById(R.id.teacher_ll);


        StorageReference storageReference;
        storageReference= FirebaseStorage.getInstance().getReference();

        Calendar calenderCC= Calendar.getInstance();
        SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        final String udateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(getStarted.this, "Token Failed"+task.getException().toString(), Toast.LENGTH_SHORT).show();
//                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        String userID = currentUser.getUid();
                        String Name = currentUser.getDisplayName();
                        String userEmailID = currentUser.getEmail();
                        Uri userPhoto = currentUser.getPhotoUrl();

                        // Get new FCM registration token
                        String token = task.getResult();
                        DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                        refUserRegister.child( "Name" ).setValue( Name );
                        refUserRegister.child( "userEmailId" ).setValue( userEmailID );
                        refUserRegister.child( "userId" ).setValue( userID );
                        refUserRegister.child( "dateTime" ).setValue( udateTimeCC );
                        refUserRegister.child( "userStatus" ).setValue("Online");
                        refUserRegister.child( "token" ).setValue(token);
                        refUserRegister.child("profilePic").setValue(userPhoto.toString());
                        notifyPB.dismiss();

                        Toast.makeText(getStarted.this, "Login Successful", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(getStarted.this, Server_Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

    }
//    private void showStudTeachBtmDialog() {
////        rl_feed.setBackgroundColor(Color.GRAY);
//        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(getStarted.this);
//        bottomSheetDialog.setCancelable(false);
//        bottomSheetDialog.setContentView(R.layout.btmdialog_studteach);
//
//        LinearLayout student_ll=bottomSheetDialog.findViewById(R.id.student_ll);
//        LinearLayout teacher_ll=bottomSheetDialog.findViewById(R.id.teacher_ll);
//
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        userID = currentUser.getUid();
//        userName = currentUser.getDisplayName();
//        userEmailID = currentUser.getEmail();
//        userPhoto = currentUser.getPhotoUrl();
//        Calendar calenderCC= Calendar.getInstance();
//        SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
//        final String udateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());
//
//        student_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
//                refUserRegister.child( "Name" ).setValue( userName );
//                refUserRegister.child( "Email" ).setValue( userEmailID );
////                refUserRegister.child( "photo" ).setValue( userPhoto );
//                refUserRegister.child( "UserId" ).setValue( userID );
//                refUserRegister.child( "DateTime" ).setValue( udateTimeCC );
//                refUserRegister.child( "Category" ).setValue("Student");
////                startActivity(new Intent(getStarted.this,Landing_Activity.class)
////                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                Toast.makeText(getStarted.this, "Login Sucessful as Student", Toast.LENGTH_SHORT).show();
//                bottomSheetDialog.dismiss();
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
//
//            }
//        });
//        teacher_ll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
//                refUserRegister.child( "Name" ).setValue( userName );
//                refUserRegister.child( "Email" ).setValue( userEmailID );
////                refUserRegister.child( "photo" ).setValue( userPhoto );
//                refUserRegister.child( "UserId" ).setValue( userID );
//                refUserRegister.child( "DateTime" ).setValue( udateTimeCC );
//                refUserRegister.child( "Category" ).setValue("Teacher");
////                startActivity(new Intent(getStarted.this,Landing_Activity.class)
////                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//                Toast.makeText(getStarted.this, "Login Sucessful as Teacher", Toast.LENGTH_SHORT).show();
//                bottomSheetDialog.dismiss();
//
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(HomeFragment.this).attach(HomeFragment.this).commit();
//            }
//        });
//
//        bottomSheetDialog.show();
//
//    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
//            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,new HomeFragment(),"home").commit();

            Intent intent=new Intent(getStarted.this, Server_Activity.class);
            intent.putExtra("Check","appRestart");
            startActivity(intent);
            getStarted.this.overridePendingTransition(0,0);
        }

    }
}