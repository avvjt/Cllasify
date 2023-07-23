package com.cllasify.cllasify.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Adapter_All_Teacher_Assign extends RecyclerView.Adapter<Adapter_All_Teacher_Assign.ViewHolder> {


    Context context;

    List<Class_Routine> weekdays;

    String day;
    String groupPushId;

    public Adapter_All_Teacher_Assign(Context context) {
        this.context = context;
        this.weekdays = new ArrayList<>();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getGroupPushId() {
        return groupPushId;
    }

    public void setGroupPushId(String groupPushId) {
        this.groupPushId = groupPushId;
    }

    public void setWeekdays(List<Class_Routine> weekdays) {
        this.weekdays = weekdays;
//        Log.e("WeekDays",weekdays.toString());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Adapter_All_Teacher_Assign.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootview = LayoutInflater.from(context).inflate(R.layout.all_teacher_item, parent, false);
        return new ViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_All_Teacher_Assign.ViewHolder holder, int pos) {

        int position = pos;

        Class_Routine class_routine = weekdays.get(position);

        if (class_routine != null) {

            Log.e("ROUTIIO", class_routine.getSubject());


            DatabaseReference databaseAllRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups")
                    .child("Routine").child(groupPushId).child("allSchedule")
//                    .child("Routine").child(groupPushId).child("allSchedule")
                    .child(class_routine.getClassPushId()).child(getDay()).child(String.valueOf(class_routine.getPeriod())).child("id");


            databaseAllRoutineStructure.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (class_routine.getId().equals(snapshot.getValue().toString())) {
//                        Log.e("ROUTI", snapshot.getValue().toString() + "\t" + snapshot.getRef());

                        holder.sub.setText(weekdays.get(position).getSubject());
                        holder.period.setText("Period : " + weekdays.get(position).getPeriod());
                        holder.classTV.setText(weekdays.get(position).getClassName());

                    } else {


                        DatabaseReference databaseRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups")
                                .child("Routine").child(groupPushId).child("schedule")
                                .child(class_routine.getId()).child(getDay()).child(String.valueOf(class_routine.getPeriod()));

                        databaseRoutineStructure.removeValue();

//                        Log.e("ROUTI01", snapshot.getValue().toString() + "\t" + snapshot.getRef());

                        ViewGroup.LayoutParams params = holder.period_item.getLayoutParams();
                        params.height = 0;
                        holder.period_item.setLayoutParams(params);

                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }
    }

    @Override
    public int getItemCount() {
        Log.e("ThursdayItem", "" + weekdays.size());
        return weekdays.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView sub, period, classTV;
        LinearLayout period_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            period_item = itemView.findViewById(R.id.period_item);
            sub = itemView.findViewById(R.id.subjectTV);
            period = itemView.findViewById(R.id.periodTV);
            classTV = itemView.findViewById(R.id.classTV);


        }
    }
}
