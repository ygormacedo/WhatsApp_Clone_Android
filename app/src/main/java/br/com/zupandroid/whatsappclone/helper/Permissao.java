package br.com.zupandroid.whatsappclone.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;

public class Permissao {

    public static boolean validaPermissoes(Activity activity, String[] permissoes){

        if (Build.VERSION.SDK_INT >= 23) {

            for (String permissao : permissoes) {

                boolean validaPermissao = ContextCompat.checkSelfPermission(activity, permissao) == PackageManager.PERMISSION_GRANTED;
                if (){

                }
            }
        }
        return true;
    }
}
