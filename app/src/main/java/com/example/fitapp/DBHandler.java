package com.example.fitapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "fitnessDb";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS user ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "username TEXT,"
                        + "password TEXT,"
                        + "email TEXT,"
                        + "phone TEXT)"
        );
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS workout ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "user_id TEXT,"
                        + "workout_name TEXT)"
        );
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS exercise ("
                        + "name TEXT PRIMARY KEY,"
                        + "exercise_type TEXT,"
                        + "exercise_gif_url TEXT)"
        );
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS workout_exercise ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "workout_id INTEGER,"
                        + "exercise_name TEXT,"
                        + "workout_exercise_sets INTEGER,"
                        + "workout_exercise_reps INTEGER,"
                        + "workout_exercise_weight REAL)"
        );
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS record ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "workout_id INTEGER,"
                        + "record_time INTEGER,"
                        + "record_date DATE)"
        );
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS meal ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "user_id INTEGER, "
                        + "meal_name TEXT,"
                        + "meal_calories INTEGER,"
                        + "meal_protein INTEGER,"
                        + "meal_date DATE)"
        );
    }

    public boolean prepopulateExerciseTable(InputStream is, BufferedReader reader) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM exercise", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                return false;
            }
            else {
                try {
                    ArrayList<Exercise> exercises = new ArrayList<>();
                    String line;
                    try {
                        while ((line = reader.readLine()) != null) {
                            String[] tokens = line.split(",");
                            ContentValues values = new ContentValues();
                            values.put("name", tokens[0]);
                            values.put("exercise_type", tokens[1]);
                            values.put("exercise_gif_url", tokens[2]);
                            db.insert("exercise", null, values);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }

                return true;
            }
        }

        return false;
    }

    // this method is use to add new user to the sqlite database.
    public boolean addUser(String username, String password, String email, String phone) {
        if (username == null || password == null || email == null || phone == null)
            return false;

        // get the writable sqlite database
        SQLiteDatabase db = this.getWritableDatabase();

        // values will store values to be inserted into user table
        ContentValues values = new ContentValues();

        String encryptedUsername = AESUtil.encrypt(username.toLowerCase());
        String encryptedPassword = AESUtil.encrypt(password);
        String encryptedEmail = AESUtil.encrypt(email.toLowerCase());
        String encryptedPhone = AESUtil.encrypt(phone.replaceAll("[^0-9]",""));

        values.put("username", encryptedUsername);
        values.put("password", encryptedPassword);
        values.put("email", encryptedEmail);
        values.put("phone", encryptedPhone);

        // insert the values into the user table if the account does not exist
        if (getUserBySignUp(encryptedUsername, encryptedEmail, encryptedPhone) == null) {
            db.insert("user", null, values);
            db.close();
            return true;
        }

        //reached if user account already exists
        db.close();
        return false;
    }

    @SuppressLint("Range")
    public User getUserByLogin(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        String encryptedUsername = AESUtil.encrypt(username.toLowerCase());
        String encryptedPassword = AESUtil.encrypt(password);
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username=? and password=?", new String[] {encryptedUsername, encryptedPassword});

        try {
            if (cursor != null && cursor.moveToFirst()) {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setUsername(AESUtil.decrypt(cursor.getString(cursor.getColumnIndex("username"))));
                user.setPassword(AESUtil.decrypt(cursor.getString(cursor.getColumnIndex("password"))));
                user.setEmail(AESUtil.decrypt(cursor.getString(cursor.getColumnIndex("email"))));
                user.setPhone(AESUtil.decrypt(cursor.getString(cursor.getColumnIndex("phone"))));

                cursor.close();

                return user;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @SuppressLint("Range")
    private User getUserBySignUp(String username, String email, String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        String encryptedUsername = AESUtil.encrypt(username.toLowerCase());
        String encryptedEmail = AESUtil.encrypt(email.toLowerCase());
        String encryptedPhone = AESUtil.encrypt(phone.replaceAll("[^0-9]",""));
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username=? or email=? or phone=?", new String[] {encryptedUsername, encryptedEmail, encryptedPhone});

        try {
            if (cursor != null && cursor.moveToFirst()) {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setUsername(AESUtil.decrypt(cursor.getString(cursor.getColumnIndex("username"))));
                user.setPassword(AESUtil.decrypt(cursor.getString(cursor.getColumnIndex("password"))));
                user.setEmail(AESUtil.decrypt(cursor.getString(cursor.getColumnIndex("email"))));
                user.setPhone(AESUtil.decrypt(cursor.getString(cursor.getColumnIndex("phone"))));

                cursor.close();

                return user;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public boolean updateUserById(int id, String username, String password, String email, String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            ContentValues values = new ContentValues();
            String encryptedUsername = AESUtil.encrypt(username.toLowerCase());
            String encryptedPassword = AESUtil.encrypt(password);
            String encryptedEmail = AESUtil.encrypt(email.toLowerCase());
            String encryptedPhone = AESUtil.encrypt(phone.replaceAll("[^0-9]",""));

            values.put("username", encryptedUsername);
            values.put("password", encryptedPassword);
            values.put("email", encryptedEmail);
            values.put("phone", encryptedPhone);

            db.update("user", values, "id=?", new String[]{String.valueOf(id)});
            db.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        db.close();
        return false;
    }

    // this method is use to add new workout to the sqlite database.
    public void addWorkout(int userId, String workoutName) {

        // get the writable sqlite database
        SQLiteDatabase db = this.getWritableDatabase();

        // values will store values to be inserted into workout table
        ContentValues values = new ContentValues();

        values.put("user_id", userId);
        values.put("workout_name", workoutName);

        // insert the values into the workout table
        db.insert("workout", null, values);

        // class the database
        db.close();
    }

    @SuppressLint("Range")
    public ArrayList<Exercise> getAllExercises() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM exercise", null);

        ArrayList<Exercise> exercises = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Exercise exercise = new Exercise();
                exercise.setName(cursor.getString(cursor.getColumnIndex("name")));
                exercise.setExerciseType(cursor.getString(cursor.getColumnIndex("exercise_type")));
                exercise.setExerciseGifUrl(cursor.getString(cursor.getColumnIndex("exercise_gif_url")));
                exercises.add(exercise);
            }
            return exercises;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            cursor.close();
        }

        return null;
    }

    // this method is use to add new workout exercise to the sqlite database.
    public void addWorkoutExercise(int workoutId, String exerciseName, int workoutExerciseSets,
                                   int workoutExerciseReps, int workoutExerciseWeight) {

        // get the writable sqlite database
        SQLiteDatabase db = this.getWritableDatabase();

        // values will store values to be inserted into workout exercise table
        ContentValues values = new ContentValues();

        values.put("workout_id", workoutId);
        values.put("exercise_name", exerciseName);
        values.put("workout_exercise_sets", workoutExerciseSets);
        values.put("workout_exercise_reps", workoutExerciseReps);
        values.put("workout_exercise_weight", workoutExerciseWeight);

        // insert the values into the workout exercise table
        db.insert("workout_exercise", null, values);

        // class the database
        db.close();
    }

    // this method is use to add new meal to the sqlite database.
    public boolean addMeal(int userId, String mealName, int mealCalories, int mealProtein, Calendar mealDate) {
        try {
            // get the writable sqlite database
            SQLiteDatabase db = this.getWritableDatabase();

            // values will store values to be inserted into meal table
            ContentValues values = new ContentValues();

            values.put("user_id", userId);
            values.put("meal_name", mealName);
            values.put("meal_calories", mealCalories);
            values.put("meal_protein", mealProtein);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = formatter.format(mealDate.getTime());
            values.put("meal_date", formattedDate);

            // insert the values into the meal table
            db.insert("meal", null, values);

            // class the database
            db.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressLint("Range")
    public ArrayList<Meal> getMealsOfUserByDate(int userId, Calendar mealDate) {
        SQLiteDatabase db = this.getReadableDatabase();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(mealDate.getTime());
        Cursor cursor = db.rawQuery("SELECT * FROM meal WHERE user_id=? and meal_date=?", new String[] {String.valueOf(userId), formattedDate});
        ArrayList<Meal> meals = new ArrayList<>();
        try {
            while (cursor.moveToNext()) {
                Meal meal = new Meal();
                meal.setId(cursor.getInt(cursor.getColumnIndex("id")));
                meal.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
                meal.setName(cursor.getString(cursor.getColumnIndex("meal_name")));
                meal.setCalories(cursor.getInt(cursor.getColumnIndex("meal_calories")));
                meal.setProtein(cursor.getInt(cursor.getColumnIndex("meal_protein")));
                meal.setDate(mealDate); //reuse of mealDate as this method returns meals from date passed in
                meals.add(meal);
            }
            return meals;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            cursor.close();
        }

        return null;
    }

    // this method is use to add new record to the sqlite database.
    public void addRecord(int workoutId, int recordTime, Calendar recordDate) {

        // get the writable sqlite database
        SQLiteDatabase db = this.getWritableDatabase();

        // values will store values to be inserted into record table
        ContentValues values = new ContentValues();

        values.put("workout_id", workoutId);
        values.put("record_time", recordTime);
        values.put("record_date", recordDate.toString()); //think this will work

        // insert the values into the record table
        db.insert("record", null, values);

        // class the database
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS workout");
        db.execSQL("DROP TABLE IF EXISTS workout_exercise");
        db.execSQL("DROP TABLE IF EXISTS exercise");
        db.execSQL("DROP TABLE IF EXISTS meal");
        db.execSQL("DROP TABLE IF EXISTS record");

        onCreate(db);
    }
}
