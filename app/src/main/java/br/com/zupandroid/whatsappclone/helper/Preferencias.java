package br.com.zupandroid.whatsappclone.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias {

    private Context context;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "whats.preferencias";
    private int MODE = 0;

    public Preferencias(Context contextParametro) {

        context = contextParametro;
        preferences = context.getSharedPreferences(NOME_ARQUIVO, MODE);
    }


}
