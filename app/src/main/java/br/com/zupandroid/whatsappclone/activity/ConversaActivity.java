package br.com.zupandroid.whatsappclone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.zupandroid.whatsappclone.R;
import br.com.zupandroid.whatsappclone.adapter.MensagemAdapter;
import br.com.zupandroid.whatsappclone.config.ConfiguracaoFirebase;
import br.com.zupandroid.whatsappclone.helper.Base64Custom;
import br.com.zupandroid.whatsappclone.helper.Preferencias;
import br.com.zupandroid.whatsappclone.model.Conversa;
import br.com.zupandroid.whatsappclone.model.Mensagem;

public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editMensagem;
    private ImageButton btnEnviar;
    private DatabaseReference firebase;
    private ListView listView;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    private ValueEventListener valueEventListenerMensagem;

    //dados do destinatario

    private String usuarioDestinaratario;
    private String idUsuarioDestinatario;

    //dados do rementente

    private String idUsuarioRementente;
    private String nomeUsuarioRemetente;

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
        listView = findViewById(R.id.listConversa);

        // dados do usuario logado

        Preferencias preferencias = new Preferencias(ConversaActivity.this);
        idUsuarioRementente = preferencias.getIdentificado();
        nomeUsuarioRemetente = preferencias.getNome();


        Bundle extra = getIntent().getExtras();

        if (extra != null) {
            usuarioDestinaratario = extra.getString("nome");
            String emailDestinatario = extra.getString("email");
            idUsuarioDestinatario = Base64Custom.codificarBase64(emailDestinatario);
        }


        toolbar.setTitle(usuarioDestinaratario);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);

        // Montando a listView e adapter

        mensagens = new ArrayList<>();
        adapter = new MensagemAdapter(ConversaActivity.this, mensagens);

//        adapter = new ArrayAdapter(
//                ConversaActivity.this,
//                android.R.layout.simple_list_item_1,
//                mensagens
//        );

        listView.setAdapter(adapter);

        //recuperar mensagem do firebase

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("mensagens")
                .child(idUsuarioRementente)
                .child(idUsuarioDestinatario);

        // Criar listener para mensagens

        valueEventListenerMensagem = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //limpar mensagens
                mensagens.clear();


                //recuperar mensagens
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Mensagem mensagem = dados.getValue(Mensagem.class);
                    mensagens.add(mensagem);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        };

        firebase.addValueEventListener(valueEventListenerMensagem);
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

                    //salvar mensagem para remetente
                    Boolean retornoMensagemRemetente = salvedMensagem(idUsuarioRementente, idUsuarioDestinatario, mensagem);
                    if (!retornoMensagemRemetente) {
                        Toast.makeText(
                                ConversaActivity.this,
                                "Problema ao salvar a mensagem, tente novamente",
                                Toast.LENGTH_LONG
                        ).show();
                    } else {

                        //salvar mensagem para o destinatario
                        Boolean retornoMensagemDestinatario = salvedMensagem(idUsuarioDestinatario, idUsuarioRementente, mensagem);
                        if (!retornoMensagemDestinatario) {
                            Toast.makeText(
                                    ConversaActivity.this,
                                    "Problema ao enviar mensagem ao destinatário, tente novamente!",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                }
                // Salvar conversa para o remetente
                Conversa conversa = new Conversa();
                conversa.setIdUsuario(idUsuarioDestinatario);
                conversa.setNome(usuarioDestinaratario);
                conversa.setMensagem(textoMensagem);
                Boolean retornoConversaRemetente = salvarConversar(idUsuarioRementente, idUsuarioDestinatario, conversa);
                if (!retornoConversaRemetente) {
                    Toast.makeText(
                            ConversaActivity.this,
                            "Problema ao salvar, tente novamente!",
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    // Salvar conversa para o Destinatario

                    Conversa talk = new Conversa();
                    talk.setIdUsuario(idUsuarioRementente);
                    talk.setNome(nomeUsuarioRemetente);
                    talk.setMensagem(textoMensagem);
                    Boolean retornoConversaDestinatario = salvarConversar(idUsuarioDestinatario, idUsuarioRementente, talk);
                    if (!retornoConversaDestinatario){
                        Toast.makeText(
                                ConversaActivity.this,
                                "Problema ao salvar conversa para o destinatario, tente novamente!",
                                Toast.LENGTH_LONG
                        ).show();

                }

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

    private boolean salvarConversar(String idRementente, String idDestinatario, Conversa conversa) {

        try {

            firebase = ConfiguracaoFirebase.getFirebase().child("conversas");
            firebase.child(idRementente)
                    .child(idDestinatario)
                    .setValue(conversa);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagem);
    }
}