package com.cllasify.cllasify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cllasify.cllasify.Adaptor.Adaptor_Friend_Chat;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class Friend_Chat_Activity extends AppCompatActivity {
    private static final String TAG = Friend_Chat_Activity.class.getSimpleName();

    Adaptor_Friend_Chat adaptor_friend_chat;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    List<Class_Single_Friend> messageList;
    Context activity = this;

    String senderRoom,receiverRoom;
    FirebaseDatabase firebaseDatabase;
    String senderUid,receiverUid;
    ImageButton ib_FrndP_csubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView friendNameTv = findViewById(R.id.tv_friend_name);
        String friendName = getIntent().getStringExtra("name");
        messageList = new ArrayList<>();
        Log.d(TAG, "onCreate: friend name ->" + friendName);
        friendNameTv.setText(friendName);
        recyclerView = findViewById(R.id.recyclerView);
        adaptor_friend_chat = new Adaptor_Friend_Chat(activity, messageList);
        linearLayoutManager = new LinearLayoutManager(activity);
        ib_FrndP_csubmit = findViewById(R.id.ib_FrndP_csubmit);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adaptor_friend_chat);

        firebaseDatabase = FirebaseDatabase.getInstance();
        senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverUid = getIntent().getStringExtra("receiverUid");
        Log.d(TAG, "onCreate: receiverUserId: " + receiverUid);
        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        ib_FrndP_csubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}