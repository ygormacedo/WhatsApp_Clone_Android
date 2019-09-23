package br.com.zupandroid.whatsappclone.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;


import br.com.zupandroid.whatsappclone.R;
import br.com.zupandroid.whatsappclone.config.ConfiguracaoFirebase;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference referenciaFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebase();

    }

    private void firebase() {
//        referenciaFirebase = ConfiguracaoFirebase.getFirebase();
//        referenciaFirebase.child("pontos").setValue("800");

    }

    public void abriCadastroUsuario( View view){
        Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
        startActivity( intent );
    }
}
