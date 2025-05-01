package org.example.barber;

import java.time.LocalDateTime;

public class Agendamento {
    private Cliente cliente;
    private LocalDateTime dataHora;

    public Agendamento(Cliente cliente, LocalDateTime dataHora) {
        this.cliente = cliente;
        this.dataHora = dataHora;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    @Override
    public String toString() {
        return cliente.getNome() + " - " + dataHora.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
