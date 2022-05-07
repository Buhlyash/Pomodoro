package com.example.pomodoro2.ui.today;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro2.AddTaskActivity;
import com.example.pomodoro2.R;
import com.example.pomodoro2.TaskAdapter;
import com.example.pomodoro2.database.Task;
import com.example.pomodoro2.databinding.TodayFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class TodayFragment extends Fragment {

    private TodayFragmentBinding binding;
    private final ArrayList<Task> tasks = new ArrayList<>();
    private TaskAdapter adapter;
    private TodayViewModel todayViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        todayViewModel =
                new ViewModelProvider(this).get(TodayViewModel.class);

        binding = TodayFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(inflater.getContext(), AddTaskActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = binding.recyclerView;
        getData();
        adapter = new TaskAdapter(tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnTasksClickListener(new TaskAdapter.OnTasksClickListener() {
            @Override
            public void onTaskClick(int position) {
                Toast.makeText(inflater.getContext(), "" + position, Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.toEditTodayFragmentAction);
            }

            @Override
            public void onLongClick(int position) {
                remove(position);
            }

            @Override
            public void onCheckedChangeListener(int position) {

                remove(position);
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
//        tasks.add(new Task(1, "Title", "Description", 1));
//        tasks.add(new Task(2, "Title2", "Description2", 1));
//        TaskAdapter adapter = new TaskAdapter(tasks);
//        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
//        recyclerView.setAdapter(adapter);

        return root;
    }

    private void remove(int position) {
        Task task = adapter.getTasks().get(position);
        todayViewModel.deleteTask(task);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getData() {
        LiveData<List<Task>> tasksFromDb = todayViewModel.getTasks();
        tasksFromDb.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasksFromLiveData) {
                adapter.setTasks(tasksFromLiveData);
            }
        });
    }
}