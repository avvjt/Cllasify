package com.cllasify.cllasify.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.databinding.RoutineAllTeacherItemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterAllTeacherSubjectMain extends RecyclerView.Adapter<AdapterAllTeacherSubjectMain.ViewHolder> {

    private final Context context;
    String groupPushId;
    private List<String> routines;


    public AdapterAllTeacherSubjectMain(Context context) {
        this.context = context;
        routines = new ArrayList<>();
    }

    public String getGroupPushId() {
        return groupPushId;
    }

    public void setGroupPushId(String groupPushId) {
        this.groupPushId = groupPushId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RoutineAllTeacherItemBinding.inflate(
                LayoutInflater.from(context), parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        String classID = routines.get(position);


        RoutineAllTeacherItemBinding binding = holder.binding;

        binding.tvDayName.setText(classID);

        Adapter_All_Teacher_Assign adapter = new Adapter_All_Teacher_Assign(context);
        binding.rvSingleDay.setAdapter(adapter);
        binding.rvSingleDay.setLayoutManager(new LinearLayoutManager(context));
//        binding.rvSingleDay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        List<Class_Routine> classDataListMonday = new ArrayList<>();
//


        DatabaseReference dbRoutineStructure = FirebaseDatabase.getInstance().getReference().child("Groups")
                .child("Routine").child("Uni_Group_No_13_Experimental School").child("schedule").child(classID).child("Monday");

        Log.d("CHKCLASSVAL", "onBindViewHolder: " + routines.get(position));

        dbRoutineStructure.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                Log.d("ROUTCHK", "onDataChange: " + snapshot.getKey());


                for (int i = 1; i <= 8; i++) {

                    Class_Routine class_routine = snapshot.child(String.valueOf(i)).getValue(Class_Routine.class);

/*
                    Class_Routine class_routine = dataSnapshot.child("Monday").child(String.valueOf(i)).getValue(Class_Routine.class);
                    Log.d("ROUTCHK", "onDataChange: " + class_routine + i);

                    classDataListMonday.add(class_routine);
*/
                    classDataListMonday.add(class_routine);

//                    Log.d("ROUTCHK", "onDataChange: " + class_routine.getPeriod());

                    adapter.setDay("Monday");
                    adapter.setWeekdays(classDataListMonday);
                    adapter.notifyDataSetChanged();
                }



                /*
                EveryDayRoutine everyDayRoutine = new EveryDayRoutine("Roronoa Sk", "g5vmLAdL8NaT2kzpO4f4wg2crO62", classDataListMonday);

//                        adapter.setDay("Monday");
//                        adapter.setWeekdays(classDataListMonday);
                classDataList.add(everyDayRoutine);
                */

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        /*
    }
        EveryDayRoutine singleDayRoutine = routines.get(position);
        RoutineMainRvItemBinding binding = holder.binding;
//        binding.tvDayName.setText(singleDayRoutine.getDay());
        Adapter_Weekdays_Assign adapter = new Adapter_Weekdays_Assign(context);
        binding.rvSingleDay.setAdapter(adapter);
        binding.rvSingleDay.setLayoutManager(new LinearLayoutManager(context));
        adapter.setWeekdays(singleDayRoutine.getRoutine());
        adapter.setGroupPushId(getGroupPushId());
//        adapter.setDay(singleDayRoutine.getDay());
        binding.dayContainer.setOnClickListener(v -> {
            toggleViewVisibility(binding.rvSingleDay);
            binding.dropDown.animate()
                    .rotationBy(180f)
                    .setDuration(150)
                    .start();
        });


        */

//        rv_assigned_monday = findViewById(R.id.rv_assigned_monday);
//

//        EveryDayRoutine everyDayRoutine = routines.get(1);
//
//        Log.d("CHKALLROUTINE", "onBindViewHolder: " + everyDayRoutine.getRoutine().size());

/*
        Adapter_All_Teacher_Assign adapter = new Adapter_All_Teacher_Assign(context);
        binding.rvSingleDay.setAdapter(adapter);
        binding.rvSingleDay.setLayoutManager(new LinearLayoutManager(context));
//        binding.rvSingleDay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        classDataListMonday = new ArrayList<>();
//
        classDataListMonday.add(everyDayRoutine.getRoutine().get(position));
        adapter.setDay("Monday");
        adapter.setWeekdays(classDataListMonday);
        adapter.notifyDataSetChanged();
*/
    }

    private void toggleViewVisibility(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            // Hide the content with animation
            view.animate()
                    .alpha(0.0f)
                    .translationY(-view.getHeight())
                    .setDuration(150)
                    .withEndAction(() -> view.setVisibility(View.GONE));
        } else {
            // Show the content with animation
            view.setVisibility(View.VISIBLE);
            view.setAlpha(0.0f);
            view.animate()
                    .translationY(0)
                    .alpha(1.0f)
                    .setDuration(150);
        }
    }

    @Override
    public int getItemCount() {
        return routines.size();
    }

    public void setRoutines(List<String> routines) {
        this.routines = routines;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public RoutineAllTeacherItemBinding binding;

        public ViewHolder(@NonNull RoutineAllTeacherItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
