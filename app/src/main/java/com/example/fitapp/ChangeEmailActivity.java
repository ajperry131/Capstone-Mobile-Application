package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Email");


        EditText edtNewEmail = findViewById(R.id.editTextNewEmail);
        EditText edtConfirmNewEmail = findViewById(R.id.editTextConfirmNewEmail);
        Button btnChangeEmail = findViewById(R.id.btnChangeEmail);

        btnChangeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = edtNewEmail.getText().toString();
                String confirmNewEmail = edtConfirmNewEmail.getText().toString();

                if (newEmail.equals("") || confirmNewEmail.equals("")) {
                    Toast.makeText(ChangeEmailActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Validator.isValidEmail(newEmail))
                    edtNewEmail.setError("Must follow a format similar to:\njohnsmith@mail.com");
                else edtNewEmail.setError(null);
                if (!confirmNewEmail.equals(newEmail))
                    edtConfirmNewEmail.setError("New emails do not match");
                else
                    edtConfirmNewEmail.setError(null);

                if (edtNewEmail.getError() != null || edtConfirmNewEmail.getError() != null) {
                    Toast.makeText(ChangeEmailActivity.this, "You entered invalid information", Toast.LENGTH_SHORT).show();
                    return;
                }

                DBHandler dbHandler = new DBHandler(ChangeEmailActivity.this);
                boolean result = dbHandler.updateUserById(LoginActivity.user.getId(),
                        LoginActivity.user.getUsername(), LoginActivity.user.getPassword(),
                        newEmail, LoginActivity.user.getPhone());

                if (result) {
                    LoginActivity.user.setEmail(newEmail);
                    Toast.makeText(ChangeEmailActivity.this, "Email successfully changes", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(ChangeEmailActivity.this, "Failed to change email", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }
}