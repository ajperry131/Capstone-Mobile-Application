package com.example.fitapp.View.AccountCreation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fitapp.Model.DBHandler;
import com.example.fitapp.R;
import com.example.fitapp.Util.Validator;

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

                //set errors for each field if the information is not valid
                if (!Validator.isValidUsername(username))
                    edtUsername.setError("Must be 6-20 characters\nunderscores are allowed\nNo spaces allowed\nNo symbols allowed");
                else edtUsername.setError(null);
                if (!Validator.isValidPassword(password))
                    edtPassword.setError("Must be 8-30 characters\nMust contain at least one uppercase letter\nMust contain at least one lowercase letter\nMust contain one symbol: @#$%^&-+=()\nUnderscores are allowed");
                else edtPassword.setError(null);
                if (!confirmPassword.equals(password))
                    edtConfirmPassword.setError("Confirm password does not match password");
                else edtConfirmPassword.setError(null);
                if (!Validator.isValidEmail(email))
                    edtEmail.setError("Must follow a format similar to:\njohnsmith@mail.com");
                else edtEmail.setError(null);
                if (!Validator.isValidPhone(phone))
                    edtPhone.setError("Must follow a format similar to:\n1234567890\n123-456-7890\n(123)456-7890");
                else
                    edtPhone.setError(null);


                //let the user know if any information is invalid
                if (edtUsername.getError() != null
                        || edtPassword.getError() != null
                        || edtConfirmPassword.getError() != null
                        || edtEmail.getError() != null
                        || edtPhone.getError() != null) {
                    Toast.makeText(SignUpActivity.this, "You entered invalid information", Toast.LENGTH_SHORT).show();
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