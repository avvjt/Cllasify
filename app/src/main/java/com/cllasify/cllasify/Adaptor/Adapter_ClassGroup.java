package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class_Group_Names;
import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Subject_Details_Model;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Adapter_ClassGroup extends RecyclerView.Adapter<Adapter_ClassGroup.ViewHolder> {

    private static final String TAG = Adapter_ClassGroup.class.getSimpleName();

    Context context;
    List<Class_Group_Names> parentItemArrayListClassName;
    List <Subject_Details_Model> testingNo;
    onAddSubjectClickListener onAddSubjectClickListener;

    public Adapter_ClassGroup(Context context, onAddSubjectClickListener onAddSubjectClickListener) {
        this.context = context;
        this.onAddSubjectClickListener = onAddSubjectClickListener;
    }

    public void setOnItemClickListener(onAddSubjectClickListener listener){
        onAddSubjectClickListener =listener;
    }


    public void setParentItemArrayListClassName(List<Class_Group_Names> parentItemArrayListClassName) {
        this.parentItemArrayListClassName = parentItemArrayListClassName;
    }

    @NonNull
    @Override
    public Adapter_ClassGroup.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_lvl2_groupname, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_ClassGroup.ViewHolder holder, int position) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();

        holder.classGroupName.setText(parentItemArrayListClassName.get(holder.getAdapterPosition()).getClassName());

        Log.d(TAG, "onBindViewHolder: Adapter Class: "+parentItemArrayListClassName.get(holder.getAdapterPosition()).getClassName());

        Class_Group_Names class_group_names = parentItemArrayListClassName.get(holder.getAdapterPosition());

        holder.addTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddSubjectClickListener.onAddSubjectClickListener(parentItemArrayListClassName.get(holder.getAdapterPosition()).getClassName(),class_group_names.getUniPushClassId());
            }
        });
/*

*/

        DatabaseReference checkAdminMe = FirebaseDatabase.getInstance().getReference().child("Groups").child("Check_Group_Admins").child(parentItemArrayListClassName.get(holder.getAdapterPosition()).getGroupPushId()).child("classAdminList").child(userID).child("admin");
        checkAdminMe.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.getValue().equals(true)) {
                        holder.addTopicButton.setVisibility(View.VISIBLE);

                    }
                } else {
                    holder.addTopicButton.setVisibility(View.GONE);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        holder.classGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked on Class", Toast.LENGTH_SHORT).show();
                Log.d("POSS", "Class position : "+class_group_names.getClassName());
                onAddSubjectClickListener.onClassClickListener(holder.getAdapterPosition(), parentItemArrayListClassName.get(holder.getAdapterPosition()).getClassName(),class_group_names.getUniPushClassId());
            }
        });

        holder.dropUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.subjectList.setVisibility(View.GONE);
                holder.dropUpBtn.setVisibility(View.GONE);
                holder.dropDownBtn.setVisibility(View.VISIBLE);
            }
        });

        holder.dropDownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.subjectList.setVisibility(View.VISIBLE);
                holder.dropDownBtn.setVisibility(View.GONE);
                holder.dropUpBtn.setVisibility(View.VISIBLE);
            }
        });
/*
        holder.classGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddSubjectClickListener.onClassClickListener(holder.getAdapterPosition());
                holder.addTopicButton.setVisibility(View.VISIBLE);
            }
        });
*/


        DatabaseReference refSaveCurrDataForSubj = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp")
                .child(SharePref.getDataFromPref(Constant.USER_ID));
//        adapter_topicList.setOnItemClickListener(new Adapter_TopicList.OnItemClickListener() {
//            @Override
//            public void subjectChildClick(String groupName, String groupPushId, String groupSubjectPushId, String groupClassSubjects) {
//                Toast.makeText(context, "Testing the subject on Adapter_ClassGroup!!", Toast.LENGTH_SHORT).show();
//                refSaveCurrDataForSubj.child("groupName").setValue(groupName);
//                refSaveCurrDataForSubj.child("SubGroupPushId").setValue(groupSubjectPushId);//*
//                refSaveCurrDataForSubj.child("GroupPushId").setValue(groupPushId);//*
//                refSaveCurrDataForSubj.child("groupClassSubjects").setValue(groupClassSubjects);
//            }
//        });

        if(parentItemArrayListClassName.get(holder.getAdapterPosition()).getChildItemList()!=null){
            Adapter_TopicList adapter_topicList = new Adapter_TopicList(context.getApplicationContext());
            holder.subjectList.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
            holder.subjectList.setAdapter(adapter_topicList);

            adapter_topicList.setOnSubjectClickListener(new Adapter_TopicList.onSubjectClickListener() {
                @Override
                public void onSubjectClick() {
                    Log.d(TAG, "onSubjectClick: "+holder.getAdapterPosition());
                    onAddSubjectClickListener.onSubClick(holder.getAdapterPosition(), parentItemArrayListClassName.get(holder.getAdapterPosition()).getClassName(),class_group_names.getUniPushClassId());

                    DatabaseReference refSaveCurrentData = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                    refSaveCurrentData.child("classPosition").setValue(holder.getAdapterPosition());
                    refSaveCurrentData.child("clickedClassName").setValue(parentItemArrayListClassName.get(holder.getAdapterPosition()).getClassName());
                    refSaveCurrentData.child("uniPushClassId").setValue(class_group_names.getUniPushClassId());


                }
            });
//            Log.d("TOP", "onBindViewHolder: "+parentItemArrayListClassName.get(position).getChildItemList().get(position).getSubjectName());
            adapter_topicList.setSubjectDetailsModelList(parentItemArrayListClassName.get(position).getChildItemList());
            adapter_topicList.notifyDataSetChanged();
        }
        else {
            testingNo = new ArrayList<>();
            Adapter_TopicList adapter_topicList = new Adapter_TopicList(context.getApplicationContext());
            holder.subjectList.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
            holder.subjectList.setAdapter(adapter_topicList);
            adapter_topicList.setSubjectDetailsModelList(testingNo);
            adapter_topicList.notifyDataSetChanged();
            Toast.makeText(context, "No sub classes", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public int getItemCount() {
        return parentItemArrayListClassName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView classGroupName;
        ImageButton dropUpBtn,dropDownBtn;
        ImageButton addTopicButton;
        RecyclerView subjectList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            classGroupName = itemView.findViewById(R.id.tv_classGroupTitle);
            addTopicButton = itemView.findViewById(R.id.addNewTopicButton);
            subjectList = itemView.findViewById(R.id.subjectList);
            dropUpBtn = itemView.findViewById(R.id.dropUpBtn);
            dropDownBtn = itemView.findViewById(R.id.dropDownBtn);
        }
    }

    public interface onAddSubjectClickListener{
        void onAddSubjectClickListener(String groupName,String uniPushClassId);
        void onClassClickListener(int position,String classGroupName,String uniPushClassId);
        void onSubClick(int classPosition,String clickedClassName,String uniClassPushId);
    }
}


