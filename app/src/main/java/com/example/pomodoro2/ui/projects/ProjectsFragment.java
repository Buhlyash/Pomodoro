package com.example.pomodoro2.ui.projects;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro2.R;
import com.example.pomodoro2.database.Projects;
import com.example.pomodoro2.database.Task;
import com.example.pomodoro2.databinding.ProjectsFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class ProjectsFragment extends Fragment {

    private ProjectsFragmentBinding binding;
    private ProjectsViewModel projectsViewModel;
    private ProjectsAdapter adapter;
    private final ArrayList<Projects> projects = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        projectsViewModel =
                new ViewModelProvider(this).get(ProjectsViewModel.class);

        binding = ProjectsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();

//        final TextView textView = binding.textProjects;
//        projectsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        binding.fabProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Projects projects = new Projects("Today");
//                projectsViewModel.insertProjects(projects);
//                Toast.makeText(inflater.getContext(), "ABOBA", Toast.LENGTH_SHORT).show();
//                projectsViewModel.deleteAllProjects();
//                Intent intent = new Intent(inflater.getContext(), AddProjectActivity.class);
//                startActivity(intent);
                navController.navigate(R.id.action_nav_projects_to_addProjectFragment);
            }
        });

        RecyclerView recyclerView = binding.recyclerViewProjects;
        getData();
        adapter = new ProjectsAdapter(projects);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnProjectsClickListener(new ProjectsAdapter.OnProjectsClickListener() {
            @Override
            public void onProjectsClick(int position) {
                Toast.makeText(inflater.getContext(), "" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(int position) {
                remove(position);
            }
        });

        return root;
    }

    private void remove(int position) {
        Projects projects = adapter.getProjects().get(position);
        projectsViewModel.deleteProjects(projects);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getData() {
        LiveData<List<Projects>> projectsFromDb = projectsViewModel.getProjects();
        projectsFromDb.observe(getViewLifecycleOwner(), new Observer<List<Projects>>() {
            @Override
            public void onChanged(List<Projects> projectsFromLiveData) {
                adapter.setProjects(projectsFromLiveData);
            }
        });
    }
}