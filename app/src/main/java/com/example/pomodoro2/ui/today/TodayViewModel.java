package com.example.pomodoro2.ui.today;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pomodoro2.database.Task;
import com.example.pomodoro2.database.TasksDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TodayViewModel extends AndroidViewModel {

    private static TasksDatabase database;
    private LiveData<List<Task>> tasks;

    public TodayViewModel(@NonNull Application application) {
        super(application);
        database = TasksDatabase.getInstance(getApplication());
        tasks = database.tasksDao().getNotCompletedTasks();
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

    public void updateTask(Task task) {
        new UpdateTask().execute(task);
    }

    public Task getTaskById(int id) throws ExecutionException, InterruptedException {
        GetTaskById getTaskById = new GetTaskById();
        Task task = getTaskById.execute(id).get();
        return task;
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

    private static class GetTaskById extends AsyncTask<Integer, Void, Task> {

        @Override
        protected Task doInBackground(Integer... integers) {
            Task task = null;
            if (integers != null && integers.length > 0) {
                task = database.tasksDao().getTaskById(integers[0]);
            }
            return task;
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
    //    public TodayViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is today fragment");
//    }


//    public LiveData<String> getText() {
//        return mText;
//    }
}