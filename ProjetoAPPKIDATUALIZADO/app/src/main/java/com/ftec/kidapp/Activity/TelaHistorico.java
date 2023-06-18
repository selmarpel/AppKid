package com.ftec.kidapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ftec.kidapp.R;

import java.util.ArrayList;
import java.util.List;

public class TelaHistorico extends AppCompatActivity {

    private RecyclerView recyclerViewHistory;
    private HistoricoDBHelper dbHelper;
    private HistoricoAdapter historicoAdapter;
    private TextView tvEmptyMessage;
    private Button btn_Voltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_historico);

        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        dbHelper = new HistoricoDBHelper(this);


        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));

        // Recupera os dados do banco de dados
        Cursor cursor = dbHelper.getAccessHistory();
        List<HistoricoItem> historicoItems = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            int buttonNameIndex = cursor.getColumnIndexOrThrow("button_name");
            int dateTimeIndex = cursor.getColumnIndexOrThrow("date_time");

            int maxItems = 7;

            int itemCount = 0;
            while (cursor.moveToNext() && itemCount < maxItems) {
                String buttonName = cursor.getString(buttonNameIndex);
                String dateTime = cursor.getString(dateTimeIndex);

                HistoricoItem historicoItem = new HistoricoItem(buttonName, dateTime);
                historicoItems.add(historicoItem);

                itemCount++;
            }
        }

        if (cursor != null) {
            cursor.close();
        }


        historicoAdapter = new HistoricoAdapter(historicoItems);
        recyclerViewHistory.setAdapter(historicoAdapter);

        if (historicoItems.isEmpty()) {
            recyclerViewHistory.setVisibility(View.GONE);
            tvEmptyMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerViewHistory.setVisibility(View.VISIBLE);
            tvEmptyMessage.setVisibility(View.GONE);
        }
        btn_Voltar = findViewById(R.id.btn_voltar4);
        btn_Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaHistorico.this, TelaSelecao.class);
                startActivity(intent);
            }
        });
    }

}
