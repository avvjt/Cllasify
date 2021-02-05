package com.cllasify.cllasify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class User_Profile extends AppCompatActivity {
    private Button btn_signout,btn_feed;
    private TextView name,email;
    private ImageView prof_pic;
    FirebaseAuth firebaseAuth;

    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);


        prof_pic=findViewById(R.id.prof_pic);
        name=findViewById(R.id.prof_name);
        btn_signout=findViewById(R.id.btn_signout);
        btn_feed=findViewById(R.id.btn_feed);
        email=findViewById(R.id.prof_email);

        firebaseAuth=FirebaseAuth.getInstance();

        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if (firebaseUser!=null){
            Glide.with(User_Profile.this)
                    .load(firebaseUser.getPhotoUrl())
                    .into(prof_pic);
            name.setText(firebaseUser.getDisplayName());
            email.setText(firebaseUser.getEmail());

        }

        googleSignInClient = GoogleSignIn.getClient(User_Profile.this, GoogleSignInOptions.DEFAULT_SIGN_IN);
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseAuth.signOut();
                        Toast.makeText(User_Profile.this, "User SUcessfully signout", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(User_Profile.this,Dashboard .class));
                        finish();

                    }
                });
            }
        });
        btn_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(User_Profile.this,Landing_Feed.class));
                finish();

            }
        });
    }
}
