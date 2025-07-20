package org.example.barber.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private int id;
    private String nome;
    private String telefone;
    private LocalDate dataNascimento;
    private List<ServicoRealizado> historicoServicos = new ArrayList<>();

    // Construtor com ID (usado ao carregar do banco)
    public Cliente(int id, String nome, String telefone, LocalDate dataNascimento) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
    }

    // Construtor sem ID (usado para novos clientes)
    public Cliente(String nome, String telefone, LocalDate dataNascimento) {
        this.nome = nome;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public List<ServicoRealizado> getHistoricoServicos() {
        return historicoServicos;
    }

    public void setHistoricoServicos(List<ServicoRealizado> historicoServicos) {
        this.historicoServicos = historicoServicos;
    }

    public void adicionarServicoRealizado(ServicoRealizado servicoRealizado) {
        historicoServicos.add(servicoRealizado);
    }

    @Override
    public String toString() {
        return nome + " - " + telefone;
    }

}
