package org.example.barber.entities;

public class Usuario {
    private int id;
    private String nome;
    private String senha;
    private boolean isAdmin;

    public Usuario(int id, String nome, String senha, boolean isAdmin) {
        this.id = id;
        this.nome = nome;
        this.senha = senha;
        this.isAdmin = isAdmin;
    }

    public Usuario(String nome, String senha, boolean isAdmin) {
        this.nome = nome;
        this.senha = senha;
        this.isAdmin = isAdmin;
    }

    public Usuario(String nome, String senha) {
        this.nome = nome;
        this.senha = senha;
        this.isAdmin = false;
    }

    public Usuario() {

    }


    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getSenha() { return senha; }
    public boolean isAdmin() { return isAdmin; }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setSenha(String senha) { this.senha = senha; }
    public void setAdmin(boolean admin) { isAdmin = admin; }

    @Override
    public String toString() {
        return this.getNome();
    }

}

