package com.example.pomodoro2.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "projects")
public class Projects {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "project_id")
    private int id;
    private String title;

    public Projects(int id, String title) {
        this.id = id;
        this.title = title;
    }

    @Ignore
    public Projects(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
