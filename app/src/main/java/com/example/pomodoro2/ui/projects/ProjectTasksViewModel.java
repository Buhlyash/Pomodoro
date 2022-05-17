package com.example.pomodoro2.ui.projects;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.pomodoro2.database.Task;
import com.example.pomodoro2.database.TasksDatabase;
import com.example.pomodoro2.ui.today.TodayViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProjectTasksViewModel extends AndroidViewModel {

    private static TasksDatabase database;
    private LiveData<List<Task>> projectTasks;

    public ProjectTasksViewModel(@NonNull Application application) {
        super(application);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
        int projectId = preferences.getInt("projectTask_id", -1);
        database = TasksDatabase.getInstance(getApplication());
        projectTasks = database.tasksDao().getTasksByProjectId(projectId);
    }

    public LiveData<List<Task>> getProjectTasks() {
        return projectTasks;
    }

    public void insertProjectTask(Task task) {
        new InsertProjectTask().execute(task);
    }

    public void deleteProjectTask(Task task) {
        new DeleteProjectTask().execute(task);
    }

    public void deleteAllTask() {
        new DeleteAllTask().execute();
    }

    public void updateProjectTask(Task task) {
        new UpdateProjectTask().execute(task);
    }

    public Task getProjectTaskById(int id) throws ExecutionException, InterruptedException {
        GetProjectTaskById getProjectTaskById = new GetProjectTaskById();
        Task task = getProjectTaskById.execute(id).get();
        return task;
    }

    private static class InsertProjectTask extends AsyncTask<Task, Void, Void> {
        @Override
        protected Void doInBackground(Task... tasks) {
            if (tasks != null && tasks.length > 0) {
                database.tasksDao().insertTask(tasks[0]);
            }
            return null;
        }
    }

    private static class DeleteProjectTask extends AsyncTask<Task, Void, Void> {
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

    private static class GetProjectTaskById extends AsyncTask<Integer, Void, Task> {

        @Override
        protected Task doInBackground(Integer... integers) {
            Task task = null;
            if (integers != null && integers.length > 0) {
                task = database.tasksDao().getTaskById(integers[0]);
            }
            return task;
        }
    }

    private static class UpdateProjectTask extends AsyncTask<Task, Void, Void> {

        @Override
        protected Void doInBackground(Task... tasks) {
            if (tasks != null && tasks.length > 0) {
                database.tasksDao().updateTask(tasks[0]);
            }
            return null;
        }
    }
}
