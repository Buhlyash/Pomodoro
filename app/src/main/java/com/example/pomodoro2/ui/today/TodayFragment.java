package com.example.pomodoro2.ui.today;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import com.example.pomodoro2.R;
import com.example.pomodoro2.database.Task;
import com.example.pomodoro2.databinding.TodayFragmentBinding;
import com.example.pomodoro2.ui.timer.TimerFragment;
import com.example.pomodoro2.util.NotificationUtil;
import com.example.pomodoro2.util.PrefUtil;

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
                navController.navigate(R.id.action_nav_today_to_addTaskFragment);
            }
        });

        RecyclerView recyclerView = binding.recyclerView;
        adapter = new TaskAdapter(tasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        getData();
        recyclerView.setAdapter(adapter);
        adapter.setOnTasksClickListener(new TaskAdapter.OnTasksClickListener() {
            @Override
            public void onTaskClick(int position) {
                navController.navigate(R.id.toEditTodayFragmentAction);
            }

            @Override
            public void onLongClick(int position) {
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
                navController.navigate(R.id.action_nav_today_to_nav_timer);
            }

            @Override
            public void onCheckedChangeListener(int position) {
                Task task = adapter.getTasks().get(position);
                task.setCompleted(true);
                todayViewModel.updateTask(task);
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