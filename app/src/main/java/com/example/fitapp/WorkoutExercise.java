package com.example.fitapp;

public class WorkoutExercise {
    private int id;
    private int workout_id;
    private int exercise_name;
    private int workout_exercise_sets;
    private int workout_exercise_reps;
    private float workout_exercise_weight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkout_id() {
        return workout_id;
    }

    public void setWorkout_id(int workout_id) {
        this.workout_id = workout_id;
    }

    public int getExercise_name() {
        return exercise_name;
    }

    public void setExercise_name(int exercise_name) {
        this.exercise_name = exercise_name;
    }

    public int getWorkout_exercise_sets() {
        return workout_exercise_sets;
    }

    public void setWorkout_exercise_sets(int workout_exercise_sets) {
        this.workout_exercise_sets = workout_exercise_sets;
    }

    public int getWorkout_exercise_reps() {
        return workout_exercise_reps;
    }

    public void setWorkout_exercise_reps(int workout_exercise_reps) {
        this.workout_exercise_reps = workout_exercise_reps;
    }

    public float getWorkout_exercise_weight() {
        return workout_exercise_weight;
    }

    public void setWorkout_exercise_weight(float workout_exercise_weight) {
        this.workout_exercise_weight = workout_exercise_weight;
    }
}
