package com.example.fitapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePhoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Change Phone");

        EditText edtNewPhone = findViewById(R.id.editTextNewPhone);
        EditText edtConfirmNewPhone = findViewById(R.id.editTextConfirmNewPhone);
        Button btnChangePhone = findViewById(R.id.btnChangePhone);

        btnChangePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPhone = edtNewPhone.getText().toString();
                String confirmNewPhone = edtConfirmNewPhone.getText().toString();

                if (newPhone.equals("") || confirmNewPhone.equals("")) {
                    Toast.makeText(ChangePhoneActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Validator.isValidPhone(newPhone))
                    edtNewPhone.setError("Must follow a format similar to:\n1234567890\n123-456-7890\n(123)456-7890");
                else edtNewPhone.setError(null);
                if (!confirmNewPhone.equals(newPhone))
                    edtConfirmNewPhone.setError("New phones do not match");
                else
                    edtConfirmNewPhone.setError(null);

                if (edtNewPhone.getError() != null || edtConfirmNewPhone.getError() != null) {
                    Toast.makeText(ChangePhoneActivity.this, "You entered invalid information", Toast.LENGTH_SHORT).show();
                    return;
                }

                DBHandler dbHandler = new DBHandler(ChangePhoneActivity.this);
                boolean result = dbHandler.updateUserById(LoginActivity.user.getId(),
                        LoginActivity.user.getUsername(), LoginActivity.user.getPassword(),
                        LoginActivity.user.getEmail(), newPhone);

                if (result) {
                    LoginActivity.user.setPhone(newPhone);
                    Toast.makeText(ChangePhoneActivity.this, "Phone successfully changes", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(ChangePhoneActivity.this, "Failed to change email", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }
}