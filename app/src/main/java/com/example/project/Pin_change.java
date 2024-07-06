package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Pin_change extends AppCompatActivity implements View.OnClickListener {
    EditText Account, newPin, confirmPin;
    Button changePinButton,b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_change);

        Account = findViewById(R.id.editTextText5);
        newPin = findViewById(R.id.editTextNumberPassword4);
        confirmPin = findViewById(R.id.editTextNumberPassword3);
        changePinButton = findViewById(R.id.button9);

        Intent in = getIntent();
        String ab = in.getStringExtra("abValue");
        Account.setText(ab);
        b2 = findViewById(R.id.button);
        b2.setOnClickListener(this);
        changePinButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button9) {
            String accountNumber = Account.getText().toString().trim();
            String newPinText = newPin.getText().toString().trim();
            String confirmPinText = confirmPin.getText().toString().trim();

            if (!accountNumber.isEmpty() && !newPinText.isEmpty() && !confirmPinText.isEmpty()) {
                if (newPinText.equals(confirmPinText)) {
                    ConnectionHelper connectionHelper = new ConnectionHelper(this);

                    // Update the PIN in the database
                    boolean isPinUpdated = connectionHelper.updatePin(accountNumber, newPinText);

                    if (isPinUpdated) {
                        Toast.makeText(this, "PIN successfully changed.", Toast.LENGTH_SHORT).show();
                        // You may want to navigate to another activity or perform additional actions here.
                    } else {
                        Toast.makeText(this, "Failed to change PIN. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "New PIN and Confirm PIN do not match.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
            }
        } else if (b2==v) {
            newPin.setText("");
            confirmPin.setText("");
        }
    }
}
