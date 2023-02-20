package com.example.fitapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class AddFoodFragment extends Fragment implements View.OnClickListener {

    Button btnMealSave;
    EditText edtMealName;
    EditText edtMealCalories;
    EditText edtMealProtein;
    Intent passedData;

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

        edtMealName = view.findViewById(R.id.editTextMealName);
        edtMealCalories = view.findViewById(R.id.editTextMealCalories);
        edtMealProtein = view.findViewById(R.id.editTextMealProtein);

        btnMealSave = view.findViewById(R.id.btnMealSave);
        btnMealSave.setOnClickListener(this);

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

        if (!passedData.hasExtra("selectedDate")) { //should never be true in the current build
            Toast.makeText(view.getContext(), "No date previously selected", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar mealDate = (Calendar) passedData.getSerializableExtra("selectedDate");

        DBHandler dbHandler = new DBHandler(view.getContext());
        boolean result = dbHandler.addMeal(LoginActivity.user.getId(), mealName, Integer.parseInt(mealCalories), Integer.parseInt(mealProtein), mealDate);

        if (result) {
            Toast.makeText(view.getContext(), "Successfully saved meal", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(view.getContext(), "Failed to save meal", Toast.LENGTH_SHORT).show();

        Toast.makeText(view.getContext(), "Save button pressed", Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStackImmediate();
    }
}