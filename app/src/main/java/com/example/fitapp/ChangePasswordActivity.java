package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Password");

        EditText edtNewPassword = findViewById(R.id.editTextNewPassword);
        EditText edtConfirmNewPassword = findViewById(R.id.editTextConfirmNewPassword);
        Button btnChangePassword = findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = edtNewPassword.getText().toString();
                String confirmNewPassword = edtConfirmNewPassword.getText().toString();
                
                if (newPassword.equals("") || confirmNewPassword.equals("")) {
                    Toast.makeText(ChangePasswordActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Validator.isValidPassword(newPassword))
                    edtNewPassword.setError("Must be 8-30 characters\nMust contain at least one uppercase letter\nMust contain at least one lowercase letter\nMust contain one symbol: @#$%^&-+=()\nUnderscores are allowed");
                else
                    edtNewPassword.setError(null);
                if (!confirmNewPassword.equals(newPassword))
                    edtConfirmNewPassword.setError("New passwords do not match");
                else
                    edtConfirmNewPassword.setError(null);

                if (edtNewPassword.getError() != null || edtConfirmNewPassword.getError() != null) {
                    Toast.makeText(ChangePasswordActivity.this, "You entered invalid information", Toast.LENGTH_SHORT).show();
                    return;
                }

                DBHandler dbHandler = new DBHandler(ChangePasswordActivity.this);
                boolean result = dbHandler.updateUserById(LoginActivity.user.getId(),
                        LoginActivity.user.getUsername(), newPassword,
                        LoginActivity.user.getEmail(), LoginActivity.user.getPhone());

                if (result) {
                    LoginActivity.user.setPassword(newPassword);
                    Toast.makeText(ChangePasswordActivity.this, "Password successfully changes", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }
}