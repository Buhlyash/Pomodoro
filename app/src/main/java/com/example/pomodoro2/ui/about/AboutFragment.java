package com.example.pomodoro2.ui.about;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pomodoro2.R;
import com.example.pomodoro2.databinding.AboutFragmentBinding;
import com.example.pomodoro2.databinding.TodayFragmentBinding;
import com.example.pomodoro2.ui.today.TodayViewModel;

public class AboutFragment extends Fragment {

    private AboutFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = AboutFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
}