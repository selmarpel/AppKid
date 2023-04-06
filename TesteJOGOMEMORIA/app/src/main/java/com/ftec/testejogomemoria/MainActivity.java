package com.ftec.testejogomemoria;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView[] mCards = new ImageView[6];
    private int[] mImages = { R.drawable.carta3, R.drawable.carta1, R.drawable.carta2, R.drawable.carta1, R.drawable.carta2, R.drawable.carta3 };
    private int mFirstCard = -1;
    private int mSecondCard = -1;
    private boolean mAllCardsMatched = false;
    private boolean mIsClickable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCards[0] = findViewById(R.id.card1);
        mCards[1] = findViewById(R.id.card2);
        mCards[2] = findViewById(R.id.card3);
        mCards[3] = findViewById(R.id.card4);
        mCards[4] = findViewById(R.id.card5);
        mCards[5] = findViewById(R.id.card6);

        shuffleImages();

        for (int i = 0; i < mCards.length; i++) {
            mCards[i].setTag(i);
            mCards[i].setOnClickListener(this);
        }

        Button button = findViewById(R.id.new_game_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });
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
                Toast.makeText(this, "Match!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Game over!", Toast.LENGTH_SHORT).show();
                }

                // Reset
                mFirstCard = -1;
                mSecondCard = -1;
                mIsClickable = true;
            } else {
                Toast.makeText(this, "No match!", Toast.LENGTH_SHORT).show();
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

        }}}