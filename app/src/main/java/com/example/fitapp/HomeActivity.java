package com.example.fitapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //start on the workouts fragment
        replaceFragment(new WorkoutsFragment());

        //set the workout item on the navigation bar to be selected
        BottomNavigationView buttonNavigationView = findViewById(R.id.bottomNavigationView);
        buttonNavigationView.setSelectedItemId(R.id.workouts);

        //get the user's selection of one of the items on the navigation bar and bring the fragment
        //into view by replacing the blank frame layout
        buttonNavigationView.setOnItemSelectedListener(item -> {

            FragmentManager fm = getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStackImmediate();
            }

            //repeated twice as the first does not clear it out completely
            for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStackImmediate();
            }

            runTest("HomeActivity Before Navigation");

            switch(item.getItemId()) {
                case R.id.foodTracker:
                    replaceFragment(new FoodTrackerFragment());
                    break;
                case R.id.workouts:
                    replaceFragment(new WorkoutsFragment());
                    break;
                case R.id.settings:
                    replaceFragment(new SettingsFragment());
                    break;
            }

            runTest("HomeActivity After Navigation");
            determineButtonVisibility();

            return true;
        });

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        determineButtonVisibility();
    }

    //method for phone's back button
    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void goBack() {
        getSupportFragmentManager().popBackStackImmediate();
        determineButtonVisibility();
    }

    public void showUpButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void hideUpButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void determineButtonVisibility() {
        if (getSupportFragmentManager().getBackStackEntryCount() < 2)
            hideUpButton();
        else
            showUpButton();
    }

    public void runTest(String name) {
        Log.i(name, String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
    }
}