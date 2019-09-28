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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.com.zupandroid.whatsappclone.R;
import br.com.zupandroid.whatsappclone.config.ConfiguracaoFirebase;
import br.com.zupandroid.whatsappclone.helper.Base64Custom;
import br.com.zupandroid.whatsappclone.helper.Preferencias;
import br.com.zupandroid.whatsappclone.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private Button botaoCadastrar;
    private Usuario usuario;


    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        setEdit();

    }

    public void setEdit() {

        name = findViewById(R.id.edit_Name_user);
        email = findViewById(R.id.edit_Email_user);
        password = findViewById(R.id.edit_Password_user);
        botaoCadastrar = findViewById(R.id.edit_Button_User);

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                usuario = new Usuario();
                usuario.setName(name.getText().toString());
                usuario.setEmail(email.getText().toString());
                usuario.setPassword(password.getText().toString());
                cadastrarUsuario();

            }
        });
    }

    public void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getPassword()
        ).addOnCompleteListener(CadastroUsuarioActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(CadastroUsuarioActivity.this, "Sucesso ao cadastrar usuario", Toast.LENGTH_LONG).show();

                    FirebaseUser userFirebase = task.getResult().getUser();

                    String indentificadorUser = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setId(indentificadorUser);
                    usuario.salvar();

                    Preferencias preferencias = new Preferencias(CadastroUsuarioActivity.this);
                    preferencias.salvarDados(indentificadorUser, usuario.getName());

                    openLoginUser();

                } else {

                    String erroExececao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExececao = "Digite uma senha mais forte, contendo letras e números";

                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExececao = "O e-mail é invalido, digite um novo e-mail.";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExececao = "Esse e-mail já esta em uso neste app!";
                    } catch (Exception e) {
                        erroExececao = "Erro ao executar o app";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroUsuarioActivity.this, "Erro: " + erroExececao, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void openLoginUser() {
        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
