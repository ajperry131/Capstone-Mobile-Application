package com.example.fitapp;

import java.util.Calendar;

public class Meal {
    private int id;
    private int userId;
    private String name;
    private int calories;
    private int protein;
    private Calendar date;

    public Meal() {}

    public Meal(int id, int userId, String name, int calories, int protein, Calendar date) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.calories = calories;
        this.protein = protein;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}
