package com.example.pomodoro2.ui.done;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro2.R;
import com.example.pomodoro2.TaskAdapter;
import com.example.pomodoro2.database.Task;

import java.util.ArrayList;
import java.util.List;

public class DoneTaskAdapter extends RecyclerView.Adapter<DoneTaskAdapter.DoneTaskViewHolder> {

    private List<Task> doneTasks;
    private OnTasksClickListener onTasksClickListener;

    public DoneTaskAdapter(ArrayList<Task> doneTasks) {
        this.doneTasks = doneTasks;
    }

    public interface OnTasksClickListener {
        void onCheckedChangeListener(int position);
    }

    public void setOnTasksClickListener(DoneTaskAdapter.OnTasksClickListener onTasksClickListener) {
        this.onTasksClickListener = onTasksClickListener;
    }

    @NonNull
    @Override
    public DoneTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new DoneTaskAdapter.DoneTaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoneTaskViewHolder holder, int position) {
        Task task = doneTasks.get(position);
        holder.textViewTitle.setText(task.getTitle());
        holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.textViewDescription.setText(task.getDescription());
        holder.textViewDescription.setPaintFlags(holder.textViewDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
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
        return doneTasks.size();
    }

    class DoneTaskViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private TextView textViewDescription;
        private CheckBox checkBox;

        public DoneTaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setChecked(true);
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

    public List<Task> getDoneTasks() {
        return doneTasks;
    }

    public void setDoneTasks(List<Task> doneTasks) {
        this.doneTasks = doneTasks;
        notifyDataSetChanged();
    }
}
