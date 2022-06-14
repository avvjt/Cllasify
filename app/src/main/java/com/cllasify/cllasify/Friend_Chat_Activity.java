package com.cllasify.cllasify;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Adaptor.Adaptor_Friend_Chat;
import com.cllasify.cllasify.swipeToReply.MessageSwipeController;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


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
    ImageButton ib_FrndP_csubmit, swipe_left, swipe_right;

    EditText messageTxtFriend;
    CircleImageView friendImg;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    Uri userPhoto;
    String userID, userEmailID, userName;


    RelativeLayout moreOptions, fragment_friend;
    LinearLayout chatOption;

    boolean found, clickedItem;

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
        friendImg = v.findViewById(R.id.friendImg);

        swipe_left = v.findViewById(R.id.swipe_left);
        swipe_right = v.findViewById(R.id.swipe_right);
        TextView friendNameTv = v.findViewById(R.id.tv_friend_name);

        chatOption = v.findViewById(R.id.ll_FrndP_btm_send);


        swipe_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast();
            }
        });

        swipe_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast();
            }
        });

        ib_FrndP_csubmit.setEnabled(false);

        messageTxtFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String c = String.valueOf(s);

                if (c.trim().isEmpty()) {
                    ib_FrndP_csubmit.setEnabled(false);
                    ib_FrndP_csubmit.setImageResource(R.drawable.ic_send_disable);
                } else {
                    ib_FrndP_csubmit.setEnabled(true);
                    ib_FrndP_csubmit.setImageResource(R.drawable.ic_send_24);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        messageList = new ArrayList<>();
        adaptor_friend_chat = new Adaptor_Friend_Chat(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adaptor_friend_chat);

        String friendName = getArguments().getString("name");
        receiverUid = getArguments().getString("receiverUid");
        senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;


        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            userID = currentUser.getUid();

            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);


            adaptor_friend_chat.setLongClickListener(new Adaptor_Friend_Chat.OnItemLongClickListener() {
                @Override
                public void getPosition(int position, String senderUserId, String receiverUserId, String messageId, String text) {
                    dialog.show();

                    if (senderUserId.equals(userID)) {

                        Log.d("USERID", "getPosition: " + senderUserId);
                        Log.d("USERID", "messageId: " + messageId);

                        dialog.setContentView(R.layout.more_chat_options);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.getWindow().setGravity(Gravity.BOTTOM);


                        Button copyBtn = dialog.findViewById(R.id.copy_button);
                        copyBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData data = (ClipData) ClipData.newPlainText("text", text);
                                clipboardManager.setPrimaryClip(data);

                            }
                        });

                        Button unsend = dialog.findViewById(R.id.unsend_button);
                        unsend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                                adaptor_friend_chat.removeItem(position);

                                DatabaseReference firebaseDatabaseUnsendMSG = FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages");

                                firebaseDatabaseUnsendMSG.child(messageId).removeValue();

                                DatabaseReference firebaseDatabaseUnsendMSGReceiver = FirebaseDatabase.getInstance().getReference().child("chats").child(receiverRoom).child("messages");

                                firebaseDatabaseUnsendMSGReceiver.child(receiverUserId).removeValue();



                            }
                        });

                        Button reply = dialog.findViewById(R.id.reply_button);
                        reply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

                    }
                    else {
                        Log.d("USERID", "getPosition: " + senderUserId);
                        Log.d("USERID", "messageId: " + messageId);

                        dialog.setContentView(R.layout.more_chat_options_others);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.getWindow().setGravity(Gravity.BOTTOM);


                        Button copyBtn = dialog.findViewById(R.id.copy_button);
                        copyBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                                ClipboardManager clipboardManager = (ClipboardManager) getActivity().getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData data = (ClipData) ClipData.newPlainText("text", text);
                                clipboardManager.setPrimaryClip(data);

                            }
                        });

                        Button report = dialog.findViewById(R.id.report_button);
                        report.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

                        Button reply = dialog.findViewById(R.id.reply_button);
                        reply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });


                    }
                }



            });


        }




        DatabaseReference refUserProfPic = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(receiverUid);
        refUserProfPic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("Name").exists()) {
                    String userName = snapshot.child("Name").getValue().toString();
                    friendNameTv.setText(userName);
                }
                if (snapshot.child("profilePic").exists()) {
                    String profilePicUrl = snapshot.child("profilePic").getValue().toString();
                    Log.d("TSTNOTIFY", "MyViewHolder: " + profilePicUrl);
                    Glide.with(Friend_Chat_Activity.this).load(profilePicUrl).into(friendImg);
                } else {
                    Glide.with(Friend_Chat_Activity.this).load(R.drawable.maharaji).into(friendImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String userID = currentUser.getUid();
        String userName = currentUser.getDisplayName();


        DatabaseReference firebaseDatabaseSaveRecChat = FirebaseDatabase.getInstance().getReference().child("chats").child("RecentChats").child(senderUid).child("recentChatUser");

        firebaseDatabaseSaveRecChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getChildrenCount() == 0) {
                    FriendsListClass friendsListClass = new FriendsListClass(receiverUid, friendName);
                    firebaseDatabaseSaveRecChat.child(receiverUid).setValue(friendsListClass);
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
                        firebaseDatabaseSaveRecChat.child(receiverUid).setValue(friendsListClass);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        DatabaseReference firebaseDatabaseSaveRecChatForFriend = FirebaseDatabase.getInstance().getReference().child("chats").child("RecentChats").child(receiverUid).child("recentChatUser");

        firebaseDatabaseSaveRecChatForFriend.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                FriendsListClass friendsListClass = new FriendsListClass(senderUid, userName);
                firebaseDatabaseSaveRecChatForFriend.child(senderUid).setValue(friendsListClass);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();


        Toolbar toolbar = v.findViewById(R.id.toolbar);
        Log.d(TAG, "onCreate: friend name ->" + friendName);

        friendNameTv.setText(friendName);


        Log.d("MESS", "receiverRoom: " + receiverRoom + "\nsenderRoom: " + senderRoom);

        firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
/*
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Class_Single_Friend message = snapshot1.getValue(Class_Single_Friend.class);
                    message.setMessageId(snapshot1.getKey());
                    messageList.add(message);
                }
                recyclerView.smoothScrollToPosition(Objects.requireNonNull(recyclerView.getAdapter()).getItemCount());
                adaptor_friend_chat.notifyDataSetChanged();
*/

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Log.d("DOUBTCHK", "onDataChange: " + postSnapshot.getValue());

                    Class_Single_Friend class_userDashBoard = postSnapshot.getValue(Class_Single_Friend.class);
                    messageList.add(class_userDashBoard);
                }
                adaptor_friend_chat.setList(messageList);
                adaptor_friend_chat.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(Objects.requireNonNull(recyclerView.getAdapter()).getItemCount());

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
                String messagePushIdSender = FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages").push().toString();
                String messagePushIdReceiver = FirebaseDatabase.getInstance().getReference().child("chats").child(receiverRoom).child("messages").push().toString();

                String pushSplitSender[] = messagePushIdSender.split("/");
                String pushSplitReceiver[] = messagePushIdReceiver.split("/");

                Log.d(TAG, "onClick: " + pushSplitSender[6]);
                Log.d(TAG, "onClick: " + pushSplitReceiver[6]);


                Date date = new Date();
                Class_Single_Friend messages = new Class_Single_Friend(pushSplitSender[6],pushSplitReceiver[6], messageText, senderUid, date.getTime());
//                Class_Group userAddGroupClass = new Class_Group(String.valueOf(date.getTime()), userName, senderUid, "nope", "classPosition[0]", messageText, "chat", "subjectUniPushId[0]", "push", 0);
                messageTxtFriend.setText("");
                firebaseDatabase.getReference().child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .child(pushSplitSender[6])
                        .setValue(messages).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                firebaseDatabase.getReference().child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .child(pushSplitReceiver[6])
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

    public void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_swipe, null);
        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }





    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_chat);




    }
     */
}
