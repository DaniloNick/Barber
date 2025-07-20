package org.example.barber.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.example.barber.Application;
import org.example.barber.DAO.ServicoDAO;
import org.example.barber.entities.Servico;

public class CadastrarServicoController {

    @FXML private TextField nomeServicoField;
    @FXML private TextField precoServicoField;
    @FXML private TextField comissaoField;
    @FXML private Label avisoGerenServico;

    @FXML private TableView<Servico> tabelaServicos;
    @FXML private TableColumn<Servico, String> colNome;
    @FXML private TableColumn<Servico, String> colPreco;
    @FXML private TableColumn<Servico, String> colComissaoReais;

    private ObservableList<Servico> servicos = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Configura as colunas da TableView
        colNome.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));

        colPreco.setCellValueFactory(cellData -> {
            double preco = cellData.getValue().getPreco();
            String precoFormatado = String.format("R$ %.2f", preco);
            return new SimpleStringProperty(precoFormatado);
        });

        colComissaoReais.setCellValueFactory(cellData -> {
            double preco = cellData.getValue().getPreco();
            double comissao = cellData.getValue().getComissao();
            double valorComissao = calcularComissaoReais(preco, comissao);
            String comissaoFormatada = String.format("R$ %.2f", valorComissao);
            return new SimpleStringProperty(comissaoFormatada);
        });

        // Carrega os dados para a tabela
        carregarServicos();

        // Preenche os campos ao selecionar um serviço na tabela
        tabelaServicos.getSelectionModel().selectedItemProperty().addListener((obs, antigo, selecionado) -> {
            if (selecionado != null) {
                nomeServicoField.setText(selecionado.getNome());
                precoServicoField.setText(String.valueOf(selecionado.getPreco()));
                comissaoField.setText(String.valueOf(selecionado.getComissao()));
            }
        });
    }


    private void carregarServicos() {
        servicos.setAll(ServicoDAO.buscarTodos());
        tabelaServicos.setItems(servicos);
    }

    private double calcularComissaoReais(double preco, double comissaoPercentual) {
        return preco * (comissaoPercentual / 100.0);
    }

    @FXML
    private void salvarServico() {
        String nome = nomeServicoField.getText().trim();
        String precoTexto = precoServicoField.getText().trim();
        String comissaoTexto = comissaoField.getText().trim();

        if (nome.isEmpty() || precoTexto.isEmpty() || comissaoTexto.isEmpty()) {
            avisoGerenServico.setText("Preencha todos os campos.");
            return;
        }

        try {
            double preco = Double.parseDouble(precoTexto);
            double comissao = Double.parseDouble(comissaoTexto);
            Servico selecionado = tabelaServicos.getSelectionModel().getSelectedItem();

            if (selecionado != null) {
                // Editar
                selecionado.setNome(nome);
                selecionado.setPreco(preco);
                selecionado.setComissao(comissao);
                ServicoDAO.atualizar(selecionado);
            } else {
                // Inserir
                ServicoDAO.inserir(new Servico(nome, preco, comissao));
            }

            limparCampos();
            carregarServicos();
            avisoGerenServico.setText("Serviço salvo com sucesso!");
        } catch (NumberFormatException e) {
            avisoGerenServico.setText("Campos numéricos inválidos.");
        }
    }

    @FXML
    private void removerServico() {
        Servico selecionado = tabelaServicos.getSelectionModel().getSelectedItem();
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
        tabelaServicos.getSelectionModel().clearSelection();
    }

    @FXML
    private void voltarMenu() {
        Application.trocarTela("menu.fxml", "Barber Shop - Menu Principal");
    }
}
