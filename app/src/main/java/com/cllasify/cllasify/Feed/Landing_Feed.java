package com.cllasify.cllasify.Feed;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Adaptor.Adaptor_QueryQuestions;
import com.cllasify.cllasify.Class.Class_Answer;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Landing_Feed extends AppCompatActivity {
    Button button;
    RecyclerView recyclerView;
    FloatingActionButton fab_addQ;
    String rName, rPhoneno,userdbName, TitleNew,currUser, quesCategory,addQuestion;
    TextView username_tv;
    int like=0,dislike=0;
    SearchView searchView;
    SwipeRefreshLayout swipeRefreshLayout;

    RelativeLayout rl_feed;
    DatabaseReference refshowQues, refaddQuestion,refuserQues,refuserName;
    Class_Answer userOtherForm,userOtherFormHi,userAddQues,userQues;
    List<Class_Answer> listnewQues;
    Adaptor_QueryQuestions showQuesadaptor;

    ImageButton ib_quesCategory,ib_quesSearch,ib_userLogin;

    GoogleSignInClient googleSignInClient;
    Calendar calenderCC= Calendar.getInstance();
    SimpleDateFormat simpleDateFormatCC= new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
    String dateTimeCC=simpleDateFormatCC.format(calenderCC.getTime());
    ProgressDialog notifyPB;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    Uri userPhoto;
    String userID,userEmailID,userName;
    boolean searchShow=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_feed_activity);




//        username_tv=findViewById(R.id.loginstatus);


        notifyPB=new ProgressDialog(this);
        notifyPB.setTitle("Exam Doubt");
        notifyPB.setMessage("Loading QuestionsTab..");
        notifyPB.setCanceledOnTouchOutside(true);
        notifyPB.show();

        searchView=findViewById(R.id.quesSearchView);
        fab_addQ=findViewById(R.id.fab_addQ);
        ib_quesCategory=findViewById(R.id.ib_quesCategory);
        ib_quesSearch=findViewById(R.id.ib_quesSearch);
        ib_userLogin=findViewById(R.id.ib_userLogin);
        rl_feed=findViewById(R.id.rl_feed);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            Glide.with(Landing_Feed.this)
                    .load(currentUser.getPhotoUrl())
                    .into(ib_userLogin);
        }
        ib_userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    showLoginDialog();
                } else {
//                    Toast.makeText(Landing_Feed.this, "Opening User Profile ", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(Landing_Feed.this,User_Profile.class));
                }


            }
        });
        fab_addQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    loginDialog();
                } else {
                    addQuestion();
                }
            }
        });
        ib_quesSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchShow){
                    searchView.setVisibility(View.VISIBLE);
                    searchShow=false;
                }else{
                    searchView.setVisibility(View.GONE);
                    searchShow=true;
                }
            }
        });

        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("1085537073642-cs67e43s871c2tm1ddcbfgdrcruio7dv.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient= GoogleSignIn.getClient(Landing_Feed.this,googleSignInOptions);

        refshowQues = FirebaseDatabase.getInstance().getReference().child( "FeedQuestions" ).child( "Question_Added" );
        refaddQuestion= FirebaseDatabase.getInstance().getReference().child( "FeedQuestions" ).child( "Question_Added" );

        //        String user=FirebaseAuth.getInstance().getUid();
        refshowQues.keepSynced(true);
        refaddQuestion.keepSynced(true);


        recyclerView = findViewById(R.id.rv_showquestion);

        swipeRefreshLayout=findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);
                showQuestionRV();
            }
        });

        if (searchView!=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return false;
                }
            });
        }

        showQuestionRV();
    }

    private void loginDialog() {
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
                showLoginDialog();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

    }

    public void addQuestion()       {
            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID=currentUser.getUid();
            userEmailID= currentUser.getEmail();
            userPhoto=currentUser.getPhotoUrl();
            userName=currentUser.getDisplayName();
            refuserQues = FirebaseDatabase.getInstance().getReference().child( "FeedQuestions" ).child( "User_Question" ).child(userID);
            refuserQues.keepSynced(true);

        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        dialogBuilder.setCanceledOnTouchOutside(false);
        //dialogBuilder.setCancelable(false);
        LayoutInflater inflater = this.getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.btmdialog_addquestion, null);
        final EditText quesCaterory_et=dialogView.findViewById(R.id.feedCategory_et);
        final EditText addQuestion_et=dialogView.findViewById(R.id.addQuestion_et);

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


                quesCategory = quesCaterory_et.getText().toString().trim();
                addQuestion=addQuestion_et.getText().toString().trim();
                //String TotalOtherForm=examName+"=="+addQuestion;

                if (quesCategory.isEmpty() && addQuestion.isEmpty()) {
//                    Toast.makeText(Landing_Feed.this, "Please Fill All Details", Toast.LENGTH_LONG).show();
                    quesCaterory_et.setError("Please Fill");
                    addQuestion_et.setError("Please Fill");
                }else   if(quesCategory.isEmpty()){
//                    Toast.makeText(Landing_Feed.this, "Please Enter Exam Name", Toast.LENGTH_LONG).show();
                    quesCaterory_et.setError("Exam Name?");
                }else if(addQuestion.isEmpty()){
//                    Toast.makeText(Landing_Feed.this, "Please Enter QuestionsTab", Toast.LENGTH_LONG).show();
                    addQuestion_et.setError("Ask Question");
                }
                else {
                    String exam= quesCategory +"_"+addQuestion;

                    userAddQues = new Class_Answer(quesCategory,addQuestion,dateTimeCC,userName,userID,userEmailID,exam);
                    userQues = new Class_Answer(quesCategory,addQuestion,dateTimeCC,userName,userID,userEmailID,exam);

                    refaddQuestion.push().setValue(userAddQues);
                    refuserQues.push().setValue(userQues);
//                            refaddQuestion.setValue(userAddQues);
//drive update.
//                    StringRequest stringRequest = new StringRequest(Request.Method.POST,
//                            "https://script.google.com/macros/s/AKfycbxND__FFUAcWwO-ljGMQHwUlS9HrLOBDAqEQwFqRTMMFjUTUQg/exec",
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                }
//                            },
//                            new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    Toast.makeText(Landing_Feed.this,"Error"+error,Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                    ) {
//                        @Override
//                        protected Map<String, String> getParams() {
//                            Map<String, String> parmas = new HashMap<>();
//                            String uuser= firebaseAuth.getUid();
//                            //here we pass params
//                            parmas.put("action","addItem");
//                            SharedPreferences sp=getSharedPreferences("register",MODE_PRIVATE);
//                            rPhoneno=sp.getString("Mobile","");
//                            examName= examname_et.getText().toString();
//                            addQuestion=addQuestion_et.getText().toString();
//
//                            parmas.put("fExam",examName);
//                            parmas.put("fQues",addQuestion);
//                            parmas.put("Name",currUser);
//                            parmas.put("Mobile",rPhoneno );
//                            parmas.put("EmailId",userEmailID);
//                            parmas.put("UserID",userID);
//                            parmas.put("Category", "Question");
//                            return parmas;
//                        }
//                    };
//                    int socketTimeOut = 50000;// u can change this .. here it is 50 seconds
//                    RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//                    stringRequest.setRetryPolicy(retryPolicy);
//                    RequestQueue queue = Volley.newRequestQueue(Landing_Feed.this);
//                    queue.add(stringRequest);
//notification
                    String AdminToken="All_Notify";
                    HashMap<String, Object> map= new HashMap<>();
                    map.put("token",AdminToken);
                    map.put("title", quesCategory);
                    map.put("Description",addQuestion);
                    map.put("heading","Exam Doubt");
                    DatabaseReference notification= FirebaseDatabase.getInstance().getReference()
                            .child("Notification");
                    notification.child(userID).push().setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
                    //startActivity( new Intent( landing_OtherForms.this, user_Profile_UploadDoc.class ) );
//                    Toast.makeText(Landing_Feed.this, "Question has been Submitted Successfully", Toast.LENGTH_LONG).show();

                    dialogBuilder.dismiss();
                }
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();

//        dialogBuilder.getWindow().setLayout(width, height);

//        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
//        SharedPreferences.Editor editor=prefs.edit();
//        editor.putBoolean("firstStart",false);
//        editor.apply();


    }
    public void showQuestionRV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        listnewQues = new ArrayList<>();

        showQuesadaptor = new Adaptor_QueryQuestions(this, listnewQues);
        recyclerView.setAdapter(showQuesadaptor);


        showQuesadaptor.setOnItemClickListener(new Adaptor_QueryQuestions.OnItemClickListener() {
            @Override
            public void shareQues(int position, String Title) {
//                generateLink(Title);
//                Toast.makeText(Landing_Feed.this, "share", Toast.LENGTH_SHORT).show();
            }
        });
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount()>0) {
                    Class_Answer userQuestions = dataSnapshot.getValue(Class_Answer.class);
                    listnewQues.add(userQuestions);
                    showQuesadaptor.notifyDataSetChanged();
                    notifyPB.dismiss();
                } else {
//                    Toast.makeText(Landing_Feed.this, "No Question asked yet,Please Ask First QuestionsTab", Toast.LENGTH_SHORT).show();
                    notifyPB.dismiss();
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
        refshowQues.addChildEventListener(childEventListener);


//        Bundle intent=getIntent().getExtras();
//        if (intent!=null){
//            String publisher=intent.getString("publisherid");
//            SharedPreferences.Editor editor=getSharedPreferences("PUBLISH",MODE_PRIVATE).edit();
//            editor.putString("publisherid",publisher);
//            editor.apply();
//
//        }

    }
    //functionfor bottom navigation
//    public void bottomNav() {
//
//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_Home:
//                        startActivity( new Intent( Landing_Feed.this, landing_Page.class ) );
//                        Toast.makeText(Landing_Feed.this, "HomePage", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.action_Notifications:
//                        Toast.makeText(Landing_Feed.this, "Notifications & Favorites", Toast.LENGTH_SHORT).show();
//                        startActivity( new Intent( Landing_Feed.this, user_Notification.class ) );
//                        break;
//                    case R.id.action_Settings:
//                        Toast.makeText(Landing_Feed.this, "Settings", Toast.LENGTH_SHORT).show();
//                        startActivity( new Intent( Landing_Feed.this, user_Settings.class ) );
//                        break;
//                    case R.id.action_Profile:
//                        Toast.makeText(Landing_Feed.this, "User Profile", Toast.LENGTH_SHORT).show();
//                        startActivity( new Intent( Landing_Feed.this, user_Profile.class ) );
//                        break;
//                    case R.id.action_Share:
//                        Toast.makeText(Landing_Feed.this, "Refer and Earn", Toast.LENGTH_LONG).show();
//                        String Title="Refer and Clear you Exam Doubts";
//                        generateLink(Title);
//                        break;
//                }
//                return true;
//            }
//        });
////end bottom nav drawer
//
//    }
    //Banner Ads
//    public void adViewBanner() {
//        MobileAds.initialize(this, new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//        AdView mAdView;
//        mAdView = new AdView(this);
//        mAdView.setAdSize(AdSize.BANNER);
//        mAdView.setAdUnitId("ca-app-pub-4564671643419900/1025692621");
//
//        mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//
//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        });
//
//    }
//
//    public void loadLocale(){
//        SharedPreferences prefs=getSharedPreferences("LanguageSettings", Activity.MODE_PRIVATE);
//        String Language=prefs.getString("MyLanguage","");
//        setLocale(Language);
//    }
//    public void setLocale(String language) {
//        Locale locale=new Locale(language);
//        Locale.setDefault(locale);
//        Configuration configuration=new Configuration();
//        configuration.locale=locale;
//        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());
//        //save data
//
//        SharedPreferences.Editor editor=getSharedPreferences("LanguageSettings",MODE_PRIVATE).edit();
//        editor.putString("MyLanguage",language);
//        editor.apply();
//        //loadLocale();
//    }
//
//    public void generateLink(String title) {
//        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                .setLink(Uri.parse("https://play.google.com/store/apps/details?id=in.dreamworld.fillformonline+"))
//                .setDomainUriPrefix("https://fillformonline.page.link")
//                // Open links with this app on Android
//                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
//                // Open links with com.example.ios on iOS
//                .setIosParameters(new DynamicLink.IosParameters.Builder("in.dreamworld.fillformonline").build())
//                .buildDynamicLink();
//
//        Uri dynamicLinkUri = dynamicLink.getUri();
//// https://fillformonline.page.link?apn=in.dreamworld.fillformonline&ibi=in.dreamworld.fillformonline&link=https%3A%2F%2Fplay.google.com%2Fstore%2Fapps%2Fdetails%3Fid%3Din.dreamworld.fillformonline
//        String userEmail=currentUser.getEmail();
//        createReferLink(userEmail,"refer",title);
//    }
//    public void createReferLink(String userId,String ExamName,String body1){
//        //manually link generation
//        String title="Fill Form Online-App";
//        String body="Your Friend "+"asked you a Question: "+body1+"If you Know Please click on the link to reply";
//        String shareLinkText= "https://fillformonline.page.link/?"+
//                "link=https://play.google.com/store/apps/details?id=in.dreamworld.fillformonline+"+userId+"-"+ExamName+
//                "&apn="+getPackageName()+
//                "&st="+title+
//                "&sd="+body+
//                "&si="+ R.mipmap.ic_launcher;
////Link Shortener
//        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                //.setLongLink(dynamicLinkUri)          //firebase generated link
//                .setLongLink(Uri.parse(shareLinkText)) //manuall link
//                .buildShortDynamicLink()
//                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
//                    @Override
//                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
//                        if (task.isSuccessful()) {
//                            // Short link created
//                            Uri shortLink = task.getResult().getShortLink();
//                            Uri flowchartLink = task.getResult().getPreviewLink();
//                            Intent intent=new Intent();
//                            intent.setAction(Intent.ACTION_SEND);
//                            intent.putExtra(Intent.EXTRA_TEXT,shortLink.toString());
//                            intent.setType("text/plain");
//                            startActivity(intent);
//                        } else {
//                            // Error
//                            // ...
//                        }
//                    }
//                });
//
//
//    }

    public void search(String newText) {
        ArrayList<Class_Answer> listSearchQues=new ArrayList<>();
        for (Class_Answer classUserSearch:listnewQues){
            if (classUserSearch.getExamfQues().toLowerCase().contains(newText.toLowerCase())){
                listSearchQues.add(classUserSearch);
            }
        }
        Adaptor_QueryQuestions adapSearchQues= new Adaptor_QueryQuestions(this,listSearchQues);
        recyclerView.setAdapter(adapSearchQues);
    }

    @Override
    public void onStart() {
        super.onStart();
        //Check if user is signed in (non-null) and update UI accordingly.
//        currentUser = firebaseAuth.getCurrentUser();
//        updateUI(currentUser);
//        userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
//        updateUser(userID);
//        loadLocale();
    }
//    private void updateUser(String userID) {
//        refuserName = FirebaseDatabase.getInstance().getReference().child("Admin").child("UserRegister").child(userID);
//        refuserName.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userdbName = Objects.requireNonNull(snapshot.child("name").getValue()).toString();
//                username_tv.setText("Hi,"+userdbName);
//                String rName,rEmail,userCName,userEmail;
//                SharedPreferences sp=getSharedPreferences("register",MODE_PRIVATE);
//                rName = sp.getString("Name", "");
//                rEmail=sp.getString("EMail","");
//                userEmail=currentUser.getEmail();
//                if(userdbName.equals(null)){
//                    if (rEmail.equals(userEmail)){
//                        userdbName=rName;
//                    }
//                    if(currentUser != null) {
//                        username_tv.setText("Hi,"+userdbName);
//                    }else{
//                        username_tv.setText("User not Logged In");
//                    }
//                }else{
//                    username_tv.setText("Hi,"+userdbName);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
//
//    }

    private void showLoginDialog() {

        BottomSheetDialog bottomSheetDialoglogin=new BottomSheetDialog(this);
        bottomSheetDialoglogin.setCancelable(false);
        bottomSheetDialoglogin.setContentView(R.layout.btmdialog_login);

        Button btn_phonelogin=bottomSheetDialoglogin.findViewById(R.id.btn_JoinGroup);
        SignInButton btn_googlelogin=bottomSheetDialoglogin.findViewById(R.id.btn_CreateGroup);

        btn_googlelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=googleSignInClient.getSignInIntent();

                startActivityForResult(intent,100);

            }
        });

        btn_phonelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Landing_Feed.this, Phone_Login.class));
            }
        });

        bottomSheetDialoglogin.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==100){

            Task<GoogleSignInAccount> signInAccountTask= GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            if (signInAccountTask.isSuccessful()){

                String s= "Google Signin is sucessful";

                Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

                try {
                    GoogleSignInAccount googleSignInAccount=signInAccountTask.getResult(ApiException.class);

                    if (googleSignInAccount!=null){
                        AuthCredential authCredential= GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
                        FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();

                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    showBottomDialog();

                                }else{
                                    Toast.makeText(Landing_Feed.this, "Authentication Failed: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }


                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showBottomDialog() {
        rl_feed.setBackgroundColor(Color.GRAY);
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(Landing_Feed.this);
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
//                startActivity(new Intent(in.dreamworld.cllasify.Landing_Feed.this,Landing_Activity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Toast.makeText(Landing_Feed.this, "Firebase Authentication Sucessful", Toast.LENGTH_SHORT).show();

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
//                startActivity(new Intent(in.dreamworld.cllasify.Landing_Feed.this,Landing_Activity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                Toast.makeText(Landing_Feed.this,"rebase Authentication Sucessful", Toast.LENGTH_SHORT).show();

            }
        });

        bottomSheetDialog.show();

    }

}
