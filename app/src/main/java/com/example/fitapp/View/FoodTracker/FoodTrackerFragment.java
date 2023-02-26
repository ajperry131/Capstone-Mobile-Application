package com.example.fitapp.View.FoodTracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.fitapp.Adapter.MealsAdapter;
import com.example.fitapp.Model.DBHandler;
import com.example.fitapp.Helper.MyButtonClickListener;
import com.example.fitapp.Helper.SwipeHelper;
import com.example.fitapp.Model.Meal;
import com.example.fitapp.R;
import com.example.fitapp.View.AccountCreation.LoginActivity;
import com.example.fitapp.View.HomeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.List;

public class FoodTrackerFragment extends Fragment {

    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private FloatingActionButton fab;
    private RecyclerView recyclerViewMeals;
    private MealsAdapter mealsAdapter;
    private LinearLayoutManager layoutManager;
    private TextView textViewTotalMealCalories;
    private TextView textViewTotalMealProtein;

    private Calendar calendar;

    private List<Meal> mealList;

    public FoodTrackerFragment() {
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
        View view = inflater.inflate(R.layout.fragment_food_tracker, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize date picker to get proper working OnDateSet
        initDatePicker();

        //initialize recycler to set variables of recycler and layout manager
        initRecycler();

        //get the current time
        calendar = Calendar.getInstance();

        //create the adapter and display the recycler
        displayRecycler();

        //set values for totals
        textViewTotalMealCalories = view.findViewById(R.id.textViewTotalMealCalories);
        textViewTotalMealProtein = view.findViewById(R.id.textViewTotalMealProtein);

        updateTotalMealCaloriesAndProtein();

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
    }

    private void updateTotalMealCaloriesAndProtein() {
        int totalCalories = 0;
        int totalProtein = 0;
        for (int i = 0; i < mealList.size(); i++) {
            totalCalories += mealList.get(i).getCalories();
            totalProtein += mealList.get(i).getProtein();
        }
        textViewTotalMealCalories.setText(String.valueOf(totalCalories));
        textViewTotalMealProtein.setText(String.valueOf(totalProtein));
    }

    private void displayRecycler() {
        DBHandler dbHandler = new DBHandler(getContext());
        mealList = dbHandler.getMealsOfUserByDate(LoginActivity.user.getId(), calendar);
        mealsAdapter = new MealsAdapter(getContext(), mealList);
        recyclerViewMeals.setAdapter(mealsAdapter);
    }


    private void initRecycler() {
        recyclerViewMeals = (RecyclerView) getView().findViewById(R.id.recyclerViewMeals);
        recyclerViewMeals.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMeals.setLayoutManager(layoutManager);

        SwipeHelper swipeHelper = new SwipeHelper(getContext(), recyclerViewMeals, 200) {
            @Override
            public void instantiateMyButton(RecyclerView.ViewHolder viewHolder, List<SwipeHelper.MyButton> buffer) {
                buffer.add(new MyButton(getActivity(),
                        "Delete",
                        30,
                        0,
                        Color.parseColor("#FF3C30"),
                        new MyButtonClickListener(){
                            @Override
                            public void onClick(int pos) {
                                removeMealAt(pos);
                                updateTotalMealCaloriesAndProtein();
                            }
                        }));

                buffer.add(new MyButton(getActivity(),
                        "Update",
                        30,
                        0,
                        Color.parseColor("#FF8D03"),
                        new MyButtonClickListener(){
                            @Override
                            public void onClick(int pos) {
                                Meal selectedMeal = mealList.get(pos);
                                Intent intent = new Intent();
                                intent.putExtra("selectedMeal", selectedMeal);

                                ((HomeActivity)getActivity()).replaceFragment(new AddFoodFragment(intent));
                            }
                        }));
            }
        };
    }

    private void removeMealAt(int pos) {
        Meal mealToRemove = mealList.get(pos);
        mealsAdapter.removeAt(pos);
        DBHandler db = new DBHandler(getContext());
        db.deleteMealById(mealToRemove.getId());
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
                displayRecycler();
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

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
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