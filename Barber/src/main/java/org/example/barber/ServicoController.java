package org.example.barber;

import javafx.fxml.FXML;

public class ServicoController {


    @FXML
    protected void abrirNovoServico() {
        Application.trocarTela("novo_servico.fxml", "Barber Shop - Novo Serviço");
    }

    @FXML
    protected void gerenciarServicos() {
        Application.trocarTela("cadastrar_servico.fxml", "Barber Shop - Cadastrar Serviço");
    }

    @FXML
    protected void voltarMenu() {
        Application.trocarTela("menu.fxml", "Barber Shop - Menu Principal");
    }
}
