package com.cllasify.cllasify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cllasify.cllasify.Adaptor.Adaptor_Friend_Chat;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Friend_Chat_Activity extends AppCompatActivity {
    private static final String TAG = Friend_Chat_Activity.class.getSimpleName();

    FirebaseDatabase firebaseDatabase;
    ArrayList<Class_Single_Friend> messageList;
    Adaptor_Friend_Chat adaptor_friend_chat;



    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    Context activity = this;

    String senderRoom,receiverRoom;
    String senderUid,receiverUid;
    ImageButton ib_FrndP_csubmit;

    EditText messageTxtFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);


        firebaseDatabase = FirebaseDatabase.getInstance();
        messageList = new ArrayList<>();
        adaptor_friend_chat = new Adaptor_Friend_Chat(this, messageList, senderRoom, receiverRoom);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptor_friend_chat);





        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView friendNameTv = findViewById(R.id.tv_friend_name);
        String friendName = getIntent().getStringExtra("name");
        Log.d(TAG, "onCreate: friend name ->" + friendName);
        friendNameTv.setText(friendName);
        recyclerView = findViewById(R.id.recyclerView);
        ib_FrndP_csubmit = findViewById(R.id.ib_FrndP_csubmit);
        messageTxtFriend = findViewById(R.id.et_FrndP_text);


        senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverUid = getIntent().getStringExtra("receiverUid");
        Log.d(TAG, "onCreate: receiverUserId: " + receiverUid);



        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;


        firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageList.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            Class_Single_Friend message = snapshot1.getValue(Class_Single_Friend.class);
                            message.setMessageId(snapshot1.getKey());
                            messageList.add(message);
                        }

                        adaptor_friend_chat.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        ib_FrndP_csubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageTxtFriend.getText().toString();

                Date date = new Date();
                Class_Single_Friend messages = new Class_Single_Friend(messageText,senderUid,date.getTime());
                messageTxtFriend.setText("");
                firebaseDatabase.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebaseDatabase.getReference().child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });

            }
        });

    }
}