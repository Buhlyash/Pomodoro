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

import com.example.pomodoro2.R;
import com.example.pomodoro2.database.Task;
import com.example.pomodoro2.databinding.FragmentEditProjectTaskBinding;

import java.util.concurrent.ExecutionException;


public class EditProjectTaskFragment extends Fragment {

    private FragmentEditProjectTaskBinding binding;
    private ProjectTasksViewModel projectTasksViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditProjectTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();

        projectTasksViewModel = new ViewModelProvider(this).get(ProjectTasksViewModel.class);
        EditText editTextTitle = binding.editTextEditTitle;
        EditText editTextDescription = binding.editTextEditDescriprion;
        RadioGroup radioGroup = binding.radioGroupEditPriority;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(inflater.getContext());
        int id = preferences.getInt("taskInProject_id", -1);
        try {
            Task task = projectTasksViewModel.getProjectTaskById(id);
            editTextTitle.setText(task.getTitle());
            editTextDescription.setText(task.getDescription());
            int checkedRadioButtonId = task.getPriority();
            switch (checkedRadioButtonId) {
                case 1:
                    binding.radioButtonEdit1.setChecked(true);
                    break;
                case 2:
                    binding.radioButtonEdit2.setChecked(true);
                    break;
                case 3:
                    binding.radioButtonEdit3.setChecked(true);
                    break;
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        binding.buttonEditSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                if (radioButtonId <= 0) {
                    Toast.makeText(view.getContext(), "Check radiobutton", Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton radioButton = container.findViewById(radioButtonId);
                    int priority = Integer.parseInt(radioButton.getText().toString());
                    if (isFilled(title, description)) {
                        try {
                            Task task = projectTasksViewModel.getProjectTaskById(id);
                            task.setTitle(title);
                            task.setDescription(description);
                            task.setPriority(priority);
                            projectTasksViewModel.updateProjectTask(task);
                            InputMethodManager imm = (InputMethodManager) inflater.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
//                            navController.navigate(R.id.action_editProjectTaskFragment_to_projectTasksFragment);
                            navController.popBackStack();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            private boolean isFilled(String title, String description) {
                return !title.isEmpty() && !description.isEmpty();
            }
        });

        return root;
    }
}