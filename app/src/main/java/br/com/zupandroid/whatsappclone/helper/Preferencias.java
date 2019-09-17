package br.com.zupandroid.whatsappclone.helper;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferencias<hashMap> {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "whats.preferencias";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private String CHAVE_NOME = "nome";
    private String CHAVE_TELEFONE = "telefone";
    private String CHAVE_TOKEN = "token";


    public Preferencias(Context contextParametro) {

        contexto = contextParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE);
        editor = preferences.edit();

    }

    public void salvarUsuariosPreferencias(String nome, String telefone, String token) {

        editor.putString("nome", nome);
        editor.putString("telefone", telefone);
        editor.putString("token", token);
        editor.commit();

    }

    public HashMap<String, String> getDadosUsuario() {

        HashMap<String, String> dadoUsuario = new HashMap<>();

        dadoUsuario.put(CHAVE_NOME, preferences.getString(CHAVE_NOME, null));
        dadoUsuario.put(CHAVE_TELEFONE, preferences.getString(CHAVE_TELEFONE, null));
        dadoUsuario.put(CHAVE_TOKEN, preferences.getString(CHAVE_TOKEN, null));

        return dadoUsuario;

    }

}
