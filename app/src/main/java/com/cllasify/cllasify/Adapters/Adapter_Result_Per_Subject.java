package com.cllasify.cllasify.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.ModelClasses.Class_Group_Names;
import com.cllasify.cllasify.ModelClasses.Class_Result_Info;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.Utility.NumbConverter;

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
    List<Class_Result_Info> class_results;
    private OnItemClickListener mListener;

    public Adapter_Result_Per_Subject(Context context) {
        this.context = context;
    }

    public void setClass_results(List<Class_Result_Info> class_results) {
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


        if (class_results != null) {

            try {

//                Log.d("GETMARKS1", "onBindViewHolder: " + subjectDetailsModel.getSubjectName());

//                Class_Result class_result = class_results.get(pos);

                Subject_Details_Model subjectDetailsModel = subjectDetailsModelList.get(position);
                Class_Result_Info class_result_info = class_results.get(position);

                String subject_name_str = class_result_info.subjectName;
                String theory_full_marks_str = String.valueOf(class_result_info.theoryFullMarks);
                String practical_full_marks_str = String.valueOf(class_result_info.practicalFullMarks);
                String theory_marks_str = String.valueOf(class_result_info.theoryMarks);
                String practical_marks_str = String.valueOf(class_result_info.practicalMarks);
                String total_marks_str = String.valueOf(class_result_info.totalSubjectMarks);
                String grade_str = class_result_info.grade;

                String return_val_in_english = NumbConverter.convert(Long.parseLong(total_marks_str));


                holder.subject_name.setText(subjectDetailsModel.getSubjectName());
                holder.theory_full_marks.setText(theory_full_marks_str);
                holder.practical_full_marks.setText(practical_full_marks_str);
                holder.theory_marks.setText(theory_marks_str);
                holder.practical_marks.setText(practical_marks_str);
                holder.total_marks.setText(total_marks_str);
                holder.grade.setText(grade_str);
                holder.numb_words.setText(return_val_in_english);

                if (grade_str.equals("F")) {
                    holder.grade.setTextColor(Color.RED);
                }


            } catch (Exception e) {
                Log.d("GETMARKS1", "onBindViewHolder: " + e.getMessage() + " Pos: " + position);
            }
        }
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
        TextView subject_name, theory_full_marks, practical_full_marks, theory_marks, practical_marks, total_marks, grade, numb_words;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            subject_name = itemView.findViewById(R.id.subject_name);
            theory_full_marks = itemView.findViewById(R.id.theory_full_marks);
            practical_full_marks = itemView.findViewById(R.id.practical_full_marks);
            theory_marks = itemView.findViewById(R.id.theory_marks);
            practical_marks = itemView.findViewById(R.id.practical_marks);
            total_marks = itemView.findViewById(R.id.total_marks);
            grade = itemView.findViewById(R.id.grade);
            numb_words = itemView.findViewById(R.id.numb_words);

        }
    }
}
