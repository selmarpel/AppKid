package com.ftec.kidapp.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ftec.kidapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import jogo.TelaMemoria1;
import jogo.TelaMemoria2;
import jogo.TelaMemoria3;

public class TelaDificuldadeMemo extends AppCompatActivity {

    Button btnMemo1, btnMemo2, btnMemo3, btnAuto, btnRanking, btnVoltar;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int idade;
    String userId;
    private SoundPool soundPool;
    private int soundId;
    private int streamId = -1; // Variável para armazenar o stream do som reproduzido
    private boolean isSoundLoaded = false;
    private final float volume = 0.3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_dificuldade_memo);
        setupSoundPool();
        loadSound();

        // Pega o usuário logado no momento
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        }


        btnRanking = findViewById(R.id.btn_ranking);
        btnRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSound();
                Intent intent = new Intent(TelaDificuldadeMemo.this, TelaRanking.class);
                startActivity(intent);
            }
        });


        btnMemo1 = findViewById(R.id.btnFacil);
        btnMemo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSound(); // Pausa o som antes de iniciar a nova atividade
                Intent intent = new Intent(TelaDificuldadeMemo.this, TelaMemoria1.class);
                startActivity(intent);
            }
        });

        btnMemo2 = findViewById(R.id.btnMedio);
        btnMemo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSound(); // Pausa o som antes de iniciar a nova atividade
                Intent intent2 = new Intent(TelaDificuldadeMemo.this, TelaMemoria2.class);
                startActivity(intent2);
            }
        });

        btnMemo3 = findViewById(R.id.btnDificil);
        btnMemo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSound(); // Pausa o som antes de iniciar a nova atividade
                Intent intent3 = new Intent(TelaDificuldadeMemo.this, TelaMemoria3.class);
                startActivity(intent3);
            }
        });

        btnAuto = findViewById(R.id.btn_auto);
        btnAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pega a idade do usuário logado no momento
                DocumentReference documentReference = db.collection("Usuarios").document(userId);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            idade = Integer.parseInt(documentSnapshot.getString("idade"));
                            if (idade < 3) {
                                stopSound(); // Pausa o som antes de iniciar a nova atividade
                                Intent intent1 = new Intent(TelaDificuldadeMemo.this, TelaMemoria1.class);
                                startActivity(intent1);
                            } else if (idade >= 3 && idade < 7) {
                                stopSound(); // Pausa o som antes de iniciar a nova atividade
                                Intent intent2 = new Intent(TelaDificuldadeMemo.this, TelaMemoria2.class);
                                startActivity(intent2);
                            } else {
                                stopSound(); // Pausa o som antes de iniciar a nova atividade
                                Intent intent3 = new Intent(TelaDificuldadeMemo.this, TelaMemoria3.class);
                                startActivity(intent3);
                            }
                        }
                    }
                });
            }
        });

        btnVoltar = findViewById(R.id.btn_voltar2);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaDificuldadeMemo.this, TelaSelecao.class);
                startActivity(intent);
            }
        });







    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSoundLoaded) {
            stopSound(); // Pausa o som antes de reiniciá-lo
            playSound();
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

        // Carregue seu arquivo de som aqui
        soundId = soundPool.load(this, R.raw.som1, 1);
    }

    private void playSound() {
        if (isSoundLoaded) {
            streamId = soundPool.play(soundId, volume, volume, 1, -1, 1.0f);
        }
    }

    private void stopSound() {
        if (isSoundLoaded && streamId != -1) {
            soundPool.stop(streamId);
        }
    }

    private void releaseSoundPool() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
