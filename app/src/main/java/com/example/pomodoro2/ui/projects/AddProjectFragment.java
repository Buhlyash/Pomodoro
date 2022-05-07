package com.example.pomodoro2.ui.projects;

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
import android.widget.Toast;

import com.example.pomodoro2.R;
import com.example.pomodoro2.database.Projects;
import com.example.pomodoro2.databinding.FragmentAddProjectBinding;
import com.example.pomodoro2.databinding.FragmentEditTodayBinding;


public class AddProjectFragment extends Fragment {
    private FragmentAddProjectBinding binding;
    private ProjectsViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddProjectBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();
        
        viewModel = new ViewModelProvider(this).get(ProjectsViewModel.class);
        EditText editText = binding.editTextProjectTitle1;

        binding.buttonSaveProject1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editText.getText().toString().trim();
                if (isFilled(title)) {
                    Projects projects = new Projects(title);
                    viewModel.insertProjects(projects);
                    InputMethodManager imm = (InputMethodManager) inflater.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                    navController.navigate(R.id.action_addProjectFragment_to_nav_projects);
                } else {
                    Toast.makeText(inflater.getContext(), "Все поля дожны быть заполнены", Toast.LENGTH_SHORT).show();
                }
            }
            private boolean isFilled(String title) {
                return !title.isEmpty();
            }
        });
        
        return root;
    }
}