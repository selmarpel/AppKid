package db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RankingDBManager_3 extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ranking3.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME_3 = "pontuacoes";
    private static final String COLUMN_TEMPO_3 = "tempo";

    public RankingDBManager_3(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_3 + " (" +
                COLUMN_TEMPO_3 + " REAL)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void adicionarPontuacao(double tempo) {
        SQLiteDatabase db = getWritableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_NAME_3;
        Cursor countCursor = db.rawQuery(countQuery, null);
        countCursor.moveToFirst();
        int count = countCursor.getInt(0);
        countCursor.close();


        if (count >= 1000) {
            String minTimeQuery = "SELECT MIN(" + COLUMN_TEMPO_3 + ") FROM " + TABLE_NAME_3;
            Cursor minTimeCursor = db.rawQuery(minTimeQuery, null);
            minTimeCursor.moveToFirst();
            double minTime = minTimeCursor.getDouble(0);
            minTimeCursor.close();


            if (tempo < minTime) {

                String deleteMinTimeQuery = "DELETE FROM " + TABLE_NAME_3 + " WHERE " + COLUMN_TEMPO_3 + " = " + minTime;
                db.execSQL(deleteMinTimeQuery);
            } else {

                db.close();
                return;
            }
        }


        String insertQuery = "INSERT INTO " + TABLE_NAME_3 + " (" + COLUMN_TEMPO_3 + ") VALUES (" + tempo + ")";
        db.execSQL(insertQuery);

        db.close();
    }
    public int somarTempoTotalJogado() {
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT SUM(" + COLUMN_TEMPO_3 + ") FROM " + TABLE_NAME_3;
        Cursor cursor = db.rawQuery(selectQuery, null);

        int tempoTotal = 0;
        if (cursor.moveToFirst()) {
            tempoTotal = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return tempoTotal;
    }

    public void zerarRanking() {
        SQLiteDatabase db = getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_NAME_3;
        db.execSQL(deleteQuery);
        db.execSQL(deleteQuery);
        db.close();
    }
    public int obterTempoTotalJogado() {
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT SUM(" + COLUMN_TEMPO_3 + ") FROM " + TABLE_NAME_3;
        Cursor cursor = db.rawQuery(selectQuery, null);

        int tempoTotal = 0;
        if (cursor.moveToFirst()) {
            tempoTotal = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        return tempoTotal;
    }
    public Cursor obterMelhoresPontuacoes() {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT " + COLUMN_TEMPO_3 +
                " FROM " + TABLE_NAME_3 +
                " ORDER BY " + COLUMN_TEMPO_3 + " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }
}
