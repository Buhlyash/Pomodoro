package com.example.pomodoro2.ui.projects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.pomodoro2.AddTaskActivity;
import com.example.pomodoro2.R;
import com.example.pomodoro2.database.Projects;
import com.example.pomodoro2.database.Task;
import com.example.pomodoro2.databinding.ActivityAddProjectBinding;
import com.example.pomodoro2.databinding.ActivityAddTaskBinding;
import com.example.pomodoro2.ui.today.TodayViewModel;

public class AddProjectActivity extends AppCompatActivity {

    ActivityAddProjectBinding binding;
    ProjectsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        binding = ActivityAddProjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ProjectsViewModel.class);
        EditText editTextTitle = binding.editTextTitle;

        binding.buttonSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString().trim();
                if (isFilled(title)) {
                    Projects projects = new Projects(title);
                    viewModel.insertProjects(projects);
                    finish();
                } else {
                    Toast.makeText(AddProjectActivity.this, "Все поля дожны быть заполнены", Toast.LENGTH_SHORT).show();
                }
            }
            private boolean isFilled(String title) {
                return !title.isEmpty();
            }
        });

    }
}