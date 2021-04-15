package com.cllasify.cllasify.Register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cllasify.cllasify.R;

public class Phone_Login extends AppCompatActivity {
    Button submit_btn;
    EditText phoneno_et;
    String userph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_login_activity);

        submit_btn=findViewById(R.id.submit_btn);
        phoneno_et=findViewById(R.id.phoneno_et);


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Phone_Login.this,Phone_Verify.class);
                userph=phoneno_et.getText().toString().trim();
                intent.putExtra("phoneno",userph);
                startActivity(intent);
                Toast.makeText(Phone_Login.this, "userph"+userph, Toast.LENGTH_SHORT).show();
            }
        });

    }
}