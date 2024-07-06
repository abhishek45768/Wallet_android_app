package com.example.project;
import android.util.Log;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class registration extends AppCompatActivity implements View.OnClickListener {

    private EditText name, usernameInput, passwordInput;
    private Button btnRegister;
    private ConnectionHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        name = findViewById(R.id.editTextText3);
        usernameInput = findViewById(R.id.editTextText4);
        passwordInput = findViewById(R.id.editTextNumberPassword2);
        btnRegister = findViewById(R.id.button2);
        btnRegister.setOnClickListener(this);

        dbHelper = new ConnectionHelper(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnRegister) {
            String nameValue = name.getText().toString();
            String username = usernameInput.getText().toString();
            String password = passwordInput.getText().toString();

            if (nameValue.isEmpty() || username.isEmpty() || password.isEmpty()) {
                new AlertDialog.Builder(this)
                        .setTitle("Registration Error")
                        .setMessage("Please enter name, username, and password.")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                try {
                    if (!dbHelper.checkUsernameExists(username)) {
                        // Check if PIN is not greater than 4 digits
                        if (password.length() <= 4) {
                            // Check if the account number is numeric
                            if (isNumeric(username)) {
                                boolean isInserted = dbHelper.insertUserData(nameValue, username, password);

                                // Pass 0 as the default value for the balance
                                if (isInserted) {
                                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(registration.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Log.e("Registration", "Registration Failed. Database insertion returned false.");
                                    Toast.makeText(this, "Registration Failed. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Account number should be numeric.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "PIN should not be greater than 4 digits.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("Registration", "Registration Failed. Username already exists.");
                        Toast.makeText(this, "Username already exists. Please choose another one.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Registration", "Exception during registration: " + e.getMessage());
                    Toast.makeText(this, "Registration Failed. An error occurred or user already Exists.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Helper method to check if a string is numeric
    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
