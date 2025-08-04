package org.example.barber.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.barber.Application;
import org.example.barber.DAO.UsuarioDAO;
import org.example.barber.entities.Sessao;

import java.io.IOException;

import static org.example.barber.entities.Sessao.usuarioLogado;

public class AtualizarCadastroController {

    @FXML
    private TextField novoUsuario;

    @FXML
    private PasswordField novaSenha;

    @FXML
    private PasswordField confirmarSenha;

    @FXML
    private Label mensagem;

    @FXML
    private Button btnAtualizar;

    @FXML
    private void voltarLogin() {
        Application.trocarTela("login.fxml", "Barber Shop - Login");
    }


    @FXML
    public void initialize() {
        btnAtualizar.setOnAction(event -> atualizarCadastro());
    }

    private void atualizarCadastro() {
        String usuario = novoUsuario.getText().trim();
        String senha = novaSenha.getText();
        String confirmar = confirmarSenha.getText();

        if (usuario.isEmpty() || senha.isEmpty() || confirmar.isEmpty()) {
            mensagem.setText("Todos os campos são obrigatórios.");
            return;
        }

        if (!senha.equals(confirmar)) {
            mensagem.setText("As senhas não coincidem.");
            return;
        }

        boolean sucesso = UsuarioDAO.atualizarUsuario("admin", usuario, senha,true);

        if (sucesso) {
            mensagem.setStyle("-fx-text-fill: green;");
            mensagem.setText("Cadastro atualizado com sucesso!");

            usuarioLogado = usuarioLogado;

            // Redireciona para o menu principal
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/barber/views/menu.fxml"));
                Parent root = loader.load();

                MenuController menuController = loader.getController();

                Stage stage = (Stage) novoUsuario.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Barber Shop - Menu Principal");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            mensagem.setText("Erro ao atualizar cadastro.");
        }
    }
}
