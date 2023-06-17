package com.cllasify.cllasify.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_Weekdays_Assign extends RecyclerView.Adapter<Adapter_Weekdays_Assign.ViewHolder> {


    Context context;

    List<Class_Routine> weekdays;

    public void setWeekdays(List<Class_Routine> weekdays) {
        this.weekdays = weekdays;
        Log.e("WeekDays",weekdays.toString());
        notifyDataSetChanged();
    }

    public Adapter_Weekdays_Assign(Context context) {
        this.context = context;
        this.weekdays = new ArrayList<>();
    }


    @NonNull
    @Override
    public Adapter_Weekdays_Assign.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.assigned_item, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Weekdays_Assign.ViewHolder holder, int position) {
        Class_Routine class_routine = weekdays.get(position);
        Log.e("Class",class_routine.toString());
        holder.sub.setText(weekdays.get(position).getSubject());
        holder.period.setText("Period : " + weekdays.get(position).getPeriod());
        holder.classTV.setText(weekdays.get(position).getClassName());

    }

    @Override
    public int getItemCount() {
        Log.e("ThursdayItem",""+weekdays.size());
        return weekdays.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sub, period, classTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sub = itemView.findViewById(R.id.subjectTV);
            period = itemView.findViewById(R.id.periodTV);
            classTV = itemView.findViewById(R.id.classTV);

        }
    }
}
