package com.cllasify.cllasify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.cllasify.cllasify.Profile.AccountSetting_Activity;

public class Terms extends AppCompatActivity {

    ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        btn_back = findViewById(R.id.btn_Back);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Terms.this, AccountSetting_Activity.class);
                startActivity(i);
                (Terms.this).overridePendingTransition(0, 0);

            }
        });
    }
}