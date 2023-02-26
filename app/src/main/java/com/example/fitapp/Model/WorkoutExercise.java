package com.example.fitapp.Model;

public class WorkoutExercise {
    private int id;
    private int workoutId;
    private String exerciseName;
    private int workoutExerciseSets;
    private int workoutExerciseReps;
    private float workoutExerciseWeight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(int workoutId) {
        this.workoutId = workoutId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getWorkoutExerciseSets() {
        return workoutExerciseSets;
    }

    public void setWorkoutExerciseSets(int workoutExerciseSets) {
        this.workoutExerciseSets = workoutExerciseSets;
    }

    public int getWorkoutExerciseReps() {
        return workoutExerciseReps;
    }

    public void setWorkoutExerciseReps(int workoutExerciseReps) {
        this.workoutExerciseReps = workoutExerciseReps;
    }

    public float getWorkoutExerciseWeight() {
        return workoutExerciseWeight;
    }

    public void setWorkoutExerciseWeight(float workoutExerciseWeight) {
        this.workoutExerciseWeight = workoutExerciseWeight;
    }
}
