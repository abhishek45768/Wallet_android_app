package com.example.project;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor; // Add this import statement
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Balance extends AppCompatActivity implements View.OnClickListener {
    EditText e1, pin;
    Button b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        e1 = findViewById(R.id.editTextText5);
        pin = findViewById(R.id.editTextNumberPassword3);
        b1 = findViewById(R.id.button9);
        b1.setOnClickListener(this);
        Intent in = getIntent();
        String ab = in.getStringExtra("abValue");
        e1.setText(ab);
        b2 = findViewById(R.id.button);
        b2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button9) {
            String accountNumber = e1.getText().toString().trim();
            String enteredPin = pin.getText().toString().trim();

            // Check if account number and pin are not empty
            if (!accountNumber.isEmpty() && !enteredPin.isEmpty()) {
                ConnectionHelper connectionHelper = new ConnectionHelper(this);

                // Check if the entered account number and pin are valid
                if (connectionHelper.checkUsernamePassword(accountNumber, enteredPin)) {
                    // Valid credentials, fetch balance from the database
                    String userBalance = fetchBalance(accountNumber);

                    // Display the balance in an alert dialog
                    showBalanceAlertDialog(userBalance);
                } else {
                    // Invalid credentials, show an error message or handle accordingly
                    Toast.makeText(this, "Wrong PIN", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (v==b2) {
            pin.setText("");
        }
    }

    private String fetchBalance(String accountNumber) {
        ConnectionHelper connectionHelper = new ConnectionHelper(this);
        SQLiteDatabase db = connectionHelper.getReadableDatabase();

        String[] columns = {ConnectionHelper.COLUMN_BALANCE};
        String selection = ConnectionHelper.COLUMN_ACCOUNT_NUMBER + "=?";
        String[] selectionArgs = {accountNumber};

        Cursor cursor = db.query(ConnectionHelper.TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        String userBalance = "";

        if (cursor.moveToFirst()) {
            userBalance = cursor.getString(cursor.getColumnIndex(ConnectionHelper.COLUMN_BALANCE));
        }

        cursor.close();
        db.close();

        return userBalance;
    }

    private void showBalanceAlertDialog(String userBalance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Balance");
        builder.setMessage("Your balance is: " + userBalance);
        builder.setPositiveButton("OK", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}