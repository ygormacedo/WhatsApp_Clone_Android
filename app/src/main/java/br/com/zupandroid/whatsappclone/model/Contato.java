package br.com.zupandroid.whatsappclone.model;

public class Contato {

    private String indentificadoUsuario;
    private String name;
    private String email;


    public Contato() {
    }

    public String getIndentificadoUsuario() {
        return indentificadoUsuario;
    }

    public void setIndentificadoUsuario(String indentificadoUsuario) {
        this.indentificadoUsuario = indentificadoUsuario;
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
}
