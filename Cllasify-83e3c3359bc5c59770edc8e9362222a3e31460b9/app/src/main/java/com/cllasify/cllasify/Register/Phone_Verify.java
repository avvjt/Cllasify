package com.cllasify.cllasify.Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cllasify.cllasify.Fragment.FeedFragment;
import com.cllasify.cllasify.Fragment.HomeFragment;
import com.cllasify.cllasify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Phone_Verify extends AppCompatActivity {

    Button verifyotp_btn;
    EditText enterotp_et;
    ProgressBar progressBar;
    String verificationcodebysystem,userphoneno,phone;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_verify_activity);
        verifyotp_btn=findViewById(R.id.verifyotp_btn);
        enterotp_et=findViewById(R.id.enterotp_et);
        progressBar=findViewById(R.id.progressbar);
        Intent intent=getIntent();
        userphoneno=intent.getStringExtra("phoneno");
        phone="+91"+userphoneno;
        Toast.makeText(this,""+userphoneno,Toast.LENGTH_SHORT).show();

        sendVerificationCodeToUser(phone);

        verifyotp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=enterotp_et.getText().toString().trim();

                if (code.isEmpty() || code.length()<6){
                    enterotp_et.setError("Wrong OTP..");
                    enterotp_et.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);

            }
        });

    }

    private void sendVerificationCodeToUser(String userphoneno) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                userphoneno,
                60,
                TimeUnit.SECONDS,
                Phone_Verify.this,
                mCallbacks);


//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(mAuth)
//                        .setPhoneNumber("+91 "+userphoneno)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(Phone_Verify.this)                 // Activity (for callback binding)
//                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationcodebysystem=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code=phoneAuthCredential.getSmsCode();

            if (code!=null) {
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);

            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(Phone_Verify.this,"Error:"+e.getMessage(),Toast.LENGTH_LONG).show();

        }
    };


    private void verifyCode(String codebyUser) {

        PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(verificationcodebysystem,codebyUser);
        signintheUserbyCredential(phoneAuthCredential);



    }

    private void signintheUserbyCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(Phone_Verify.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            showBottomDialog();
                        }
                        else{
                            Toast.makeText(Phone_Verify.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void showBottomDialog() {

        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(Phone_Verify.this);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(R.layout.btmdialog_studteach);

        LinearLayout student_ll=bottomSheetDialog.findViewById(R.id.student_ll);
        LinearLayout teacher_ll=bottomSheetDialog.findViewById(R.id.teacher_ll);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = currentUser.getUid();
        final String userName = currentUser.getDisplayName();
        final String userEmail = currentUser.getEmail();
        Calendar calenderCC= Calendar.getInstance();
        SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        final String udateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());

        student_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                refUserRegister.child( "Name" ).setValue( userName );
                refUserRegister.child( "Email id" ).setValue( userEmail );
                refUserRegister.child( "UserId" ).setValue( userID );
                refUserRegister.child( "Date & Time" ).setValue( udateTimeCC );
                refUserRegister.child( "Category" ).setValue("Student");
//                startActivity(new Intent(Phone_Verify.this, FeedFragment.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

                Fragment fragment=null;
                FragmentTransaction ft;
                fragment = new FeedFragment();
//                ft = getSupportFragmentManager().beginTransaction();

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//                ft = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.fragment_container, fragment,"feed");
                fragmentTransaction.commit();

                Toast.makeText(Phone_Verify.this, "Firebase Authentication Sucessful", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });
        teacher_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                refUserRegister.child( "Name" ).setValue( userName );
                refUserRegister.child( "Email id" ).setValue( userEmail );
                refUserRegister.child( "UserId" ).setValue( userID );
                refUserRegister.child( "Date & Time" ).setValue( udateTimeCC );
                refUserRegister.child( "Category" ).setValue("Teacher");
                Fragment fragment=null;
                FragmentTransaction ft;
                fragment = new HomeFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.fragment_container, fragment,"home");
                ft.commit();
                Toast.makeText(Phone_Verify.this, "Firebase Authentication Sucessful", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();

    }

}