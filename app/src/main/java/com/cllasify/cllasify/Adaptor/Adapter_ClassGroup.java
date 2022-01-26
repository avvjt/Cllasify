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

import com.cllasify.cllasify.Class.Class_Group;
import com.cllasify.cllasify.Class_Group_Names;
import com.cllasify.cllasify.Constant;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Subject_Details_Model;
import com.cllasify.cllasify.Utility.SharePref;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Adapter_ClassGroup extends RecyclerView.Adapter<Adapter_ClassGroup.ViewHolder> {

    private static final String TAG = Adapter_ClassGroup.class.getSimpleName();

    Context context;
    List<Class_Group_Names> class_groupList;
    onAddSubjectClickListener onAddSubjectClickListener;


    public Adapter_ClassGroup(Context context,onAddSubjectClickListener onAddSubjectClickListener) {
        this.context = context;
        this.onAddSubjectClickListener = onAddSubjectClickListener;
    }


    public void setClass_groupList(List<Class_Group_Names> class_groupList) {
        this.class_groupList = class_groupList;
    }

    @NonNull
    @Override
    public Adapter_ClassGroup.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.list_item_lvl2_groupname, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_ClassGroup.ViewHolder holder, int position) {

        holder.classGroupName.setText(class_groupList.get(position).getGroupName());

        holder.addTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddSubjectClickListener.onAddSubjectClickListener(class_groupList.get(holder.getAdapterPosition()).getGroupName());
            }
        });



        DatabaseReference refSaveCurrDataForSubj = FirebaseDatabase.getInstance().getReference().child("Groups").child("Temp").child(SharePref.getDataFromPref(Constant.USER_ID));
        Adapter_TopicList adapter_topicList = new Adapter_TopicList(context.getApplicationContext());
        holder.subjectList.setLayoutManager(new LinearLayoutManager(context.getApplicationContext()));
        adapter_topicList.setOnItemClickListener(new Adapter_TopicList.OnItemClickListener() {
            @Override
            public void subjectChildClick(String groupName, String groupPushId, String groupSubjectPushId, String groupClassSubjects) {
                Toast.makeText(context, "Testing the subject on Adapter_ClassGroup!!", Toast.LENGTH_SHORT).show();
                refSaveCurrDataForSubj.child("groupName").setValue(groupName);
                refSaveCurrDataForSubj.child("SubGroupPushId").setValue(groupSubjectPushId);
                refSaveCurrDataForSubj.child("GroupPushId").setValue(groupPushId);
                refSaveCurrDataForSubj.child("groupClassSubjects").setValue(groupClassSubjects);
            }
        });
        holder.subjectList.setAdapter(adapter_topicList);
        adapter_topicList.setSubjectDetailsModelList(class_groupList.get(position).getSubjectDetailsModelList());
        adapter_topicList.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        Log.d("Sizee", "Class Items: "+class_groupList.size());
        return class_groupList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView classGroupName;
        ImageButton addTopicButton;
        RecyclerView subjectList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            classGroupName = itemView.findViewById(R.id.tv_classGroupTitle);
            addTopicButton = itemView.findViewById(R.id.addNewTopicButton);
            subjectList = itemView.findViewById(R.id.subjectList);

        }
    }

    public interface onAddSubjectClickListener{
        void onAddSubjectClickListener(String groupName);
    }
}


