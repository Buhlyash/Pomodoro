package com.example.pomodoro2.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.pomodoro2.database.Task;

import java.util.List;

@Dao
public interface TasksDao {
    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM tasks WHERE task_id = :id")
    Task getTaskById(int id);

    @Query("SELECT * FROM tasks WHERE isCompleted = 0 AND projectId IS NULL")
    LiveData<List<Task>> getNotCompletedTasks();

    @Query("SELECT * FROM tasks WHERE isCompleted = 1")
    LiveData<List<Task>> getCompletedTasks();

    @Query("SELECT * FROM tasks WHERE projectId = :projectId")
    LiveData<List<Task>> getTasksByProjectId(int projectId);

    @Insert
    void insertTask(Task task);

    @Update
    void updateTask(Task task);

    @Delete
    void deleteTasks(Task task);

    @Query("DELETE FROM tasks WHERE projectId = :projectId")
    void deleteTasksInProject(int projectId);

    @Query("DELETE FROM tasks")
    void deleteAllTasks();
}
