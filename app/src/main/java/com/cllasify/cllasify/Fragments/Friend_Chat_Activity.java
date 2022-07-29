package com.cllasify.cllasify.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.Activities.Server.Server_Activity;
import com.cllasify.cllasify.Adapters.Adaptor_Friend_Chat;
import com.cllasify.cllasify.ModelClasses.Class_Single_Friend;
import com.cllasify.cllasify.Utility.Constant;
import com.cllasify.cllasify.ModelClasses.FriendsListClass;
import com.cllasify.cllasify.Activities.Server.PDFBACK.BaseBackPressedListener;
import com.cllasify.cllasify.Activities.Server.PDFBACK.OnBackPressedListener;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    private int currentProgress = 0;
    private ProgressBar uploadProgressBar;
    TextView uploadPercentage;

    RelativeLayout moreOptions, fragment_friend;
    LinearLayout chatOption;

    boolean found, clickedItem;
    private ImageButton ib_pdf_btn;
    private Uri fileUri;
    private String displayName;
    boolean pdf_flag = false;
    WebView_Fragment webView_fragment;
    private FragmentActivity activity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected OnBackPressedListener onBackPressedListener;

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    private void checkTypingStatus(String userID, String status) {
        DatabaseReference setStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID).child("userStatus");
        setStatus.setValue(status);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.activity_friend_chat, container, false);

        activity = getActivity();

        ((Server_Activity) activity).setOnBackPressedListener(new BaseBackPressedListener(activity));

        recyclerView = v.findViewById(R.id.recyclerView);
        ib_FrndP_csubmit = v.findViewById(R.id.ib_FrndP_csubmit);
        messageTxtFriend = v.findViewById(R.id.et_FrndP_text);
        friendImg = v.findViewById(R.id.friendImg);
        ib_pdf_btn = v.findViewById(R.id.ib_pdf_btn);

        uploadProgressBar = v.findViewById(R.id.uploadProgress);
        uploadPercentage = v.findViewById(R.id.percentage);

        swipe_left = v.findViewById(R.id.swipe_left);
        swipe_right = v.findViewById(R.id.swipe_right);
        TextView friendNameTv = v.findViewById(R.id.tv_friend_name);

        chatOption = v.findViewById(R.id.ll_FrndP_btm_send);

        //3 July 2022

        SimpleDateFormat simpleDateFormatCC = new SimpleDateFormat("yyyy-MM-dd  hh:mm a");
        String dateTimeCC = simpleDateFormatCC.format(Long.parseLong("1656837274321"));

        final String stringDate = dateTimeCC;

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = inputFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);

        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);

        if (calendarDate.compareTo(midnight) >= 0) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Log.d("DATECOMP", "Time format: " + timeFormat.format(date));
        } else {
            SimpleDateFormat dateTimeForm = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Log.d("DATECOMP", "Date format: " + dateTimeForm.format(date));

        }


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

        ib_pdf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPdf();
            }
        });

        messageTxtFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String c = String.valueOf(s);

                if (c.trim().isEmpty()) {
                    ib_pdf_btn.setVisibility(View.VISIBLE);
                    ib_FrndP_csubmit.setVisibility(View.GONE);
                } else {
                    ib_pdf_btn.setVisibility(View.GONE);
                    ib_FrndP_csubmit.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        messageList = new ArrayList<>();
        adaptor_friend_chat = new Adaptor_Friend_Chat(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String friendName = getArguments().getString("name");
        receiverUid = getArguments().getString("receiverUid");
        senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        senderRoom = senderUid + receiverUid;
        receiverRoom = receiverUid + senderUid;

        adaptor_friend_chat.setSenderUserId(senderUid);
        adaptor_friend_chat.setReceiverUserId(receiverUid);
        recyclerView.setAdapter(adaptor_friend_chat);


        adaptor_friend_chat.setOnDownloadClickListener(new Adaptor_Friend_Chat.onDownloadClickListener() {
            @Override
            public void onDownloadClick(String path, String doc_name) {
                onBackPressedListener = null;
                webView_fragment = new WebView_Fragment();
                if (!pdf_flag) {
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle();
                    bundle.putString("path", path);
                    bundle.putString("docName", doc_name);
                    bundle.putString("name", friendName);
                    bundle.putString("receiverUid", receiverUid);
                    bundle.putString("type", "fragment");
                    webView_fragment.setArguments(bundle);
                    getFragmentManager().getBackStackEntryCount();
                    transaction.replace(R.id.below_friend_toolbar, webView_fragment, "FirstFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                    pdf_flag = true;
                }
            }
        });

        adaptor_friend_chat.setOnPDFClickListener(new Adaptor_Friend_Chat.onPDFClickListener() {
            @Override
            public void onPDFClick(int position, String path) {


                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(R.layout.more_pdf_options);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.getWindow().setGravity(Gravity.BOTTOM);
                dialog.create();
                dialog.show();


                Button reply = dialog.findViewById(R.id.reply_button);
                reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


                Button download = dialog.findViewById(R.id.download_btn);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        dialog.dismiss();

                        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            // this will request for permission when permission is not true
                        } else {
                            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(path));
                            String title = URLUtil.guessFileName(path, null, null);
                            request.setTitle(title);
                            request.setDescription(" Downloading File please wait ..... ");
                            String cookie = CookieManager.getInstance().getCookie(path);
                            request.addRequestHeader("cookie", cookie);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
                            DownloadManager downloadManager = (DownloadManager) getActivity().getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
                            downloadManager.enqueue(request);
//                            Toast.makeText(getActivity().getApplicationContext(), " Downloading Started . ", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });

        long delay = 1000;
        final long[] last_text_edit = {0};
        Handler handler = new Handler();

        Runnable input_finish_checker = new Runnable() {
            public void run() {
                if (System.currentTimeMillis() > (last_text_edit[0] + delay - 500)) {
                    checkTypingStatus(senderUid, "online");
                }
            }
        };

        messageTxtFriend.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("TYPECHK", "beforeTextChanged: " + charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.d("TYPECHK", "onTextChanged: " + charSequence);


                if (charSequence.toString().trim().length() == 0) {
                    checkTypingStatus(senderUid, "online");
                } else {
                    checkTypingStatus(senderUid, receiverUid);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.d("TYPECHK", "afterTextChanged: " + editable);
                if (editable.length() > 0) {
                    last_text_edit[0] = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                } else {

                }

            }
        });


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
                Class_Single_Friend messages = new Class_Single_Friend(pushSplitSender[6], pushSplitReceiver[6], messageText, senderUid, date.getTime(), "chat", "");
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

    private void sendPdf() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select PDF File"), 438);

    }

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 438 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            fileUri = data.getData();

            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = getActivity().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }
            setDocumentInFB(fileUri, displayName);

            Log.d("BUNDLESTRING", "onDownloadClick: " + displayName);


        }

    }

    private void setDocumentInFB(Uri fileUri, String fileName) {


        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(SharePref.getDataFromPref(Constant.USER_ID));

        refSaveCurrentData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String subjectUniPushId = snapshot.child("subjectUniPushId").getValue().toString().trim();
                String uniPushClassId = snapshot.child("uniPushClassId").getValue().toString().trim();
                String groupPushId = snapshot.child("clickedGroupPushId").getValue().toString().trim();

                DatabaseReference allDocumentReference = FirebaseDatabase.getInstance().getReference().
                        child("Groups").child("Documents").child(groupPushId).child(uniPushClassId).child(subjectUniPushId).child("All_Document");

                String fileUriPath = fileUri.toString();

                String onlyPath = fileUriPath.substring(0, fileUriPath.indexOf("/"));

                Log.d("ONLYPATH", "onDataChange: " + onlyPath);


                String pushValue[] = allDocumentReference.push().toString().split("/");

                String push = pushValue[9];

                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Document Files");
                String userMsgKeyRef = allDocumentReference.child(onlyPath).push().getKey();
                StorageReference filePath = storageReference.child(userMsgKeyRef + "." + "pdf");

                String messagePushIdSender = FirebaseDatabase.getInstance().getReference().child("chats").child(senderRoom).child("messages").push().toString();
                String messagePushIdReceiver = FirebaseDatabase.getInstance().getReference().child("chats").child(receiverRoom).child("messages").push().toString();

                String pushSplitSender[] = messagePushIdSender.split("/");
                String pushSplitReceiver[] = messagePushIdReceiver.split("/");

                Log.d(TAG, "onClick: " + pushSplitSender[6]);
                Log.d(TAG, "onClick: " + pushSplitReceiver[6]);
                Date date = new Date();

                filePath.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

//                        messageAdapter.setProgVal(1);

                        if (task.isComplete()) {

                            storageReference.child(userMsgKeyRef + "." + "pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d(TAG, "onClick: " + uri);
                                    Class_Single_Friend messages = new Class_Single_Friend(pushSplitSender[6], pushSplitReceiver[6], fileName, senderUid, date.getTime(), "pdf", uri.toString());

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

//                                    Toast.makeText(getActivity().getApplicationContext(), "Document sending successful", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(getActivity(), "Document uploading successful", Toast.LENGTH_SHORT).show();
                                    uploadPercentage.setVisibility(View.GONE);
                                    ib_pdf_btn.setVisibility(View.VISIBLE);
                                    uploadProgressBar.setVisibility(View.GONE);
                                }
                            });


                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getActivity().getApplicationContext(), "Document sending failed", Toast.LENGTH_SHORT).show();
//                        messageAdapter.setProgVal(2);
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

//                        messageAdapter.setProgVal(3);

                        currentProgress = 10;

                        uploadProgressBar.setVisibility(View.VISIBLE);
                        uploadPercentage.setVisibility(View.VISIBLE);
                        ib_pdf_btn.setVisibility(View.GONE);
                        double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();

                        int uploadProg = Math.toIntExact(Math.round(progress));

//                        currentProgress = ;

                        uploadProgressBar.setProgress(uploadProg);
                        uploadProgressBar.setMax(100);

                        uploadPercentage.setText(uploadProg + "%");

//                        Toast.makeText(Server_Activity.this, "Document sending: " + progress, Toast.LENGTH_SHORT).show();


                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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

}
