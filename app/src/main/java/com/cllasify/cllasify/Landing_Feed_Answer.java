package com.cllasify.cllasify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Landing_Feed_Answer extends AppCompatActivity {


    EditText addAnswer_et;
    TextView postAnswer_tv, dispQues_tv,QuesAskedByTime_tv;
    int positionQues;
    String publisherName,userdbName,publishedDate;
    String question;
    adaptor_QueryAnswers answerAdaptor;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String userID,userEmailID;
    List<class_Answer> list_answer;
    class_Answer addAnswer;
    DatabaseReference addAnsRef,showAnsRef,ansUserRef;
    RelativeLayout rl_bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_feed_answer_activity);

        rl_bottom=findViewById(R.id.bottom);
        rl_bottom.setVisibility(View.GONE);
        if (InternetConnection.checkConnection(getApplicationContext())) {

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                firebaseAuth = FirebaseAuth.getInstance();
                currentUser = firebaseAuth.getCurrentUser();
                rl_bottom.setVisibility(View.VISIBLE);
            }



            addAnswer_et=findViewById(R.id.add_Answer_et);
            postAnswer_tv=findViewById(R.id.post_Answer_tv);
            dispQues_tv =findViewById(R.id.displayQues_tv);
            QuesAskedByTime_tv =findViewById(R.id.QuesAskedByTime_tv);

            Intent intent= getIntent();
            positionQues =intent.getIntExtra("position",0);
            publisherName =intent.getStringExtra("publisherName");
            publishedDate =intent.getStringExtra("quesaskDate");
            question=intent.getStringExtra("quesask");

            dispQues_tv.setText(question);
            QuesAskedByTime_tv.setText("Asked by: "+publisherName+"on"+publishedDate);

            recyclerView=findViewById(R.id.rv_Answers);

            addAnsRef= FirebaseDatabase.getInstance().getReference().child("FeedQuestions").child("Answer_Ques").child(String.valueOf(positionQues));
            showAnsRef= FirebaseDatabase.getInstance().getReference().child("FeedQuestions").child("Answer_Ques").child(String.valueOf(positionQues));
            ansUserRef= FirebaseDatabase.getInstance().getReference().child("FeedQuestions").child("User_Answer").child(String.valueOf(positionQues));

            addAnsRef.keepSynced(true);
            showAnsRef.keepSynced(true);
            ansUserRef.keepSynced(true);

            list_answer=new ArrayList<>();
            answerAdaptor =new adaptor_QueryAnswers(this,list_answer);
            recyclerView.setAdapter(answerAdaptor);


            Toolbar toolbar=findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Answers");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


            final SwipeRefreshLayout swipeRefreshLayout;
            swipeRefreshLayout=findViewById(R.id.swipe);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // cancel the Visual indication of a refresh
                    swipeRefreshLayout.setRefreshing(false);
                    showAnswer(positionQues, publisherName);
                }
            });


            postAnswer_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addAnswer_et.getText().toString().equals("")){
                        addAnswer_et.setError("Enter Answer");
                        Toast.makeText(Landing_Feed_Answer.this,"Enter Answer",Toast.LENGTH_SHORT).show();

                    } else{
                        addAnswer(positionQues, publisherName,question);
                    }
                }
            });

            showAnswer(positionQues, publisherName);

        } else {
            Toast.makeText(this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    public void addAnswer(int position, String postid, String publisher) {
//        updateUser();
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
            dialogBuilder.setCanceledOnTouchOutside(false);
            //dialogBuilder.setCancelable(false);
            LayoutInflater inflater = this.getLayoutInflater();

            final View dialogView = inflater.inflate(R.layout.dialog_loginaccess, null);
            Button tapCancel=dialogView.findViewById(R.id.buttonCancel);
            Button tapSubmit=dialogView.findViewById(R.id.buttonSubmit);
//        int width = (int)(getResources().getDisplayMetrics().widthPixels*1.00);
//        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

            tapCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder.dismiss();
                }

            });
            tapSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                     }
            });
        }
        else {
            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID = currentUser.getUid();
            userEmailID = currentUser.getEmail();


            String rName = null, rPhoneno = null;
            String Answer = addAnswer_et.getText().toString();

//        SharedPreferences sp = getSharedPreferences("register", MODE_PRIVATE);
//        if (sp.contains("Name")) {
//            rName = sp.getString("Name", "");
//            rPhoneno = sp.getString("Mobile", "");
//        }
            Calendar calenderCC = Calendar.getInstance();
            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
            final String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());

            String positionQ = String.valueOf(position);

            addAnswer = new class_Answer(positionQ, Answer, dateTimeCC, userdbName, userID);
            addAnsRef.push().setValue(addAnswer);
//        HashMap<String,Object> hashMap=new HashMap<>();
//        hashMap.put("Answer",addAnswer_et.getText().toString());
//        hashMap.put("Postid",postid);
//        hashMap.put("Publisherid",userId);
//        hashMap.put("Publisher",rName);
//        hashMap.put("DateTime",dateTimeCC);
//        reference.push().setValue(hashMap);
            addAnswer_et.setText("");
        }
    }

    public void showAnswer(int postId, String publisher){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        list_answer = new ArrayList<>();

        answerAdaptor = new adaptor_QueryAnswers(this, list_answer);
        recyclerView.setAdapter(answerAdaptor);
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.hasChildren()) {
                    addAnswer = dataSnapshot.getValue(class_Answer.class);
                    list_answer.add(addAnswer);
                    answerAdaptor.notifyDataSetChanged();


                } else {
                    Toast.makeText(Landing_Feed_Answer.this, "No one Answer yet,Be first to give Answer", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        //refAdmin.addChildEventListener(childEventListener);
        showAnsRef.addChildEventListener(childEventListener);

//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                list_answer.clear();
//                for (DataSnapshot snapshot1:snapshot.getChildren()){
//                    class_Answers Answers=snapshot1.getValue(class_Answers.class);
//                    list_answer.add(Answers);
//
//                }
//                answerAdaptor.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }
//
//    private void updateUser() {
//        DatabaseReference refuserName = FirebaseDatabase.getInstance().getReference().child("Admin").child("UserRegister").child(userId);
//        refuserName.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userdbName = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
//                String rName,rEmail,userCName,userEmail;
//                SharedPreferences sp=getSharedPreferences("register",MODE_PRIVATE);
//                rName = sp.getString("Name", "");
//                rEmail=sp.getString("EMail","");
//                userEmail=currentUser.getEmail();
//                userCName=currentUser.getEmail();
//
//                if(userdbName.equals(null)) {
//                    if (rEmail.equals(userEmail)) {
//                        userdbName = rName;
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//    }

    @Override
    public void onStart() {
        super.onStart();
//        updateUser();

    }


}