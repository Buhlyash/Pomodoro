package com.example.pomodoro2.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Tasks", foreignKeys = @ForeignKey(entity = Projects.class, parentColumns = "project_id", childColumns = "projectId"))
public class Task {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private int id;
    private String title;
    private String description;
    private int priority;
    private int projectId;

    public Task(int id, String title, String description, int priority, int projectId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.projectId = projectId;
    }

    @Ignore
    public Task(String title, String description, int priority, int projectId) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.projectId = projectId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
