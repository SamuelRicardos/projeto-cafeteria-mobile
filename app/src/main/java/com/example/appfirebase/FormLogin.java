package com.example.appfirebase;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FormLogin extends AppCompatActivity {

    private TextView textTelaCadastro, textTelaRecuperarConta;
    private EditText edit_email, edit_senha;
    private ProgressBar progressBar;
    private AppCompatButton bt_entrar;
    private String [] msgs = {"Preencha todos os campos.", "Login realizado com sucesso."};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_login);
        this.iniciarComponentes();
        this.chamaTelaCadastro();
        this.chamarTelaRecuperarConta();
        this.login(); // regra de negocio para autenticar o usuario

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    } // fim onCreate

    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioCorrente = FirebaseAuth.getInstance().getCurrentUser();
        if(usuarioCorrente != null){
            chamarTelaPrincipal();
        }// fim IF
    }// fim onStart

    // metodo capturar e tratar o evento de logar
    public void login() {
        bt_entrar.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             String email = edit_email.getText().toString();
                                             String senha = edit_senha.getText().toString();
                                             if(email.isEmpty() || senha.isEmpty()){// esta vazio
                                                 Snackbar objSnacbar = Snackbar.make(view, msgs[0], Snackbar.LENGTH_SHORT);
                                                 objSnacbar.setBackgroundTint(Color.WHITE);
                                                 objSnacbar.setTextColor(Color.BLACK);
                                                 objSnacbar.show();
                                             }// fim IF esta vazio
                                             else {// ambos email e senha foram preenchidos
                                                    autenticarUsuario(view, email, senha);
                                             }// fim else
                                         }// fim onClick
                                     }// fim View.OnClickListener()

        );
    }// fim login

    public void autenticarUsuario(View view, String email, String senha) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener(
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            progressBar.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(new Runnable() {
                                                          @Override
                                                          public void run() {
                                                              chamarTelaPrincipal();
                                                          }
                                                      }, 3000);
                        }// fim if
                        else{ // deu erro no momento da autenticao
                            String erro = "";
                            try {
                                throw task.getException();
                            }// fim try
                            catch (Exception e){
                                erro = "Erro ao logar no app!";
                                Snackbar objSnacbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                                objSnacbar.setBackgroundTint(Color.WHITE);
                                objSnacbar.setTextColor(Color.BLACK);
                                objSnacbar.show();
                            }// fim catch
                        }// fim else
                    }// fim onComplete
                }// fim OnCompleteListener<AuthResult>
        );// fim addOnCompleteListener
    }// fim autenticarUsuario

    public void chamarTelaPrincipal() {
        Intent intent = new Intent(FormLogin.this, TelaPrincipal.class);
        startActivity(intent);
        finish();
    }//fim chamarTelaPrincipal

    public void chamarTelaRecuperarConta() {
        textTelaRecuperarConta= findViewById(R.id.textViewRecuperarLogin);
        textTelaRecuperarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormLogin.this, RecuperarConta.class);
                startActivity(intent);
                finish();
            }
        });
    }//fim chamarTelaRecuperarConta

    public void chamaTelaCadastro() {
        textTelaCadastro = findViewById(R.id.textViewCadastrarLogin);
        textTelaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
                finish();
            }
        });
    }// fim chamaTelaCadastro

    private void iniciarComponentes() {
        edit_email= findViewById(R.id.editTextEmailLogin);
        edit_senha= findViewById(R.id.editTextSenhaLogin);
        bt_entrar= findViewById(R.id.bt_logar);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE); // Para ocultar a ProgressBar

    }// iniciarComponentes



}// fim classe