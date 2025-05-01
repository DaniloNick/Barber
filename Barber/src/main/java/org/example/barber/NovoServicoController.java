package org.example.barber;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDateTime;

public class NovoServicoController {

    @FXML
    private TextField nomeClienteField;

    @FXML
    private ComboBox<Servico> tipoServicoComboBox;

    @FXML
    private void initialize() {
        tipoServicoComboBox.setItems(Repositorio.getServicos());

        // Exibe tipo e preço no ComboBox
        tipoServicoComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Servico servico, boolean empty) {
                super.updateItem(servico, empty);
                setText(empty || servico == null ? null : servico.getTipo() + " - R$ " + servico.getPreco());
            }
        });

        tipoServicoComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Servico servico, boolean empty) {
                super.updateItem(servico, empty);
                setText(empty || servico == null ? null : servico.getTipo() + " - R$ " + servico.getPreco());
            }
        });
    }

    @FXML
    private void adicionarServico() {
        String nomeCliente = nomeClienteField.getText().trim();
        Servico servicoSelecionado = tipoServicoComboBox.getValue();

        if (nomeCliente.isEmpty()) {
            mostrarAlerta("Erro", "Por favor, insira o nome do cliente.");
            return;
        }

        if (servicoSelecionado == null) {
            mostrarAlerta("Erro", "Por favor, selecione um tipo de serviço.");
            return;
        }

        Cliente cliente = Repositorio.buscarClientePorNome(nomeCliente);
        if (cliente == null) {
            mostrarAlerta("Erro", "Cliente não encontrado.");
            return;
        }

        ServicoRealizado servicoRealizado = new ServicoRealizado(servicoSelecionado, LocalDateTime.now());
        cliente.getHistoricoServicos().add(servicoRealizado);

        mostrarAlerta("Sucesso", "Serviço adicionado ao histórico do cliente!");
        limparCampos();
    }

    private void limparCampos() {
        nomeClienteField.clear();
        tipoServicoComboBox.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    @FXML
    private void voltarMenu() {
        Application.trocarTela("servico.fxml", "Barber Shop - Servicos");
    }
}
