package com.cllasify.cllasify.Activities.Routine.priority_subject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cllasify.cllasify.Adapters.Adapter_Weekdays_Assign;
import com.cllasify.cllasify.ModelClasses.SingleDayRoutine;
import com.cllasify.cllasify.databinding.RoutineMainRvItemBinding;

import java.util.ArrayList;
import java.util.List;

public class AdapterPrioritySubjectMain extends RecyclerView.Adapter<AdapterPrioritySubjectMain.ViewHolder> {

    private final Context context;
    private List<SingleDayRoutine> routines;

    public AdapterPrioritySubjectMain(Context context) {
        this.context = context;
        routines = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RoutineMainRvItemBinding.inflate(
                LayoutInflater.from(context), parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SingleDayRoutine singleDayRoutine = routines.get(position);
        RoutineMainRvItemBinding binding = holder.binding;
        binding.tvDayName.setText(singleDayRoutine.getDay());
        Adapter_Weekdays_Assign adapter = new Adapter_Weekdays_Assign(context);
        binding.rvSingleDay.setAdapter(adapter);
        binding.rvSingleDay.setLayoutManager(new LinearLayoutManager(context));
        adapter.setWeekdays(singleDayRoutine.getRoutine());
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
        return routines.size();
    }

    public void setRoutines(List<SingleDayRoutine> routines) {
        this.routines = routines;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public RoutineMainRvItemBinding binding;

        public ViewHolder(@NonNull RoutineMainRvItemBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
