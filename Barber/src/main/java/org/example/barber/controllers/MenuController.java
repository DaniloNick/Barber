package org.example.barber.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.barber.Application;
import org.example.barber.DAO.UsuarioDAO;
import org.example.barber.entities.Sessao;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    @FXML
    protected Label avisoMenu;

    @FXML
    protected Label userNameMenu;

    @FXML
    protected void abrirServico() {
        Application.trocarTela("novo_servico.fxml", "Barber Shop - Novo Atendimento");
    }

    @FXML
    protected void abrirCliente() {
        Application.trocarTela("cliente.fxml", "Barber Shop - Gerenciamento de Cliente");
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
    private void voltarLogin() {

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Deseja deslogar agora ?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("ATENÇÃO!");

        // Aplica o CSS
        confirm.getDialogPane().getStylesheets().add(
                getClass().getResource("/styles/style.css").toExternalForm());

        // ✅ Impede mover (tira barra de título)
        confirm.setOnShowing(e -> {
            Stage stage = (Stage) confirm.getDialogPane().getScene().getWindow();
            stage.initStyle(StageStyle.UNDECORATED);
        });

        // ✅ Garante centralizar ao abrir
        confirm.setOnShown(e -> {
            Stage stage = (Stage) confirm.getDialogPane().getScene().getWindow();
            stage.centerOnScreen();
        });

        // Exibe o alerta e aguarda resposta
        Optional<ButtonType> resposta = confirm.showAndWait();

        if (resposta.isPresent() && resposta.get() == ButtonType.YES) {
            Application.trocarTela("login.fxml", "Barber Shop - Login");
        }
        // Se clicar NO ou fechar, não faz nada
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (Sessao.usuarioLogado != null) {
            String nome = Sessao.usuarioLogado.getNome();
            nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();
            userNameMenu.setText("Bem-vindo, " + nome + "!");
        }
    }
    @FXML
    private void abrirMenuAdministrativo() {
        if (!Sessao.usuarioLogado.isAdmin()) {
            // Mostrar alerta de permissão negada
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Acesso negado");
            alert.setHeaderText(null);
            alert.setContentText("Você não tem permissão para acessar o Menu Administrativo.");

            // Aplica o CSS
            alert.getDialogPane().getStylesheets().add(
                    getClass().getResource("/styles/style.css").toExternalForm()
            );
            // ✅ Impede mover (tira barra de título)
            alert.setOnShowing(e -> {
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.initStyle(StageStyle.UNDECORATED);
            });

            // ✅ Garante centralizar ao abrir
            alert.setOnShown(e -> {
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                stage.centerOnScreen();
            });
            alert.showAndWait();
            return;
        }

        // Se for admin, pedir senha com Dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Confirmação de Acesso");
        dialog.setHeaderText("Digite a senha de administrador para continuar:");

        //Carrega estilo CSS
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Senha");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(new Label("Senha:"), 0, 0);
        grid.add(passwordField, 1, 0);
        dialog.getDialogPane().setContent(grid);

        Platform.runLater(passwordField::requestFocus);

        // ✅ Impede mover (tira barra de título)
        dialog.setOnShowing(e -> {
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.initStyle(StageStyle.UNDECORATED);
        });

        // ✅ Garante centralizar ao abrir
        dialog.setOnShown(e -> {
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.centerOnScreen();
        });

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return passwordField.getText();
            }
            return null;
        });

        Optional<String> resultado = dialog.showAndWait();

        if (resultado.isPresent()) {
            String senhaDigitada = resultado.get();
            boolean senhaCorreta = UsuarioDAO.validarSenha(Sessao.usuarioLogado.getNome(), senhaDigitada);

            if (senhaCorreta) {
                // Abrir tela administrativa, exemplo:
                Application.trocarTela("menuAdmin.fxml","Barber Shop - Relatorios");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Senha incorreta");
                alert.setHeaderText(null);
                alert.setContentText("Senha incorreta. Acesso negado.");

                // Aplica o CSS
                alert.getDialogPane().getStylesheets().add(
                        getClass().getResource("/styles/style.css").toExternalForm()
                );
                // ✅ Impede mover (tira barra de título)
                alert.setOnShowing(e -> {
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.initStyle(StageStyle.UNDECORATED);
                });

                // ✅ Garante centralizar ao abrir
                alert.setOnShown(e -> {
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.centerOnScreen();
                });

                alert.showAndWait();
            }
        } else {
            // Cancelou o diálogo, não faz nada
        }
    }






}
