package br.com.zupandroid.whatsappclone.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import br.com.zupandroid.whatsappclone.fragment.ContatosFragment;
import br.com.zupandroid.whatsappclone.fragment.ConversasFragment;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloTabs = {"CONVERSAS", "CONTATOS"};

    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position){
            case 0:
                fragment = new ConversasFragment();
                break;
            case 1:
                fragment = new ContatosFragment();
                break;
        }

        return fragment;
    }
        @Override
    public int getCount() {
        return tituloTabs.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tituloTabs[position];


    }
}
