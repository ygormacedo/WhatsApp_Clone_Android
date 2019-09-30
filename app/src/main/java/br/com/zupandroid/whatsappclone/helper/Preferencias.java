package br.com.zupandroid.whatsappclone.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferencias<hashMap> {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "whats.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadoUsarioLogado";
    private final String CHAVE_NOME = "nomeUsarioLogado";

    public Preferencias(Context contextParametro) {

        contexto = contextParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();

    }

    public void salvarDados(String identificadousuario, String nomeUsuario) {

        editor.putString(CHAVE_IDENTIFICADOR, identificadousuario);
        editor.putString(CHAVE_NOME, nomeUsuario);
        editor.commit();


    }

    public String getIdentificado() {
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome() {
        return preferences.getString(CHAVE_NOME, null);
    }

}
