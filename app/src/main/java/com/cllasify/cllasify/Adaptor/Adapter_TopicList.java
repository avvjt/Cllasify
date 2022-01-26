package com.cllasify.cllasify.Adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Subject_Details_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Adapter_TopicList extends RecyclerView.Adapter<Adapter_TopicList.ViewHolder> {


    Context context;
    List<Subject_Details_Model> subjectDetailsModelList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {

        void subjectChildClick(String groupName, String groupPushId, String groupSubjectPushId, String groupClassSubjects);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public Adapter_TopicList(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_TopicList.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.subject_list_item, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_TopicList.ViewHolder holder, int position) {
        if(subjectDetailsModelList.get(position).getSubjectTitle()!=null){
            holder.subjectTopic.setText(subjectDetailsModelList.get(position).getSubjectTitle());
        }else{
            holder.subListItem.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("Sizee", "Topic Subjects: "+subjectDetailsModelList.size());
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


            DatabaseReference changeCOor = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(userID);

            subjectTopic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "Testing the subject", Toast.LENGTH_SHORT).show();
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            /*
                            When doing this in the previous commit of the project it's working
                            Class_Group user = mDatalistNew.get(getAdapterPosition());
                            String groupName = user.getGroupName();
                            String groupSubjectPushId = user.getUserEmailId();
                            String groupPushId = user.getGroupCategory();
                            String groupClassSubjects = user.getGroup2();
                             */

                            Subject_Details_Model user = subjectDetailsModelList.get(getAdapterPosition());
                            String groupName = user.getGroupName();
                            String groupSubjectPushId = user.getGroupSubjectPushId();
                            String groupPushId = user.getGroupPushId();
                            String groupClassSubjects = user.getSubjectTitle();

                            subjectTopic.setBackgroundColor(context.getColor(R.color.colorPrimary));
//                            changeCOor.child("tgroupClassSubjects").setValue(groupClassSubjects);

                            Log.d("Topiccs", "onClick: "+"GroupName: "+groupName+"\nGroupSubjectId: "+groupSubjectPushId+"\ngroupPushId: "+groupPushId+"\ngroupClassSubjects: "+groupClassSubjects);

                            mListener.subjectChildClick(groupName, groupPushId, groupSubjectPushId, groupClassSubjects);
                        }
                    }
                }
            });

        }
    }
}
