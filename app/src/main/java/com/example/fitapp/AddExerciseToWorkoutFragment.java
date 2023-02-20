package com.example.fitapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddExerciseToWorkoutFragment extends Fragment {

    private ListView listViewExercises;
    private ExercisesAdapter adapter;
    private SearchView searchView;
    private ArrayList<Exercise> exerciseList;


    public AddExerciseToWorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_exercise_to_workout, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayExercises();

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
        ArrayList<Exercise> filteredList = new ArrayList<>();
        for (Exercise exercise : exerciseList) {
            if (exercise.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(exercise);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No exercises found", Toast.LENGTH_SHORT).show();
        } else {
            adapter.setFilteredList(filteredList);
        }
    }

    private void displayExercises() {
        //Construct the data source for exercises to be displayed
        DBHandler dbHandler = new DBHandler(getContext());
        exerciseList = dbHandler.getAllExercises();
        // Create and attach the adapter to a ListView
        listViewExercises = (ListView) getView().findViewById(R.id.listViewExercises);
        if (exerciseList != null) {
            adapter = new ExercisesAdapter(getActivity(), exerciseList);
            listViewExercises.setAdapter(adapter);
        }
    }
}