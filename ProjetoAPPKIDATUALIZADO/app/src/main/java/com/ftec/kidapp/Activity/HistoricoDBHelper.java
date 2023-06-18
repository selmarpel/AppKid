package com.ftec.kidapp.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoricoDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "access_history";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_BUTTON_NAME = "button_name";
    private static final String COLUMN_DATE_TIME = "date_time";

    public HistoricoDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BUTTON_NAME + " TEXT, " +
                COLUMN_DATE_TIME + " TEXT" +
                ")";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Lógica para atualizar o banco de dados, se necessário
    }
    public Cursor getAccessHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_BUTTON_NAME, COLUMN_DATE_TIME};
        String orderBy = COLUMN_DATE_TIME + " DESC";
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, orderBy);
        return cursor;
    }

    public void saveAccessHistory(String buttonName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BUTTON_NAME, buttonName);
        values.put(COLUMN_DATE_TIME, getCurrentDateTime());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}
