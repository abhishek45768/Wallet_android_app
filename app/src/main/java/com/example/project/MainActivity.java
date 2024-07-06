package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister, btnLogin;
    private EditText usernameInput, pinInput;
    private ConnectionHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRegister = findViewById(R.id.button3);
        usernameInput = findViewById(R.id.editTextText);
        pinInput = findViewById(R.id.editTextNumberPassword);
        btnLogin = findViewById(R.id.button4);

        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        dbHelper = new ConnectionHelper(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnRegister) {
            // Handle registration button click
            Intent intent = new Intent(MainActivity.this, registration.class);
            startActivity(intent);
        } else if (v == btnLogin) {
            String username = usernameInput.getText().toString();
            String enteredPin = pinInput.getText().toString();

            if (username.isEmpty() || enteredPin.isEmpty()) {
                // Show error message for empty username or PIN
                new AlertDialog.Builder(this)
                        .setTitle("Input Error")
                        .setMessage("Please enter your username and PIN.")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                // Check if the username exists
                if (dbHelper.checkUsernameExists(username)) {
                    // Retrieve the stored PIN for the username
                    String storedPin = dbHelper.getPinByUsername(username);

                    // Check if the entered PIN matches the stored PIN
                    if (enteredPin.equals(storedPin)) {
                        Intent intent = new Intent(MainActivity.this, Options.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        Toast.makeText(this, "Welcome " , Toast.LENGTH_SHORT).show();
                    } else {
                        // Show error message for incorrect PIN
                        new AlertDialog.Builder(this)
                                .setTitle("Login Failed")
                                .setMessage("Incorrect PIN. Please try again.")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                } else {
                    // Show error message for non-existing username
                    new AlertDialog.Builder(this)
                            .setTitle("Login Failed")
                            .setMessage("Username does not exist. Please register.")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }
        }
    }
}
