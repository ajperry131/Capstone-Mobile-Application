package com.example.fitapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;
import java.util.Calendar;

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
                "CREATE TABLE user ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "username TEXT,"
                        + "password TEXT,"
                        + "email TEXT,"
                        + "phone TEXT)"
        );
        db.execSQL(
                "CREATE TABLE workout ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "user_id TEXT,"
                        + "workout_name TEXT)"
        );
        db.execSQL(
                "CREATE TABLE exercise ("
                        + "type TEXT PRIMARY KEY,"
                        + "exercise_description TEXT,"
                        + "exercise_image BLOB)"
        );
        db.execSQL(
                "CREATE TABLE workout_exercise ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "workout_id INTEGER,"
                        + "exercise_type TEXT,"
                        + "workout_exercise_sets INTEGER,"
                        + "workout_exercise_reps INTEGER,"
                        + "workout_exercise_weight REAL)"
        );
        db.execSQL(
                "CREATE TABLE record ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "workout_id INTEGER,"
                        + "record_time INTEGER,"
                        + "record_date DATE)"
        );
        db.execSQL(
                "CREATE TABLE meal ("
                        + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "user_id INTEGER, "
                        + "meal_name TEXT,"
                        + "meal_calories INTEGER,"
                        + "meal_protein INTEGER,"
                        + "meal_date DATE)"
        );

    }

    // this method is use to add new user to the sqlite database.
    public boolean addUser(String username, String password, String email, String phone) {

        // get the writable sqlite database
        SQLiteDatabase db = this.getWritableDatabase();

        // values will store values to be inserted into user table
        ContentValues values = new ContentValues();

        values.put("username", username.toLowerCase());
        values.put("password", password);
        values.put("email", email.toLowerCase());
        values.put("phone", phone);

        // insert the values into the user table if the account does not exist
        if (getUserBySignUp(username, email, phone) == null) {
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
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username=? and password=?", new String[] {username.toLowerCase(), password});

        try {
            if (cursor != null && cursor.moveToFirst()) {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));

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
        Cursor cursor = db.rawQuery("SELECT * FROM user WHERE username=? or email=? or phone=?", new String[] {username.toLowerCase(), email.toLowerCase(), phone});

        try {
            if (cursor != null && cursor.moveToFirst()) {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));

                cursor.close();

                return user;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
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

    // this method is use to add new workout exercise to the sqlite database.
    public void addWorkoutExercise(int workoutId, String exerciseType, int workoutExerciseSets,
                                   int workoutExerciseReps, int workoutExerciseWeight) {

        // get the writable sqlite database
        SQLiteDatabase db = this.getWritableDatabase();

        // values will store values to be inserted into workout exercise table
        ContentValues values = new ContentValues();

        values.put("workout_id", workoutId);
        values.put("exercise_type", exerciseType);
        values.put("workout_exercise_sets", workoutExerciseSets);
        values.put("workout_exercise_reps", workoutExerciseReps);
        values.put("workout_exercise_weight", workoutExerciseWeight);

        // insert the values into the workout exercise table
        db.insert("workout_exercise", null, values);

        // class the database
        db.close();
    }

    // this method is use to add new meal to the sqlite database.
    public void addMeal(int userId, String mealName, int mealCalories, int mealProtein, Calendar mealDate) {

        // get the writable sqlite database
        SQLiteDatabase db = this.getWritableDatabase();

        // values will store values to be inserted into meal table
        ContentValues values = new ContentValues();

        values.put("user_id", userId);
        values.put("meal_name", mealName);
        values.put("meal_calories", mealCalories);
        values.put("meal_protein", mealProtein);
        values.put("meal_date", mealDate.toString()); //think this will work

        // insert the values into the meal table
        db.insert("meal", null, values);

        // class the database
        db.close();
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
