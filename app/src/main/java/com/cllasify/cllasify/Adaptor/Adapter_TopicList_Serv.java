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
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Subject_Details_Model;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class Adapter_TopicList_Serv extends RecyclerView.Adapter<Adapter_TopicList_Serv.ViewHolder> {


    Context context;
    List<Subject_Details_Model> subjectDetailsModelList;
    private OnItemClickListener mListener;

    public interface  OnItemClickListener{

        void renameSubject(String groupPushId,String classUniPushId,String classPosition,String subjectName);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener=listener;
    }

    public Adapter_TopicList_Serv(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_TopicList_Serv.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.subject_list_item_serv, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_TopicList_Serv.ViewHolder holder, int position) {
        holder.subjectTopic.setText(subjectDetailsModelList.get(position).getSubjectName());

    }

    @Override
    public int getItemCount() {
        return subjectDetailsModelList.size();
    }

    public void setSubjectDetailsModelList(List<Subject_Details_Model> subjectDetailsModelList) {
        this.subjectDetailsModelList = subjectDetailsModelList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTopic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTopic = itemView.findViewById(R.id.tv_subjectTopic);

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            String userID = currentUser.getUid();

            subjectTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Clicked on Subject", Toast.LENGTH_SHORT).show();
                    String userID = SharePref.getDataFromPref(Constant.USER_ID);
                    DatabaseReference posTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                    posTemp.child("subjectPosition").setValue(getAdapterPosition());
                    posTemp.child("clickedSubjectName").setValue(subjectDetailsModelList.get(getAdapterPosition()).getSubjectName());

                    posTemp.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(mListener != null){
                                int subPos = getAdapterPosition();
                                Subject_Details_Model subject_details_model = subjectDetailsModelList.get(subPos);
                                String groupPushId = String.valueOf(snapshot.child("clickedGroupPushId").getValue());
                                String classUniPushId = String.valueOf(snapshot.child("uniPushClassId").getValue());
                                String subjectPosition = String.valueOf(subPos);
                                String subjectName = subject_details_model.getSubjectName();
                                mListener.renameSubject(groupPushId,classUniPushId,subjectPosition,subjectName);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




                    Toast.makeText(context, "Clicked on Delete Subject: "+subjectDetailsModelList.get(getAdapterPosition()).getSubjectName(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}
