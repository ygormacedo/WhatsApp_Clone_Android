package br.com.zupandroid.whatsappclone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.auth.FirebaseAuth;

import br.com.zupandroid.whatsappclone.R;
import br.com.zupandroid.whatsappclone.adapter.TabAdapter;
import br.com.zupandroid.whatsappclone.config.ConfiguracaoFirebase;
import br.com.zupandroid.whatsappclone.helper.SlidingTabLayout;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth userAuth;
    private Toolbar toolbar1;

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
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorAccente));

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
