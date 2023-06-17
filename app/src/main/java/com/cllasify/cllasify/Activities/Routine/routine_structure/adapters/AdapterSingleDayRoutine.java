package com.cllasify.cllasify.Activities.Routine.routine_structure.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Activities.interfaces.OnItemSelectedOrChange;
import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.databinding.SubAssignmentItemBinding;

import java.util.List;
import java.util.stream.Collectors;

public class AdapterSingleDayRoutine extends RecyclerView.Adapter<AdapterSingleDayRoutine.ViewHolder> {

    private final Context context;
    private List<Class_Routine> routines;
    private final List<Class_Student_Details> teachers;
    private final List<String> teacherNames;
    private final List<Subject_Details_Model> subjects;
    private final List<String> subjectNames;
    private final String className;
    private final String classPushId;

    private OnItemSelectedOrChange onItemSelectedOrChange;

    public AdapterSingleDayRoutine(Context context, List<Class_Routine> routines, List<Class_Student_Details> teachers, List<Subject_Details_Model> subjects, String className, String classPushId) {
        this.context = context;
        this.routines = routines;
        this.teachers = teachers;
        this.subjects = subjects;
        this.className = className;
        this.classPushId = classPushId;
        teacherNames = teachers.stream().map(Class_Student_Details::getUserName).collect(Collectors.toList());
        subjectNames = subjects.stream().map(Subject_Details_Model::getSubjectName).collect(Collectors.toList());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(SubAssignmentItemBinding.inflate(
                LayoutInflater.from(context), parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Class_Routine class_routine = routines.get(position);
        SubAssignmentItemBinding binding = holder.getBinding();
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_fall_down);
        binding.getRoot().startAnimation(animation);
        int period = position + 1;
        if (class_routine.getPeriod() < 1) {
            class_routine.setPeriod(period);
        }
        class_routine.setClassName(className);
        class_routine.setClassPushId(classPushId);
        class_routine.setClassID(classPushId);
        binding.periodSubject.setText(context.getString(R.string.period_s, class_routine.getPeriod()));
        binding.assignSubs.setText(class_routine.getSubject());
        binding.assignTeacher.setText(class_routine.getTeacher());
        ArrayAdapter<String> teacherDropDown = new ArrayAdapter<>(context, R.layout.priority_list_item, teacherNames);
        binding.assignTeacher.setAdapter(teacherDropDown);
        binding.assignTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Class_Student_Details class_student_details = teachers.get(i);
                class_routine.setId(class_student_details.userId);
                class_routine.setTeacher(class_student_details.getUserName());
                routines.set(holder.getAdapterPosition(), class_routine);
                if(onItemSelectedOrChange != null){
                    onItemSelectedOrChange.onItemChange(routines);
                }
            }
        });
        ArrayAdapter<String> subjectDropDown = new ArrayAdapter<>(context, R.layout.priority_list_item, subjectNames);
        binding.assignSubs.setAdapter(subjectDropDown);
        binding.assignSubs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Subject_Details_Model subject_details_model = subjects.get(i);
                class_routine.setSubject(subject_details_model.getSubjectName());
                routines.set(holder.getAdapterPosition(), class_routine);
                if(onItemSelectedOrChange != null){
                    onItemSelectedOrChange.onItemChange(routines);
                }
            }
        });
    }

    public void setOnItemSelectedOrChange(OnItemSelectedOrChange onItemSelectedOrChange) {
        this.onItemSelectedOrChange = onItemSelectedOrChange;
    }

    @Override
    public int getItemCount() {
        return routines.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private SubAssignmentItemBinding binding;

        public ViewHolder(SubAssignmentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.assignSubs.setClickable(false);
            binding.assignSubs.setFocusable(false);
            binding.assignTeacher.setClickable(false);
            binding.assignTeacher.setFocusable(false);
        }

        public SubAssignmentItemBinding getBinding() {
            return binding;
        }
    }
}
