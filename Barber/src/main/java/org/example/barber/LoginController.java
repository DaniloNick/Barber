package org.example.barber;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    protected void login() {
        String user = username.getText();
        String pass = password.getText();

        if (validarCredenciais(user, pass)) {
            Application.trocarTela("menu.fxml", "Barber Shop - Menu Principal");
        } else {
            mostrarAlerta("Login falhou", "Usuário ou senha incorretos.");
        }
    }

    private boolean validarCredenciais(String nome, String senha) {
        String sql = "SELECT * FROM usuarios WHERE nome = ? AND senha = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Se existir pelo menos um resultado, login é válido

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
