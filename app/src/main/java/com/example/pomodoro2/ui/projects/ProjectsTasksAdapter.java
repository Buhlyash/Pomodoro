package com.example.pomodoro2.ui.projects;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro2.R;
import com.example.pomodoro2.database.Task;

import java.util.ArrayList;
import java.util.List;

public class ProjectsTasksAdapter extends RecyclerView.Adapter<ProjectsTasksAdapter.ProjectTaskViewHolder> {

    private List<Task> projectTasks;
    private OnTasksClickListener onTasksClickListener;

    public ProjectsTasksAdapter(ArrayList<Task> projectTasks) {
        this.projectTasks = projectTasks;
    }

    public interface OnTasksClickListener {
        void onTaskClick(int position);
        void onCheckedChangeListener(int position);
    }

    public void setOnTasksClickListener(OnTasksClickListener onTasksClickListener) {
        this.onTasksClickListener = onTasksClickListener;
    }

    @NonNull
    @Override
    public ProjectTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_play_task_item, parent, false);
        return new ProjectsTasksAdapter.ProjectTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectTaskViewHolder holder, int position) {
        Task task = projectTasks.get(position);
        holder.textViewTitle.setText(task.getTitle());
        holder.textViewDescription.setText(task.getDescription());
        int colorId;
        int priority = task.getPriority();
        switch (priority) {
            case 1:
                colorId = holder.itemView.getResources().getColor(android.R.color.holo_red_light);
                break;
            case 2:
                colorId = holder.itemView.getResources().getColor(android.R.color.holo_orange_light);
                break;
            default:
                colorId = holder.itemView.getResources().getColor(android.R.color.holo_green_light);
                break;
        }
        holder.textViewTitle.setBackgroundColor(colorId);
    }

    @Override
    public int getItemCount() {
        return projectTasks.size();
    }

    class ProjectTaskViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewDescription;
        private CheckBox checkBox;

        public ProjectTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitleNoPlay);
            textViewDescription = itemView.findViewById(R.id.textViewDescriptionNoPLay);
            checkBox = itemView.findViewById(R.id.checkBoxNoPlay);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
//                    preferences.edit().putInt("task_id" ,tasks.get(getAdapterPosition()).getId()).apply();
                    if (onTasksClickListener != null) {
                        onTasksClickListener.onTaskClick(getAdapterPosition());
                    }
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (onTasksClickListener != null) {
                        onTasksClickListener.onCheckedChangeListener(getAdapterPosition());
                    }
                }
            });
        }
    }

    public List<Task> getProjectTasks() {
        return projectTasks;
    }

    public void setProjectTasks(List<Task> projectTasks) {
        this.projectTasks = projectTasks;
        notifyDataSetChanged();
    }
}
