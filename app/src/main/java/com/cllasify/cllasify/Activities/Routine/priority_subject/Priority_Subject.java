package com.cllasify.cllasify.Activities.Routine.priority_subject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cllasify.cllasify.Activities.Routine.All_Routine;
import com.cllasify.cllasify.Activities.Routine.priority_subject.adapters.AdapterPrioritySubjectMain;
import com.cllasify.cllasify.ModelClasses.Class_Individual_Routine;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;
import com.cllasify.cllasify.R;
import com.cllasify.cllasify.databinding.ActivityPrioritySubjectBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Priority_Subject extends AppCompatActivity {

    private ActivityPrioritySubjectBinding binding;
    private PrioritySubjectViewModel viewModel;

    private AdapterPrioritySubjectMain adapter;

    private List<Subject_Details_Model> subject;

    private String groupPushId = "";

    private Class_Individual_Routine prioritySub;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        binding = ActivityPrioritySubjectBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        prioritySub = new Class_Individual_Routine();
        viewModel = new ViewModelProvider(this, getDefaultViewModelProviderFactory()).get(PrioritySubjectViewModel.class);
        if (getIntent().hasExtra("groupPushId")) {
            groupPushId = getIntent().getStringExtra("groupPushId");
        }
        subject = new ArrayList<>();
        viewModel.setBasicData(groupPushId);
        adapter = new AdapterPrioritySubjectMain(this);
        adapter.setGroupPushId(groupPushId);
        initView();

        binding.generateAllRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Priority_Subject.this, All_Routine.class);
                intent.putExtra("groupPushId", groupPushId);
                startActivity(intent);

            }
        });

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        binding.doneBtn.setOnClickListener(v -> {
            if (prioritySub.getPrimarySubject() == null) {
                Toast.makeText(this, "Select Primary subject", Toast.LENGTH_SHORT).show();
                return;
            }
            if (prioritySub.getSecondarySubject() == null) {
                Toast.makeText(this, "Select Secondary subject", Toast.LENGTH_SHORT).show();
                return;
            }
            if(prioritySub.getPrimarySubject().equals(prioritySub.getSecondarySubject())){
                Toast.makeText(this, "Cannot set same subject for primary and secondary ", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.setSubjects(prioritySub.getPrimarySubject(), prioritySub.getSecondarySubject());
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            observeStates();
        }
    }


    private void initView() {
        binding.mainRv.setAdapter(adapter);
        binding.mainRv.setLayoutManager(new LinearLayoutManager(this));
        binding.primaryACT.setClickable(false);
        binding.primaryACT.setFocusable(false);
        binding.secondaryACT.setClickable(false);
        binding.secondaryACT.setFocusable(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void observeStates() {
        viewModel.getIsLoading().observe(this, isLoading -> {
            int progressVisibility = (isLoading) ? View.VISIBLE : View.GONE;
            binding.progressBar.setVisibility(progressVisibility);
        });

        viewModel.getError().observe(this, error -> {
            if (error != null && !error.equals("")) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getRoutines().observe(this, routines -> {
            if (routines != null) {
                adapter.setRoutines(routines);
            }
        });
        viewModel.getSubjects().observe(this, subjects -> {
            if (subjects != null) {
                this.subject = subjects;
                initACT();
            }
        });
        viewModel.getPrioritySubject().observe(this, prioritySubject -> {
            if (prioritySubject != null) {
                this.prioritySub = prioritySubject;
                binding.primarySub.setVisibility(View.VISIBLE);
                binding.secondarySub.setVisibility(View.VISIBLE);
                binding.textInputLayoutPrimary.setVisibility(View.GONE);
                binding.doneBtn.setVisibility(View.GONE);
                binding.textInputLayoutSecondary.setVisibility(View.GONE);
                binding.primarySub.setText(prioritySubject.getPrimarySubject());
                binding.secondarySub.setText(prioritySubject.getSecondarySubject());
                return;
            }
            binding.primarySub.setVisibility(View.GONE);
            binding.secondarySub.setVisibility(View.GONE);
            binding.textInputLayoutPrimary.setVisibility(View.VISIBLE);
            binding.textInputLayoutSecondary.setVisibility(View.VISIBLE);
            binding.doneBtn.setVisibility(View.VISIBLE);
        });
    }

    public void onClick(View view) {
        onBackPressed();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initACT() {
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, R.layout.priority_list_item,
                subject.stream().map(Subject_Details_Model::getSubjectName).collect(Collectors.toList())
        );
        binding.primaryACT.setAdapter(stringArrayAdapter);
        binding.secondaryACT.setAdapter(stringArrayAdapter);
        binding.primaryACT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                prioritySub.setPrimarySubject(adapterView.getItemAtPosition(i).toString());
            }
        });
        binding.secondaryACT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                prioritySub.setSecondarySubject(adapterView.getItemAtPosition(i).toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}