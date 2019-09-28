package br.com.zupandroid.whatsappclone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;

import br.com.zupandroid.whatsappclone.R;
import br.com.zupandroid.whatsappclone.config.ConfiguracaoFirebase;
import br.com.zupandroid.whatsappclone.helper.Base64Custom;
import br.com.zupandroid.whatsappclone.helper.Preferencias;
import br.com.zupandroid.whatsappclone.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editMensagem;
    private ImageButton btnEnviar;
    private DatabaseReference firebase;

    //dados do destinatario

    private String usuarioDestinaratario;
    private String idUsuarioDestinatario;

    //dados do rementente

    private String idUsuarioRementente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        setTalks();

    }

    private void setTalks() {
        editMensagem = findViewById(R.id.editMensagem);
        btnEnviar = findViewById(R.id.btnEnviar);
        toolbar = findViewById(R.id.toolbarConversa);

        // dados do usuario logado

        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioRementente = preferencias.getIdentificado();


        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            usuarioDestinaratario = extra.getString("nome");
            String emailDestinatario = extra.getString("email");
            idUsuarioDestinatario = Base64Custom.codificarBase64(emailDestinatario);
        }


        toolbar.setTitle(usuarioDestinaratario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        //enviar mensagem

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textoMensagem = editMensagem.getText().toString();

                if (textoMensagem.isEmpty()) {
                    Toast.makeText(ConversaActivity.this, "Escreva uma mensagem antes de clicar em enviar", Toast.LENGTH_LONG).show();
                } else {

                    Mensagem mensagem = new Mensagem();
                    mensagem.setIdUsuario(idUsuarioRementente);
                    mensagem.setMensagem(textoMensagem);


                    salvedMensagem(idUsuarioRementente, idUsuarioDestinatario,mensagem);

                    editMensagem.setText("");


                }
            }
        });
    }

    private boolean salvedMensagem(String idRementente, String idDestinatario, Mensagem mensagem) {
        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");
            firebase.child(idRementente)
                    .child(idDestinatario)
                    .push()
                    .setValue(mensagem);



            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}