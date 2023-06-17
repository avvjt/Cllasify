package com.cllasify.cllasify.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Subject_Assign extends RecyclerView.Adapter<Adapter_Subject_Assign.ViewHolder> {


    Context context;
    List<String> subjectDetailsModelList;
    List<String> classStudentList;
    List<String> classStudentIDList;
    String uniPush;
    List<String> periods;

    int lastPosition = -1;

    ArrayAdapter<String> adapterItems;

    ArrayList<String> subs = new ArrayList<>();
    ArrayList<String> teacher = new ArrayList<>();
    List<String> newTeacher1 = new ArrayList<>();
    List<String> newTeacher2 = new ArrayList<>();

    String itemSub = "null";
    String itemTeacher = "null";

    boolean isSaturday = false;

    public String getUniPush() {
        return uniPush;
    }

    public void setUniPush(String uniPush) {
        this.uniPush = uniPush;
    }

    public Adapter_Subject_Assign(Context context) {
        this.context = context;
    }

    public boolean isSaturday() {
        return isSaturday;
    }

    public void setSaturday(boolean saturday) {
        isSaturday = saturday;
    }

    public String getItemSub() {
        return itemSub;
    }

    public String getItemTeacher() {
        return itemTeacher;
    }

    List<Class_Routine> class_routines = new ArrayList<>();

    public List<Class_Routine> getClass_routines() {
        return class_routines;
    }

    public void setClass_routines(List<Class_Routine> class_routines) {
        this.class_routines = class_routines;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Adapter_Subject_Assign.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.sub_assignment_item, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Subject_Assign.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        int period = position + 1;

        holder.subjectTopic.setText("Period: " + period);
        Class_Routine clRoutine = class_routines.get(holder.getAdapterPosition());


        holder.autoTvSub.setText(class_routines.get(position).getSubject());
        holder.autoTvTeacher.setText(class_routines.get(position).getTeacher());

//        Class_Routine clRoutineAll = class_routines.get(period);
//
//        Log.d("ALLCHK", "onBindViewHolder: " + clRoutineAll);

        clRoutine.setPeriod(period);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_fall_down);
        holder.itemView.startAnimation(animation);
        lastPosition = holder.getAdapterPosition();

        setPeriods(periods);

        adapterItems = new ArrayAdapter<String>(context, R.layout.priority_list_item, subjectDetailsModelList);
        holder.autoTvSub.setAdapter(adapterItems);

        holder.autoTvSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemSub = adapterView.getItemAtPosition(i).toString();
//                doneBtn.setEnabled(true);
                subs.add(itemSub);
                clRoutine.setSubject(itemSub);
                class_routines.set(position, clRoutine);
            }
        });

        adapterItems = new ArrayAdapter<String>(context, R.layout.priority_list_item, classStudentList);
        holder.autoTvTeacher.setAdapter(adapterItems);

        holder.autoTvTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemTeacher = adapterView.getItemAtPosition(i).toString();
//                doneBtn.setEnabled(true);
                teacher.add(itemTeacher);
                clRoutine.setTeacher(itemTeacher);
                clRoutine.setId(classStudentIDList.get(i));
                class_routines.set(position, clRoutine);
            }
        });
    }

    @Override
    public int getItemCount() {
        return class_routines.size();
    }

    public void setPeriods(List<String> periods) {
        this.periods = periods;
    }

    public void setSubjectDetailsModelList(List<String> subjectDetailsModelList) {
        this.subjectDetailsModelList = subjectDetailsModelList;
    }

    public void setClassStudentList(List<String> classStudentList) {
        this.classStudentList = classStudentList;
    }

    public void setClassStudentIDList(List<String> classStudentIDList) {
        this.classStudentIDList = classStudentIDList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectTopic;
        ImageButton subjectMore;
        AutoCompleteTextView autoTvTeacher, autoTvSub;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTopic = itemView.findViewById(R.id.period_subject);
            autoTvSub = itemView.findViewById(R.id.assignSubs);

            autoTvSub.setFocusable(false);
            autoTvSub.setClickable(true);
            autoTvTeacher = itemView.findViewById(R.id.assignTeacher);

            autoTvTeacher.setFocusable(false);
            autoTvTeacher.setClickable(true);
/*

            for (int pos = 0; pos < classStudentList.size(); pos++) {
//                newTeacher1.add(classStudentList.get(pos).getUserName());

                if (!classStudentList.contains(newTeacher1.get(pos))) {
                    newTeacher2.add(newTeacher1.get(pos));
                }
/*
                if (classStudentList.get(pos).getUserName().equals(newTeacher1.get(pos))) {
                    Toast.makeText(context, "cant add", Toast.LENGTH_SHORT).show();

                } else {
                }
                */

        }
    }
}
