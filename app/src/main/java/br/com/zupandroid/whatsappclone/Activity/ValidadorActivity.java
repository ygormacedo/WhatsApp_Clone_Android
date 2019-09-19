package br.com.zupandroid.whatsappclone.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

import br.com.zupandroid.whatsappclone.R;
import br.com.zupandroid.whatsappclone.helper.Preferencias;

public class ValidadorActivity extends AppCompatActivity {

    private EditText codigoValidacao;
    private Button validar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validador);

        codigoValidacao = findViewById(R.id.emptySMS);
        validar = findViewById(R.id.idButtonValidar);

        SimpleMaskFormatter simpleCodigoValidacao = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher mascaraCodigoValidacao = new MaskTextWatcher(codigoValidacao, simpleCodigoValidacao);

        codigoValidacao.addTextChangedListener(mascaraCodigoValidacao);
        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Recuperar dados do usuario.
                Preferencias preferencias = new Preferencias(ValidadorActivity.this);
                HashMap<String, String> usuario = preferencias.getDadosUsuario();

                String tokenGerado = usuario.get("token");
                String tokenDigitado = codigoValidacao.getText().toString();

                if (tokenDigitado.equals(tokenGerado)) {
                    Toast.makeText(ValidadorActivity.this, "Token validado! ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ValidadorActivity.this, "Token Não validado! ", Toast.LENGTH_LONG).show();

                }
            }
        });

    }
}
