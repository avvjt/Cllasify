package com.cllasify.cllasify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class About extends AppCompatActivity {
    LinearLayout cardView , cardView1;
    LinearLayout ll_details , ll_details1;
    LinearLayout Layout , Layout1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        cardView = findViewById(R.id.cardView);
        cardView1 = findViewById(R.id.cardView1);
        ll_details = findViewById(R.id.details);
        ll_details1 = findViewById(R.id.details1);
        Layout = findViewById(R.id.layout);
        Layout1 = findViewById(R.id.layout1);

        Layout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        Layout1.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int v = (ll_details.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
                TransitionManager.beginDelayedTransition(Layout, new AutoTransition());
                ll_details.setVisibility(v);
            }
        });
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int x = (ll_details1.getVisibility() == View.GONE)? View.VISIBLE: View.GONE;
                TransitionManager.beginDelayedTransition(Layout1, new AutoTransition());
                ll_details1.setVisibility(x);
            }
        });
    }
}