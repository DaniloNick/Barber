package org.example.barber.entities;

import java.time.LocalDateTime;

public class ServicoRealizado {

    private int id;
    private Cliente cliente;
    private Servico servico;
    private LocalDateTime dataHora;
    private Usuario barbeiro;

    // Construtor ajustado para Cliente, Servico e LocalDateTime
    public ServicoRealizado(Cliente cliente, Servico servico, LocalDateTime dataHora, Usuario barbeiro) {
        this.cliente = cliente;
        this.servico = servico;
        this.dataHora = dataHora;
        this.barbeiro = barbeiro;
    }

    public ServicoRealizado() {}

    public int getId() {
        return id;
    }
    public Usuario getBarbeiro() {
        return barbeiro;
    }

    public void setBarbeiro(Usuario barbeiro) {
        this.barbeiro = barbeiro;
    }

    // Getters e setters
    public Cliente getCliente() {
        return cliente;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
