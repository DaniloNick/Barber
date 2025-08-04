package org.example.barber.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.barber.Application;
import org.example.barber.DAO.ClienteDAO;
import org.example.barber.DAO.ServicoRealizadoDAO;
import org.example.barber.entities.Cliente;
import org.example.barber.entities.ServicoRealizado;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ConsultaController {

    @FXML
    private TextField nomeClienteField;
    @FXML
    private ListView<Cliente> clientesListView;
    @FXML
    private ListView<String> historicoListView;
    private List<Cliente> todosClientes;

    @FXML private TableView<ServicoRealizado> historicoTableView;
    @FXML private TableColumn<ServicoRealizado, String> colData;
    @FXML private TableColumn<ServicoRealizado, String> colServico;
    @FXML private TableColumn<ServicoRealizado, Double> colPreco;
    @FXML private TableColumn<ServicoRealizado, String> colBarbeiro;

    @FXML
    private void initialize() {
        // Inicializa a lista de todos os clientes (a lista completa será filtrada conforme a busca)
        todosClientes = ClienteDAO.listarTodos();

        // Configura listagem de clientes
        clientesListView.setItems(FXCollections.observableArrayList());
        clientesListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);
                if (empty || cliente == null) {
                    setText(null);
                } else {
                    String nascimentoFormatado = cliente.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    setText("Nome: " + cliente.getNome() +
                            " | Telefone: " + cliente.getTelefone() +
                            " | Nascimento: " + nascimentoFormatado);
                }
            }
        });

        // Configura as colunas do histórico
        colData.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
        );
        colServico.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getServico().getNome())
        );
        colPreco.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(
                        cellData.getValue().getServico().getPreco()
                )
        );
        colBarbeiro.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getBarbeiro().getNome())
        );

        // Formata a célula para exibir como "R$ 10,00"
        colPreco.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double preco, boolean empty) {
                super.updateItem(preco, empty);
                if (empty || preco == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", preco));
                }
            }
        });

        // Filtro dinâmico
        nomeClienteField.textProperty().addListener((observable, oldValue, newValue) -> filtrarClientes());

        // Ao clicar no cliente, carrega histórico
        clientesListView.setOnMouseClicked(this::mostrarHistorico);
    }

    @FXML
    private void filtrarClientes() {
        String nomePesquisa = nomeClienteField.getText().toLowerCase().trim();
        ObservableList<Cliente> filtrados = FXCollections.observableArrayList();

        if (nomePesquisa.isEmpty()) {
            clientesListView.setItems(FXCollections.observableArrayList());
            return;
        }

        for (Cliente cliente : todosClientes) {
            if (cliente.getNome().toLowerCase().contains(nomePesquisa)) {
                filtrados.add(cliente);
            }
        }
        clientesListView.setItems(filtrados);
    }

    @FXML
    private void mostrarHistorico(MouseEvent event) {
        Cliente clienteSelecionado = clientesListView.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            // Busca os serviços realizados no banco
            List<ServicoRealizado> historico = ServicoRealizadoDAO.listarPorCliente(clienteSelecionado.getId());
            historicoTableView.setItems(FXCollections.observableArrayList(historico));
        }
    }

    @FXML
    protected void voltarMenu() {
        Application.trocarTela("menu.fxml", "Barber Shop - Menu Principal");
    }
}
