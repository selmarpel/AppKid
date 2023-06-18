package historia_3_porquinhos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ftec.kidapp.Activity.TelaCadastro;
import com.ftec.kidapp.Activity.TelaLogin;
import com.ftec.kidapp.R;

public class capa extends AppCompatActivity {
    private Button btnProximo;
    private SoundPool soundPool;
    private int soundId;
    private boolean isSoundLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_capa);

        btnProximo = findViewById(R.id.btnCapa);
        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(capa.this, pg1.class);
                startActivity(intent);
                stopSound();
            }

            private void stopSound() {
                if (isSoundLoaded) {
                    soundPool.stop(soundId);
                }
            }

        });

    }
}