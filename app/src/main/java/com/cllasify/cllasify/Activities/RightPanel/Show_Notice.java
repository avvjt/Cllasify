package com.cllasify.cllasify.Activities.RightPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cllasify.cllasify.Activities.WebViewActivity;
import com.cllasify.cllasify.R;

public class Show_Notice extends AppCompatActivity {

    String title, notes, date, docs;
    TextView notesDate, notesTitle, notesData;
    RelativeLayout pdfFile;
    ImageButton btn_Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notice);

        btn_Back = findViewById(R.id.btn_Back);

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

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