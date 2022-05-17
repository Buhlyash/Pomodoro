package com.example.pomodoro2.ui.projects;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.pomodoro2.AddTaskActivity;
import com.example.pomodoro2.R;
import com.example.pomodoro2.database.Task;
import com.example.pomodoro2.databinding.FragmentAddProjectBinding;
import com.example.pomodoro2.databinding.FragmentAddProjectTaskBinding;

import java.util.Objects;

public class AddProjectTaskFragment extends Fragment {
    private FragmentAddProjectTaskBinding binding;
    private ProjectTasksViewModel projectTasksViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        projectTasksViewModel = new ViewModelProvider(this).get(ProjectTasksViewModel.class);

        binding = FragmentAddProjectTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();

        EditText editTextTitle = binding.editTextTitle;
        EditText editTextDescription = binding.editTextDescriprion;
        RadioGroup radioGroup = binding.radioGroupProjectPriority;
        binding.buttonSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = requireActivity().findViewById(radioButtonId);
                int priority = Integer.parseInt(radioButton.getText().toString());
                if (isFilled(title, description)) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                    int projectId = preferences.getInt("projectTask_id", -1);
                    Task task = new Task(title, description, priority, projectId, false);
                    projectTasksViewModel.insertProjectTask(task);
                    InputMethodManager imm = (InputMethodManager) inflater.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
//                    navController.navigate(R.id.action_addProjectTaskFragment_to_projectTasksFragment);
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