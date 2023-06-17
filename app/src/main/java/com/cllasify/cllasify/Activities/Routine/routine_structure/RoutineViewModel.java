package com.cllasify.cllasify.Activities.Routine.routine_structure;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cllasify.cllasify.Activities.interfaces.DataFetchListener;
import com.cllasify.cllasify.ModelClasses.Class_Routine;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.ModelClasses.RoutineStructureModel;

import java.util.List;
import java.util.Map;

public class RoutineViewModel extends ViewModel {

    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> routineUpdated = new MutableLiveData<>(false);
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<RoutineStructureModel> state = new MutableLiveData<>();

    private RoutineRepository routineRepository;

    private String groupPushId;
    private String classPushId;

    public void setBasicData(String groupPushId, String classPushId) {
        this.groupPushId = groupPushId;
        this.classPushId = classPushId;
        routineRepository = new RoutineRepository(groupPushId, classPushId);
        loadData();
    }

    public void loadData() {

        routineRepository.getRoutineStructureModel(new DataFetchListener<RoutineStructureModel>() {
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
            public void onDataLoad(RoutineStructureModel data) {
                isLoading.postValue(false);
                state.postValue(data);
            }
        });
    }

    public void updateRoutines(
            List<Class_Student_Details> teachers,
            Map<String, List<Class_Routine>> mappedRoutine
    ) {
        routineRepository.startSettingRoutines(teachers, mappedRoutine, new DataFetchListener<Boolean>() {
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
            public void onDataLoad(Boolean data) {
                isLoading.postValue(false);
                routineUpdated.postValue(true);
            }
        });
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<RoutineStructureModel> getState() {
        return state;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getRoutineUpdated() {
        return routineUpdated;
    }


}
