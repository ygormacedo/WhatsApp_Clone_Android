package br.com.zupandroid.whatsappclone.model;

import com.google.firebase.database.DatabaseReference;

import br.com.zupandroid.whatsappclone.config.ConfiguracaoFirebase;

public class Usuario {

    private String id;
    private String name;
    private String email;
    private String password;

    public Usuario() {

    }

    public void salvar() {
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("usuarios").child(getId()).setValue(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
