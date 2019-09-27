package br.com.zupandroid.whatsappclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import br.com.zupandroid.whatsappclone.R;
import br.com.zupandroid.whatsappclone.config.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth userAuth;
    private Toolbar toolbar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        toolbar1 = findViewById(R.id.menuToolbar);
        toolbar1.setTitle("WhatsApp");


        setSupportActionBar((Toolbar) findViewById(R.id.menuToolbar));
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deslogarUser(){

        userAuth.signOut();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
