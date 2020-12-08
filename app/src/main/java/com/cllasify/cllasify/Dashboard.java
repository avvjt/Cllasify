package com.cllasify.cllasify;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        int i=1;
        if(i==1)
            print("done");
        else
            print("not done");
    }
}