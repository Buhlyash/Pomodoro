package com.example.pomodoro2.ui.projects;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pomodoro2.database.Projects;
import com.example.pomodoro2.database.ProjectsDao;
import com.example.pomodoro2.database.Task;
import com.example.pomodoro2.database.TasksDatabase;
import com.example.pomodoro2.ui.today.TodayViewModel;

import java.util.List;

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

    public void insertProjects(Projects projects) {
        new ProjectsViewModel.InsertProjects().execute(projects);
    }

    public void deleteProjects(Projects projects) {
        new ProjectsViewModel.DeleteProjects().execute(projects);
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


//    public ProjectsViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is projects fragment");
//    }

//    public LiveData<String> getText() {
//        return mText;
//    }
}