package com.cllasify.cllasify.Activities.interfaces;

public interface DataFetchListener<T> {

    void onLoading();

    void onError(String message);

    void onDataLoad(T data);
}
