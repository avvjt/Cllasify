package com.cllasify.cllasify.Adapters;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class Adapter_Result_Per_Subject extends RecyclerView.Adapter<Adapter_Result_Per_Subject.ViewHolder> {


    Context context;
    List<Subject_Details_Model> subjectDetailsModelList;
    List<Class_Group_Names> class_group_namesList;
    String uniPush, studentUserId;
    int specPos;
    int lastPosition = -1;
    List<String> marks = new ArrayList<>();
    List<Integer> posss = new ArrayList<>();
    List<Class_Result> class_results;
    private OnItemClickListener mListener;

    public Adapter_Result_Per_Subject(Context context) {
        this.context = context;
    }

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
    public Adapter_Result_Per_Subject.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.subject_result, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Result_Per_Subject.ViewHolder holder, int position) {


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

        }
    }
}
