package com.example.fitapp.View.Workout;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitapp.Model.DBHandler;
import com.example.fitapp.R;
import com.example.fitapp.View.HomeActivity;

public class EditExerciseFragment extends Fragment {
    private Bundle passedData;
    private EditText editTextWorkoutExerciseSets;
    private EditText editTextWorkoutExerciseReps;
    private EditText editTextWorkoutExerciseWeight;
    private Button btnWorkoutExerciseSave;

    public EditExerciseFragment(Intent passedData) {
        this.passedData = passedData.getExtras();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((HomeActivity) getActivity()).determineButtonVisibility();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_exercise, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextWorkoutExerciseSets = view.findViewById(R.id.editTextWorkoutExerciseSets);
        editTextWorkoutExerciseSets.setText(String.valueOf(passedData.getInt("workoutExerciseSets")));
        editTextWorkoutExerciseReps = view.findViewById(R.id.editTextWorkoutExerciseReps);
        editTextWorkoutExerciseReps.setText(String.valueOf(passedData.getInt("workoutExerciseReps")));
        editTextWorkoutExerciseWeight = view.findViewById(R.id.editTextWorkoutExerciseWeight);
        editTextWorkoutExerciseWeight.setText(String.valueOf(passedData.getFloat("workoutExerciseWeight")));

        btnWorkoutExerciseSave = view.findViewById(R.id.btnWorkoutExerciseSave);
        btnWorkoutExerciseSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sets = editTextWorkoutExerciseSets.getText().toString();
                String reps = editTextWorkoutExerciseReps.getText().toString();
                String weight = editTextWorkoutExerciseWeight.getText().toString();

                if (sets.equals("") || reps.equals("") || weight.equals("")) {
                    Toast.makeText(view.getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                DBHandler dbHandler = new DBHandler(getContext());
                dbHandler.updateWorkoutExerciseById(passedData.getInt("workoutExerciseId"), Integer.parseInt(sets), Integer.parseInt(reps), Float.parseFloat(weight));

                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });
    }
}