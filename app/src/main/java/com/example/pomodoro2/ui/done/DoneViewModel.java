package com.example.pomodoro2.ui.done;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pomodoro2.database.Task;
import com.example.pomodoro2.database.TasksDatabase;
import com.example.pomodoro2.ui.today.TodayViewModel;

import java.util.List;

public class DoneViewModel extends AndroidViewModel {
    private static TasksDatabase database;
    private LiveData<List<Task>> doneTasks;


    public DoneViewModel(@NonNull Application application) {
        super(application);
        database = TasksDatabase.getInstance(getApplication());
        doneTasks = database.tasksDao().getCompletedTasks();
    }

    public LiveData<List<Task>> getDoneTasks() {
        return doneTasks;
    }

    public void deleteTask(Task task) {
        new DoneViewModel.DeleteTask().execute(task);
    }

    public void deleteAllTask() {
        new DoneViewModel.DeleteAllTask().execute();
    }

    public void updateTask(Task task) {
        new DoneViewModel.UpdateTask().execute(task);
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

    private static class UpdateTask extends AsyncTask<Task, Void, Void> {

        @Override
        protected Void doInBackground(Task... tasks) {
            if (tasks != null && tasks.length > 0) {
                database.tasksDao().updateTask(tasks[0]);
            }
            return null;
        }
    }
}