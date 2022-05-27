package com.example.pomodoro2.ui.projects;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import android.widget.Toast;

import com.example.pomodoro2.R;
import com.example.pomodoro2.database.Task;
import com.example.pomodoro2.databinding.FragmentProjectTasksBinding;
import com.example.pomodoro2.ui.timer.TimerFragment;
import com.example.pomodoro2.util.NotificationUtil;
import com.example.pomodoro2.util.PrefUtil;

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

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onImageClick(int position) {
                if (PrefUtil.getTimerState(requireContext()) == TimerFragment.TimerState.Stopped) {
                    if (PrefUtil.getCountOfTimer(requireContext()) > PrefUtil.getCountOfRest(requireContext()) && PrefUtil.getCountOfTimer(requireContext()) != 4) {
                        Toast.makeText(requireContext(), "Время для отдыха!", Toast.LENGTH_SHORT).show();
                    } else if (PrefUtil.getCountOfTimer(requireContext()) == PrefUtil.getCountOfRest(requireContext()) && PrefUtil.getCountOfTimer(requireContext()) != 4){
                        Toast.makeText(requireContext(), "Время для работы!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "Время для большого отдыха!", Toast.LENGTH_SHORT).show();
                    }
                    int minutesRemaining;
                    if (PrefUtil.getCountOfTimer(requireContext()) > PrefUtil.getCountOfRest(requireContext()) && PrefUtil.getCountOfTimer(requireContext()) != 4) {
                        minutesRemaining = PrefUtil.getRestLength(requireContext());
                    } else if (PrefUtil.getCountOfTimer(requireContext()) == PrefUtil.getCountOfRest(requireContext()) && PrefUtil.getCountOfTimer(requireContext()) != 4){
                        minutesRemaining = PrefUtil.getTimerLength(requireContext());
                    } else {
                        minutesRemaining = PrefUtil.getLongRestLength(requireContext());
                    }
                    if (PrefUtil.getCountOfTimer(getContext()) > PrefUtil.getCountOfRest(getContext())) {
                        PrefUtil.setCountOfRest(PrefUtil.getCountOfRest(getContext()) + 1, getContext());
                    } else {
                        PrefUtil.setCountOfTimer(PrefUtil.getCountOfTimer(getContext()) + 1, getContext());
                    }
                    long secondsRemaining3 = minutesRemaining * 60L;
                    long wakeUpTime2 = TimerFragment.setAlarm(requireContext(), TimerFragment.getNowSeconds(), secondsRemaining3);
                    PrefUtil.setTimerState(TimerFragment.TimerState.Running, requireContext());
                    PrefUtil.setSecondsRemaining(secondsRemaining3, requireContext());
                    NotificationUtil.showTimerRunning(requireContext(), wakeUpTime2);
                } else {
                    Toast.makeText(requireContext(), "Таймер идет!", Toast.LENGTH_SHORT).show();
                }
                navController.navigate(R.id.action_projectTasksFragment_to_nav_timer);
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