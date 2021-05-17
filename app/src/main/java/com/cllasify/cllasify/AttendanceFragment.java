package com.cllasify.cllasify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cllasify.cllasify.Adaptor.Adaptor_ShowGroup;
import com.cllasify.cllasify.Adaptor.Adaptor_ShowGrpMember;
import com.cllasify.cllasify.Class.Class_Group;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class AttendanceFragment extends Fragment {


    List<Class_Group>  listGrpMemberList;
    DatabaseReference refGrpMemberList;
    RecyclerView rv_GrpMemberList;
    Adaptor_ShowGrpMember showGrpMemberList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        String groupPushId = getArguments().getString("groupPushId");

        rv_GrpMemberList=view.findViewById(R.id.rv_GrpMemberAttendance);
        rv_GrpMemberList.setLayoutManager(new LinearLayoutManager(getContext()));


        listGrpMemberList = new ArrayList<>();
//            listGroupSTitle=new ArrayList<>();


        showGrpMemberList = new Adaptor_ShowGrpMember(getContext(), listGrpMemberList);
        rv_GrpMemberList.setAdapter(showGrpMemberList);

        refGrpMemberList = FirebaseDatabase.getInstance().getReference().child( "Groups" ).child( "All_Universal_Group" ).child(groupPushId).child("User_Subscribed_Groups");

        refGrpMemberList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

//                String value=snapshot.getValue(String.class);
//                Toast.makeText(getContext(), "c"+value, Toast.LENGTH_SHORT).show();
////                arrayList.add(value);
////                arrayAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList);
////                listView.setAdapter(arrayAdapter);

                Class_Group class_userDashBoard = snapshot.getValue(Class_Group.class);
                listGrpMemberList.add(class_userDashBoard);
                showGrpMemberList.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;
    }
}