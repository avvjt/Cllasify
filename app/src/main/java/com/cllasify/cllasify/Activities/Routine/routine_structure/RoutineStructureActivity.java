package com.cllasify.cllasify.Activities.Routine.routine_structure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.cllasify.cllasify.Activities.Routine.routine_structure.adapters.AdapterRoutineMain;
import com.cllasify.cllasify.Activities.attendance.Attendance_Activity_Teacher;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.databinding.ActivityRoutineStructure2Binding;

import java.util.ArrayList;
import java.util.List;

public class RoutineStructureActivity extends AppCompatActivity {

    private ActivityRoutineStructure2Binding binding;

    private String grpPushId;
    private String classPushId;
    private String className;

    private RoutineViewModel routineViewModel;
    private AdapterRoutineMain adapterRoutineMain;
    private List<Class_Student_Details> teachers;
    private List<Subject_Details_Model> subject_details_models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoutineStructure2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        routineViewModel = new ViewModelProvider(this, getDefaultViewModelProviderFactory()).get(RoutineViewModel.class);
        if (getIntent().hasExtra("groupPushId") && getIntent().hasExtra("classPushId") && getIntent().hasExtra("className")) {
            grpPushId = getIntent().getStringExtra("groupPushId");
            classPushId = getIntent().getStringExtra("classPushId");
            className = getIntent().getStringExtra("className");
        }
        teachers = new ArrayList<>();
        subject_details_models = new ArrayList<>();
        initPopupMenu();
        routineViewModel.setBasicData(grpPushId, classPushId);
        binding.tvClassName.setText(getString(R.string.class_name_s, className));
        binding.btnDone.setOnClickListener(v -> {
            if (adapterRoutineMain == null) return;
            routineViewModel.updateRoutines(
                    teachers, adapterRoutineMain.getMappedRoutines()
            );
        });
        binding.btnBack.setOnClickListener(v->{
            finish();
        });
        observeStates();
    }

    private void initAdapterAndRecyclerView() {
        adapterRoutineMain = new AdapterRoutineMain(this, teachers, subject_details_models, className, classPushId);
        binding.routineMainRv.setAdapter(adapterRoutineMain);
        binding.routineMainRv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initPopupMenu() {
        final PopupMenu dropDownMenu = new PopupMenu(this, binding.routineMore);

        final Menu menu = dropDownMenu.getMenu();

        dropDownMenu.getMenuInflater().inflate(R.menu.routine_more, menu);

        dropDownMenu.setOnMenuItemClickListener(item -> {

            switch (item.toString()) {
                case "Teacher Attendance":
                    Intent intent = new Intent(this, Attendance_Activity_Teacher.class);
                    intent.putExtra("groupPushId", grpPushId);
                    intent.putExtra("classPushId", classPushId);
                    startActivity(intent);
                    break;

                case "Maximum periods":

                    break;
            }

            return false;
        });

        binding.routineMore.setOnClickListener(view -> dropDownMenu.show());
    }


    private void observeStates() {
        routineViewModel.getIsLoading().observe(this, isLoading -> {
            int progressVisibility = (isLoading) ? View.VISIBLE : View.GONE;
            int doneButtonVisibility = (!isLoading) ? View.VISIBLE : View.GONE;
            binding.progressBar.setVisibility(progressVisibility);
            binding.btnDone.setVisibility(doneButtonVisibility);
        });

        routineViewModel.getError().observe(this, error -> {
            if (error != null && !error.equals("")) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        routineViewModel.getState().observe(this, routineStructureModel -> {
            if (routineStructureModel != null) {
                this.teachers = routineStructureModel.getTeachers();
                this.subject_details_models = routineStructureModel.getSubjects();
                initAdapterAndRecyclerView();
                adapterRoutineMain.submitSingleDayRoutines(routineStructureModel.getSingleDayRoutines());
            }
        });
        routineViewModel.getRoutineUpdated().observe(this, isUpdated -> {
            if(isUpdated) {
                Toast.makeText(this, "Routine updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}