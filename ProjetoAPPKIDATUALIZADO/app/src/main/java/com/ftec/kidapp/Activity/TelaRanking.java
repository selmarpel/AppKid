package com.ftec.kidapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ftec.kidapp.Activity.RankingAdapter;
import com.ftec.kidapp.R;

import java.util.ArrayList;
import java.util.List;

import db.RankingDBManager_1;
import db.RankingDBManager_2;
import db.RankingDBManager_3;

public class TelaRanking extends AppCompatActivity {

    private RankingAdapter rankingFacilAdapter;
    private RankingAdapter rankingMedioAdapter;
    private RankingAdapter rankingDificilAdapter;
    private List<String> rankingFacilData; // Lista de tempos do ranking fácil
    private List<String> rankingMedioData;
    private List<String> rankingDificilData;// Lista de tempos do ranking médio
    private RankingDBManager_1 rankingDBManager1; // Gerenciador do banco de dados para o ranking fácil
    private RankingDBManager_2 rankingDBManager2; // Gerenciador do banco de dados para o ranking médio
    private RankingDBManager_3 rankingDBManager3;

    private TextView textViewFacil;
    private TextView textViewMedio;
    private TextView textViewDificil;
    private Button btnZerar, btnVoltar;
    private SoundPool soundPool;
    private int soundId;
    private boolean isSoundLoaded = false;
    private final float volume = 0.3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_ranking);
        setupSoundPool();
        loadSound();

        textViewFacil = findViewById(R.id.txtTempoTotalFacil);
        textViewMedio = findViewById(R.id.txtTempoTotalMedio);
        textViewDificil = findViewById(R.id.txtTempoTotalDificil);

        // Inicializar os gerenciadores do banco de dados
        rankingDBManager1 = new RankingDBManager_1(this);
        rankingDBManager2 = new RankingDBManager_2(this);
        rankingDBManager3 = new RankingDBManager_3(this);

        // Obter os dados dos rankings do banco de dados
        rankingFacilData = getRankingDataFromDatabase(rankingDBManager1);
        rankingMedioData = getRankingDataFromDatabase(rankingDBManager2);
        rankingDificilData = getRankingDataFromDatabase(rankingDBManager3);


        btnVoltar = findViewById(R.id.btn_voltar3);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaRanking.this, TelaDificuldadeMemo.class);
                startActivity(intent);
            }
        });

        mostrarTempoTotal();
    }
    private String getFormattedRankingData(List<String> rankingData) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < rankingData.size(); i++) {
            String time = rankingData.get(i);
            stringBuilder.append((i + 1)).append(". ").append(time).append("\n");
        }

        return stringBuilder.toString();
    }

    private void mostrarTempoTotal() {
        int tempoTotalFacil = rankingDBManager1.obterTempoTotalJogado();
        int tempoTotalMedio = rankingDBManager2.obterTempoTotalJogado();
        int tempoTotalDificil = rankingDBManager3.obterTempoTotalJogado();


        String textoFacil = "Ranking Fácil\n" + getFormattedRankingData(rankingFacilData) + "\nTempo Total: " + formatTime(tempoTotalFacil);
        String textoMedio = "Ranking Médio\n" + getFormattedRankingData(rankingMedioData) + "\nTempo Total: " + formatTime(tempoTotalMedio);
        String textoDificil = "Ranking Difícil\n" + getFormattedRankingData(rankingDificilData) + "\nTempo Total: " + formatTime(tempoTotalDificil);

        textViewFacil.setText(textoFacil);
        textViewMedio.setText(textoMedio);
        textViewDificil.setText(textoDificil);
    }

    private List<String> getRankingDataFromDatabase(Object rankingDBManager) {
        List<String> rankingData = new ArrayList<>(3);

        if (rankingDBManager instanceof RankingDBManager_1) {
            RankingDBManager_1 dbManager = (RankingDBManager_1) rankingDBManager;
            Cursor cursor = dbManager.obterMelhoresPontuacoes();

            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow("tempo");
                int count = 0;
                do {
                    double tempo = cursor.getDouble(columnIndex);
                    String formattedTime = formatTime(tempo);
                    rankingData.add(formattedTime);
                    count++;
                } while (cursor.moveToNext() && count < 3);
            }

            cursor.close();
        } else if (rankingDBManager instanceof RankingDBManager_2) {
            RankingDBManager_2 dbManager = (RankingDBManager_2) rankingDBManager;
            Cursor cursor = dbManager.obterMelhoresPontuacoes();

            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow("tempo");
                int count = 0;
                do {
                    double tempo = cursor.getDouble(columnIndex);
                    String formattedTime = formatTime(tempo);
                    rankingData.add(formattedTime);
                    count++;
                } while (cursor.moveToNext() && count < 3);
            }
            cursor.close();

        } else if (rankingDBManager instanceof RankingDBManager_3) {
            RankingDBManager_3 dbManager = (RankingDBManager_3) rankingDBManager;
            Cursor cursor = dbManager.obterMelhoresPontuacoes();

            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow("tempo");
                int count = 0;
                do {
                    double tempo = cursor.getDouble(columnIndex);
                    String formattedTime = formatTime(tempo);
                    rankingData.add(formattedTime);
                    count++;
                } while (cursor.moveToNext() && count < 3);
            }
            cursor.close();
        }

        return rankingData;
    }

    private String formatTime(double tempo) {
        int segundos = (int) tempo;
        return String.format("%d segundos", segundos);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (isSoundLoaded) {
            soundPool.play(soundId, volume, volume, 1, -1, 1.0f);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSound();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseSoundPool();
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

        soundId = soundPool.load(this, R.raw.som1, 1);
    }

    private void playSound() {
        if (isSoundLoaded) {
            soundPool.play(soundId, volume, volume, 1, -1, 1.0f);
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



