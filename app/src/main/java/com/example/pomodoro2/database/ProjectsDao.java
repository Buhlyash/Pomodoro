package com.example.pomodoro2.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProjectsDao {
    @Query("SELECT * FROM projects")
    LiveData<List<Projects>> getAllProjects();

    @Insert
    void insertProject(Projects projects);

    @Delete
    void deleteProjects(Projects projects);

    @Query("DELETE FROM projects")
    void deleteAllProjects();
}
