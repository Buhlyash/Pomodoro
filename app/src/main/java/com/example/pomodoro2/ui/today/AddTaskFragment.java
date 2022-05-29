package com.example.pomodoro2.ui.today;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.pomodoro2.R;
import com.example.pomodoro2.database.Task;
import com.example.pomodoro2.databinding.FragmentAddTaskBinding;

public class AddTaskFragment extends Fragment {
    private FragmentAddTaskBinding binding;
    private TodayViewModel todayViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();

        todayViewModel = new ViewModelProvider(this).get(TodayViewModel.class);
        EditText editTextTitle = binding.editTextTitle;
        EditText editTextDescription = binding.editTextDescriprion;
        RadioGroup radioGroup = binding.radioGroupPriority;

        binding.buttonSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = requireActivity().findViewById(radioButtonId);
                int priority = Integer.parseInt(radioButton.getText().toString());
                if (isFilled(title, description)) {
                    Task task = new Task(title, description, priority, null, false);
                    todayViewModel.insertTask(task);
                    InputMethodManager imm = (InputMethodManager) inflater.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                    navController.popBackStack();
                } else {
                    Toast.makeText(getContext(), "Все поля дожны быть заполнены", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean isFilled(String title, String description) {
                return !title.isEmpty() && !description.isEmpty();
            }
        });

        return root;
    }
}