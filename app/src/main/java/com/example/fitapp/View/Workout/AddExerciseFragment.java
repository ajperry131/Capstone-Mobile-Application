package com.example.fitapp.View.Workout;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fitapp.Adapter.ExercisesAdapter;
import com.example.fitapp.Adapter.SelectListener;
import com.example.fitapp.Model.DBHandler;
import com.example.fitapp.Model.Exercise;
import com.example.fitapp.R;
import com.example.fitapp.View.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class AddExerciseFragment extends Fragment implements SelectListener {

    private RecyclerView recyclerViewExercises;
    private ExercisesAdapter exercisesAdapter;
    private LinearLayoutManager layoutManager;
    private List<Exercise> exerciseList;
    private SearchView searchView;
    private Bundle passedValues;

    public AddExerciseFragment(Intent intent) {
        // Required empty public constructor

        passedValues = intent.getExtras();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // determine button visibility here as stack entry count is no properly updated until created
        ((HomeActivity)getActivity()).determineButtonVisibility();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecycler();
        displayRecycler();

        searchView = view.findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterList(newText);
                return true;
            }
        });
    }

    private void filterList(String text) {
        List<Exercise> filteredList = new ArrayList<>();
        for (Exercise exercise : exerciseList) {
            if (exercise.getName().toLowerCase().contains(text.toLowerCase()) ||
                    exercise.getExerciseType().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(exercise);
            }
        }

        if (!filteredList.isEmpty()) {
            exercisesAdapter.setFilteredList(filteredList);
        }
    }

    private void displayRecycler() {
        DBHandler dbHandler = new DBHandler(getContext());
        exerciseList = dbHandler.getAllExercises();
        exercisesAdapter = new ExercisesAdapter(getContext(), exerciseList, this);
        recyclerViewExercises.setAdapter(exercisesAdapter);
        System.out.println(exercisesAdapter.getItemCount());
    }

    private void initRecycler() {
        recyclerViewExercises = (RecyclerView) getView().findViewById(R.id.recyclerViewExercises);
        recyclerViewExercises.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewExercises.setLayoutManager(layoutManager);
    }

    @Override
    public void onItemClicked(Exercise exercise) {
        DBHandler dbHandler = new DBHandler(getContext());
        dbHandler.addWorkoutExercise(passedValues.getInt("workoutId"), exercise.getName(), 0, 0, 0);
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }
}