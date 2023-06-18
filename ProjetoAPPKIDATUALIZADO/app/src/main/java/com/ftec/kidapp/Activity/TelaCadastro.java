package com.ftec.kidapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ftec.kidapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TelaCadastro extends AppCompatActivity {

    private EditText edt_email, edt_senha, edt_idade, edt_nome;
    private Button btn_cadastrar, btn_voltar;
    String[]mensagens = {"Preencha todos os campos","Cadastro realizado com sucesso"};
    String usuarioID;
    private SoundPool soundPool;
    private int soundId;
    private boolean isSoundLoaded = false;
    private final float volume = 0.3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro);
        getSupportActionBar().hide();
        IniciarComponentes();
        setupSoundPool();
        loadSound();

        //Botão Voltar
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaCadastro.this, TelaLogin.class);
                startActivity(intent);
            }
        });
        //Botao cadastro
        btn_cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText(). toString();
                String senha = edt_senha.getText().toString();
                String idade = edt_idade.getText().toString();


                if (email.isEmpty()|| senha.isEmpty()|| idade.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v,mensagens[0],Snackbar.LENGTH_LONG);
                    snackbar.setBackgroundTint(Color.BLUE);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                }else{
                    CadastrarUsuario(v);
                }
            }
        });

    }



    private void CadastrarUsuario(View v){
                String email = edt_email.getText(). toString();
                String senha = edt_senha.getText().toString();
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            SalvarDadosUsuario();

                            Snackbar snackbar = Snackbar.make(v,mensagens[1],Snackbar.LENGTH_LONG);
                            snackbar.setBackgroundTint(Color.BLUE);
                            snackbar.setTextColor(Color.WHITE);
                            snackbar.show();
                        }else{
                            String erro;
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e) {
                                erro = "A senha precisa ter minimo de 6 digitos";
                            }catch (FirebaseAuthUserCollisionException e) {
                                erro = "Email já cadastrado";
                            }catch (FirebaseAuthInvalidCredentialsException e) {
                                erro = "Email Inválido";
                            }catch (Exception e) {
                                erro = "Erro ao cadastrar usuário";
                            }
                            Snackbar snackbar = Snackbar.make(v,erro,Snackbar.LENGTH_LONG);
                                snackbar.setBackgroundTint(Color.BLUE);
                                snackbar.setTextColor(Color.WHITE);
                                snackbar.show();
                            }

                        }


                });
            }
    private void SalvarDadosUsuario(){
        String idade = edt_idade.getText().toString();
        String nome = edt_nome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("nome",nome);
        usuarios.put("idade",idade);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db","Sucesso ao salvar os dados");
            }
        });

    }
    private void IniciarComponentes(){

        edt_email = findViewById(R.id.edt_novoEmail);
        edt_senha = findViewById(R.id.edt_novaSenha);
        edt_idade = findViewById(R.id.edt_novaIdade);
        edt_nome = findViewById(R.id.edt_Nome);
        btn_cadastrar =findViewById(R.id.btn_cadastrar);
        btn_voltar = findViewById(R.id.btn_voltar);
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

        // Carregue seu arquivo de som aqui
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
