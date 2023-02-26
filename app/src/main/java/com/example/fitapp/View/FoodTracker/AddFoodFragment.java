package com.example.fitapp.View.FoodTracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitapp.Model.DBHandler;
import com.example.fitapp.Model.Meal;
import com.example.fitapp.R;
import com.example.fitapp.View.AccountCreation.LoginActivity;
import com.example.fitapp.View.HomeActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddFoodFragment extends Fragment implements View.OnClickListener {

    Button btnMealSave;
    EditText edtMealName;
    EditText edtMealCalories;
    EditText edtMealProtein;
    Intent passedData;
    Calendar selectedDate;
    Meal meal;

    public AddFoodFragment(Intent passedData) {
        this.passedData = passedData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // determine button visibility here as stack entry count is no properly updated until created
        ((HomeActivity) getActivity()).determineButtonVisibility();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_food, container, false);

        btnMealSave = view.findViewById(R.id.btnMealSave);
        btnMealSave.setOnClickListener(this);

        edtMealName = view.findViewById(R.id.editTextMealName);
        edtMealCalories = view.findViewById(R.id.editTextMealCalories);
        edtMealProtein = view.findViewById(R.id.editTextMealProtein);

        if (passedData.hasExtra("selectedDate")) {
            selectedDate = (Calendar) passedData.getSerializableExtra("selectedDate");
            meal = new Meal();
            meal.setId(-1);
        }

        //selected meal indicates the user wants to edit a meal, so fields should be prefilled
        if (passedData.hasExtra("selectedMeal")) {
            meal = passedData.getParcelableExtra("selectedMeal");

            edtMealName.setText(meal.getName());
            edtMealCalories.setText(String.valueOf(meal.getCalories()));
            edtMealProtein.setText(String.valueOf(meal.getProtein()));
        }

        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onClick(View view) {
        String mealName = edtMealName.getText().toString();
        String mealCalories = edtMealCalories.getText().toString();
        String mealProtein = edtMealProtein.getText().toString();

        if (mealName.equals("") || mealCalories.equals("") || mealProtein.equals("")) {
            Toast.makeText(view.getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DBHandler dbHandler = new DBHandler(view.getContext());
        if (meal.getId() == -1) { //user is adding a meal, add to meal table with the selected date
            dbHandler.addMeal(LoginActivity.user.getId(), mealName, Integer.parseInt(mealCalories), Integer.parseInt(mealProtein), selectedDate);

        } else { //user is editing a meal, update the meal table to reflect changes
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            try {
                cal.setTime(sdf.parse(meal.getDate()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            dbHandler.updateMealById(meal.getId(), meal.getUserId(), mealName, Integer.parseInt(mealCalories), Integer.parseInt(mealProtein), cal);
        }

        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }
}