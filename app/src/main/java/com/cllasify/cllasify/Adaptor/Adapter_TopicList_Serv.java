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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Adapter_TopicList_Serv extends RecyclerView.Adapter<Adapter_TopicList_Serv.ViewHolder> {


    Context context;
    List<Subject_Details_Model> subjectDetailsModelList;
    private OnItemClickListener mListener;

    public interface  OnItemClickListener{

        void delSubject();

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

        holder.subjectTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked on Subject", Toast.LENGTH_SHORT).show();
                String userID = SharePref.getDataFromPref(Constant.USER_ID);
                DatabaseReference posTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                posTemp.child("subjectPosition").setValue(holder.getAdapterPosition());
                posTemp.child("clickedSubjectName").setValue(subjectDetailsModelList.get(holder.getAdapterPosition()).getSubjectName());


            }
        });

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
        ImageButton deleteSubject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTopic = itemView.findViewById(R.id.tv_subjectTopic);
            deleteSubject = itemView.findViewById(R.id.deleteSubject);

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            String userID = currentUser.getUid();

            deleteSubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.d("DELL", "Clicked on Delete Subject: ");

                    Toast.makeText(context, "Clicked on Delete Subject: "+subjectDetailsModelList.get(getAdapterPosition()).getSubjectName(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }
}
