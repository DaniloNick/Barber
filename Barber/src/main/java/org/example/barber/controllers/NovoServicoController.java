package org.example.barber.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.barber.Application;
import org.example.barber.DAO.ClienteDAO;
import org.example.barber.DAO.ServicoDAO;
import org.example.barber.DAO.ServicoRealizadoDAO;
import org.example.barber.entities.Cliente;
import org.example.barber.entities.Servico;
import org.example.barber.entities.ServicoRealizado;
import org.example.barber.entities.Sessao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class NovoServicoController {

    @FXML private TextField nomeClienteField;
    @FXML private ComboBox<Servico> tipoServicoComboBox;
    @FXML private ListView<Servico> listaServicos;
    @FXML private ListView<Cliente> buscaCliente;
    @FXML private Label totalServicos;
    @FXML private Label avisoServico;

    private Cliente clienteSelecionado;
    private final ObservableList<Servico> servicosSelecionados = FXCollections.observableArrayList();
    private List<Cliente> todosClientes;

    @FXML
    private void initialize() {
        // Carregar clientes uma única vez
        todosClientes = ClienteDAO.listarTodos();

        tipoServicoComboBox.setItems(FXCollections.observableArrayList(ServicoDAO.buscarTodos()));
        listaServicos.setItems(servicosSelecionados);
        buscaCliente.setItems(FXCollections.observableArrayList());

        // Formatação de exibição dos clientes na ListView
        buscaCliente.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Cliente cliente, boolean empty) {
                super.updateItem(cliente, empty);
                if (empty || cliente == null) {
                    setText(null);
                } else {
                    setText(cliente.getNome() + " | " + cliente.getTelefone());
                }
            }
        });

        // Filtro conforme digitação
        nomeClienteField.textProperty().addListener((obs, oldVal, newVal) -> filtrarClientes());

        // Seleção de cliente
        buscaCliente.setOnMouseClicked(e -> {
            clienteSelecionado = buscaCliente.getSelectionModel().getSelectedItem();
            if (clienteSelecionado != null) {
                nomeClienteField.setText(clienteSelecionado.getNome());
                buscaCliente.getItems().clear();
                avisoServico.setText("");
            }
        });

        // Remover serviço com duplo clique
        listaServicos.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Servico servico = listaServicos.getSelectionModel().getSelectedItem();
                if (servico != null) {
                    servicosSelecionados.remove(servico);
                    atualizarTotal();
                }
            }
        });

        atualizarTotal();
    }

    private void filtrarClientes() {
        String busca = nomeClienteField.getText().toLowerCase().trim();

        if (busca.isEmpty()) {
            buscaCliente.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Cliente> filtrados = todosClientes.stream()
                .filter(c -> c.getNome().toLowerCase().contains(busca))
                .collect(Collectors.toList());

        buscaCliente.setItems(FXCollections.observableArrayList(filtrados));
    }

    @FXML
    private void adicionarServico() {
        Servico servico = tipoServicoComboBox.getValue();
        if (servico != null) {
            servicosSelecionados.add(servico);
            atualizarTotal();
            avisoServico.setText("");
        } else {
            avisoServico.setText("Selecione um serviço antes de adicionar.");
        }
    }

    private void atualizarTotal() {
        double total = servicosSelecionados.stream().mapToDouble(Servico::getPreco).sum();
        totalServicos.setText(String.format("Total: R$ %.2f", total));
    }

    @FXML
    private void finalizarAtendimento() {
        if (clienteSelecionado == null) {
            avisoServico.setText("Selecione um cliente válido.");
            return;
        }
        if (servicosSelecionados.isEmpty()) {
            avisoServico.setText("Adicione pelo menos um serviço.");
            return;
        }

        for (Servico servico : servicosSelecionados) {
            Servico servicoSelecionado = ServicoDAO.buscarPorId(servico.getId());
            ServicoRealizado realizado = new ServicoRealizado(clienteSelecionado, servicoSelecionado, LocalDateTime.now(), Sessao.usuarioLogado);
            ServicoRealizadoDAO.salvar(realizado);
        }

        avisoServico.setText("Atendimento registrado com sucesso!");
        limparCampos();
    }

    private void limparCampos() {
        servicosSelecionados.clear();
        nomeClienteField.clear();
        buscaCliente.getItems().clear();
        clienteSelecionado = null;
        tipoServicoComboBox.getSelectionModel().clearSelection();
        atualizarTotal();
    }

    @FXML
    private void voltarMenu() {
        Application.trocarTela("menu.fxml", "Barber Shop - Menu");
    }
}
