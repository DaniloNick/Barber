package org.example.barber.entities;

public class Servico {
    private int id;
    private String nome;
    private double preco;

    private double comissao;

    public Servico(int id, String nome, double preco, double comissao) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.comissao = comissao;
    }

    public Servico(String nome, double preco, double comissao) {
        this.nome = nome;
        this.preco = preco;
        this.comissao = comissao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getComissao() {
        return comissao;
    }

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }

    @Override
    public String toString() {
        return nome + " - R$ " + String.format("%.2f", preco);
    }
}
