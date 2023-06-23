package com.cllasify.cllasify.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.ModelClasses.Class_Group_Names;
import com.cllasify.cllasify.ModelClasses.Class_Result;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Result_Subject extends RecyclerView.Adapter<Adapter_Result_Subject.ViewHolder> {


    Context context;
    List<Subject_Details_Model> subjectDetailsModelList;
    List<Class_Group_Names> class_group_namesList;
    String uniPush, studentUserId;

    boolean totalMarksPresent = false;
    int specPos;
    int lastPosition = -1;
    private OnItemClickListener mListener;
    List<String> marks = new ArrayList<>();

    List<Integer> posss = new ArrayList<>();

    List<Class_Result> class_results;

    public void setClass_results(List<Class_Result> class_results) {
        this.class_results = class_results;
    }

    public void setPosss(List<Integer> posss) {
        this.posss = posss;
    }

    public List<String> getMarks() {
        return marks;
    }

    public void setMarks(List<String> marks) {
        this.marks = marks;
    }

    public void setSpecPos(int specPos) {
        this.specPos = specPos;
    }

    public Adapter_Result_Subject(Context context) {
        this.context = context;
    }

    public void setUniPush(String uniPush) {
        this.uniPush = uniPush;
    }

    public void setStudentUserId(String studentUserId) {
        this.studentUserId = studentUserId;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Adapter_Result_Subject.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.subject_list_item_result, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Result_Subject.ViewHolder holder, int position) {
        holder.subjectTopic.setText(subjectDetailsModelList.get(position).getSubjectName());
/*
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_fall_down);
        holder.itemView.startAnimation(animation);
        lastPosition = holder.getAdapterPosition();
*/
        int pos = position;

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        assert currentUser != null;
        String userID = currentUser.getUid();


        Subject_Details_Model subjectDetailsModel = subjectDetailsModelList.get(pos);


        if (class_results != null) {

            try {

                Class_Result class_result = class_results.get(pos);


                int fullTheoryPractical = class_result.theoryFullMarks + class_result.practicalFullMarks;
                int theoryPractical = class_result.theoryMarks + class_result.practicalMarks;

                String allMarks = theoryPractical + "/" + fullTheoryPractical;

                holder.marks.setText(allMarks);
            } catch (Exception e) {
                Log.d("GETMARKS1", "onBindViewHolder: " + e.getMessage() + " Pos: " + pos);
            }
        }
        holder.subListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.clickedSub(subjectDetailsModel.getSubjectUniPushId(), subjectDetailsModel.getSubjectName(), String.valueOf(pos));
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

    public interface OnItemClickListener {

        void clickedSub(String subUniPushId, String subjectName, String position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTopic;
        ImageButton subjectMore;
        RelativeLayout subListItem;

        TextView marks;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTopic = itemView.findViewById(R.id.tv_subjectTopic);
            subjectMore = itemView.findViewById(R.id.subjectMore);
            subListItem = itemView.findViewById(R.id.subListItem);
            marks = itemView.findViewById(R.id.marks);

        }
    }
}
