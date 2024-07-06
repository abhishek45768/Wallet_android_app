package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Withdraw extends AppCompatActivity implements View.OnClickListener {
    EditText yourAccount, receiver, amount, pin;
    Button transferButton,b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        yourAccount = findViewById(R.id.editTextText5);
        receiver = findViewById(R.id.editTextText2);
        amount = findViewById(R.id.editTextNumber2);
        pin = findViewById(R.id.editTextNumberPassword3);
        transferButton = findViewById(R.id.button9);
        b2 = findViewById(R.id.button);
        Intent in = getIntent();
        String ab = in.getStringExtra("abValue");
        yourAccount.setText(ab);
        b2.setOnClickListener(this);
        transferButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button9) {
            String senderAccountNumber = yourAccount.getText().toString().trim();
            String receiverAccountNumber = receiver.getText().toString().trim();
            String transferAmount = amount.getText().toString().trim();
            String enteredPin = pin.getText().toString().trim();

            if (!senderAccountNumber.isEmpty() && !receiverAccountNumber.isEmpty() && !transferAmount.isEmpty() && !enteredPin.isEmpty()) {
                ConnectionHelper connectionHelper = new ConnectionHelper(this);

                // Check if the sender's account number and pin are valid
                if (connectionHelper.checkUsernamePassword(senderAccountNumber, enteredPin)) {
                    // Check if the receiver's account number exists
                    if (connectionHelper.checkUsernameExists(receiverAccountNumber)) {
                        // Check if the sender has sufficient balance
                        int senderBalance = Integer.parseInt(connectionHelper.fetchBalance(senderAccountNumber));
                        int transferAmountInt = Integer.parseInt(transferAmount);

                        if (senderBalance >= transferAmountInt) {
                            // Deduct the amount from the sender's account
                            int newSenderBalance = senderBalance - transferAmountInt;
                            boolean isDeducted = connectionHelper.updateBalance(senderAccountNumber, String.valueOf(newSenderBalance));

                            if (isDeducted) {
                                // Add the amount to the receiver's account
                                int receiverBalance = Integer.parseInt(connectionHelper.fetchBalance(receiverAccountNumber));
                                int newReceiverBalance = receiverBalance + transferAmountInt;
                                boolean isAdded = connectionHelper.updateBalance(receiverAccountNumber, String.valueOf(newReceiverBalance));

                                if (isAdded) {
                                    Toast.makeText(this, "Transfer successful.", Toast.LENGTH_SHORT).show();
                                    // You may want to navigate to another activity or perform additional actions here.
                                } else {
                                    Toast.makeText(this, "Failed to add amount to the receiver's account.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Failed to deduct amount from the sender's account.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Insufficient balance in the sender's account.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Receiver's account does not exist.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Invalid sender credentials, show an error message or handle accordingly
                    Toast.makeText(this, "Invalid sender account number or pin.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Show a message if any of the fields are empty
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
            }
        } else if (b2==v) {

            pin.setText("");
            amount.setText("");
            receiver.setText("");
        }
    }
}
