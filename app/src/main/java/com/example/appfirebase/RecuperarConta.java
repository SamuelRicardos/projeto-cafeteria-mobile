package com.example.appfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecuperarConta extends AppCompatActivity {

    private EditText recuperarEmail;
    private AppCompatButton bt_recuperar;
    private ImageButton bt_back;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar_conta);
        this.iniciarComponentes();
        this.voltarTelaLogin();
        this.coreRecuperarConta();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }// fim onCreate

    private void coreRecuperarConta() {
        bt_recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperarSenha();
            }
        });
    }// fim coreRecuperarConta

    private void recuperarSenha() {
        String email = recuperarEmail.getText().toString().trim();
        auth = FirebaseAuth.getInstance();

        if (!email.isEmpty()) {
            if (isValidEmail(email)) {
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RecuperarConta.this, "Enviado com sucesso!", Toast.LENGTH_SHORT).show();
                        }
                    }// fim onComplete
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        trataErro(e.toString());
                    }
                });
            } else {
                Toast.makeText(RecuperarConta.this, "Email inválido!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(RecuperarConta.this, "Insira um email!", Toast.LENGTH_SHORT).show();
        }
    }// fim recuperarSenha

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void trataErro(String erro) {
        if (erro.contains("address is badly")) {
            Toast.makeText(RecuperarConta.this, "Email inválido!", Toast.LENGTH_SHORT).show();
        } else if (erro.contains("there is no user")) {
            Toast.makeText(RecuperarConta.this, "Email não cadastrado!", Toast.LENGTH_SHORT).show();
        } else if (erro.contains("INVALID_EMAIL")) {
            Toast.makeText(RecuperarConta.this, "Email Inválido!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(RecuperarConta.this, erro, Toast.LENGTH_SHORT).show();
        }
    }// fim trataErro

    private void iniciarComponentes() {
        recuperarEmail = findViewById(R.id.editTextEmailRecuperarConta);
        bt_recuperar = findViewById(R.id.bt_recuperar);
        bt_back = findViewById(R.id.ButtonBackRecuperarConta);
    }// fim iniciarComponentes

    private void voltarTelaLogin() {
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecuperarConta.this, FormLogin.class);
                startActivity(intent);
                finish();
            }
        });
    }

}// fim classe
