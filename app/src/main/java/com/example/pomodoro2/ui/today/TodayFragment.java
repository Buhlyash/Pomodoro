package com.example.pomodoro2.ui.today;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro2.Task;
import com.example.pomodoro2.databinding.TodayFragmentBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class TodayFragment extends Fragment {

    private TodayFragmentBinding binding;
    private ArrayList<Task> tasks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TodayViewModel todayViewModel =
                new ViewModelProvider(this).get(TodayViewModel.class);

        binding = TodayFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        final TextView textView = binding.textToday;
//        todayViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        RecyclerView recyclerView = binding.recyclerView;
        tasks.add(new Task(1, "Title", "Description", 1));
        tasks.add(new Task(2, "Title2", "Description2", 1));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}