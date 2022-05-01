package com.example.pomodoro2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TasksDao {
    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();

    @Insert
    void insertTask(Task task);

    @Delete
    void deleteTasks(Task task);

    @Query("DELETE FROM tasks")
    void deleteAllTasks();
}
