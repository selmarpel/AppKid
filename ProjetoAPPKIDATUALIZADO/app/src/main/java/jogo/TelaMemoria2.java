package jogo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;

import com.ftec.kidapp.Activity.RankingAdapter;
import com.ftec.kidapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import db.DataBaseHelper_2;
import db.RankingDBManager_2;

public class TelaMemoria2 extends AppCompatActivity implements View.OnClickListener {
    private SoundPool soundPool;
    private int soundId;
    private boolean isSoundLoaded = false;
    private final float volume = 0.3f;
    private final ImageView[] mCards = new ImageView[12];
    private final int[] mImages = {
            R.drawable.carta1,
            R.drawable.carta1,
            R.drawable.carta2,
            R.drawable.carta2,
            R.drawable.carta3,
            R.drawable.carta3,
            R.drawable.carta4,
            R.drawable.carta4,
            R.drawable.carta5,
            R.drawable.carta5,
            R.drawable.carta6,
            R.drawable.carta6,
    };
    private RankingDBManager_2 rankingDBManager2;
    private int mFirstCard = -1;
    private int mSecondCard = -1;
    private boolean mAllCardsMatched = false;
    private boolean mIsClickable = true;
    private DataBaseHelper_2 mDbHelper;
    private long mStartTime = 0;
    private final Handler handler = new Handler();
    private TextView timerView;
    private TextView timerTextView;
    private RecyclerView rankingRecyclerView;
    private RankingAdapter rankingAdapter;
    private List<String> rankingList;
    private String completedTime = "";
    private List<String> rankingTimes;
    private static final long MAX_TIME = 20000 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_memoria2);
        timerView = findViewById(R.id.timer2);
        mStartTime = System.currentTimeMillis();
        rankingRecyclerView = findViewById(R.id.rankingRecyclerView2);
        rankingList = new ArrayList<>();
        rankingAdapter = new RankingAdapter(rankingList);
        rankingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rankingRecyclerView.setAdapter(rankingAdapter);
        mDbHelper = new DataBaseHelper_2(this);
        rankingDBManager2 = new RankingDBManager_2(this);
        rankingTimes = new ArrayList<>();
        startTimer();
        setupSoundPool();
        loadSound();

        mCards[0] = findViewById(R.id.card1);
        mCards[1] = findViewById(R.id.card2);
        mCards[2] = findViewById(R.id.card3);
        mCards[3] = findViewById(R.id.card4);
        mCards[4] = findViewById(R.id.card5);
        mCards[5] = findViewById(R.id.card6);
        mCards[6] = findViewById(R.id.card7);
        mCards[7] = findViewById(R.id.card8);
        mCards[8] = findViewById(R.id.card9);
        mCards[9] = findViewById(R.id.card10);
        mCards[10] = findViewById(R.id.card11);
        mCards[11] = findViewById(R.id.card12);

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


        mDbHelper = new DataBaseHelper_2(this);
    }
    private void updateRanking(long elapsedTime) {
        String formattedTime = formatTime(elapsedTime);
        rankingList.add(formattedTime);

        // Obter os dados atualizados do ranking
        saveTimeToRanking(rankingList);
        rankingList = getRankingData();

        // Se a lista rankingData tiver mais de 5 elementos, remove o tempo mais longo
        if (rankingList.size() > 5) {
            rankingList.remove(rankingList.size() - 1);
        }

        // Notifica o adaptador sobre a alteração nos dados
        rankingAdapter.setRankingList(rankingList);
    }

    private List<String> getRankingData() {
        List<String> data = new ArrayList<>();
        data.add(completedTime);
        // Adicione mais tempos conforme necessário
        return data;
    }
    private String formatTime(long elapsedTime) {
        int seconds = (int) (elapsedTime / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
    private void saveTimeToRanking(List<String> times) {
        for (String time : times) {
            String[] components = time.split(":");
            int minutes = Integer.parseInt(components[0]);
            int seconds = Integer.parseInt(components[1]);
            double Time = minutes * 60 + seconds;
            rankingDBManager2.adicionarPontuacao(Time);
        }
    }

    private void startTimer() {
        final TextView timerView = findViewById(R.id.timer2);
        mStartTime = System.currentTimeMillis(); // definir o tempo de início do cronômetro
        handler.post(new Runnable() {
            @Override
            public void run() {
                long millis = System.currentTimeMillis() - mStartTime;
                int seconds = (int) (millis / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;

                timerView.setText(String.format("%d:%02d", minutes, seconds));
                if (millis >= MAX_TIME) {
                    // Inicia a contagem regressiva de 5 segundos antes de fechar o aplicativo
                    new CountDownTimer(5000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            Snackbar.make(findViewById(R.id.timer2), "O aplicativo será fechado em " + millisUntilFinished / 1000 + " segundos.", Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFinish() {
                            finishAffinity(); // Fecha a activity
                        }
                    }.start();
                } else {
                    handler.postDelayed(this, 500);
                }
            }
        });
    }


    private void salvarEstadoDoJogo() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + DataBaseHelper_2.MEMO_TABLE_2);

        for (int i = 0; i < mImages.length; i++) {
            boolean cartaVirada = mCards[i].getVisibility() != View.VISIBLE;
            salvarPosicaoImagem(db, i, mImages[i], cartaVirada);
        }

        db.close();
    }


    private void salvarPosicaoImagem(SQLiteDatabase db, int posicao, int imagem, boolean virada) {
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper_2.COLUNA_POSICAO_2, posicao);
        values.put(DataBaseHelper_2.COLUNA_IMAGEM_2, imagem);
        values.put(DataBaseHelper_2.COLUNA_VIRADA_2, virada ? 0 : 1); // 0 para false (virada) e 1 para true (não virada)
        db.insert(DataBaseHelper_2.MEMO_TABLE_2, null, values);
    }

    private void carregarEstadoDoJogo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                DataBaseHelper_2.COLUNA_IMAGEM_2,
                DataBaseHelper_2.COLUNA_POSICAO_2,
                DataBaseHelper_2.COLUNA_VIRADA_2
        };
        Cursor cursor = db.query(
                DataBaseHelper_2.MEMO_TABLE_2,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int position = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper_2.COLUNA_POSICAO_2));
                int imageId = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper_2.COLUNA_IMAGEM_2));
                boolean isCardFaceUp = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseHelper_2.COLUNA_VIRADA_2)) == 1;
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
        mStartTime = System.currentTimeMillis();
        startTimer();
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
    public void onClick(View v) {
        if (!mIsClickable) {
            return;
        }

        int position = (int) v.getTag();
        if (position == mFirstCard) {
            return;
        }

        ImageView card = mCards[position];
        card.setImageResource(mImages[position]);

        if (mFirstCard == -1) {
            mFirstCard = position;
        } else {
            mSecondCard = position;
            mIsClickable = false;

            if (mImages[mFirstCard] == mImages[mSecondCard]) {
                AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(this, R.anim.anim_card_stack);
                mCards[mFirstCard].startAnimation(animationSet);
                card.startAnimation(animationSet);
                mCards[mFirstCard].setVisibility(View.INVISIBLE);
                mCards[mSecondCard].setVisibility(View.INVISIBLE);
                checkAllCardsMatched();
                mFirstCard = -1;
                mSecondCard = -1;
                mIsClickable = true;
            } else {
                mCards[mFirstCard].postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCards[mFirstCard].setImageResource(R.drawable.carta0);
                        mCards[mSecondCard].setImageResource(R.drawable.carta0);
                        mFirstCard = -1;
                        mSecondCard = -1;
                        mIsClickable = true;
                    }
                }, 800);
            }
        }
    }

    private void checkAllCardsMatched() {
        for (ImageView card : mCards) {
            if (card.getVisibility() == View.VISIBLE) {
                return;
            }
        }
        mAllCardsMatched = true;

        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(rootView, "Parabéns! Você ganhou o jogo!", Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        snackbar.show();

        long elapsedTime = System.currentTimeMillis() - mStartTime;
        String formattedTime = formatTime(elapsedTime);

        if (completedTime.isEmpty() || formattedTime.compareTo(completedTime) < 0) {
            completedTime = formattedTime;
            rankingTimes.add(formattedTime); // Adicione o tempo atual à lista
            updateRanking(elapsedTime);
        }

        pauseTimer();
    }
    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
        if (isSoundLoaded) {
            playSound();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null); // Para o contador ao pausar a activity
        salvarEstadoDoJogo();
        stopSound();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
        releaseSoundPool();
    }
    private void pauseTimer() {
        handler.removeCallbacksAndMessages(null);
        mStartTime = System.currentTimeMillis() - mStartTime;
    }

    private void setupSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(attributes)
                .build();
    }

    private void loadSound() {
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status == 0) {
                    isSoundLoaded = true;
                    soundId = sampleId;
                    playSound();
                }
            }
        });

        // Carregue seu arquivo de som aqui
        soundId = soundPool.load(this, R.raw.som2, 1);
    }

    private void playSound() {
        if (isSoundLoaded) {
            soundPool.play(soundId, volume, volume, 1, -1, 1.2f);
        }
    }

    private void stopSound() {
        if (isSoundLoaded) {
            soundPool.stop(soundId);
        }
    }

    private void releaseSoundPool() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}

