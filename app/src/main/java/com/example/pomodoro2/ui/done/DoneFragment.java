package com.example.pomodoro2.ui.done;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro2.database.Task;
import com.example.pomodoro2.databinding.DoneFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class DoneFragment extends Fragment {

    private DoneFragmentBinding binding;
    private final ArrayList<Task> doneTasks = new ArrayList<>();
    private DoneTaskAdapter adapter;
    private DoneViewModel doneViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        doneViewModel =
                new ViewModelProvider(this).get(DoneViewModel.class);

        binding = DoneFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.doneRecyclerView;
        adapter = new DoneTaskAdapter(doneTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        getData();
        recyclerView.setAdapter(adapter);
        adapter.setOnTasksClickListener(new DoneTaskAdapter.OnTasksClickListener() {
            @Override
            public void onCheckedChangeListener(int position) {
                Task task = adapter.getDoneTasks().get(position);
                task.setCompleted(false);
                doneViewModel.updateTask(task);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                remove(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void remove(int position) {
        Task task = adapter.getDoneTasks().get(position);
        doneViewModel.deleteTask(task);
    }

    public void getData() {
        LiveData<List<Task>> tasksFromDb = doneViewModel.getDoneTasks();
        tasksFromDb.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasksFromLiveData) {

                adapter.setDoneTasks(tasksFromLiveData);
            }
        });
    }
}