package org.example.barber.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.barber.Application;
import org.example.barber.entities.Usuario;
import org.example.barber.entities.Sessao;

public class MenuAdminController {

    @FXML
    private Label userNameMenu;

    @FXML
    private Label avisoMenu;

    @FXML
    public void initialize() {
        // Exibe o nome do usuário logado no topo
        Usuario usuario = Sessao.usuarioLogado;
        if (usuario != null) {
            String nome = Sessao.usuarioLogado.getNome();
            nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();
            userNameMenu.setText("Bem-vindo, " + nome + "!");
        }
    }

    @FXML
    private void gerenciarUsuarios() {
        // Abre a tela de gerenciamento de usuários
        Application.trocarTela("usuarios.fxml","Barber Shop - Gerenciamento de Usuarios");
    }

    @FXML
    private void relatorios() {
        // Abre a tela de relatórios
        Application.trocarTela("relatorios.fxml","Barber Shop - Relatorios");
    }

    @FXML
    private void gerenciarServicos() {
        // Abre a tela de gerenciamento de serviços
        Application.trocarTela("cadastrar_servico.fxml", "Barber Shop - Gerenciamento de Serviço");
    }

    @FXML
    private void abrirExcluirServicos() {
        //// Abre a tela de excluir serviços
        Application.trocarTela("excluir_servicos.fxml", "Barber Shop - Excluir Serviços");
    }


    @FXML
    private void voltarLogin() {
        // Volta para a tela de login ou menu principal (ajuste conforme seu fluxo)
        Application.trocarTela("menu.fxml","Barber Shop - Menu Principal");
    }
}
