package com.example.fitapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class FoodTrackerFragment extends Fragment {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private FloatingActionButton fab;
    private ListView listViewMeals;

    private Calendar calendar;

    public FoodTrackerFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("FoodTracker onCreate", String.valueOf(getActivity().getSupportFragmentManager().getBackStackEntryCount()));

        // determine button visibility here as stack entry count is no properly updated until created
        ((HomeActivity)getActivity()).determineButtonVisibility();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_tracker, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize date picker to getp roper working OnDateSet
        initDatePicker();

        //get the current time
        calendar = Calendar.getInstance();

        //When floating action button is clicked, open a fragment to add a new food and pass
        //the currently selected date
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("selectedDate", calendar);

                ((HomeActivity)getActivity()).replaceFragment(new AddFoodFragment(intent));
            }
        });

        //preset the date on the button to today's date
        dateButton = view.findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());
        //set on click to display the date picker dialogue
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        //display meals of the users on the preselected date
        displayMeals();
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //update the calendar
                calendar.set(year, month, dayOfMonth);

                //configure readable date for button text
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                dateButton.setText(date);

                //display meals of the users on the selected date
                displayMeals();
            }
        };

        //create the date picker dialogue
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(getActivity(), style, dateSetListener, year, month, day);

    }

    private void displayMeals() {
        //Construct the data source for meals to be displayed
        DBHandler dbHandler = new DBHandler(getContext());
        ArrayList<Meal> meals = dbHandler.getMealsOfUserByDate(LoginActivity.user.getId(), calendar);
        // Create and attach the adapter to a ListView
        listViewMeals = (ListView) getView().findViewById(R.id.listViewMeals);
        if (meals != null) {
            MealsAdapter adapter = new MealsAdapter(getActivity(), meals);
            listViewMeals.setAdapter(adapter);
        }
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " + dayOfMonth + " " + year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEP";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
            default:
                return "N/A";
        }
    }
}