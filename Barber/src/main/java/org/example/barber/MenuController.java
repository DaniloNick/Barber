package org.example.barber;

import javafx.fxml.FXML;

public class MenuController {

    @FXML
    protected void abrirServico() {
        Application.trocarTela("servico.fxml", "Barber Shop - Serviços");
    }

    @FXML
    protected void abrirCadastro() {
        Application.trocarTela("cadastro.fxml", "Barber Shop - Cadastro de Cliente");
    }

    @FXML
    protected void abrirAgendamento() {
        Application.trocarTela("agendamento.fxml", "Barber Shop - Agendamento");
    }

    @FXML
    protected void abrirConsulta() {
        Application.trocarTela("consulta.fxml", "Barber Shop - Consulta de Clientes");
    }

    @FXML
    protected void voltarLogin() {
        Application.trocarTela("login.fxml", "Barber Shop - Login");
    }
}
