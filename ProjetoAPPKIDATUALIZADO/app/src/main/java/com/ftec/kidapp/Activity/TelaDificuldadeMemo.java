package com.ftec.kidapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ftec.kidapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class TelaDificuldadeMemo extends AppCompatActivity {

    Button btnMemo1, btnMemo2, btnMemo3, btnAuto;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int idade;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_dificuldade_memo);

        // Pega o usuário logado no momento
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            userId = mAuth.getCurrentUser().getUid();
        }

        btnMemo1 = findViewById(R.id.btnFacil);
        btnMemo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaDificuldadeMemo.this, TelaMemoria1.class);
                startActivity(intent);
            }
        });

        btnMemo2 = findViewById(R.id.btnMedio);
        btnMemo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(TelaDificuldadeMemo.this, TelaMemoria2.class);
                startActivity(intent2);
            }
        });

        btnMemo3 = findViewById(R.id.btnDificil);
        btnMemo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error){
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            idade = Integer.parseInt(documentSnapshot.getString("idade"));
                            if (idade <3) {
                                Intent intent1 = new Intent(TelaDificuldadeMemo.this, TelaMemoria1.class);
                                startActivity(intent1);
                            }else if (idade >=3 && idade < 7 ){
                                Intent intent2 = new Intent(TelaDificuldadeMemo.this, TelaMemoria2.class);
                                startActivity(intent2);
                            }else {
                                Intent intent3 = new Intent(TelaDificuldadeMemo.this, TelaMemoria3.class);
                                startActivity(intent3);
                            }
                        }
                    }
                });
            }
        });
        Button btnVoltar = findViewById(R.id.btn_voltar2);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
