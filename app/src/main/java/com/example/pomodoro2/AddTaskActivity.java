package com.example.pomodoro2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.pomodoro2.databinding.ActivityAddTaskBinding;
import com.example.pomodoro2.databinding.TodayFragmentBinding;
import com.example.pomodoro2.ui.today.TodayViewModel;

public class AddTaskActivity extends AppCompatActivity {

    ActivityAddTaskBinding binding;
    TodayViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(TodayViewModel.class);
        EditText editTextTitle = binding.editTextTitle;
        EditText editTextDescription = binding.editTextDescriprion;
        RadioGroup radioGroup = binding.radioGroupPriority;

        binding.buttonSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextTitle.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                int radioButtonId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(radioButtonId);
                int priority = Integer.parseInt(radioButton.getText().toString());
                if (isFilled(title, description)) {
                    Task task = new Task(title, description, priority);
                    viewModel.insertTask(task);
                } else {
                    Toast.makeText(AddTaskActivity.this, "Все поля дожны быть заполнены", Toast.LENGTH_SHORT).show();
                }
            }

            private boolean isFilled(String title, String description) {
                return !title.isEmpty() && !description.isEmpty();
            }
        });
    }
}