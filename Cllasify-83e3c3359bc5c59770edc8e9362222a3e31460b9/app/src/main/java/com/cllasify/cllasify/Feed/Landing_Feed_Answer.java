package com.cllasify.cllasify.Feed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cllasify.cllasify.Adaptor.Adaptor_QueryAnswer;
import com.cllasify.cllasify.Adaptor.Adaptor_QueryComment;
import com.cllasify.cllasify.Class.Class_Answer;
import com.cllasify.cllasify.Register.InternetConnection;
import com.cllasify.cllasify.Register.Phone_Login;
import com.cllasify.cllasify.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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

public class Landing_Feed_Answer extends AppCompatActivity {


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
        setContentView(R.layout.landing_feed_answer_activity);

        if (InternetConnection.checkConnection(getApplicationContext())) {


            Intent intent= getIntent();
            positionQues =intent.getIntExtra("position",0);
            quesUserName =intent.getStringExtra("quesUserName");
            publishedDate =intent.getStringExtra("quesaskDate");
            question=intent.getStringExtra("quesask");
            push=intent.getStringExtra("pushId");
            questionCategory=intent.getStringExtra("quesCategory");


             rl_bottom=findViewById(R.id.rl_Bottom);

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                firebaseAuth = FirebaseAuth.getInstance();
                currentUser = firebaseAuth.getCurrentUser();
                rl_bottom.setEnabled(true);
                checkQuestionStatus(push);


            }else{
            rl_bottom.setEnabled(false);
            rl_bottom.setFocusable(true);

            }

            addAnswer_et=findViewById(R.id.add_Answer_et);
            postAnswer_tv=findViewById(R.id.post_Answer_tv);
            dispQues_tv =findViewById(R.id.displayQues_tv);
            QuesAskedByTime_tv =findViewById(R.id.QuesAskedByTime_tv);
            ib_likeQues =findViewById(R.id.ib_likeQues);
            ib_dislikeQues =findViewById(R.id.ib_dislikeQues);
            ib_flagQues =findViewById(R.id.ib_flagQues);

//            ib_likeQues.setTag("like");
//            ib_dislikeQues.setTag("dislike");
//            ib_flagQues.setTag("flag");


            dispQues_tv.setText(question);
            QuesAskedByTime_tv.setText("Asked by : "+quesUserName+" on "+publishedDate);

            recyclerView=findViewById(R.id.rv_Answers);
            showAnsRef= FirebaseDatabase.getInstance().getReference().child("Feed").child("Answer_List").child("Feed_All_Answer").child(push);

            GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                    GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("1085537073642-dq2djhhvidcgmb4c3a5ushet55jk6hf5.apps.googleusercontent.com")
                    .requestEmail()
                    .build();

            googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions);



//
//            addAnsRef= FirebaseDatabase.getInstance().getReference().child("FeedQuestions").child("Answer_Ques").child(String.valueOf(positionQues));
//            showAnsRef= FirebaseDatabase.getInstance().getReference().child("FeedQuestions").child("Answer_Ques").child(String.valueOf(positionQues));
//            ansUserRef= FirebaseDatabase.getInstance().getReference().child("FeedQuestions").child("User_Answer").child(String.valueOf(positionQues));

//            addAllAnsRef.keepSynced(true);
//            showAnsRef.keepSynced(true);
//            userAllAnsRef.keepSynced(true);

            list_answer=new ArrayList<>();
            answerAdaptor =new Adaptor_QueryAnswer(this,list_answer);
            recyclerView.setAdapter(answerAdaptor);


            Toolbar toolbar=findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("AnswersTab");
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
                    showAnswer(positionQues, quesUserName);
                }
            });


            postAnswer_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addAnswer_et.getText().toString().equals("")){
                        addAnswer_et.setError("Enter Answer");
                        Toast.makeText(Landing_Feed_Answer.this,"Enter Answer",Toast.LENGTH_SHORT).show();

                    } else{
                        String Answer=addAnswer_et.getText().toString().trim();
                        addAnswer(positionQues, quesUserName,question,push,questionCategory,Answer);
                    }
                }
            });


            ib_likeQues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            if (ib_likeQues.getTag().equals("like")){
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Feed")
                                        .child("AllQuestionReview")
                                        .child("Like")
                                        .child(push)
                                        .child(currentUser.getUid()).setValue(true);
                                ib_flagQues.setEnabled(false);
                                ib_dislikeQues.setEnabled(false);
                                ib_likeQues.setTag("like_user");
                            }else{
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Feed")
                                        .child("AllQuestionReview")
                                        .child("Like")
                                        .child(push)
                                        .child(currentUser.getUid()).removeValue();
                                ib_flagQues.setEnabled(true);
                                ib_dislikeQues.setEnabled(true);
                                ib_likeQues.setTag("like");

                            }
                    }
                }
            });
            ib_dislikeQues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            if (ib_dislikeQues.getTag().equals("dislike")){
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Feed")
                                        .child("AllQuestionReview")
                                        .child("Dislike")
                                        .child(push)
                                        .child(currentUser.getUid()).setValue(true);
                                ib_flagQues.setEnabled(false);
                                ib_likeQues.setEnabled(false);
                                ib_dislikeQues.setTag("dislike_user");
                            }else{
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Feed")
                                        .child("AllQuestionReview")
                                        .child("Dislike")
                                        .child(push)
                                        .child(currentUser.getUid()).removeValue();
                                ib_flagQues.setEnabled(true);
                                ib_likeQues.setEnabled(true);
                                ib_dislikeQues.setTag("dislike");

                            }
                    }
                }
            });
            ib_flagQues.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                            if (ib_flagQues.getTag().equals("flag")){
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Feed")
                                        .child("AllQuestionReview")
                                        .child("Flag")
                                        .child(push)
                                        .child(currentUser.getUid()).setValue(true);
                                ib_likeQues.setEnabled(false);
                                ib_dislikeQues.setEnabled(false);
                                ib_flagQues.setTag("flag_user");
                            }else{
                                FirebaseDatabase.getInstance().getReference()
                                        .child("Feed")
                                        .child("AllQuestionReview")
                                        .child("Flag")
                                        .child(push)
                                        .child(currentUser.getUid()).removeValue();
                                ib_likeQues.setEnabled(true);
                                ib_dislikeQues.setEnabled(true);
                                ib_flagQues.setTag("flag");
                            }
                    }
                }
            });



            showAnswer(positionQues, quesUserName);



        } else {
            Toast.makeText(this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }


    }

    private void checkQuestionStatus(String pushid) {

        DatabaseReference referencedislike=FirebaseDatabase.getInstance().getReference()
                    .child("Feed")
                    .child("AllQuestionReview")
                    .child("Dislike")
                    .child(pushid);
            referencedislike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(currentUser.getUid()).exists()){
                        ib_dislikeQues.setTag("dislike_user");
                        ib_dislikeQues.setImageResource(R.drawable.thumbdowndark_72);
//                        ib_likeQues.setEnabled(false);
//                        ib_flagQues.setEnabled(false);
                    }else{
                        ib_dislikeQues.setTag("dislike");
                        ib_dislikeQues.setImageResource(R.drawable.thumbdown_72);
//                        ib_likeQues.setEnabled(true);
//                        ib_flagQues.setEnabled(true);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        DatabaseReference referencelike=FirebaseDatabase.getInstance().getReference()
                    .child("Feed")
                    .child("AllQuestionReview")
                    .child("Like")
                    .child(pushid);
            referencelike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(currentUser.getUid()).exists()){
                        ib_likeQues.setTag("like_user");
                        ib_likeQues.setImageResource(R.drawable.ic_baseline_thumb_up_dark);
//                        ib_dislikeQues.setEnabled(false);
//                        ib_flagQues.setEnabled(false);
                    }else{
                        ib_likeQues.setTag("like");
                        ib_likeQues.setImageResource(R.drawable.thumbup_72);
//                        ib_dislikeQues.setEnabled(true);
//                        ib_flagQues.setEnabled(true);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        DatabaseReference referenceflag=FirebaseDatabase.getInstance().getReference()
                    .child("Feed")
                    .child("AllQuestionReview")
                    .child("Flag")
                    .child(pushid);
            referenceflag.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(currentUser.getUid()).exists()){
                        ib_flagQues.setTag("flag_user");
                        ib_flagQues.setImageResource(R.drawable.flagdark_72);
//                        ib_dislikeQues.setEnabled(false);
//                        ib_likeQues.setEnabled(false);

                    }else{
                        ib_flagQues.setTag("flag");
                        ib_flagQues.setImageResource(R.drawable.flags_72);
//                        ib_dislikeQues.setEnabled(true);
//                        ib_likeQues.setEnabled(true);

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


    }

    public void addAnswer(int position, String quesaskUserName, String question,String pushid,String quesCategory,String Answer) {
//        updateUser();
        if (FirebaseAuth.getInstance().getCurrentUser() == null)
        {
            loginDialogPopUp();
        }
        else {
            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID = currentUser.getUid();
            userEmailID = currentUser.getEmail();
            userdbName = currentUser.getDisplayName();

            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().
                    child( "Feed" ).child( "Answer_List" ).child( "Feed_All_Answer").child(pushid);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" AnswersTab");
                    long noofQuesinCategory=snapshot.getChildrenCount()+1;
//                    String push=quesCategory+"_Qno_"+noofQuesinCategory;

                    String anspush=pushid+"_Ansno_"+noofQuesinCategory;

                    String Privacy="public";
                    addAnsCategoryRef= FirebaseDatabase.getInstance().getReference().child("Feed").child("Answer_List").child("Feed_Answer_Category").child(quesCategory).child(pushid);
                    addAllAnsRef = FirebaseDatabase.getInstance().getReference().child( "Feed" ).child( "Answer_List" ).child( "Feed_All_Answer").child(pushid);
                    userAllAnsRef = FirebaseDatabase.getInstance().getReference().child("Feed").child("User_Answer").child("User_All_Answer").child(userID).child(pushid);
                    userAnsCategoryRef = FirebaseDatabase.getInstance().getReference().child("Feed").child("User_Answer").child("User_Answer_Category").child(quesCategory).child(userID).child(pushid);
                    userAnsPrivacyRef = FirebaseDatabase.getInstance().getReference().child("Feed").child("User_Answer").child("User_Answer_Privacy").child(Privacy).child(userID).child(pushid);

                    Calendar calenderCC = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                    final String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());

                    String positionQ = String.valueOf(position);

//                    addAnswer = new Class_Answer(positionQ, Answer, dateTimeCC, userdbName, userID);
//                    addAllAnsRef.push().setValue(addAnswer);

                    userAddQues = new Class_Answer(quesCategory,question,Answer,dateTimeCC,quesaskUserName,userdbName,userID,userEmailID,push,anspush);
//                    addQuesCategory = new Class_Answer(quesCategory,question,dateTimeCC,quesaskUserName,userID,userEmailID,anspush,push);
//                    addQuesUsers = new Class_Answer(quesCategory,question,dateTimeCC,quesaskUserName,userID,userEmailID,anspush,push);
//                    addQuesUsersCategory = new Class_Answer(quesCategory,question,dateTimeCC,quesaskUserName,userID,userEmailID,anspush,push);
//                    addQuesUsersPrivacy = new Class_Answer(quesCategory,question,dateTimeCC,quesaskUserName,userID,userEmailID,anspush,push);

                    addAllAnsRef.child(anspush).setValue(userAddQues);
                    addAnsCategoryRef.child(anspush).setValue(userAddQues);
                    userAllAnsRef.child(anspush).setValue(userAddQues);
                    userAnsPrivacyRef.child(anspush).setValue(userAddQues);
                    userAnsCategoryRef.child(anspush).setValue(userAddQues);


                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });


//        SharedPreferences sp = getSharedPreferences("register", MODE_PRIVATE);
//        if (sp.contains("Name")) {
//            rName = sp.getString("Name", "");
//            rPhoneno = sp.getString("Mobile", "");
//        }
//        HashMap<String,Object> hashMap=new HashMap<>();
//        hashMap.put("Answer",addAnswer_et.getText().toString());
//        hashMap.put("Postid",postid);
//        hashMap.put("Publisherid",userId);
//        hashMap.put("Publisher",rName);
//        hashMap.put("DateTime",dateTimeCC);
//        reference.push().setValue(hashMap);


        }
        addAnswer_et.setText("");
    }

    public void showAnswer(int postId, String publisher){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        list_answer = new ArrayList<>();

        answerAdaptor = new Adaptor_QueryAnswer(this, list_answer);
        recyclerView.setAdapter(answerAdaptor);

        answerAdaptor.setOnItemClickListener(new Adaptor_QueryAnswer.OnItemClickListener() {
//            @Override
//            public void shareQues(int position, String question) {
//
//            }

            @Override
            public void listitem(int position, String question,String pushQues,String pushAns,String category) {

                showbtmDialogComment(position,question,pushQues,pushAns,category);

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

    private void showbtmDialogComment(int position, String question,String pushQues,String pushAns,String Category) {

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            loginDialogPopUp();
        } else {

            BottomSheetDialog btmDialogAddQues = new BottomSheetDialog(Landing_Feed_Answer.this);
            btmDialogAddQues.setCancelable(true);
            btmDialogAddQues.setContentView(R.layout.btmdialog_comment);
            showFeedCommentRV(pushQues,pushAns,btmDialogAddQues);

            EditText addComment_et = btmDialogAddQues.findViewById(R.id.addComment_et);
            RecyclerView rv_Comment = btmDialogAddQues.findViewById(R.id.rv_Comment);

            Button tapSubmit = btmDialogAddQues.findViewById(R.id.buttonSubmit);
            int width = (int) (getResources().getDisplayMetrics().widthPixels * 1.00);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.60);

            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID = currentUser.getUid();
            userEmailID = currentUser.getEmail();
            userdbName = currentUser.getDisplayName();

            tapSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String comment = addComment_et.getText().toString().trim();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().
                            child("Feed").child("Comment_List").child("Feed_All_Comment").child(pushQues).child(pushAns);
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    userallAns_tv.setText("View All "+ snapshot.getChildrenCount()+" AnswersTab");
                            long noofQuesinCategory = snapshot.getChildrenCount() + 1;
//                    String push=quesCategory+"_Qno_"+noofQuesinCategory;

                            String commentpush = pushAns + "_Commentno_" + noofQuesinCategory;

                            String Privacy = "public";
                            addAllAnsRef = FirebaseDatabase.getInstance().getReference().child("Feed").child("Comment_List").child("Feed_All_Comment").child(pushQues).child(pushAns);
                            userAllAnsRef = FirebaseDatabase.getInstance().getReference().child("Feed").child("User_Comment").child("User_All_Comment").child(userID).child(pushQues).child(pushAns);

                            Calendar calenderCC = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
                            final String dateTimeCC = simpleDateFormatCC.format(calenderCC.getTime());

                            String positionQ = String.valueOf(position);

//                    addAnswer = new Class_Answer(positionQ, Answer, dateTimeCC, userdbName, userID);
//                    addAllAnsRef.push().setValue(addAnswer);

                            userAddQues = new Class_Answer(Category, question, comment, dateTimeCC, userdbName, userID, userEmailID, pushQues, pushAns, commentpush);
//                    addQuesCategory = new Class_Answer(quesCategory,question,dateTimeCC,quesaskUserName,userID,userEmailID,anspush,push);
//                    addQuesUsers = new Class_Answer(quesCategory,question,dateTimeCC,quesaskUserName,userID,userEmailID,anspush,push);
//                    addQuesUsersCategory = new Class_Answer(quesCategory,question,dateTimeCC,quesaskUserName,userID,userEmailID,anspush,push);
//                    addQuesUsersPrivacy = new Class_Answer(quesCategory,question,dateTimeCC,quesaskUserName,userID,userEmailID,anspush,push);
                            addAllAnsRef.child(commentpush).setValue(userAddQues);
                            userAllAnsRef.child(commentpush).setValue(userAddQues);

                            addComment_et.setText("");
                            Toast.makeText(Landing_Feed_Answer.this, "Comment has been updated Successfully", Toast.LENGTH_LONG).show();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


//        SharedPreferences sp = getSharedPreferences("register", MODE_PRIVATE);
//        if (sp.contains("Name")) {
//            rName = sp.getString("Name", "");
//            rPhoneno = sp.getString("Mobile", "");
//        }
//        HashMap<String,Object> hashMap=new HashMap<>();
//        hashMap.put("Answer",addAnswer_et.getText().toString());
//        hashMap.put("Postid",postid);
//        hashMap.put("Publisherid",userId);
//        hashMap.put("Publisher",rName);
//        hashMap.put("DateTime",dateTimeCC);
//        reference.push().setValue(hashMap);


                }
            });

            btmDialogAddQues.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
//        updateUser();

    }

    private void loginDialogPopUp() {
        final android.app.AlertDialog dialogBuilder = new android.app.AlertDialog.Builder(Landing_Feed_Answer.this).create();
        dialogBuilder.setCanceledOnTouchOutside(true);
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
                showLoginBtmDialog();
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }
    private void showLoginBtmDialog() {

        BottomSheetDialog bottomSheetDialoglogin=new BottomSheetDialog(Landing_Feed_Answer.this);
        bottomSheetDialoglogin.setCancelable(true);
        bottomSheetDialoglogin.setContentView(R.layout.btmdialog_login);

        Button btn_phonelogin=bottomSheetDialoglogin.findViewById(R.id.btn_JoinGroup);
        SignInButton btn_googlelogin=bottomSheetDialoglogin.findViewById(R.id.btn_CreateGroup);

        btn_googlelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=googleSignInClient.getSignInIntent();

                startActivityForResult(intent,100);
                bottomSheetDialoglogin.dismiss();

            }
        });

        btn_phonelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Landing_Feed_Answer.this, Phone_Login.class));
                bottomSheetDialoglogin.dismiss();
            }
        });

        bottomSheetDialoglogin.show();

    }
    private void showStudTeachBtmDialog() {
//        rl_feed.setBackgroundColor(Color.GRAY);
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(Landing_Feed_Answer.this);
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(R.layout.btmdialog_studteach);

        LinearLayout student_ll=bottomSheetDialog.findViewById(R.id.student_ll);
        LinearLayout teacher_ll=bottomSheetDialog.findViewById(R.id.teacher_ll);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String userID = currentUser.getUid();
        final String userName = currentUser.getDisplayName();
        final String userEmail = currentUser.getEmail();
        final Uri userPhoto = currentUser.getPhotoUrl();
        Calendar calenderCC= Calendar.getInstance();
        SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        final String udateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());

        student_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                refUserRegister.child( "Name" ).setValue( userName );
                refUserRegister.child( "Email" ).setValue( userEmail );
//                refUserRegister.child( "photo" ).setValue( userPhoto );
                refUserRegister.child( "UserId" ).setValue( userID );
                refUserRegister.child( "DateTime" ).setValue( udateTimeCC );
                refUserRegister.child( "Category" ).setValue("Student");
//                startActivity(new Intent(getContext(),Landing_Activity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Toast.makeText(Landing_Feed_Answer.this, "Login Sucessful as Student", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();

            }
        });
        teacher_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference refUserRegister = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
                refUserRegister.child( "Name" ).setValue( userName );
                refUserRegister.child( "Email" ).setValue( userEmail );
//                refUserRegister.child( "photo" ).setValue( userPhoto );
                refUserRegister.child( "UserId" ).setValue( userID );
                refUserRegister.child( "DateTime" ).setValue( udateTimeCC );
                refUserRegister.child( "Category" ).setValue("Teacher");
//                startActivity(new Intent(getContext(),Landing_Activity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Toast.makeText(Landing_Feed_Answer.this, "Login Successful as Teacher", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==100){

            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
                    .getSignedInAccountFromIntent(data);


            if (signInAccountTask.isSuccessful()){

                String s= "Google Signin is sucessful";


                try {
                    GoogleSignInAccount googleSignInAccount=signInAccountTask.getResult(ApiException.class);

                    if (googleSignInAccount!=null){
                        AuthCredential authCredential= GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
                        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(Landing_Feed_Answer.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    showStudTeachBtmDialog();

                                }else{
                                    Toast.makeText(Landing_Feed_Answer.this, "Authentication Failed: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(Landing_Feed_Answer.this, "google account null", Toast.LENGTH_SHORT).show();

                    }


                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }

        }
        else{
            Toast.makeText(Landing_Feed_Answer.this, "wrong request code", Toast.LENGTH_SHORT).show();

        }
    }
    public void showFeedCommentRV(String pushQues,String pushAns, BottomSheetDialog bottomSheetDialog) {
        ArrayList<Class_Answer> listnewComment;
        Adaptor_QueryComment showCommentadaptor;
        RecyclerView rv_Comments;
        rv_Comments=bottomSheetDialog.findViewById(R.id.rv_Comment);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Landing_Feed_Answer.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        DatabaseReference refShowAllComment;
        refShowAllComment=FirebaseDatabase.getInstance().getReference().child("Feed").child("Comment_List").child("Feed_All_Comment").child(pushQues).child(pushAns);
        rv_Comments.setLayoutManager(linearLayoutManager);
        listnewComment = new ArrayList<>();
        showCommentadaptor = new Adaptor_QueryComment(Landing_Feed_Answer.this, listnewComment);
        rv_Comments.setAdapter(showCommentadaptor);


        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount()>0) {
                    Class_Answer userComment = dataSnapshot.getValue(Class_Answer.class);
                    listnewComment.add(userComment);
                    showCommentadaptor.notifyDataSetChanged();
//                    notifyPB.dismiss();
                } else {
                    Toast.makeText(Landing_Feed_Answer.this, "No Question asked yet,Please Ask First QuestionsTab", Toast.LENGTH_SHORT).show();
//                    notifyPB.dismiss();
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
        refShowAllComment.addChildEventListener(childEventListener);


//        Bundle intent=getIntent().getExtras();
//        if (intent!=null){
//            String publisher=intent.getString("publisherid");
//            SharedPreferences.Editor editor=getSharedPreferences("PUBLISH",MODE_PRIVATE).edit();
//            editor.putString("publisherid",publisher);
//            editor.apply();
//
//        }

    }
}