package com.example.pomodoro2.ui.projects;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.pomodoro2.database.Projects;
import com.example.pomodoro2.database.TasksDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProjectsViewModel extends AndroidViewModel {

//    private final MutableLiveData<String> mText;
    private static TasksDatabase database;
    private LiveData<List<Projects>> projects;

    public ProjectsViewModel(@NonNull Application application) {
        super(application);
        database = TasksDatabase.getInstance(getApplication());
        projects = database.projectsDao().getAllProjects();
    }

    public LiveData<List<Projects>> getProjects() {
        return projects;
    }

    public LiveData<List<Projects>> getProjectsForFragment() throws ExecutionException, InterruptedException {
        GetProjectsForFragment getProjectsForFragment = new GetProjectsForFragment();
        LiveData<List<Projects>> projectsForFragment = getProjectsForFragment.execute().get();
        return projectsForFragment;
    }

    public void insertProjects(Projects projects) {
        new InsertProjects().execute(projects);
    }

    public void deleteProjects(Projects projects) {
        new DeleteProjects().execute(projects);
    }

    public void deleteTasksInProject(int id) {
        new DeleteTasksInProject().execute(id);
    }

    public void deleteAllProjects() {
        new ProjectsViewModel.DeleteAllProjects().execute();
    }

    private static class InsertProjects extends AsyncTask<Projects, Void, Void> {
        @Override
        protected Void doInBackground(Projects... projects) {
            if(projects != null && projects.length > 0) {
                database.projectsDao().insertProject(projects[0]);
            }
            return null;
        }
    }

    private static class DeleteProjects extends AsyncTask<Projects, Void, Void> {
        @Override
        protected Void doInBackground(Projects... projects) {
            if(projects != null && projects.length > 0) {
                database.projectsDao().deleteProjects(projects[0]);
            }
            return null;
        }
    }

    private static class DeleteAllProjects extends AsyncTask<Projects, Void, Void> {
        @Override
        protected Void doInBackground(Projects... projects) {
            if(projects != null && projects.length > 0) {
                database.projectsDao().deleteAllProjects();
            }
            return null;
        }
    }

    private static class GetProjectsForFragment extends AsyncTask<Void, Void, LiveData<List<Projects>>> {

        @Override
        protected LiveData<List<Projects>> doInBackground(Void... voids) {
            LiveData<List<Projects>> p = null;
            p = database.projectsDao().getProjects();
            return p;
        }
    }

    private static class DeleteTasksInProject extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            database.tasksDao().deleteTasksInProject(integers[0]);
            return null;
        }
    }
}