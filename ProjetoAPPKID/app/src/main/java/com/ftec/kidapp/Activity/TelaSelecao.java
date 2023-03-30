package com.ftec.kidapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.ftec.kidapp.R;
import com.google.android.material.snackbar.Snackbar;

public class TelaSelecao extends AppCompatActivity {
    private long startTime = 0;
    private Handler handler = new Handler();
    private TextView timerTextView;
    private static final long MAX_TIME = 30 * 1000; // tempo máximo em milissegundos
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            timerTextView.setText(String.format("%d:%02d", minutes, seconds));
            if (millis >= MAX_TIME) {
                // Inicia a contagem regressiva de 5 segundos antes de fechar o aplicativo
                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        Snackbar.make(findViewById(R.id.timer), "O aplicativo será fechado em " + millisUntilFinished / 1000 + " segundos.", Snackbar.LENGTH_SHORT).show();
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_selecao);
        timerTextView = findViewById(R.id.timer);

        // Inicia o cronômetro ao abrir o aplicativo
        startTime = System.currentTimeMillis();
        handler.postDelayed(timerRunnable, 0);
    }

    public void stopTimer(View view) {
        handler.removeCallbacks(timerRunnable);

    }
}