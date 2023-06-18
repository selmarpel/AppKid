package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper_3 extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "memory_game.db3";
    public static final int DATABASE_VERSION = 1;

    public static final String MEMO_TABLE_3 = "memory_game";
    public static final String COLUNA_POSICAO_3 = "posicao";
    public static final String COLUNA_IMAGEM_3 = "imagem";
    public static final String COLUNA_VIRADA_3 = "virada";


    private static final String SQL_CREATE_ENTRIES_3 =
            "CREATE TABLE " + MEMO_TABLE_3 + " (" +
                    COLUNA_POSICAO_3 + " INTEGER PRIMARY KEY," +
                    COLUNA_IMAGEM_3 + " INTEGER," +
                    COLUNA_VIRADA_3 + " INTEGER DEFAULT 0 CHECK(" + COLUNA_VIRADA_3 + " IN (0,1)))";



    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MEMO_TABLE_3;

    public DataBaseHelper_3(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db3) {

        db3.execSQL(SQL_CREATE_ENTRIES_3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db3, int oldVersion, int newVersion) {
        db3.execSQL(SQL_CREATE_ENTRIES_3);
        onCreate(db3);
    }
}




