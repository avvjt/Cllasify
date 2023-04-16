package com.cllasify.cllasify.Adapters;

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

import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Subject_Assign extends RecyclerView.Adapter<Adapter_Subject_Assign.ViewHolder> {


    Context context;
    List<Subject_Details_Model> subjectDetailsModelList;
    List<String> classStudentList;
    String uniPush;

    int lastPosition = -1;

    ArrayAdapter<String> adapterItems;

    ArrayList<String> subs = new ArrayList<>();
    ArrayList<String> teacher = new ArrayList<>();
    List<String> newTeacher1 = new ArrayList<>();
    List<String> newTeacher2 = new ArrayList<>();

    String itemPrimary = "null";
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

    @NonNull
    @Override
    public Adapter_Subject_Assign.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.sub_assignment_item, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Subject_Assign.ViewHolder holder, int position) {

        int period = position + 1;

        holder.subjectTopic.setText("Period: " + period);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.item_fall_down);
        holder.itemView.startAnimation(animation);
        lastPosition = holder.getAdapterPosition();


        subs.add(subjectDetailsModelList.get(position).getSubjectName());

        adapterItems = new ArrayAdapter<String>(context, R.layout.priority_list_item, subs);
        holder.autoTvSub.setAdapter(adapterItems);

        holder.autoTvSub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemPrimary = adapterView.getItemAtPosition(i).toString();
//                doneBtn.setEnabled(true);

            }
        });

        adapterItems = new ArrayAdapter<String>(context, R.layout.priority_list_item, classStudentList);
        holder.autoTvTeacher.setAdapter(adapterItems);

        holder.autoTvTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                itemPrimary = adapterView.getItemAtPosition(i).toString();
//                doneBtn.setEnabled(true);

            }
        });


    }

    @Override
    public int getItemCount() {
        if (isSaturday == true) {
            return 4;
        } else {
            return subjectDetailsModelList.size();
        }
    }

    public void setSubjectDetailsModelList(List<Subject_Details_Model> subjectDetailsModelList) {
        this.subjectDetailsModelList = subjectDetailsModelList;
    }

    public void setClassStudentList(List<String> classStudentList) {
        this.classStudentList = classStudentList;
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
