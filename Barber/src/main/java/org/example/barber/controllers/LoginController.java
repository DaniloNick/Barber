package org.example.barber.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.barber.Application;
import org.example.barber.DAO.UsuarioDAO;
import org.example.barber.entities.Sessao;
import org.example.barber.entities.Usuario;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label msgLogin;

    @FXML
    protected void login() {
        String nome = username.getText();
        String senha = password.getText();

        Usuario usuarioLogado = UsuarioDAO.validarLogin(nome, senha);

        if (usuarioLogado != null) {
            Sessao.usuarioLogado = usuarioLogado;

            if (usuarioLogado.getNome().equals("admin") && usuarioLogado.getSenha().equals("123")) {
                Application.trocarTela("atualizarCadastro.fxml", "Barber Shop - Atualizar Cadastro");
            } else {
                Application.trocarTela("menu.fxml", "Barber Shop - Menu Principal");
            }

        } else {
            msgLogin.setStyle("-fx-text-fill: red;");
            msgLogin.setText("Usuário ou senha inválido.");
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Verifica se a tabela de usuários está vazia
        if (!UsuarioDAO.tabelaUsuariosExistente()) {
            // Cria o usuário admin apenas se a tabela estiver vazia
            UsuarioDAO.inserir("admin", "123", true);
        }
    }

}
