package com.example.pomodoro2.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.pomodoro2.database.Task;

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
