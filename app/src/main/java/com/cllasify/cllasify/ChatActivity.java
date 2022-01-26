//package com.cllasify.cllasify;
//
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//
//import com.cllasify.cllasify.Class.Class_Group;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class ChatActivity extends AppCompatActivity implements TextWatcher {
//
//    private static final String TAG = "ChatActivity";
//    ValueEventListener seenListener, readLiveMessageListener;
//    private DatabaseReference reference;
//    private boolean notify = false;
//    private String user_name, order_id, color_code, userId;
//    private ArrayList<Class_Group> chats;
//    private MessageAdapter messageAdapter;
//    private boolean onScreen;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//        onScreen = false;
//        initialize();
//        initializeSendButton();
//
//    }
//
//    private void initializeSendButton() {
//        chatBinding.messageInput.addTextChangedListener(this);
//
//        chatBinding.sendButton.setOnClickListener(v -> {
//            notify = true;
//            String message = chatBinding.messageInput.getText().toString();
//            if (!message.equals("")) {
//                sendMessage(userId, "100", message);
//            } else {
//                Toast.makeText(this, "You cannot send empty message", Toast.LENGTH_SHORT).show();
//            }
//            chatBinding.messageInput.setText("");
//        });
//    }
//
//    private void initialize() {
//
//        api = Client.getClient("https://fcm.googleapis.com/").create(Api.class);
//        user_name = getIntent().getStringExtra("user_name");
//        order_id = getIntent().getStringExtra("order_id");
//        color_code = getIntent().getStringExtra("color_code");
//        userId = getIntent().getStringExtra("user_id");
//        reference = FirebaseDatabase.getInstance().getReference("Chats");
//
//
//        //set heading title
//        if (user_name != null) {
//            chatBinding.headerTitle.setText(user_name.concat(" " + Constant.HASH.concat(order_id)));
//            chatBinding.shortName.setText(Funtionality.getShortName(user_name));
//        } else
//            chatBinding.headerTitle.setText(Constant.HASH.concat(order_id));
//
//        //set header circle color_id
//        if (color_code != null) {
//            chatBinding.circularNameView.setCardBackgroundColor(Color.parseColor(color_code));
//        }
//
//
//        chatBinding.shimmerFrame.startShimmer();
//
//        //read Chats
//        readMessage(order_id);
//        reference.addValueEventListener(readLiveMessageListener);
//
//        //seen message
//        seenMessage(userId);
//        reference.addValueEventListener(seenListener);
//
//        //initialize
//        chats = new ArrayList<>();
//
//        //Setting up RecyclerView
//        messageAdapter = new MessageAdapter(this);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);
//        chatBinding.conversationList.setLayoutManager(linearLayoutManager);
//        chatBinding.conversationList.setAdapter(messageAdapter);
//
//
//        chatBinding.backButton.setOnClickListener(v -> {
//            onBackPressed();
//        });
//
//
//    }
//
//    private void readMessage(String OrderId) {
//        readLiveMessageListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                chats.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    if (dataSnapshot.getKey().equals(OrderId)) {
//                        for (DataSnapshot child : dataSnapshot.getChildren()) {
//                            Chat chat = child.getValue(Chat.class);
//                            chats.add(chat);
//                        }
//                    }
//                }
//                messageAdapter.setList(chats);
//                messageAdapter.notifyDataSetChanged();
//                chatBinding.shimmerFrame.stopShimmer();
//                chatBinding.shimmerFrame.setVisibility(View.GONE);
//                chatBinding.conversationList.smoothScrollToPosition(chatBinding.conversationList.getAdapter().getItemCount());
//                chatBinding.conversationList.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
//    }
//
//    private void seenMessage(String userId) {
//        seenListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    if (dataSnapshot.getKey().equals(order_id)) {
//                        for (DataSnapshot child : dataSnapshot.getChildren()) {
//                            Chat chat = child.getValue(Chat.class);
//                            Log.d(TAG, "onDataChange: " + chat.getMessage());
//                            if (chat.getReceiver().equals("101")) {
//                                HashMap<String, Object> hashMap = new HashMap<>();
//                                hashMap.put("isseen", true);
//                                child.getRef().updateChildren(hashMap);
//                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        };
//    }
//
//
//    private void sendMessage(String sender, String receiver, String message) {
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//
//        HashMap<String, Object> map = new HashMap<>();
//
//        map.put("sender", sender);
//        map.put("receiver", receiver);
//        map.put("message", message);
//        map.put("username", "Sourabh shah");
//        long timestamp;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            timestamp = Instant.now().getEpochSecond();
//        } else {
//            timestamp = System.currentTimeMillis() / 1000;
//        }
//        map.put("last_msg_time", timestamp);
//        map.put("order_id", order_id);
//        map.put("isseen", false);
//        database.child("Chats").child(order_id).push().setValue(map);
//
//        final String msg = message;
//
//        if (notify) {
//            sendNotification(receiver, msg);
//        }
//        notify = false;
//    }
//
//    private void sendNotification(String receiver, String msg) {
//        Data data = new Data("New Message", msg, "101");
//        Sender sender = new Sender(data, "eB_t5zJyTG-zdFzqaBxH8L:APA91bEfIJS9yn7irgPZhkcZ-LC1PZ1OLyfto1eAzk3UUwb-j9NG6Cn58x8Tl3zomQuYe81xVpA_wG0sn7yyvO23onkRU6js3YpbS8aMVKjHcZskZq-IzmynseOQyFe4fjRtNf3pJs4Q");
//
//        api.sendNotification(sender)
//                .enqueue(new Callback<MyResponse>() {
//                    @Override
//                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                        if (response.code() == 200) {
//                            Log.d(TAG, "onResponse: " + response.body().success);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<MyResponse> call, Throwable t) {
//
//                    }
//                });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d(TAG, "onResume: ");
//        if(onScreen){
//            reference.addValueEventListener(seenListener);
//            reference.addValueEventListener(readLiveMessageListener);
//            onScreen=false;
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        ConversationActivity.isOpen = true;
//        reference.removeEventListener(seenListener);
//        reference.removeEventListener(readLiveMessageListener);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        ConversationActivity.isOpen = true;
//        reference.removeEventListener(seenListener);
//        reference.removeEventListener(readLiveMessageListener);
//        onScreen =  true;
//    }
//
//    @Override
//    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//    }
//
//    @Override
//    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//    }
//
//    @Override
//    public void afterTextChanged(Editable s) {
//        chatBinding.sendButton.setColorFilter(s.length() > 0 ? getResources().getColor(R.color.chat_text_right) : getResources().getColor(R.color.chat_box_right));
//    }
//
//
//}