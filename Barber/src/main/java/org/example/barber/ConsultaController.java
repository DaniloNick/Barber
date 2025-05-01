package org.example.barber;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.collections.ObservableList;
import java.time.format.DateTimeFormatter;

public class ConsultaController {

    @FXML
    private TextField nomeClienteField;

    @FXML
    private ListView<Cliente> clientesListView;

    @FXML
    private ListView<String> historicoListView;

    @FXML
    protected void voltarMenu() {
        Application.trocarTela("menu.fxml", "Barber Shop - Menu Principal");
    }

    @FXML
    private void initialize() {
        // Inicializa a ListView com todos os clientes
        clientesListView.setItems(Repositorio.getClientes());

        // Configuração do ListCell para exibir o nome do cliente
        clientesListView.setCellFactory(param -> new ListCell<Cliente>() {
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
    }

    // Método para filtrar os clientes com base no nome
    @FXML
    private void filtrarClientes() {
        String nomePesquisa = nomeClienteField.getText().toLowerCase().trim();

        // Filtra a lista de clientes com base no nome digitado
        ObservableList<Cliente> clientesFiltrados = FXCollections.observableArrayList();
        for (Cliente cliente : Repositorio.getClientes()) {
            if (cliente.getNome().toLowerCase().contains(nomePesquisa)) {
                clientesFiltrados.add(cliente);
            }
        }
        // Atualiza a ListView com os clientes filtrados
        clientesListView.setItems(clientesFiltrados);
    }

    @FXML
    private void mostrarHistorico(MouseEvent event) {
        Cliente clienteSelecionado = clientesListView.getSelectionModel().getSelectedItem();

        if (clienteSelecionado != null) {
            // Limpa o histórico de serviços anterior
            historicoListView.getItems().clear();

            // Exibe o histórico de serviços do cliente selecionado
            for (ServicoRealizado servicoRealizado : clienteSelecionado.getHistoricoServicos()) {
                String dataHoraFormatada = servicoRealizado.getDataHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                String descricao = "Serviço: " + servicoRealizado.getServico().getTipo() +
                        " - Preço: R$ " + servicoRealizado.getServico().getPreco() +
                        " - Data: " + dataHoraFormatada;
                historicoListView.getItems().add(descricao);
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}
