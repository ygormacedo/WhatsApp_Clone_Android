package br.com.zupandroid.whatsappclone.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import br.com.zupandroid.whatsappclone.R;
import br.com.zupandroid.whatsappclone.config.ConfiguracaoFirebase;
import br.com.zupandroid.whatsappclone.model.Usuario;

import static br.com.zupandroid.whatsappclone.ValindandoCPF.ValidaCPF.isValidCPF;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private Button botaoCadastrar;
    private Usuario usuario;
    private EditText cpfUser;

    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        setEdit();
        cpfMask();
    }

    public void setEdit() {

        cpfUser = findViewById(R.id.cpfId);
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
                usuario.setCpf(cpfUser.getText().toString());

                usuario.setCpf(usuario.getCpf().replace("-", "").replace(".", ""));
                if (isValidCPF(usuario.getCpf())){

                    Toast.makeText(CadastroUsuarioActivity.this,"CPF OK !!!!!!",Toast.LENGTH_LONG).show();
                    cadastrarUsuario();
                }else {
                    Toast.makeText(CadastroUsuarioActivity.this,"CPF Invalido", Toast.LENGTH_LONG).show();
                }
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
                    usuario.setId( userFirebase.getUid());
                    usuario.salvar();

                } else {
                    Toast.makeText(CadastroUsuarioActivity.this, "Erro ao cadastrar Usuario", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void cpfMask(){
        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher  maskTextWatcher = new MaskTextWatcher(cpfUser, simpleMaskFormatter);
        cpfUser.addTextChangedListener(maskTextWatcher);
    }
}
