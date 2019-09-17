package br.com.zupandroid.whatsappclone.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {

    public static boolean validaPermissoes(int requestCode, Activity activity, String[] permissoes) {
        List<String> listaPermissoes = new ArrayList<String>();

        if (Build.VERSION.SDK_INT >= 23) {

            // Percorre as permissões, verificando um a uma se já tem a permissão liberada...

            for (String permissao : permissoes) {
                boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (!validaPermissao) listaPermissoes.add(permissao);
            }
            // caso a lista esteja vazia, não é necessário solicitar permissão

            if (listaPermissoes.isEmpty()) return true;

            String[] novasPermissoes = new String[listaPermissoes.size()];
            listaPermissoes.toArray(novasPermissoes);

            //solicita permissão
            ActivityCompat.requestPermissions(activity, novasPermissoes, requestCode);
        }
        return true;
    }
}
