package org.example.barber.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.barber.DAO.ServicoRealizadoDAO;
import org.example.barber.entities.ServicoRealizado;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ExcluirServicosController {

    @FXML private TextField campoBusca;
    @FXML private TableView<ServicoRealizado> tabelaServicos;
    @FXML private TableColumn<ServicoRealizado, String> colData;
    @FXML private TableColumn<ServicoRealizado, String> colCliente;
    @FXML private TableColumn<ServicoRealizado, String> colServico;
    @FXML private TableColumn<ServicoRealizado, String> colBarbeiro;

    private final ServicoRealizadoDAO servicoRealizadoDAO = new ServicoRealizadoDAO();

    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        colData.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDataHora().format(FORMATADOR))
        );
        colCliente.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getCliente().getNome())
        );
        colServico.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getServico().getNome())
        );
        colBarbeiro.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getBarbeiro().getNome())
        );

        carregarServicos();

        campoBusca.textProperty().addListener((obs, oldText, newText) -> filtrar(newText));
    }

    private void carregarServicos() {
        List<ServicoRealizado> lista = servicoRealizadoDAO.listarTodos();
        tabelaServicos.setItems(FXCollections.observableArrayList(lista));
    }

    private void filtrar(String filtro) {
        if (filtro == null || filtro.isBlank()) {
            carregarServicos();
            return;
        }
        List<ServicoRealizado> lista = servicoRealizadoDAO.buscarPorBarbeiro(filtro);
        tabelaServicos.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void excluirSelecionado() {
        ServicoRealizado selecionado = tabelaServicos.getSelectionModel().getSelectedItem();

        if (selecionado == null) {

            Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um serviço para excluir.", ButtonType.OK);

            // Aplica o CSS
            alert.getDialogPane().getStylesheets().add(
                    getClass().getResource("/styles/style.css").toExternalForm());

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

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja excluir o serviço selecionado?", ButtonType.YES, ButtonType.NO);
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

        Optional<ButtonType> resposta = confirm.showAndWait();

        if (resposta.isPresent() && resposta.get() == ButtonType.YES) {
            boolean sucesso = servicoRealizadoDAO.excluir(selecionado.getId());
            if (sucesso) {
                tabelaServicos.getItems().remove(selecionado);
            } else {
                Alert erro = new Alert(Alert.AlertType.ERROR, "Erro ao excluir o serviço.", ButtonType.OK);

                // Aplica o CSS
                erro.getDialogPane().getStylesheets().add(
                        getClass().getResource("/styles/style.css").toExternalForm());

                // ✅ Impede mover (tira barra de título)
                erro.setOnShowing(e -> {
                    Stage stage = (Stage) erro.getDialogPane().getScene().getWindow();
                    stage.initStyle(StageStyle.UNDECORATED);
                });

                // ✅ Garante centralizar ao abrir
                erro.setOnShown(e -> {
                    Stage stage = (Stage) erro.getDialogPane().getScene().getWindow();
                    stage.centerOnScreen();
                });

                erro.showAndWait();
            }
        }
    }

    @FXML
    private void voltarMenuAdmin() {
        // Ajuste para seu método de troca de tela
        org.example.barber.Application.trocarTela("menuAdmin.fxml", "Menu Administrativo");
    }
}
