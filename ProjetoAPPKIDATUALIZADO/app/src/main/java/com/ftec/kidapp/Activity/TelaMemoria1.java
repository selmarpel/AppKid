package com.ftec.kidapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ftec.kidapp.R;

import java.util.Random;

public class TelaMemoria1 extends AppCompatActivity implements View.OnClickListener {

    private ImageView[] mCards = new ImageView[8];
    private int[] mImages = {
            R.drawable.carta1,
            R.drawable.carta1,
            R.drawable.carta2,
            R.drawable.carta2,
            R.drawable.carta3,
            R.drawable.carta3,
            R.drawable.carta4,
            R.drawable.carta4,

    };
    private int mFirstCard = -1;
    private int mSecondCard = -1;
    private boolean mAllCardsMatched = false;
    private boolean mIsClickable = true;
    private DatabaseHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_memoria1);

        mCards[0] = findViewById(R.id.card1);
        mCards[1] = findViewById(R.id.card2);
        mCards[2] = findViewById(R.id.card3);
        mCards[3] = findViewById(R.id.card4);
        mCards[4] = findViewById(R.id.card5);
        mCards[5] = findViewById(R.id.card6);
        mCards[6] = findViewById(R.id.card7);
        mCards[7] = findViewById(R.id.card8);


        shuffleImages();

        for (int i = 0; i < mCards.length; i++) {
            mCards[i].setTag(i);
            mCards[i].setOnClickListener(this);
        }

        Button buttonNewGame = findViewById(R.id.new_game_button);
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });

        Button buttonSaveAndClose = findViewById(R.id.salvar_e_fechar_button);
        buttonSaveAndClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                salvarEstadoDoJogo();
                finish();
            }
        });
        Button buttonLoadGame = findViewById(R.id.load_game_button);
        buttonLoadGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carregarEstadoDoJogo();
            }
        });



        mDbHelper = new DatabaseHelper(this);
    }

    private void salvarEstadoDoJogo() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseHelper.MEMO_TABLE);

        for (int i = 0; i < mImages.length; i++) {
            boolean cartaVirada = mCards[i].getVisibility() == View.VISIBLE ? false : true;
            salvarPosicaoImagem(db, i, mImages[i], cartaVirada);
        }

        db.close();
    }



    private void salvarPosicaoImagem(SQLiteDatabase db, int posicao, int imagem, boolean virada) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUNA_POSICAO, posicao);
        values.put(DatabaseHelper.COLUNA_IMAGEM, imagem);
        values.put(DatabaseHelper.COLUNA_VIRADA, virada ? 0 : 1); // 0 para false (virada) e 1 para true (não virada)
        db.insert(DatabaseHelper.MEMO_TABLE, null, values);
    }

    private void carregarEstadoDoJogo(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseHelper.COLUNA_IMAGEM,
                DatabaseHelper.COLUNA_POSICAO,
                DatabaseHelper.COLUNA_VIRADA
        };
        Cursor cursor = db.query(
                DatabaseHelper.MEMO_TABLE,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int position = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUNA_POSICAO));
                int imageId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUNA_IMAGEM));
                boolean isCardFaceUp = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUNA_VIRADA)) == 1;
                mImages[position] = imageId;
                if (isCardFaceUp) {
                    mCards[position].setVisibility(View.VISIBLE);
                } else {
                    mCards[position].setVisibility(View.INVISIBLE);
                    mCards[position].setImageResource(R.drawable.carta0);
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
    }


    private void startNewGame() {
        shuffleImages();
        mFirstCard = -1;
        mSecondCard = -1;
        mAllCardsMatched = false;
        mIsClickable = true;
        for (ImageView card : mCards) {
            card.setVisibility(View.VISIBLE);
            card.setImageResource(R.drawable.carta0);
        }
    }

    private void shuffleImages() {
        Random random = new Random();
        for (int i = mImages.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = mImages[i];
            mImages[i] = mImages[j];
            mImages[j] = temp;
        }
    }

    @Override
    public void onClick(View view) {
        if (!mIsClickable) {
            return;
        }

        ImageView card = (ImageView) view;

        int index = Integer.parseInt(card.getTag().toString());
        if (mFirstCard == -1) {
            mFirstCard = index;
            card.setImageResource(mImages[index]);
        } else if (mSecondCard == -1 && mFirstCard != index) {
            mSecondCard = index;
            card.setImageResource(mImages[index]);

            if (mImages[mFirstCard] == mImages[mSecondCard]) {
                mCards[mFirstCard].setVisibility(View.INVISIBLE);
                mCards[mSecondCard].setVisibility(View.INVISIBLE);

                // Check if all cards are matched
                mAllCardsMatched = true;
                for (ImageView c : mCards) {
                    if (c.getVisibility() == View.VISIBLE) {
                        mAllCardsMatched = false;
                        break;
                    }
                }

                if (mAllCardsMatched) {
                    Toast.makeText(this, "Você Conseguiu parabéns!!", Toast.LENGTH_SHORT).show();
                }

                // Reset
                mFirstCard = -1;
                mSecondCard = -1;
                mIsClickable = true;

            } else {

                mIsClickable = false;
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCards[mFirstCard].setImageResource(R.drawable.carta0);
                        mCards[mSecondCard].setImageResource(R.drawable.carta0);
                        mFirstCard = -1;
                        mSecondCard = -1;
                        mIsClickable = true;
                    }
                }, 1000);
            }
        }

    }

}
