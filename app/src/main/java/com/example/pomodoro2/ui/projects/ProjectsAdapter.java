package com.example.pomodoro2.ui.projects;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro2.R;
import com.example.pomodoro2.TaskAdapter;
import com.example.pomodoro2.database.Projects;
import com.example.pomodoro2.database.Task;

import java.util.ArrayList;
import java.util.List;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.ProjectsViewHolder> {

    private List<Projects> projects;
    private OnProjectsClickListener onProjectsClickListener;

    public ProjectsAdapter(ArrayList<Projects> projects) {
        this.projects = projects;
    }

    @NonNull
    @Override
    public ProjectsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.projects_item, parent, false);
        return new ProjectsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectsViewHolder holder, int position) {
        Projects project = projects.get(position);
        holder.textViewTitle.setText(project.getTitle());
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public interface OnProjectsClickListener {
        void onProjectsClick(int position);
        void onLongClick(int position);
    }

    public void setOnProjectsClickListener(OnProjectsClickListener onProjectsClickListener) {
        this.onProjectsClickListener = onProjectsClickListener;
    }


    class ProjectsViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;

        public ProjectsViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewProjectsTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onProjectsClickListener != null) {
                        onProjectsClickListener.onProjectsClick(getAdapterPosition());
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(onProjectsClickListener != null) {
                        onProjectsClickListener.onLongClick(getAdapterPosition());
                    }
                    return false;
                }
            });
        }
    }

    public List<Projects> getProjects() {
        return projects;
    }

    public void setProjects(List<Projects> projects) {
        this.projects = projects;
        notifyDataSetChanged();
    }
}
