package br.com.zupandroid.whatsappclone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import br.com.zupandroid.whatsappclone.R;
import br.com.zupandroid.whatsappclone.helper.Preferencias;
import br.com.zupandroid.whatsappclone.model.Mensagem;

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagens;

    public MensagemAdapter(@NonNull Context c, ArrayList<Mensagem> objects) {
        super(c, 0, objects);

        this.context = c;
        this.mensagens = objects;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        //Verificar se  a lista está preenchida
        if (mensagens != null) {

            // recuperar dados do usuario rementente
            Preferencias preferencias = new Preferencias(context);
            String idUsuarioRementente = preferencias.getIdentificado();

            //iniciliza objeto para montagem de layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //recuperar mensagem
            Mensagem mensagem = mensagens.get(position);


            // monta view a partir do xml
            if ( idUsuarioRementente.equals(mensagem.getIdUsuario())) {
                view = inflater.inflate(R.layout.item_mensagem_direita,parent,false);

            }else{
                view = inflater.inflate(R.layout.item_mensagem_esquerda,parent,false);
            }

            //recuperar elemento para exibição

            TextView textoMensagem = view.findViewById(R.id.textMensagem);
            textoMensagem.setText( mensagem.getMensagem());

        }
        return view;
    }
}
