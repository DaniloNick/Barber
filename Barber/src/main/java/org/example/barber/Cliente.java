package org.example.barber;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nome;
    private String telefone;
    private LocalDate dataNascimento;
    private List<ServicoRealizado> historicoServicos;  // Histórico de serviços realizados

    public Cliente(String nome, String telefone, LocalDate dataNascimento) {
        this.nome = nome;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.historicoServicos = new ArrayList<>();
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

    // Adiciona um serviço realizado ao histórico
    public void adicionarServicoRealizado(ServicoRealizado servicoRealizado) {
        historicoServicos.add(servicoRealizado);
    }

}
