package com.cllasify.cllasify.Fragment.Feed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cllasify.cllasify.Adaptor.Adaptor_QueryAnswer;
import com.cllasify.cllasify.R;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class User_Question extends AppCompatActivity {

    EditText addAnswer_et;
    TextView postAnswer_tv, dispQues_tv,QuesAskedByTime_tv;
    int positionQues;
    String quesUserName,userdbName,publishedDate,push;
    String question,questionCategory;
    Adaptor_QueryAnswer answerAdaptor;
    RecyclerView recyclerView;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String userID,userEmailID;
    List<Class_Answer> list_answer;
    Class_Answer addAnswer,userAddQues,addQuesCategory,addQuesUsers,addQuesUsersCategory,addQuesUsersPrivacy,adduserAnsPrivacy;
    DatabaseReference addAllAnsRef,showAnsRef, userAllAnsRef,addAnsCategoryRef,userAnsCategoryRef,userAnsPrivacyRef;
    RelativeLayout rl_bottom;
    ImageButton ib_likeQues,ib_dislikeQues,ib_flagQues;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_question_activity);

        recyclerView.findViewById(R.id.rv_QuestionList);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Your QuestionsTab");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        list_answer = new ArrayList<>();

        answerAdaptor = new Adaptor_QueryAnswer(this, list_answer);
        recyclerView.setAdapter(answerAdaptor);

        showAnsRef= FirebaseDatabase.getInstance().getReference().child( "Feed" ).child( "User_Questions" ).child( "User_All_Question" ).child(userID);
        answerAdaptor.setOnItemClickListener(new Adaptor_QueryAnswer.OnItemClickListener() {
//            @Override
//            public void shareQues(int position, String question) {
//
//            }

            @Override
            public void listitem(int position, String question,String pushQues,String pushAns,String category) {

//                showbtmDialogComment(position,question,pushQues,pushAns,category);

            }
        });


        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.hasChildren()) {
                    addAnswer = dataSnapshot.getValue(Class_Answer.class);
                    list_answer.add(addAnswer);
                    answerAdaptor.notifyDataSetChanged();


                } else {
//                    Toast.makeText(Landing_Feed_Answer.this, "No one Answer yet,Be first to give Answer", Toast.LENGTH_SHORT).show();

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
//                    class_Answers AnswersTab=snapshot1.getValue(class_Answers.class);
//                    list_answer.add(AnswersTab);
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
}