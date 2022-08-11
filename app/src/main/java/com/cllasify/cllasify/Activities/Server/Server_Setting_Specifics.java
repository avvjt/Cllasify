package com.cllasify.cllasify.Activities.Server;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.NetworkBroadcast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class Server_Setting_Specifics extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    EditText et_ServerName, et_schoolBio, et_schoolEmail;
    DatabaseReference getTempData;
    String currUserId;
    String groupPushId;
    ImageButton doneBtn, btn_Back;
    DatabaseReference databaseReference;

    ImageView serverImage;
    TextView changeServerImage;
    Button serverDelete;

    StorageReference storageReference;
    ProgressBar progBar;


    public void checkDarkLightDefaultStatusBar() {
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {

            case Configuration.UI_MODE_NIGHT_YES:
                getWindow().setStatusBarColor(Color.parseColor("#17181c"));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                // edited here
                getWindow().setStatusBarColor(Color.parseColor("#ffffff"));

                break;


        }
    }

    private void checkOnlineStatus(String status) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();

        DatabaseReference setStatus = FirebaseDatabase.getInstance().getReference().child("Users").child("Registration").child(userID);
        setStatus.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("userStatus").exists()) {
                    setStatus.child("userStatus").setValue(status);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    protected void onStart() {
        checkOnlineStatus("online");
        super.onStart();
    }

    @Override
    protected void onPause() {

        String timestamp = String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timestamp);
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkDarkLightDefaultStatusBar();
        setContentView(R.layout.activity_server_setting_specifics);

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        et_ServerName = findViewById(R.id.et_ServerName);
        et_schoolBio = findViewById(R.id.et_schoolBio);
        et_schoolEmail = findViewById(R.id.et_schoolEmail);
        doneBtn = findViewById(R.id.doneBtn);
        btn_Back = findViewById(R.id.btn_Back);
        serverDelete = findViewById(R.id.deleteBtn);

        progBar = findViewById(R.id.progBar);

        changeServerImage = findViewById(R.id.changeServerImage);
        serverImage = findViewById(R.id.serverImage);

        storageReference = FirebaseStorage.getInstance().getReference();

        if (getIntent().hasExtra("currUserId")) {
            currUserId = getIntent().getStringExtra("currUserId");
        }
        if (getIntent().hasExtra("groupPushId")) {
            groupPushId = getIntent().getStringExtra("groupPushId");
        }
        serverDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                androidx.appcompat.app.AlertDialog.Builder alertdialogbuilder = new androidx.appcompat.app.AlertDialog.Builder(Server_Setting_Specifics.this, R.style.AlertDialogCustom);
                alertdialogbuilder.setTitle("Please confirm !!!")
                        .setMessage("Do you want to delete the Server?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteSpecificServer(groupPushId, currUserId);
                            }
                        }).setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                androidx.appcompat.app.AlertDialog alert = alertdialogbuilder.create();
                alert.show();

            }
        });


        DatabaseReference refSaveServerProfPic = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(groupPushId).child("serverProfilePic");
        refSaveServerProfPic.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (!(Server_Setting_Specifics.this).isFinishing()) {
                        Glide.with(Server_Setting_Specifics.this).load(snapshot.getValue()).into(serverImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        getTempData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(currUserId);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Groups");

        getTempData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String clickedGrpPushId = String.valueOf(snapshot.child("clickedGroupPushId").getValue());

                groupPushId = String.valueOf(snapshot.child("clickedGroupPushId").getValue());

                databaseReference.child("All_Universal_Group").child(clickedGrpPushId).child("groupName").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        et_ServerName.setText(snapshot.getValue(String.class));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                databaseReference.child("All_Universal_Group").child(clickedGrpPushId).child("ServerEmail").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        et_schoolEmail.setText(snapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                databaseReference.child("All_Universal_Group").child(clickedGrpPushId).child("ServerBio").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        et_schoolBio.setText(snapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                doneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String serverName = et_ServerName.getText().toString().trim();
                        final String serverBio = et_schoolBio.getText().toString().trim();
                        final String serverEmail = et_schoolEmail.getText().toString().trim();

                        databaseReference.child("All_Universal_Group").child(clickedGrpPushId).child("ServerBio").setValue(serverBio);
                        databaseReference.child("All_Universal_Group").child(clickedGrpPushId).child("ServerEmail").setValue(serverEmail);
                        databaseReference.child("All_Universal_Group").child(clickedGrpPushId).child("groupName").setValue(serverName);
                        databaseReference.child("All_Universal_Group").child(clickedGrpPushId).child("User_Subscribed_Groups").child(currUserId).child("groupName").setValue(serverName);
                        databaseReference.child("User_All_Group").child(currUserId).child(clickedGrpPushId).child("groupName").setValue(serverName);
                        databaseReference.child("User_Public_Group").child(currUserId).child(clickedGrpPushId).child("groupName").setValue(serverName);

                        finish();
//                serverName = String.valueOf(snapshot.child("clickedGroupName").getValue());
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


        changeServerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri = data.getData();
                uploadImageToFirebaseStorage(imageUri);
            }
        }

    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        // upload Image To FirebaseStorage
        progBar.setVisibility(View.VISIBLE);
        final StorageReference fileRef = storageReference.child("server profile image/" + groupPushId + "/ServerProfile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (!(Server_Setting_Specifics.this).isFinishing()) {
                            Log.d("PROFPIC", "onSuccess: " + uri);
                            DatabaseReference refSaveServerProfPic = FirebaseDatabase.getInstance().getReference().child("Groups").child("All_Universal_Group").child(groupPushId);
                            refSaveServerProfPic.child("serverProfilePic").setValue(uri.toString());
                            Glide.with(Server_Setting_Specifics.this).load(uri).into(serverImage);
                            progBar.setVisibility(View.GONE);
                            showToast();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Server_Setting_Specifics.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_server_dp, (ViewGroup) findViewById(R.id.toast));
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private void deleteSpecificServer(String groupPushId, String userID) {

        DatabaseReference databaseReferenceServDel = FirebaseDatabase.getInstance().getReference().child("Groups");

        databaseReferenceServDel.child("UserAddedOrJoinedGrp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.d("CHLDELSERV", "onDataChange: " + dataSnapshot.getKey());
                    databaseReferenceServDel.child("UserAddedOrJoinedGrp").child(Objects.requireNonNull(dataSnapshot.getKey())).child(groupPushId).setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReferenceServDel.child("Check_Group_Admins").child(groupPushId).removeValue();
        databaseReferenceServDel.child("All_GRPs").child(groupPushId).removeValue();
        databaseReferenceServDel.child("All_Universal_Group").child(groupPushId).removeValue();
        databaseReferenceServDel.child("User_All_Group").child(userID).child(groupPushId).removeValue();
        databaseReferenceServDel.child("User_Public_Group").child(userID).child(groupPushId).removeValue();
        databaseReferenceServDel.child("Chat_Message").child(groupPushId).removeValue();
        databaseReferenceServDel.child("Doubt").child(groupPushId).removeValue();
        databaseReferenceServDel.child("Documents").child(groupPushId).removeValue();
        databaseReferenceServDel.child("Temp").child(userID).child("clickedClassName").removeValue();
        databaseReferenceServDel.child("Temp").child(userID).child("clickedGroupName").removeValue();
        databaseReferenceServDel.child("Temp").child(userID).child("clickedGroupPushId").removeValue();
        databaseReferenceServDel.child("Temp").child(userID).child("clickedStudentUniPushClassId").removeValue();
        databaseReferenceServDel.child("Temp").child(userID).child("clickedSubjectName").removeValue();


        Intent intent = new Intent(Server_Setting_Specifics.this, Server_Activity.class);
        intent.putExtra("stateShimmering","stop");
        startActivity(intent);


    }

}