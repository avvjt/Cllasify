package com.cllasify.cllasify.Activities.Routine.routine_structure.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.ModelClasses.SingleDayRoutine;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.databinding.RoutineMainRvItemBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterRoutineMain extends RecyclerView.Adapter<AdapterRoutineMain.ViewHolder> {

    private final List<Class_Student_Details> teachers;
    private final List<Subject_Details_Model> subjects;

    private final String className, classPushId;
    private final Context context;

    private List<SingleDayRoutine> singleDayRoutines;

    public AdapterRoutineMain(
            Context context,
            List<Class_Student_Details> teachers,
            List<Subject_Details_Model> subjects, String className, String classPushId) {
        this.teachers = teachers;
        this.subjects = subjects;
        this.context = context;
        this.className = className;
        this.classPushId = classPushId;
        singleDayRoutines = new ArrayList<>();
        mappedRoutines = new HashMap<>();
    }

    public void submitSingleDayRoutines(List<SingleDayRoutine> singleDayRoutines) {
        this.singleDayRoutines = singleDayRoutines;
        notifyDataSetChanged();
    }

    private Map<String, List<Class_Routine>> mappedRoutines;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(RoutineMainRvItemBinding.inflate(
                LayoutInflater.from(context), parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SingleDayRoutine singleDayRoutine = singleDayRoutines.get(position);
        RoutineMainRvItemBinding binding = holder.binding;
        mappedRoutines.put(singleDayRoutine.getDay(),singleDayRoutine.getRoutine());
        binding.tvDayName.setText(singleDayRoutine.getDay());
        AdapterSingleDayRoutine adapter = new AdapterSingleDayRoutine(context,
                singleDayRoutine.getRoutine(), teachers, subjects, className, classPushId);
        adapter.setOnItemSelectedOrChange(newRoutines -> {
            mappedRoutines.put(singleDayRoutine.getDay(), newRoutines);
        });
        binding.rvSingleDay.setAdapter(adapter);
        binding.rvSingleDay.setLayoutManager(new LinearLayoutManager(context));
        binding.dayContainer.setOnClickListener(v -> {
            toggleViewVisibility(binding.rvSingleDay);
            binding.dropDown.animate()
                    .rotationBy(180f)
                    .setDuration(150)
                    .start();
        });
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
        return singleDayRoutines.size();
    }

    public Map<String, List<Class_Routine>> getMappedRoutines() {
        return mappedRoutines;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public RoutineMainRvItemBinding binding;

        public ViewHolder(@NonNull RoutineMainRvItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
