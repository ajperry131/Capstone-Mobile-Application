package com.example.fitapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ExercisesAdapter extends ArrayAdapter<Exercise> {

    private ArrayList<Exercise> exerciseList;

    public ExercisesAdapter(Context context, ArrayList<Exercise> exercises) {
        super(context, 0, exercises);
    }

    public void setFilteredList(ArrayList<Exercise> filteredList) {
        this.exerciseList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Exercise exercise = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_meal, parent, false);
        }

        // Lookup view for data population
        ImageView imageViewExerciseGif = (ImageView) convertView.findViewById(R.id.imageViewExerciseGif);
        TextView textViewExerciseName = (TextView) convertView.findViewById(R.id.textViewExerciseName);
        TextView textViewExerciseType = (TextView) convertView.findViewById(R.id.textViewExerciseType);

        // Populate the data into the template view using the data object
        Drawable retrievedImageFromInternet = ImageLoader.LoadImageFromWebOperations("https://media.istockphoto.com/id/1322277517/photo/wild-grass-in-the-mountains-at-sunset.webp?s=2048x2048&w=is&k=20&c=eBcsIJmWFYds4lRtGcQi8rRsXsdzBgacGRL4iUoFGgQ=");
        imageViewExerciseGif.setImageDrawable(retrievedImageFromInternet);
        textViewExerciseName.setText(exercise.getName());
        textViewExerciseType.setText(exercise.getExerciseType());

        // Return the completed view to render on screen
        return convertView;
    }
}