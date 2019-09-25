package br.com.zupandroid.whatsappclone.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;


import br.com.zupandroid.whatsappclone.R;
import br.com.zupandroid.whatsappclone.config.ConfiguracaoFirebase;
import br.com.zupandroid.whatsappclone.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference referenciaFirebase;
    private Button botaoLogar;
    private EditText email;
    private EditText password;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        verificarUser();
        setLogin();
    }

    private void verificarUser(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
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
                    abriTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso ao efetuar Login !", Toast.LENGTH_LONG).show();
                } else {
                    String erroExecucao = "";
                    try {
                        throw task.getException();

                    } catch (FirebaseAuthInvalidUserException e) {
                        erroExecucao = "Erro a conta de usuário correspondente ao email não existe ou foi desativada ";
                        e.printStackTrace();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExecucao = "Sua senha esta errada";
                        e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this, "Erro ao efetuar Login !", Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    private void abriTelaPrincipal(){

        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void abriCadastroUsuario(View view) {
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity(intent);
    }
}
