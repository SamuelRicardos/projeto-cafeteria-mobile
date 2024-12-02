package com.example.appfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class TelaPrincipal extends AppCompatActivity {

    private Button bt_deslogar;
    private TextView nomeUsuario, emailUsuario;
    protected String usuarioId;
    protected FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_principal);

        deslogar();
        this.iniciarComponentes();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }// fim onCreate

    protected void onStart() {
        super.onStart();

        // Verificar se o usuário está autenticado
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user != null) {
            usuarioId = user.getUid();
            String email = user.getEmail();
            DocumentReference obj = db.collection("Usuarios").document(usuarioId);

            obj.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (value != null) {
                        nomeUsuario.setText(value.getString("nome"));
                        emailUsuario.setText(email);
                    }
                }
            });
        } else {
            // O usuário não está autenticado, redirecionar para a tela de login
            Intent intent = new Intent(TelaPrincipal.this, FormLogin.class);
            startActivity(intent);
            finish(); // Finaliza a tela principal
        }
    }// fim onStart

    private void iniciarComponentes() {
        nomeUsuario = findViewById(R.id.TextViewNomeUsuarioPrincipal);
        emailUsuario = findViewById(R.id.TextViewEmailUsuarioPrincipal);
        db = FirebaseFirestore.getInstance();
    }// fim iniciarComponentes

    private void deslogar(){
        bt_deslogar = findViewById(R.id.buttonDeslogar);
        bt_deslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent objIntent = new Intent(TelaPrincipal.this, FormLogin.class);
                startActivity(objIntent);
                finish();
            }
        });
    }// fim deslogar


}// fim classe