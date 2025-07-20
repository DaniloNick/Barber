package org.example.barber.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.example.barber.Application;
import org.example.barber.entities.Sessao;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    protected Label avisoMenu;

    @FXML
    protected Label userNameMenu;

    @FXML
    protected void abrirServico() {
        Application.trocarTela("novo_servico.fxml", "Barber Shop - Serviços");
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
    protected void genrenciarServico() {
        Application.trocarTela("cadastrar_servico.fxml", "Barber Shop - Gerenciamento de Serviço");
    }

    @FXML
    protected void gerenciarUsuarios(){
        Application.trocarTela("usuarios.fxml","Barber Shop - Gerenciamento de Usuarios");
    }

    @FXML
    protected void relatorios() {
        avisoMenu.setText("Em Breve!");
    }

    @FXML
    private void voltarLogin() {
        Application.trocarTela("login.fxml", "Barber Shop - Login");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Sessao.usuarioLogado != null) {
            String nome = Sessao.usuarioLogado;
            nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();
            userNameMenu.setText("Bem-vindo, " + nome + "!");
        }
    }





}
