package com.example.pomodoro2.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Task.class, Projects.class}, version = 6, exportSchema = false)
public abstract class TasksDatabase extends RoomDatabase{
    private static TasksDatabase database;
    private static final String DB_NAME = "tasks.db";
    private static final Object LOCK = new Object();

    public static TasksDatabase getInstance(Context context) {
        synchronized (LOCK){
            if (database == null) {
                database = Room.databaseBuilder(context, TasksDatabase.class, DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return database;
    }

    public abstract TasksDao tasksDao();
    public abstract ProjectsDao projectsDao();
}
