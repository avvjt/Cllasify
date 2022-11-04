package com.cllasify.cllasify.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cllasify.cllasify.ModelClasses.Class_Notice;
import com.cllasify.cllasify.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Show_Notice extends AppCompatActivity {

    String title, notes, date, docs;
    TextView notesDate, notesTitle, notesData;
    RelativeLayout pdfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notice);

        title = getIntent().getStringExtra("title");
        notes = getIntent().getStringExtra("notes");
        date = getIntent().getStringExtra("date");
        docs = getIntent().getStringExtra("docs");


        notesDate = findViewById(R.id.notesDate);
        notesTitle = findViewById(R.id.notesTitle);
        notesData = findViewById(R.id.notesData);
        pdfFile = findViewById(R.id.document);

        notesDate.setText("Created on: " + date);
        notesTitle.setText(title);
        notesData.setText(notes);

        if(!docs.equals("null")){
            pdfFile.setVisibility(View.VISIBLE);
        }else{
            pdfFile.setVisibility(View.GONE);
        }

        pdfFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Show_Notice.this, WebViewActivity.class);
                intent.putExtra("pdfUrl", docs);
                startActivity(intent);

            }
        });


    }
}