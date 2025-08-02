package org.example.barber.controllers;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.barber.Application;
import org.example.barber.DAO.ServicoRealizadoDAO;
import org.example.barber.DAO.UsuarioDAO;
import org.example.barber.entities.RelatorioItem;
import org.example.barber.entities.Usuario;

import java.time.LocalDate;
import java.util.List;

public class RelatoriosController {

    @FXML private ComboBox<String> barbeiroComboBox;
    @FXML private DatePicker dataInicialPicker, dataFinalPicker;
    @FXML private TableView<RelatorioItem> relatorioTableView;
    @FXML private TableColumn<RelatorioItem, String> colPeriodo;
    @FXML private TableColumn<RelatorioItem, Integer> colAtendimentos;
    @FXML private TableColumn<RelatorioItem, Double> colFaturamento;
    @FXML private TableColumn<RelatorioItem, Double> colComissao;

    @FXML
    private void initialize() {
        // Configura colunas da tabela
        colPeriodo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPeriodo()));
        colAtendimentos.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAtendimentos()).asObject());
        colFaturamento.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getFaturamento()).asObject());
        colComissao.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getComissao()).asObject());

        // Carrega barbeiros no ComboBox
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        List<Usuario> usuarios = usuarioDAO.listarTodos();

        barbeiroComboBox.getItems().add("Todos");
        for (Usuario u : usuarios) {
            barbeiroComboBox.getItems().add(u.getNome());
        }
        barbeiroComboBox.getSelectionModel().selectFirst();

        // Quando selecionar barbeiro, carrega relatório
        barbeiroComboBox.setOnAction(event -> carregarRelatorio());

        // Carregar inicialmente
        carregarRelatorio();
    }

    @FXML
    private void carregarRelatorio() {

        LocalDate hoje = LocalDate.now();
        LocalDate seteDiasAtras = hoje.minusDays(6); // incl. hoje
        LocalDate trintaDiasAtras = hoje.minusDays(29);

        String barbeiro = barbeiroComboBox.getValue(); // pode ser "Todos"

        RelatorioItem hojeItem = ServicoRealizadoDAO.gerarRelatorio(barbeiro, hoje, hoje, "Hoje");
        RelatorioItem seteDiasItem = ServicoRealizadoDAO.gerarRelatorio(barbeiro, seteDiasAtras, hoje, "7 Dias");
        RelatorioItem trintaDiasItem = ServicoRealizadoDAO.gerarRelatorio(barbeiro, trintaDiasAtras, hoje, "30 Dias");

        ObservableList<RelatorioItem> dados = FXCollections.observableArrayList(
                hojeItem, seteDiasItem, trintaDiasItem
        );

        relatorioTableView.setItems(dados);
    }

    @FXML
    private void buscarPorPeriodo() {
        String barbeiro = barbeiroComboBox.getValue();
        LocalDate dataInicial = dataInicialPicker.getValue();
        LocalDate dataFinal = dataFinalPicker.getValue();

        if (dataInicial != null && dataFinal != null) {
            RelatorioItem periodoItem = ServicoRealizadoDAO.gerarRelatorio(barbeiro, dataInicial, dataFinal, "Período");
            relatorioTableView.setItems(FXCollections.observableArrayList(periodoItem));
        }
    }

    @FXML
    private void voltarMenu() {
        Application.trocarTela("menu.fxml", "Barber Shop - Menu Principal");
    }
}
