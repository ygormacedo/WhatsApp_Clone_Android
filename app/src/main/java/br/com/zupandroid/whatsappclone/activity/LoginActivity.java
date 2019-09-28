package br.com.zupandroid.whatsappclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.zupandroid.whatsappclone.R;
import br.com.zupandroid.whatsappclone.config.ConfiguracaoFirebase;
import br.com.zupandroid.whatsappclone.helper.Base64Custom;
import br.com.zupandroid.whatsappclone.helper.Preferencias;
import br.com.zupandroid.whatsappclone.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference referenciaFirebase;
    private Button botaoLogar;
    private EditText email;
    private EditText password;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private ValueEventListener valueEventListenerUsuario;
    private DatabaseReference firebaseReference;
    private String indentificadoUsuarioLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUser();
        setLogin();
    }

    private void verificarUser() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            abriTelaPrincipal();
        }

    }


    private void setLogin() {
        email = findViewById(R.id.emailIdLogin);
        password = findViewById(R.id.passwordIdLogin);

        botaoLogar = findViewById(R.id.idButtonLogin);
        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setEmail(email.getText().toString());
                usuario.setPassword(password.getText().toString());

                validarLogin();

            }
        });

    }

    private void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getPassword()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    indentificadoUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());

                    firebaseReference = ConfiguracaoFirebase.getFirebase()
                            .child("usuarios")
                            .child(indentificadoUsuarioLogado);

                    valueEventListenerUsuario = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Usuario usuarioRecuperado = dataSnapshot.getValue( Usuario.class);

                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                            preferencias.salvarDados(indentificadoUsuarioLogado, usuarioRecuperado.getName());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    firebaseReference.addListenerForSingleValueEvent(valueEventListenerUsuario);


                    abriTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso ao efetuar Login !", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Erro ao efetuar Login !", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void abriTelaPrincipal() {

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abriCadastroUsuario(View view) {
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }
}
