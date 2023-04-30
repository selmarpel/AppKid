package com.ftec.kidapp.Activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "memory_game.db";
    public static final int DATABASE_VERSION = 1;

    public static final String MEMO_TABLE = "memory_game";
    public static final String COLUNA_POSICAO = "posicao";
    public static final String COLUNA_IMAGEM = "imagem";
    public static final String COLUNA_VIRADA = "virada";


    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MEMO_TABLE + " (" +
                    COLUNA_POSICAO + " INTEGER PRIMARY KEY," +
                    COLUNA_IMAGEM + " INTEGER," +
                    COLUNA_VIRADA + " INTEGER DEFAULT 0 CHECK(" + COLUNA_VIRADA + " IN (0,1)))";



    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MEMO_TABLE;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
