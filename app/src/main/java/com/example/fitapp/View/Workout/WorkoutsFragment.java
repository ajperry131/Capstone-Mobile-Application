package com.example.fitapp.View.Workout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitapp.Adapter.WorkoutExercisesAdapter;
import com.example.fitapp.Helper.SwipeHelper;
import com.example.fitapp.Model.DBHandler;
import com.example.fitapp.Model.Workout;
import com.example.fitapp.Model.WorkoutExercise;
import com.example.fitapp.R;
import com.example.fitapp.View.AccountCreation.LoginActivity;
import com.example.fitapp.View.HomeActivity;
import com.example.fitapp.View.Workout.AddExerciseFragment;
import com.example.fitapp.View.Workout.EditExerciseFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class WorkoutsFragment extends Fragment {

    private FloatingActionButton fabSwapWorkout;
    private FloatingActionButton fabCreateWorkout;
    private FloatingActionButton fabRemoveWorkout;
    private Button btnAddExercise;
    ArrayList<Workout> workouts;
    private Workout currentWorkout;
    private ArrayList<WorkoutExercise> workoutExerciseList;
    private TextView textViewWorkoutName;
    private RecyclerView recyclerViewWorkoutExercises;
    private WorkoutExercisesAdapter workoutExercisesAdapter;
    private LinearLayoutManager layoutManager;

    public WorkoutsFragment() {
        // Required empty public constructor
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

        View view = inflater.inflate(R.layout.fragment_workouts, container, false);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewWorkoutName = view.findViewById(R.id.textViewWorkoutName);
        recyclerViewWorkoutExercises = view.findViewById(R.id.recyclerViewExercisesInWorkout);

        initMembers();



        //On click, navigate to Add Exercise fragment for user to search through the list of
        //available workouts
        btnAddExercise = view.findViewById(R.id.btnAddExercise);
        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Do not navigate if the user has no workout selected
                if (currentWorkout == null) {
                    Toast.makeText(getActivity(), "Create a workout first", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent();
                intent.putExtra("workoutId", currentWorkout.getId());
                ((HomeActivity)getActivity()).replaceFragment(new AddExerciseFragment(intent));
            }
        });

        //On click, show a menu of selectable workouts the user has created and set the screen to
        //the one selected
        fabSwapWorkout = view.findViewById(R.id.fabSwapWorkout);
        fabSwapWorkout.setOnClickListener(v -> {
            if (workouts.isEmpty()) {
                Toast.makeText(getActivity(), "Create a workout first", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Choose a workout");

            String[] workoutNames = new String[workouts.size()];
            for (int i = 0; i < workouts.size(); i++)
                workoutNames[i] = workouts.get(i).getWorkoutName();

            int[] selectedWorkout = {-1};
            builder.setSingleChoiceItems(workoutNames, selectedWorkout[0], ((dialog, which) -> {
                selectedWorkout[0] = which;
                Workout workout = workouts.get(which);
                updateMembers(workout);
                dialog.dismiss();
            }));
            builder.setNegativeButton("Cancel", (dialog, which) -> {});

            builder.show();
        });

        //On click, prompt user to enter the name of the new workout and confirm or exit
        fabCreateWorkout = view.findViewById(R.id.fabCreateWorkout);
        fabCreateWorkout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Enter new workout name");

            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("Confirm", (dialog, which) -> {
                String text = input.getText().toString();
                if (text.isEmpty())
                    return;

                DBHandler dbHandler1 = new DBHandler(getContext());
                dbHandler1.addWorkout(LoginActivity.user.getId(), input.getText().toString());
                updateMembers(null);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {});

            builder.show();
        });

        //On click, prompt user to confirm deletion of the current workout displayed, if confirmed
        //then delete workout and set current workout to the next available one, else display nothing
        fabRemoveWorkout = view.findViewById(R.id.fabRemoveWorkout);
        fabRemoveWorkout.setOnClickListener(v -> {
            if (workouts.isEmpty()) {
                Toast.makeText(getActivity(), "Create a workout first", Toast.LENGTH_SHORT).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Select a workout to delete");

            builder.setMessage("Are you sure you want to delete " + currentWorkout.getWorkoutName() + "?");
            builder.setPositiveButton("Confirm", (dialog, which) -> {
                deleteWorkout();
                updateMembers(null);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> {});
            builder.show();
        });
    }

    private void deleteWorkout() {
        workouts.remove(currentWorkout);
        DBHandler dbHandler = new DBHandler(getContext());
        dbHandler.deleteWorkoutById(currentWorkout.getId());
    }

    private void updateMembers(Workout workout) {
        DBHandler dbHandler = new DBHandler(getContext());
        ArrayList<Workout> updatedWorkouts = dbHandler.getWorkoutsFromUserId(LoginActivity.user.getId());
        if (workouts.size() != updatedWorkouts.size())
            workouts = dbHandler.getWorkoutsFromUserId(LoginActivity.user.getId());

        if (workouts.isEmpty()) {
            currentWorkout = null;
            workoutExerciseList = null;
            workoutExercisesAdapter.clear();
            textViewWorkoutName.setText("");
            return;
        }

        if (workout == null)
            currentWorkout = workouts.get(workouts.size()-1); //get the most recent workout
        else
            currentWorkout = workout;
        textViewWorkoutName.setText(currentWorkout.getWorkoutName());
        workoutExerciseList = dbHandler.getExercisesOfWorkout(currentWorkout.getId());
        workoutExercisesAdapter = new WorkoutExercisesAdapter(getContext(), workoutExerciseList);
        recyclerViewWorkoutExercises.setAdapter(workoutExercisesAdapter);
    }

    private void initMembers() {
        recyclerViewWorkoutExercises = (RecyclerView) getView().findViewById(R.id.recyclerViewExercisesInWorkout);
        recyclerViewWorkoutExercises.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewWorkoutExercises.setLayoutManager(layoutManager);

        DBHandler dbHandler = new DBHandler(getContext());
        workouts = dbHandler.getWorkoutsFromUserId(LoginActivity.user.getId());

        if (workouts.isEmpty()) {
            currentWorkout = null;
            textViewWorkoutName.setText("");
            workoutExerciseList = new ArrayList<>();
        } else {
            currentWorkout = workouts.get(workouts.size() - 1);
            textViewWorkoutName.setText(currentWorkout.getWorkoutName());
            workoutExerciseList = dbHandler.getExercisesOfWorkout(currentWorkout.getId());
        }

        workoutExercisesAdapter = new WorkoutExercisesAdapter(getContext(), workoutExerciseList);
        recyclerViewWorkoutExercises.setAdapter(workoutExercisesAdapter);

        SwipeHelper swipeHelper = new SwipeHelper(getContext(), recyclerViewWorkoutExercises, 200) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<MyButton> buffer) {
                buffer.add(new MyButton(getActivity(),
                        "Delete",
                        30,
                        0,
                        Color.parseColor("#FF3C30"),
                        pos -> {
                            removeWorkoutExerciseAt(pos);
                        }));

                buffer.add(new MyButton(getActivity(),
                        "Update",
                        30,
                        0,
                        Color.parseColor("#FF8D03"),
                        pos -> {
                            WorkoutExercise selectedWorkoutExercise = workoutExerciseList.get(pos);
                            Intent intent = new Intent();
                            intent.putExtra("workoutExerciseId", selectedWorkoutExercise.getId());
                            intent.putExtra("workoutExerciseSets", selectedWorkoutExercise.getWorkoutExerciseSets());
                            intent.putExtra("workoutExerciseReps", selectedWorkoutExercise.getWorkoutExerciseReps());
                            intent.putExtra("workoutExerciseWeight", selectedWorkoutExercise.getWorkoutExerciseWeight());

                            ((HomeActivity)getActivity()).replaceFragment(new EditExerciseFragment(intent));
                        }));
            }
        };
    }
    private void removeWorkoutExerciseAt(int pos) {
        WorkoutExercise workoutExerciseToRemove = workoutExerciseList.get(pos);
        workoutExercisesAdapter.removeAt(pos);
        DBHandler db = new DBHandler(getContext());
        db.deleteWorkoutExerciseById(workoutExerciseToRemove.getId());
    }
}