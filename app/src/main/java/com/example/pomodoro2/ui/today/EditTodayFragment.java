package com.example.pomodoro2.ui.today;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.example.pomodoro2.databinding.FragmentEditTodayBinding;

import java.util.concurrent.ExecutionException;


public class EditTodayFragment extends Fragment {

    private FragmentEditTodayBinding binding;
    private TodayViewModel todayViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEditTodayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();

        todayViewModel =
                new ViewModelProvider(this).get(TodayViewModel.class);
        EditText editTextTitle = binding.editTextEditTitle;
        EditText editTextDescription = binding.editTextEditDescriprion;
        RadioGroup radioGroup = binding.radioGroupEditPriority;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(inflater.getContext());
        int id = preferences.getInt("task_id", -1);
        try {
            Task task = todayViewModel.getTaskById(id);
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
                            Task task = todayViewModel.getTaskById(id);
                            task.setTitle(title);
                            task.setDescription(description);
                            task.setPriority(priority);
                            todayViewModel.updateTask(task);
                            InputMethodManager imm = (InputMethodManager) inflater.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                            navController.navigate(R.id.action_editTodayFragment_to_nav_today);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "Все поля дожны быть заполнены", Toast.LENGTH_SHORT).show();
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