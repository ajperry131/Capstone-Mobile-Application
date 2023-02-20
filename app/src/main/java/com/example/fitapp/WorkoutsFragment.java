package com.example.fitapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WorkoutsFragment extends Fragment {

    FloatingActionButton fabSwapWorkout;
    FloatingActionButton fabAddExercise;

    public WorkoutsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_workouts, container, false);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //When floating action button is clicked, open a fragment to swap workout and pass
        //the currently selected date
        fabSwapWorkout = view.findViewById(R.id.fabSwapWorkout);
        fabSwapWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).replaceFragment(new SwapWorkoutFragment());
            }
        });

        //When floating action button is clicked, open a fragment to add a new food and pass
        //the currently selected date
        fabAddExercise = view.findViewById(R.id.fabAddExercise);
        fabAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).replaceFragment(new AddExerciseToWorkoutFragment());
            }
        });
    }
}