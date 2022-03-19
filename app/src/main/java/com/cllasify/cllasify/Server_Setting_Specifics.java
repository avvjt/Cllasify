package com.cllasify.cllasify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cllasify.cllasify.Home.Server_Activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Server_Setting_Specifics extends AppCompatActivity {

    EditText et_ServerName, et_schoolBio;
    DatabaseReference getTempData;
    String currUserId;
    String groupPushId;
    ImageButton doneBtn, serverDelete;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_setting_specifics);

        et_ServerName = findViewById(R.id.et_ServerName);
        et_schoolBio = findViewById(R.id.et_schoolBio);
        doneBtn = findViewById(R.id.doneBtn);
        serverDelete = findViewById(R.id.deleteBtn);

        if (getIntent().hasExtra("currUserId")) {
            currUserId = getIntent().getStringExtra("currUserId");
        }
        if (getIntent().hasExtra("groupPushId")) {
            groupPushId = getIntent().getStringExtra("groupPushId");
        }
        serverDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSpecificServer(groupPushId, currUserId);
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
                        databaseReference.child("All_Universal_Group").child(clickedGrpPushId).child("ServerBio").setValue(serverBio);
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

    }


    private void deleteSpecificServer(String groupPushId, String userID) {

        DatabaseReference databaseReferenceServDel = FirebaseDatabase.getInstance().getReference().child("Groups");

        databaseReferenceServDel.child("Check_Group_Admins").child(groupPushId).removeValue();
        databaseReferenceServDel.child("All_GRPs").child(groupPushId).removeValue();
        databaseReferenceServDel.child("All_Universal_Group").child(groupPushId).removeValue();
        databaseReferenceServDel.child("User_All_Group").child(userID).child(groupPushId).removeValue();
        databaseReferenceServDel.child("User_Public_Group").child(userID).child(groupPushId).removeValue();
        databaseReferenceServDel.child("Chat_Message").child(groupPushId).removeValue();
        databaseReferenceServDel.child("Doubt").child(groupPushId).removeValue();
        databaseReferenceServDel.child("Temp").child(userID).child("clickedClassName").removeValue();
        databaseReferenceServDel.child("Temp").child(userID).child("clickedGroupName").removeValue();
        databaseReferenceServDel.child("Temp").child(userID).child("clickedGroupPushId").removeValue();
        databaseReferenceServDel.child("Temp").child(userID).child("clickedStudentUniPushClassId").removeValue();
        databaseReferenceServDel.child("Temp").child(userID).child("clickedSubjectName").removeValue();


        Intent intent = new Intent(Server_Setting_Specifics.this, Server_Activity.class);
        startActivity(intent);


    }

}