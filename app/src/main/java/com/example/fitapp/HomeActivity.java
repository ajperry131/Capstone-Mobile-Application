package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //workout fragment should be shown on screen when first logged in
        replaceFragment(new WorkoutsFragment());

        //set the workout item on the navigation bar to be selected
        BottomNavigationView buttonNavigationView = findViewById(R.id.bottomNavigationView);
        buttonNavigationView.setSelectedItemId(R.id.workouts);

        //get the user's selection of one of the items on the navigation bar and bring the fragment
        //into view by replacing the blank frame layout
        buttonNavigationView.setOnItemSelectedListener(item -> {

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

            return true;
        });


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }
}