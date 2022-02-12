package com.cllasify.cllasify;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adaptor.Adaptor_Friend_Chat;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class Friend_Chat_Activity extends Fragment {
    private static final String TAG = Friend_Chat_Activity.class.getSimpleName();


    Adaptor_Friend_Chat adaptor_friend_chat;
    ArrayList<Class_Single_Friend> messageList;

    String senderRoom, receiverRoom;

    FirebaseDatabase firebaseDatabase;


    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
//    Context activity = Friend_Chat_Activity.this;


    String senderUid, receiverUid;
    ImageButton ib_FrndP_csubmit;

    EditText messageTxtFriend;

    boolean found;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.activity_friend_chat, container, false);


        recyclerView = v.findViewById(R.id.recyclerView);
        ib_FrndP_csubmit = v.findViewById(R.id.ib_FrndP_csubmit);
        messageTxtFriend = v.findViewById(R.id.et_FrndP_text);


        messageList = new ArrayList<>();
        adaptor_friend_chat = new Adaptor_Friend_Chat(getContext(), messageList, senderRoom, receiverRoom);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adaptor_friend_chat);

        String friendName = getArguments().getString("name");
        receiverUid = getArguments().getString("receiverUid");
        senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;


        DatabaseReference firebaseDatabaseSaveRecChat = FirebaseDatabase.getInstance().getReference().child("chats").child("RecentChats").child(senderUid).child("recentChatUser");

        firebaseDatabaseSaveRecChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getChildrenCount() == 0) {
                    FriendsListClass friendsListClass = new FriendsListClass(receiverUid, friendName);
                    firebaseDatabaseSaveRecChat.child(String.valueOf(snapshot.getChildrenCount())).setValue(friendsListClass);
                } else {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.d("MINE", "onDataChange: " + receiverUid + " == " + dataSnapshot.child("userId").getValue());
                        Log.d("FRND", "onDataChange: " + dataSnapshot.child("userId").getValue());
                        if (receiverUid.equals(dataSnapshot.child("userId").getValue().toString())) {
                            found = true;
                            break;

                        } else {
                            found = false;

                        }
                    }
                    if (!found) {
                        FriendsListClass friendsListClass = new FriendsListClass(receiverUid, friendName);
                        firebaseDatabaseSaveRecChat.child(String.valueOf(snapshot.getChildrenCount())).setValue(friendsListClass);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        firebaseDatabase = FirebaseDatabase.getInstance();


        Toolbar toolbar = v.findViewById(R.id.toolbar);
        Log.d(TAG, "onCreate: friend name ->" + friendName);
        TextView friendNameTv = v.findViewById(R.id.tv_friend_name);

        friendNameTv.setText(friendName);


        Log.d("MESS", "receiverRoom: " + receiverRoom + "\nsenderRoom: " + senderRoom);

        firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
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


        Log.d(TAG, "onCreate: receiverUserId: " + receiverUid);

        ib_FrndP_csubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = messageTxtFriend.getText().toString();

                Date date = new Date();
                Class_Single_Friend messages = new Class_Single_Friend(messageText, senderUid, date.getTime());
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
                                .push()
                                .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });

            }
        });


        return v;

    }



    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);




    }
     */
}
