package org.example.barber;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CadastrarServicoController {

    @FXML
    private TextField nomeServicoField;

    @FXML
    private TextField precoServicoField;

    @FXML
    private ListView<Servico> listaServicos;

    @FXML
    private Button salvarButton;

    @FXML
    private void initialize() {
        listaServicos.setItems(Repositorio.getServicos());

        listaServicos.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Servico servico, boolean empty) {
                super.updateItem(servico, empty);
                if (empty || servico == null) {
                    setText(null);
                } else {
                    setText(servico.getTipo() + " - R$ " + servico.getPreco());
                }
            }
        });

        listaServicos.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                nomeServicoField.setText(selecionado.getTipo());
                precoServicoField.setText(String.valueOf(selecionado.getPreco()));
            }
        });
    }

    @FXML
    private void salvarServico() {
        String nome = nomeServicoField.getText().trim();
        String precoTexto = precoServicoField.getText().trim();

        if (nome.isEmpty() || precoTexto.isEmpty()) {
            mostrarAlerta("Erro", "Preencha todos os campos.");
            return;
        }

        try {
            double preco = Double.parseDouble(precoTexto);
            Servico selecionado = listaServicos.getSelectionModel().getSelectedItem();

            if (selecionado != null) {
                // Editar serviço existente
                selecionado.setTipo(nome);
                selecionado.setPreco(preco);
                listaServicos.refresh();
            } else {
                // Adicionar novo serviço
                Repositorio.adicionarServico(new Servico(nome, preco));
            }

            limparCampos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro", "Preço inválido. Use números.");
        }
    }

    @FXML
    private void removerServico() {
        Servico selecionado = listaServicos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            Repositorio.getServicos().remove(selecionado);
            limparCampos();
        } else {
            mostrarAlerta("Erro", "Selecione um serviço para remover.");
        }
    }

    private void limparCampos() {
        nomeServicoField.clear();
        precoServicoField.clear();
        listaServicos.getSelectionModel().clearSelection();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    @FXML
    private void voltarMenu() {
        Application.trocarTela("servico.fxml", "Barber Shop - Servicos");
    }
}
