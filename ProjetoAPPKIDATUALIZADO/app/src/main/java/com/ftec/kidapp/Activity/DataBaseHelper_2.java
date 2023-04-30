package com.ftec.kidapp.Activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper_2 extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "memory_game.db2";
    public static final int DATABASE_VERSION = 1;

    public static final String MEMO_TABLE_2 = "memory_game";
    public static final String COLUNA_POSICAO_2 = "posicao";
    public static final String COLUNA_IMAGEM_2 = "imagem";
    public static final String COLUNA_VIRADA_2 = "virada";


    private static final String SQL_CREATE_ENTRIES_2 =
            "CREATE TABLE " + MEMO_TABLE_2 + " (" +
                    COLUNA_POSICAO_2 + " INTEGER PRIMARY KEY," +
                    COLUNA_IMAGEM_2 + " INTEGER," +
                    COLUNA_VIRADA_2 + " INTEGER DEFAULT 0 CHECK(" + COLUNA_VIRADA_2 + " IN (0,1)))";



    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MEMO_TABLE_2;

    public DataBaseHelper_2(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db2) {

        db2.execSQL(SQL_CREATE_ENTRIES_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db2, int oldVersion, int newVersion) {
        db2.execSQL(SQL_CREATE_ENTRIES_2);
        onCreate(db2);
    }
}


