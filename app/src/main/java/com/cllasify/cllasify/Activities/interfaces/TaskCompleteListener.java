package com.cllasify.cllasify.Activities.interfaces;

public interface TaskCompleteListener<T> {

    void onSuccess(T data);

    void onFailure(String message);
}
