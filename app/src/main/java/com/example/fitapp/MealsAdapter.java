package com.example.fitapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MealsAdapter extends ArrayAdapter<Meal> {
    public MealsAdapter(Context context, ArrayList<Meal> meals) {
        super(context, 0, meals);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Meal meal = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_meal, parent, false);
        }

        // Lookup view for data population
        TextView textViewMealName = (TextView) convertView.findViewById(R.id.textViewMealName);
        TextView textViewMealCalories = (TextView) convertView.findViewById(R.id.textViewMealCalories);
        TextView textViewMealProtein = (TextView) convertView.findViewById(R.id.textViewMealProtein);

        // Populate the data into the template view using the data object
        textViewMealName.setText(meal.getName());
        textViewMealCalories.setText(String.valueOf(meal.getCalories()));
        textViewMealProtein.setText(String.valueOf(meal.getProtein()));

        // Return the completed view to render on screen
        return convertView;
    }
}
