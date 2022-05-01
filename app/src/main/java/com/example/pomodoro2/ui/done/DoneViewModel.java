package com.example.pomodoro2.ui.done;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DoneViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DoneViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is done fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}