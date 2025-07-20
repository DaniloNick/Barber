package org.example.barber.controllers;

import javafx.fxml.FXML;
import org.example.barber.Application;

public class AgendamentoController {
    @FXML
    protected void agendarButton() {
        Application.trocarTela("agendar_cliente.fxml", "Barber Shop - Agendar Cliente");
    }

    @FXML
    protected void clienteAgendadoButton() {
        Application.trocarTela("clientes_agendados.fxml", "Barber Shop - Clientes Agendados");
    }

    @FXML
    protected void voltar() {
        Application.trocarTela("menu.fxml", "Barber Shop - Menu Principal");
    }

}
