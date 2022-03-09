package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.Home.Server_Activity;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Subject_Details_Model;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Adapter_TopicList extends RecyclerView.Adapter<Adapter_TopicList.ViewHolder> {


    Context context;
    List<Subject_Details_Model> subjectDetailsModelList;
    onSubjectClickListener onSubjectClickListener;

    public void setOnSubjectClickListener(Adapter_TopicList.onSubjectClickListener onSubjectClickListener) {
        this.onSubjectClickListener = onSubjectClickListener;
    }

    public Adapter_TopicList(Context context) {
        this.context = context;
    }

    public interface onSubjectClickListener{
        void onSubjectClick();
    }

    @NonNull
    @Override
    public Adapter_TopicList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.subject_list_item, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_TopicList.ViewHolder holder, int position) {
        holder.subjectTopic.setText(subjectDetailsModelList.get(position).getSubjectName());

        holder.subjectTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Clicked on Subject", Toast.LENGTH_SHORT).show();
                String userID = SharePref.getDataFromPref(Constant.USER_ID);
                DatabaseReference posTemp = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                posTemp.child("subjectPosition").setValue(holder.getAdapterPosition());
                posTemp.child("clickedSubjectName").setValue(subjectDetailsModelList.get(holder.getAdapterPosition()).getSubjectName());
                posTemp.child("subjectUniPushId").setValue(subjectDetailsModelList.get(holder.getAdapterPosition()).getSubjectUniPushId());

                onSubjectClickListener.onSubjectClick();

//                Intent intent = new Intent(context.getApplicationContext(),Server_Activity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("closePanels","close");
//                context.startActivity(intent);

                    /*
                    DatabaseReference saveTempClassName = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);
                    saveTempClassName.child("clickedClassName").setValue(classGroupName);
                    */
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
        LinearLayout subListItem;
        TextView subjectTopic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subListItem = itemView.findViewById(R.id.subListItem);
            subjectTopic = itemView.findViewById(R.id.tv_subjectTopic);

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            assert currentUser != null;
            String userID = currentUser.getUid();


//            DatabaseReference changeCOor = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

//            subjectTopic.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "Testing the subject", Toast.LENGTH_SHORT).show();
//                    if (mListener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            /*
//                            When doing this in the previous commit of the project it's working
//                            Class_Group user = mDatalistNew.get(getAdapterPosition());
//                            String groupName = user.getGroupName();
//                            String groupSubjectPushId = user.getUserEmailId();
//                            String groupPushId = user.getGroupCategory();
//                            String groupClassSubjects = user.getGroup2();
//                             */
//                            Subject_Details_Model user = subjectDetailsModelList.get(getAdapterPosition());
//
//                            String groupPushId = user.getGroupPushId();
//
//                            String groupSubjectPushId = user.getGroupSubjectPushId();
//                            String groupClassSubjects = user.getSubjectTitle();
//                            String groupName = user.getGroupName();
//
//                            Log.i("lionlion", groupPushId);
//                            Log.i("lionlion", groupSubjectPushId);
//
//                            Log.i("lionlion", groupClassSubjects);
//                            Log.i("lionlion", groupName);
//                            subjectTopic.setBackgroundColor(context.getColor(R.color.colorPrimary));
////                            changeCOor.child("tgroupClassSubjects").setValue(groupClassSubjects);
//
//
//                            Log.d("Topiccs", "onClick: "+"GroupName: "+groupName+"\nGroupSubjectId: "+groupSubjectPushId+"\ngroupPushId: "+groupPushId+"\ngroupClassSubjects: "+groupClassSubjects);
//
//                            mListener.subjectChildClick(groupName, groupPushId, groupSubjectPushId, groupClassSubjects);
//                        }
//                    }
//                }
//            });

        }
    }
}
