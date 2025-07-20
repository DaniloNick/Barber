package org.example.barber.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.barber.Application;
import org.example.barber.DAO.ServicoDAO;
import org.example.barber.entities.Servico;

public class CadastrarServicoController {

    @FXML
    private TextField nomeServicoField;

    @FXML
    private TextField precoServicoField;

    @FXML
    private ListView<Servico> listaServicos;

    @FXML
    private Label avisoGerenServico;

    @FXML
    private Button salvarButton;

    @FXML
    private TextField comissaoField;


    private ObservableList<Servico> servicos = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        carregarServicos();

        listaServicos.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Servico servico, boolean empty) {
                super.updateItem(servico, empty);
                if (empty || servico == null) {
                    setText(null);
                } else {
                    double valorComissao = servico.getPreco() * (servico.getComissao() / 100.0);
                    setText(servico.getNome() + " - R$ "
                            + String.format("%.2f", servico.getPreco())
                            + " | Comissão: R$ " + String.format("%.2f", valorComissao) + " ("+servico.getComissao()+"%)" );
                }
            }
        });

        listaServicos.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                nomeServicoField.setText(selecionado.getNome());
                precoServicoField.setText(String.valueOf(selecionado.getPreco()));
                comissaoField.setText(String.valueOf(selecionado.getComissao()));
            }
        });
    }

    private void carregarServicos() {
        servicos.setAll(ServicoDAO.buscarTodos());
        listaServicos.setItems(servicos);
    }

    @FXML
    private void salvarServico() {
        String nome = nomeServicoField.getText().trim();
        String precoTexto = precoServicoField.getText().trim();
        String comiisaoTexto = comissaoField.getText().trim();

        if (nome.isEmpty() || precoTexto.isEmpty() || comiisaoTexto.isEmpty()) {
            avisoGerenServico.setText("Preencha todos os campos.");
            return;
        }

        try {
            double preco = Double.parseDouble(precoTexto);
            double comissao = Double.parseDouble(comiisaoTexto);
            Servico selecionado = listaServicos.getSelectionModel().getSelectedItem();

            if (selecionado != null) {
                // Editar serviço existente
                selecionado.setNome(nome);
                selecionado.setPreco(preco);
                selecionado.setComissao(comissao);
                ServicoDAO.atualizar(selecionado);
            } else {
                // Adicionar novo serviço
                ServicoDAO.inserir(new Servico(nome, preco, comissao));
            }

            limparCampos();
            carregarServicos();
            avisoGerenServico.setText("Serviço salvo com sucesso!");
        } catch (NumberFormatException e) {
            avisoGerenServico.setText("Preço inválido. Use números.");
        }
    }

    @FXML
    private void removerServico() {
        Servico selecionado = listaServicos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            ServicoDAO.excluir(selecionado.getId());
            limparCampos();
            carregarServicos();
            avisoGerenServico.setText("Serviço removido.");
        } else {
            avisoGerenServico.setText("Selecione um serviço para remover.");
        }
    }

    private void limparCampos() {
        nomeServicoField.clear();
        precoServicoField.clear();
        comissaoField.clear();
        listaServicos.getSelectionModel().clearSelection();
    }

    @FXML
    private void voltarMenu() {
        Application.trocarTela("menu.fxml", "Barber Shop - Menu Principal");
    }
}
