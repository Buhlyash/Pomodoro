package com.example.pomodoro2.ui.today;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pomodoro2.Task;
import com.example.pomodoro2.TasksDatabase;

import java.util.List;

public class TodayViewModel extends AndroidViewModel {

    //    private final MutableLiveData<String> mText;
    private static TasksDatabase database;
    private LiveData<List<Task>> tasks;

    public TodayViewModel(@NonNull Application application) {
        super(application);
        database = TasksDatabase.getInstance(getApplication());
        tasks = database.tasksDao().getAllTasks();
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void insertTask(Task task) {
        new InsertTask().execute(task);
    }

    public void deleteTask(Task task) {
        new DeleteTask().execute(task);
    }

    public void deleteAllTask() {
        new DeleteAllTask().execute();
    }


    private static class InsertTask extends AsyncTask<Task, Void, Void> {
        @Override
        protected Void doInBackground(Task... tasks) {
            if (tasks != null && tasks.length > 0) {
                database.tasksDao().insertTask(tasks[0]);
            }
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Task, Void, Void> {
        @Override
        protected Void doInBackground(Task... tasks) {
            if (tasks != null && tasks.length > 0) {
                database.tasksDao().deleteTasks(tasks[0]);
            }
            return null;
        }
    }

    private static class DeleteAllTask extends AsyncTask<Task, Void, Void> {
        @Override
        protected Void doInBackground(Task... tasks) {
            if (tasks != null && tasks.length > 0) {
                database.tasksDao().deleteAllTasks();
            }
            return null;
        }
    }

    //    public TodayViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is today fragment");
//    }


//    public LiveData<String> getText() {
//        return mText;
//    }
}