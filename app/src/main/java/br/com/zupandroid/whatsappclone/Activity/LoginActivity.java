package br.com.zupandroid.whatsappclone.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.Random;

import br.com.zupandroid.whatsappclone.R;
import br.com.zupandroid.whatsappclone.helper.Permissao;
import br.com.zupandroid.whatsappclone.helper.Preferencias;

public class LoginActivity extends AppCompatActivity {

    private EditText telephone;
    private EditText name;
    private EditText code_Area;
    private EditText code_Ddd;
    private Button cadastrar;
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.INTERNET,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setInfo();
        Permissao.validaPermissoes(this,  permissoesNecessarias);

    }

    private void setInfo() {
        telephone = findViewById(R.id.editTelephone);
        name = findViewById(R.id.editNameLogin);
        code_Area = findViewById(R.id.edit55Login);
        code_Ddd = findViewById(R.id.editDDDLogin);
        cadastrar = findViewById(R.id.idButtonLogin);

        SimpleMaskFormatter simpleMaskTel = new SimpleMaskFormatter("NNNNN-NNNN");
        SimpleMaskFormatter simpleMaskTexDddI = new SimpleMaskFormatter("+NN");
        SimpleMaskFormatter simpleMasTextDdd = new SimpleMaskFormatter("NN");

        MaskTextWatcher maskTextTel = new MaskTextWatcher(telephone, simpleMaskTel);
        MaskTextWatcher maskTexDddI = new MaskTextWatcher(code_Area, simpleMaskTexDddI);
        MaskTextWatcher maskTextDdd = new MaskTextWatcher(code_Ddd, simpleMasTextDdd);

        telephone.addTextChangedListener(maskTextTel);
        code_Area.addTextChangedListener(maskTexDddI);
        code_Ddd.addTextChangedListener(maskTextDdd);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nameUser = name.getText().toString();
                String telefoneCompleto =
                        code_Area.getText().toString() +
                                code_Ddd.getText().toString() +
                                telephone.getText().toString();

                String telefoneSemFormatacao = telefoneCompleto.replace("+", "");
                telefoneSemFormatacao = telefoneSemFormatacao.replace("-", "");

                //Gerar token

                Random randomico = new Random();
                int numeroAleatorio = randomico.nextInt(9999 - 1000) + 1000;
                // entre 1000 e 9999//
                String token = String.valueOf(numeroAleatorio);
                String mensagemEnvio = "WhatsApp codigo de confirmação: " + token;


                Preferencias preferencias = new Preferencias(LoginActivity.this);
                preferencias.salvarUsuariosPreferencias(nameUser, telefoneSemFormatacao, token);

                // Send to SMS.(+5511947578682)
                telefoneSemFormatacao = "5554";
                boolean enviadoSms = enviaSms("+" + telefoneSemFormatacao, mensagemEnvio);

//                HashMap<String, String> usuario = preferencias.getDadosUsuario();
//                Log.e(" TOKEN", "  NOME:" +usuario.get("nome") + " FONE:" +usuario.get("telefone"));

            }
        });
    }

    private boolean enviaSms(String telefone, String mensagem) {

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone, null, mensagem, null, null);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
