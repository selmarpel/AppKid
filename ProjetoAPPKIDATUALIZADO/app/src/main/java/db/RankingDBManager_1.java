package db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RankingDBManager_1 extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ranking.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "pontuacoes";
    private static final String COLUMN_TEMPO = "tempo";
    private static final String COLUMN_TEMPO_TOTAL = "tempo_total"; // Nova coluna

    public RankingDBManager_1(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_TEMPO + " REAL, " +
                COLUMN_TEMPO_TOTAL + " INTEGER DEFAULT 0)"; // Adiciona a coluna com valor padrão 0
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement database schema upgrade logic if needed
    }

    public void adicionarPontuacao(double tempo) {
        SQLiteDatabase db = getWritableDatabase();

        // Obter o número total de pontuações no banco de dados
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_NAME;
        Cursor countCursor = db.rawQuery(countQuery, null);
        countCursor.moveToFirst();
        int count = countCursor.getInt(0);
        countCursor.close();

        // Verificar se o número total de pontuações é maior que 5
        if (count >= 1000) {
            // Obter o tempo mínimo atualmente armazenado no banco de dados
            String minTimeQuery = "SELECT MIN(" + COLUMN_TEMPO + ") FROM " + TABLE_NAME;
            Cursor minTimeCursor = db.rawQuery(minTimeQuery, null);
            minTimeCursor.moveToFirst();
            double minTime = minTimeCursor.getDouble(0);
            minTimeCursor.close();

            // Verificar se o novo tempo é maior que o tempo mínimo
            if (tempo < minTime) {
                // Remover o tempo mínimo do banco de dados
                String deleteMinTimeQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_TEMPO + " = " + minTime;
                db.execSQL(deleteMinTimeQuery);
            } else {
                // Não adicionar o novo tempo ao banco de dados
                db.close();
                return;
            }
        }

        // Inserir o novo tempo
        String insertQuery = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_TEMPO + ") VALUES (" + tempo + ")";
        db.execSQL(insertQuery);

        // Somar o novo tempo ao tempo total
        String updateTotalTimeQuery = "UPDATE " + TABLE_NAME + " SET " + COLUMN_TEMPO_TOTAL + " = " + COLUMN_TEMPO_TOTAL + " + " + tempo;
        db.execSQL(updateTotalTimeQuery);

        db.close();
    }

    public void zerarRanking() {
        SQLiteDatabase db = getWritableDatabase();
        String deleteQuery = "DELETE FROM " + TABLE_NAME;
        db.execSQL(deleteQuery);
        db.close();
    }

    public int obterTempoTotalJogado() {
        SQLiteDatabase db = getReadableDatabase();

        String selectQuery = "SELECT " + COLUMN_TEMPO_TOTAL + " FROM " + TABLE_NAME;
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
        String selectQuery = "SELECT " + COLUMN_TEMPO +
                " FROM " + TABLE_NAME +
                " ORDER BY " + COLUMN_TEMPO + " ASC";
        Cursor cursor = db.rawQuery(selectQuery, null);
        return cursor;
    }
}
