package br.com.zupandroid.whatsappclone.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.zupandroid.whatsappclone.R;
import br.com.zupandroid.whatsappclone.activity.ConversaActivity;
import br.com.zupandroid.whatsappclone.adapter.ConversaAdapter;
import br.com.zupandroid.whatsappclone.config.ConfiguracaoFirebase;
import br.com.zupandroid.whatsappclone.helper.Base64Custom;
import br.com.zupandroid.whatsappclone.helper.Preferencias;
import br.com.zupandroid.whatsappclone.model.Conversa;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private ListView listView;
    private ArrayList<Conversa> conversas;
    private ArrayAdapter<Conversa> adapter;

    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerConversas;

    public ConversasFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        conversas = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        listView = view.findViewById(R.id.listConversas);
        adapter = new ConversaAdapter(getActivity(), conversas);
        listView.setAdapter(adapter);

        //recuperar daddos do usuario.

        Preferencias preferencias = new Preferencias(getActivity());
        String idUsuarioLogado = preferencias.getIdentificado();


        //recuperar conversas do firebase.
        firebase = ConfiguracaoFirebase.getFirebase()
                .child("conversas")
                .child(idUsuarioLogado);

        valueEventListenerConversas = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                conversas.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Conversa conversa = dados.getValue(Conversa.class);
                    conversas.add(conversa);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Conversa conversa = conversas.get(position);
                Intent intent = new Intent(getActivity(), ConversaActivity.class);
                intent.putExtra("nome", conversa.getNome());
                String email = Base64Custom.decodificadorBase64(conversa.getIdUsuario());
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addListenerForSingleValueEvent(valueEventListenerConversas);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerConversas);
    }
}
