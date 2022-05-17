package com.example.pomodoro2.database;

import androidx.annotation.Nullable;
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
    @Nullable
    private Integer projectId;
    private boolean isCompleted;

    public Task(int id, String title, String description, int priority, @Nullable Integer projectId, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.projectId = projectId;
        this.isCompleted = isCompleted;
    }

    @Ignore
    public Task(String title, String description, int priority, @Nullable Integer projectId, boolean isCompleted) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.projectId = projectId;
        this.isCompleted = isCompleted;
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

    @Nullable
    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(@Nullable Integer projectId) {
        this.projectId = projectId;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
