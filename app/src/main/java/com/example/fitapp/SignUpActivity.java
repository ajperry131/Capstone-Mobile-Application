package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText edtUsername = findViewById(R.id.editTextUsername);
        EditText edtPassword = findViewById(R.id.editTextPassword);
        EditText edtConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        EditText edtEmail = findViewById(R.id.editTextEmailAddress);
        EditText edtPhone = findViewById(R.id.editTextPhone);
        Button btnCreate = findViewById(R.id.btnCreate);
        Button btnBack = findViewById(R.id.btnBack);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();
                String email = edtEmail.getText().toString();
                String phone = edtPhone.getText().toString();

                //do not create account is user neglected to fill in a field
                if (username.isEmpty()
                        || password.isEmpty()
                        || confirmPassword.isEmpty()
                        || email.isEmpty()
                        || phone.isEmpty()) {

                    Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                //do not create account if user failed to enter the same password into both password boxes
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                //attempt to add the user to the database
                DBHandler db = new DBHandler(SignUpActivity.this);
                boolean result = db.addUser(username, password, email, phone);

                //if adding the user succeeded or not, let the user know
                if (result) {
                    Toast.makeText(SignUpActivity.this, "Account successfully created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                } else {
                    Toast.makeText(SignUpActivity.this, "Account already exists with this username, email, or phone", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

    }
}