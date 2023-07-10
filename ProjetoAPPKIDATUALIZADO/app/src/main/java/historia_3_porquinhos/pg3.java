package historia_3_porquinhos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ftec.kidapp.R;

public class pg3 extends AppCompatActivity {
    private Button btnProximo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_pg3);
        btnProximo = findViewById(R.id.btnCapa);
        btnProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pg3.this, pg4.class);
                startActivity(intent);
            }

        });
    }
}