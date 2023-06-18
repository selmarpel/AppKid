package com.ftec.kidapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import com.ftec.kidapp.Activity.HistoricoDBHelper;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.ftec.kidapp.R;
import com.google.firebase.auth.FirebaseAuth;

import historia_3_porquinhos.capa;

public class TelaSelecao extends AppCompatActivity {

    private SoundPool soundPool;
    private int soundId;
    private boolean isSoundLoaded = false;
    private final float volume = 0.3f;

    private Button btnJogoMemo;
    private Button btnHistoria;
    private Button btnHistorico;
    private Button btnSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_selecao);
        setupSoundPool();
        loadSound();

        btnJogoMemo = findViewById(R.id.btnjogoMemoria);
        btnJogoMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoricoDBHelper databaseHelper = new HistoricoDBHelper(TelaSelecao.this);
                databaseHelper.saveAccessHistory("btnJogoMemo");

                Intent intent = new Intent(TelaSelecao.this, TelaDificuldadeMemo.class);
                startActivity(intent);
            }
        });

        btnHistoria = findViewById(R.id.btnhistoria);
        btnHistoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoricoDBHelper databaseHelper = new HistoricoDBHelper(TelaSelecao.this);
                databaseHelper.saveAccessHistory("btnHistoria");

                Intent intent = new Intent(TelaSelecao.this, capa.class);
                startActivity(intent);
            }
        });

        btnHistorico = findViewById(R.id.btnhistorico);
        btnHistorico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaSelecao.this, TelaHistorico.class);
                startActivity(intent);
            }
        });
        btnSair = findViewById(R.id.btnSair);
        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(TelaSelecao.this, TelaLogin.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent2);
            }
        });
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
