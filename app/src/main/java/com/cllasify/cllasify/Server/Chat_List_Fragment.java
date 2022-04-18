package com.cllasify.cllasify.Server;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cllasify.cllasify.Adaptor.Adaptor_Friends;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGroup;
import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class Chat_List_Fragment extends Fragment {

    RecyclerView rv_FrndP_ChatList;
    RelativeLayout rl_FrndPanel;

    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    String userID,userName,userEmailID;

    List<Class_Group> list_ChatDashboard;
    Adaptor_Friends showChatDashadaptor;
    DatabaseReference refChatDashboard;

    SearchView sv_ChatSearch;
    TextView tv_FrndP_Title;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.chat_list_fragment, container, false);



//        rl_FrndPanel=view.findViewById(R.id.rl_FrndPanel);
        rv_FrndP_ChatList=view.findViewById(R.id.rv_FrndP_ChatList);

        tv_FrndP_Title=view.findViewById(R.id.tv_FrndP_Title);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        userID=currentUser.getUid();
        userName=currentUser.getDisplayName();
        userEmailID=currentUser.getEmail();

        DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child("Temp").child(userID);
        refSaveCurrentData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
//                        Class_Group class_group=new Class_Group();
//                    refSaveCurrentData.child("TempChatUserId").setValue(frndUserId);
//                    refSaveCurrentData.child("TempChatUserName").setValue(subGroupName);
                    if(snapshot.child("TempChat").exists() && snapshot.child("TempChatUserName").exists()  ) {
                        String frndUserId = snapshot.child("TempChatUserId").getValue().toString().trim();
                        String frndUserName = snapshot.child("TempChatUserName").getValue().toString().trim();
                        tv_FrndP_Title.setText(frndUserName);
                        checkChatDashboard(frndUserId);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        rv_FrndP_ChatList.setLayoutManager(new LinearLayoutManager(getContext()));
        list_ChatDashboard = new ArrayList<>();

        showChatDashadaptor = new Adaptor_Friends(getContext(), list_ChatDashboard);
        rv_FrndP_ChatList.setAdapter(showChatDashadaptor);

        return view;
    }

    private void checkChatDashboard(String frndUserId) {
        refChatDashboard = FirebaseDatabase.getInstance().getReference().child("Users").child("Friends").child(userID);
        list_ChatDashboard.clear();
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getChildrenCount() > 0) {
                    Class_Group class_userDashBoard = dataSnapshot.getValue(Class_Group.class);
                    list_ChatDashboard.add(class_userDashBoard);
                    showChatDashadaptor.notifyDataSetChanged();
//                    notifyPB.dismiss();
                } else {
//                    Toast.makeText(getContext(), "No Question asked yet,Please Ask First Questions", Toast.LENGTH_SHORT).show();
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
        refChatDashboard.addChildEventListener(childEventListener);


    }

}
