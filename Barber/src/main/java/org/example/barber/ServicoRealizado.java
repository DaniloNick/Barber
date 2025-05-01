package org.example.barber;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServicoRealizado {
    private Servico servico;  // Tipo de serviço
    private LocalDateTime dataHora;  // Data e hora de quando o serviço foi realizado

    public ServicoRealizado(Servico servico, LocalDateTime dataHora) {
        this.servico = servico;
        this.dataHora = dataHora;
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

    @Override
    public String toString() {
        return "Serviço: " + servico.getTipo() + " - " + "Data: " + dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

}
