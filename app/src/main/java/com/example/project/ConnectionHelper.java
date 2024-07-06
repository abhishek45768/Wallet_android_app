package com.example.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConnectionHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "users";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ACCOUNT_NUMBER = "account_number";
    public static final String COLUMN_BALANCE = "balance";
    public static final String COLUMN_PIN = "pin";
   // Add this line

    public ConnectionHelper(@Nullable Context context) {
        super(context, "Users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME + " TEXT, " +
                COLUMN_ACCOUNT_NUMBER + " INTEGER PRIMARY KEY, " +  // Change data type to INTEGER
                COLUMN_BALANCE + " INTEGER DEFAULT 0, " +
                COLUMN_PIN + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUserData(String name, String account_number, String pin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_ACCOUNT_NUMBER, generateAccountNumber(account_number));
        contentValues.put(COLUMN_BALANCE, 0);
        contentValues.put(COLUMN_PIN, pin);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Add this method to your ConnectionHelper class
    public String getPinByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_PIN};
        String selection = COLUMN_ACCOUNT_NUMBER + "=?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        String storedPin = "";

        if (cursor.moveToFirst()) {
            storedPin = cursor.getString(cursor.getColumnIndex(COLUMN_PIN));
        }

        cursor.close();
        return storedPin;
    }

    private String generateAccountNumber(String username) {
        // Use the provided username as the account number
        return username;
    }

    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ACCOUNT_NUMBER + " = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    public boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ACCOUNT_NUMBER + " = ? AND " + COLUMN_PIN + " = ?", new String[]{username, password});
        return cursor.getCount() > 0;
    }
    public String fetchBalance(String accountNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_BALANCE};
        String selection = COLUMN_ACCOUNT_NUMBER + "=?";
        String[] selectionArgs = {accountNumber};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        String userBalance = "";

        if (cursor.moveToFirst()) {
            userBalance = cursor.getString(cursor.getColumnIndex(COLUMN_BALANCE));
        }

        cursor.close();
        db.close();

        return userBalance;
    }
    public boolean updateBalance(String accountNumber, String newBalance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BALANCE, newBalance);

        String selection = COLUMN_ACCOUNT_NUMBER + "=?";
        String[] selectionArgs = {accountNumber};

        int rowsAffected = db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();

        return rowsAffected > 0;
    }
    public boolean updatePin(String accountNumber, String newPin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PIN, newPin);

        String selection = COLUMN_ACCOUNT_NUMBER + "=?";
        String[] selectionArgs = {accountNumber};

        int rowsAffected = db.update(TABLE_NAME, values, selection, selectionArgs);
        db.close();

        return rowsAffected > 0;
    }
}
