package com.cllasify.cllasify.Activities.Routine.priority_subject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cllasify.cllasify.Activities.interfaces.DataFetchListener;
import com.cllasify.cllasify.ModelClasses.Class_Individual_Routine;
import com.cllasify.cllasify.ModelClasses.SingleDayRoutine;
import com.cllasify.cllasify.ModelClasses.Subject_Details_Model;

import java.util.List;

public class PrioritySubjectViewModel extends ViewModel {

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<List<SingleDayRoutine>> routines = new MutableLiveData<>();
    private MutableLiveData<Class_Individual_Routine> prioritySubject = new MutableLiveData<>();
    private MutableLiveData<List<Subject_Details_Model>> subjects = new MutableLiveData<>();

    private PrioritySubjectRepository repository;

    private String groupPushId;
    private String classPushId;

    public void setBasicData(String groupPushId) {
        this.groupPushId = groupPushId;
        this.classPushId = classPushId;
        repository = new PrioritySubjectRepository(groupPushId);
        loadData();
    }

    public void loadData() {
        repository.getAllSubjects(new DataFetchListener<List<Subject_Details_Model>>() {
            @Override
            public void onLoading() {
            }

            @Override
            public void onError(String message) {
            }

            @Override
            public void onDataLoad(List<Subject_Details_Model> data) {
                subjects.postValue(data);
            }
        });
        repository.getPrioritySubject(new DataFetchListener<Class_Individual_Routine>() {
            @Override
            public void onLoading() {
                isLoading.postValue(true);
            }

            @Override
            public void onError(String message) {
                isLoading.postValue(false);
                error.postValue(message);
            }

            @Override
            public void onDataLoad(Class_Individual_Routine data) {
                isLoading.postValue(false);
                prioritySubject.postValue(data);
            }
        });
        repository.getAllRoutine(new DataFetchListener<List<SingleDayRoutine>>() {
            @Override
            public void onLoading() {
                isLoading.postValue(true);
            }

            @Override
            public void onError(String message) {
                isLoading.postValue(false);
                error.postValue(message);
            }

            @Override
            public void onDataLoad(List<SingleDayRoutine> data) {
                isLoading.postValue(false);
                routines.postValue(data);
            }
        });
    }

    public void setSubjects(String primary,String secondary){
        repository.setPrioritySubject(primary, secondary, new DataFetchListener<Boolean>() {
            @Override
            public void onLoading() {
                isLoading.postValue(true);
            }

            @Override
            public void onError(String message) {
                isLoading.postValue(false);
            }

            @Override
            public void onDataLoad(Boolean data) {
                isLoading.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<List<SingleDayRoutine>> getRoutines() {
        return routines;
    }

    public LiveData<Class_Individual_Routine> getPrioritySubject() {
        return prioritySubject;
    }

    public LiveData<List<Subject_Details_Model>> getSubjects() {
        return subjects;
    }
}
