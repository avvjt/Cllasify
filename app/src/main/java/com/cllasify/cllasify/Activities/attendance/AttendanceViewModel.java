package com.cllasify.cllasify.Activities.attendance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cllasify.cllasify.Activities.interfaces.DataFetchListener;
import com.cllasify.cllasify.ModelClasses.Class_Student_Details;
import com.cllasify.cllasify.ModelClasses.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceViewModel extends ViewModel {

    private MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> _error = new MutableLiveData<>();

    private MutableLiveData<Integer> _attendanceStatus = new MutableLiveData<>(0);

    private Map<User, Boolean> temporaryAttendance = new HashMap<>();

    private MutableLiveData<Boolean> attendanceMarked = new MutableLiveData<>(false);
    private MutableLiveData<List<User>> teachers = new MutableLiveData<>(
            new ArrayList<>()
    );

    private String groupPushId;
    private String subGroupPushId;
    private String classPushId;

    private AttendanceRepository attendanceRepository;

    public void setBasicData(String groupPushId, String subGroupPushId, String classPushId) {
        this.groupPushId = groupPushId;
        this.subGroupPushId = subGroupPushId;
        this.classPushId = classPushId;
        attendanceRepository = new AttendanceRepository(groupPushId, subGroupPushId, classPushId);
        getAttendanceStatus();
        loadTeachers();
    }

    public void getAttendanceStatus() {
        attendanceRepository.getAttendanceStatus(new DataFetchListener<Integer>() {
            @Override
            public void onLoading() {
                _isLoading.postValue(true);
            }

            @Override
            public void onError(String message) {
                _isLoading.postValue(false);
                _error.postValue(message);

            }

            @Override
            public void onDataLoad(Integer data) {
                _isLoading.postValue(false);
                _attendanceStatus.postValue(data);
            }
        });
    }

    public void loadTeachers() {
        attendanceRepository.getTeachers(new DataFetchListener<List<Class_Student_Details>>() {
            @Override
            public void onLoading() {
                _isLoading.postValue(true);
            }

            @Override
            public void onError(String message) {
                _isLoading.postValue(false);
                _error.postValue(message);
            }

            @Override
            public void onDataLoad(List<Class_Student_Details> data) {
                _isLoading.postValue(false);
                loadTeacherWithValue(data);
            }
        });
    }

    private void loadTeacherWithValue(List<Class_Student_Details> adminTeachers) {
        attendanceRepository.getTeacherUser(adminTeachers, new DataFetchListener<List<User>>() {
            @Override
            public void onLoading() {
                _isLoading.postValue(true);
            }

            @Override
            public void onError(String message) {
                _isLoading.postValue(false);
                _error.postValue(message);
            }

            @Override
            public void onDataLoad(List<User> data) {
                teachers.postValue(data);
                _isLoading.postValue(false);
            }
        });
    }

    public void markAttendance(User user, int position,boolean isPresent) {
        temporaryAttendance.put(user, isPresent);
        List<User> tempTeacher = teachers.getValue();
        if (tempTeacher != null) {
            tempTeacher.remove(user);
            teachers.postValue(tempTeacher);
            if (_attendanceStatus.getValue() != 0) {
                tempTeacher.add(position,user);
                teachers.postValue(tempTeacher);
                _error.postValue("Attendance already started");
                return;
            }
            attendanceRepository.removePreviousTeacherSchedule(user.getUserId());
        }
    }

    public void startAttendance() {
        if (_attendanceStatus.getValue() != 0) {
            _error.postValue("Someone started attendance already");
            return;
        }
        attendanceRepository.startAttendance(temporaryAttendance, new DataFetchListener<Object>() {
            @Override
            public void onLoading() {
                _isLoading.postValue(true);
            }

            @Override
            public void onError(String message) {
                _isLoading.postValue(false);
                _error.postValue(message);
            }

            @Override
            public void onDataLoad(Object data) {
                _isLoading.postValue(false);
                attendanceMarked.postValue(true);
            }
        });
    }

    public LiveData<Boolean> get_isLoading() {
        return _isLoading;
    }

    public LiveData<String> get_error() {
        return _error;
    }

    public LiveData<List<User>> getTeachers() {
        return teachers;
    }

    public LiveData<Boolean> getAttendanceMarked() {
        return attendanceMarked;
    }

    public LiveData<Integer> get_attendanceStatus() {
        return _attendanceStatus;
    }
}
