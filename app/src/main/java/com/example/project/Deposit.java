package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.view.View;
import android.widget.Toast;

public class Deposit extends AppCompatActivity implements View.OnClickListener {
    EditText Account, Amount, Pin;
    Button b1,b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        Account = findViewById(R.id.editTextText5);
        Amount = findViewById(R.id.editTextNumber2);
        Pin = findViewById(R.id.editTextNumberPassword3);
        b1 = findViewById(R.id.button9);
        Intent in = getIntent();
        String ab = in.getStringExtra("abValue");
        Account.setText(ab);
        b2 = findViewById(R.id.button);
        b2.setOnClickListener(this);
        b1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == b1) {
            String accountNumber = Account.getText().toString().trim();
            String enteredPin = Pin.getText().toString().trim();
            String depositAmount = Amount.getText().toString().trim();

            if (!accountNumber.isEmpty() && !enteredPin.isEmpty() && !depositAmount.isEmpty()) {
                ConnectionHelper connectionHelper = new ConnectionHelper(this);

                // Check if the entered account number and pin are valid
                if (connectionHelper.checkUsernamePassword(accountNumber, enteredPin)) {
                    // Valid credentials, proceed with deposit
                    int currentBalance = Integer.parseInt(connectionHelper.fetchBalance(accountNumber));

                    // Check if the current balance is less than 50000
                    if (currentBalance < 50000) {
                        // Update the balance in the database
                        int newBalance = currentBalance + Integer.parseInt(depositAmount);
                        boolean isDeposited = connectionHelper.updateBalance(accountNumber, String.valueOf(newBalance));

                        if (isDeposited) {
                            Toast.makeText(this, "Deposit successful.  " , Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Deposit failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Your account is full. Deposit not allowed.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Invalid credentials, show an error message or handle accordingly
                    Toast.makeText(this, "Invalid account number or pin.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Show a message if any of the fields are empty
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
            }
        } else if (b2==v) {
            Amount.setText("");
            Pin.setText("");
        }
    }
}
