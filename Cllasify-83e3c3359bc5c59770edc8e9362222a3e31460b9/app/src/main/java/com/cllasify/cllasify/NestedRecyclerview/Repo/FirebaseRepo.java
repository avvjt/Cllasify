package com.cllasify.cllasify.NestedRecyclerview.Repo;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cllasify.cllasify.NestedRecyclerview.Model.ChildItem;
import com.cllasify.cllasify.NestedRecyclerview.Model.ParentItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepo {
    private DatabaseReference databaseReference;
    private OnRealtimeDbTaskComplete onRealtimeDbTaskComplete;

    public FirebaseRepo(OnRealtimeDbTaskComplete onRealtimeDbTaskComplete) {
        this.onRealtimeDbTaskComplete = onRealtimeDbTaskComplete;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("All_GRPs").child("Uni_Group_Name");
    }

    public void getAllData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ParentItem> parentItemList = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Log.d("Firee", "onDataChange: "+ds.child("className"));

                    ParentItem parentItem = new ParentItem();
                    parentItem.setClassName(ds.child("className").getValue(String.class));

                    GenericTypeIndicator<ArrayList<ChildItem>> genericTypeIndicator =
                            new GenericTypeIndicator<ArrayList<ChildItem>>() {};

                    parentItem.setChildItemList(ds.child("classSubjectData").getValue(genericTypeIndicator));
                    parentItemList.add(parentItem);

                }
                onRealtimeDbTaskComplete.onSuccess(parentItemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                onRealtimeDbTaskComplete.onFailure(error);
            }
        });
    }
    public interface OnRealtimeDbTaskComplete {
        void onSuccess(List<ParentItem> parentItemList);
        void onFailure (DatabaseError error);
    }
}
