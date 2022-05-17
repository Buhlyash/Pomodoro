package com.example.pomodoro2.ui.projects;

import android.content.SharedPreferences;
import android.os.Bundle;

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

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pomodoro2.R;
import com.example.pomodoro2.database.Task;
import com.example.pomodoro2.databinding.FragmentProjectTasksBinding;

import java.util.ArrayList;
import java.util.List;

public class ProjectTasksFragment extends Fragment {

    private FragmentProjectTasksBinding binding;
    private ProjectTasksViewModel projectTasksViewModel;
    private ProjectsTasksAdapter adapter;
    private final ArrayList<Task> projectTasks = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        projectTasksViewModel = new ViewModelProvider(this).get(ProjectTasksViewModel.class);

        binding = FragmentProjectTasksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();

        binding.fabProjectTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_projectTasksFragment_to_addProjectTaskFragment);
            }
        });

        RecyclerView recyclerView = binding.projectTasksRecyclerView;
        adapter = new ProjectsTasksAdapter(projectTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        getData();
        recyclerView.setAdapter(adapter);
        adapter.setOnTasksClickListener(new ProjectsTasksAdapter.OnTasksClickListener() {
            @Override
            public void onTaskClick(int position) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                preferences.edit().putInt("taskInProject_id" ,adapter.getProjectTasks().get(position).getId()).apply();
                navController.navigate(R.id.action_projectTasksFragment_to_editProjectTaskFragment);
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

        return root;
    }

    private void remove(int position) {
        Task task = adapter.getProjectTasks().get(position);
        projectTasksViewModel.deleteProjectTask(task);
    }

    public void getData() {
        LiveData<List<Task>> tasksFromDb = projectTasksViewModel.getProjectTasks();
        tasksFromDb.observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasksFromLiveData) {

                adapter.setProjectTasks(tasksFromLiveData);
            }
        });
    }
}