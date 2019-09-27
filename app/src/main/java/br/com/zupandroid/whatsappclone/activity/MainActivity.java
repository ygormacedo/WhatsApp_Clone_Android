package br.com.zupandroid.whatsappclone.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import br.com.zupandroid.whatsappclone.R;
import br.com.zupandroid.whatsappclone.adapter.TabAdapter;
import br.com.zupandroid.whatsappclone.config.ConfiguracaoFirebase;
import br.com.zupandroid.whatsappclone.helper.Preferencias;
import br.com.zupandroid.whatsappclone.helper.SlidingTabLayout;
import br.com.zupandroid.whatsappclone.model.Contato;
import br.com.zupandroid.whatsappclone.model.Usuario;

import static br.com.zupandroid.whatsappclone.helper.Base64Custom.codificarBase64;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth userAuth;
    private Toolbar toolbar1;
    private String indentificadorContato;
    private DatabaseReference referenciaFirebase;

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();

        toolbar1 = findViewById(R.id.menuToolbar);
        toolbar1.setTitle("WhatsApp");
        setSupportActionBar((Toolbar) findViewById(R.id.menuToolbar));

        slidingTabLayout = findViewById(R.id.styleTab);
        viewPager = findViewById(R.id.vp_pagina);

        //configurar sliding tabs

        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this, R.color.colorAccente));

        //configurar adapter
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_Sair:
                deslogarUser();
                return true;
            case R.id.item_Settings:
                return true;
            case R.id.item_Contact:
                abriCadastroUsuario();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void abriCadastroUsuario() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        //configuração do dialog

        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("E-mail do usuário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView(editText);

        // configurando os botões

        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String emailContato = editText.getText().toString();

                //valindando se o e-mail foi registrado.
                if (emailContato.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Preencha com um E-mail", Toast.LENGTH_LONG).show();
                } else {
                    //verificar se o usuario já esta no app

                    indentificadorContato = codificarBase64(emailContato);

                    //recuperar instancia do firebase
                    referenciaFirebase = ConfiguracaoFirebase.getFirebase().child("usuarios").child(indentificadorContato);
                    referenciaFirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getValue() != null) {

                                //recuperar dados do contato a ser adicionado

                                Usuario usuarioContato = dataSnapshot.getValue(Usuario.class);


                                //Recuperar identificado usuario logado (BASE64)
                                Preferencias preferencias = new Preferencias(MainActivity.this);
                                String identificadoUsarioLogado = preferencias.getIdentificado();


                                referenciaFirebase = ConfiguracaoFirebase.getFirebase();
                                referenciaFirebase = referenciaFirebase.child("contatos")
                                        .child(identificadoUsarioLogado)
                                        .child(indentificadorContato);

                                Contato contato = new Contato();
                                contato.setIndentificadoUsuario(indentificadorContato);
                                contato.setEmail(usuarioContato.getEmail());
                                contato.setName(usuarioContato.getName());


                                referenciaFirebase.setValue(contato);


                            } else {
                                Toast.makeText(MainActivity.this, "Usuário não possui cadastro.", Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();

    }

    private void deslogarUser() {

        userAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
