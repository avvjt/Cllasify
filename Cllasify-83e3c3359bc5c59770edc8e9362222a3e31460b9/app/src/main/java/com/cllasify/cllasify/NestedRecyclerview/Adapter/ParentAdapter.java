package com.cllasify.cllasify.NestedRecyclerview.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.NestedRecyclerview.Model.ParentItem;
import com.cllasify.cllasify.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.ParentViewHolder> {

    private List<ParentItem> parentItemList;
    Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setParentItemList(List<ParentItem> parentItemList) {
        this.parentItemList = parentItemList;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_parent_item , parent, false);
        return new ParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {

        Toast.makeText(context, "TestFirebaseActivity", Toast.LENGTH_SHORT).show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("All_GRPs").child("Uni_Group_Name")
                .child("0").child("classSubjectData");

//        DatabaseReference testDatabaseReference = FirebaseDatabase.getInstance().getReference().child("All_GRPs").child("Uni_Group_No_1_Hmm").child("det");
        DatabaseReference testDatabaseReference = FirebaseDatabase.getInstance().getReference().child("All_GRPs").child("Uni_Group_Name");

        holder.addSubjBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subjectNames = holder.addSubjEt.getText().toString().trim();

                testDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

//                        Log.d("SNAAP", "onDataChange: "+snapshot.getChildrenCount());
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            Log.d("SNAAP", "onDataChange: " + dataSnapshot.child("classSubjectData").child("1").child("subjectName").getValue());
//                            Log.d("SNAAPY", "onDataChange: " + dataSnapshot.getKey());

                            testDatabaseReference.child(String.valueOf(dataSnapshot.getKey())).child("classSubjectData").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                        Log.d("SNAAPYKey", "onDataChange: " + snapshot.getKey());
                                        Log.d("SNAAPY", "onDataChange: " + dataSnapshot1.child("subjectName").getValue());
                                        Log.d("SNAAPY", "onDataChange: " + testDatabaseReference.child(String.valueOf(dataSnapshot1.getChildrenCount())).child("subjectName").setValue(subjectNames));

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

//                                .child(String.valueOf(snapshot1.getChildrenCount())).child("subjectName").setValue("Physics");

//                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()){

//                            Log.d("SNAAP", "onDataChange: "+testDatabaseReference.child("0").child("classSubjectData").child(String.valueOf(dataSnapshot.getChildrenCount())).child("subjectName").setValue("Physics"));
//                            dataSnapshot.child("classSubjectData");
//                            testDatabaseReference.child("classSubjectData").child(String.valueOf(dataSnapshot.getChildrenCount())).child("subjectName").setValue(subjectNames + "Testttt");
//                            dataSnapshot.child("classSubjectData").child(String.valueOf(dataSnapshot.getChildrenCount())).child("subjectName").setValue(subjectNames + "Testttt");
//                        }


                        testDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                long noofGroupinCategory = snapshot.getChildrenCount();
                                String push = "Uni_Group_No_" + noofGroupinCategory + "_" + subjectNames;

                                testDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        testDatabaseReference.child("classSubjectData").child(String.valueOf(snapshot.getChildrenCount())).child("subjectName").setValue(subjectNames + "Testttt");
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                                /*
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    Log.d("SnapD", "onDataChange: "+dataSnapshot.getKey());
                                    testDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            testDatabaseReference.child("det").child(String.valueOf(snapshot.getChildrenCount())).child("subN").setValue("sljhljhslfad");
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                    */
                        /*
                        testDatabaseReference.child(String.valueOf(snapshot.getChildrenCount())).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                testDatabaseReference.child(String.valueOf(snapshot.getChildrenCount())).child("subN").setValue(subjectNames);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    */
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                Log.d("TS", "ParentViewHolder: " + subjectNames);
                databaseReference.child("0").child("subjectName").setValue(subjectNames);
            }
        });

        ParentItem parentItem = parentItemList.get(position);
        holder.parentName.setText(parentItem.getClassName());
        holder.childRecyclerView.setHasFixedSize(true);
        holder.childRecyclerView.setLayoutManager(new GridLayoutManager(holder.itemView.getContext(), 4));
        ChildAdapter childAdapter = new ChildAdapter();
        childAdapter.setChildItemList(parentItem.getChildItemList());
        holder.childRecyclerView.setAdapter(childAdapter);
        childAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        if (parentItemList != null) {
            return parentItemList.size();
        } else {
            return 0;
        }
    }

    public class ParentViewHolder extends RecyclerView.ViewHolder{
        private TextView parentName;
        private Button addSubjBtn;
        private RecyclerView childRecyclerView;
        private EditText addSubjEt;

        public ParentViewHolder(@NonNull View itemView) {
            super(itemView);
            parentName = itemView.findViewById(R.id.eachParentName);
            childRecyclerView = itemView.findViewById(R.id.childRecyclerview);
            addSubjBtn = itemView.findViewById(R.id.addServersOrGroupsBtn);
            addSubjEt = itemView.findViewById(R.id.addServersOrGroups);


        }
    }
}
