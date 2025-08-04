package org.example.barber.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.barber.Application;
import org.example.barber.DAO.ClienteDAO;
import org.example.barber.DAO.ServicoDAO;
import org.example.barber.DAO.ServicoRealizadoDAO;
import org.example.barber.DAO.UsuarioDAO;
import org.example.barber.entities.*;

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
    @FXML
    private ComboBox<Usuario> barbeiroComboBox;


    private Cliente clienteSelecionado;
    private final ObservableList<Servico> servicosSelecionados = FXCollections.observableArrayList();
    private List<Cliente> todosClientes;

    @FXML
    private void initialize() {
        // Carregar clientes uma única vez
        todosClientes = ClienteDAO.listarTodos();
        ObservableList<Usuario> barbeiros = FXCollections.observableArrayList(UsuarioDAO.listarBarbeiros());
        barbeiroComboBox.setItems(barbeiros);

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

        Usuario barbeiroSelecionado = barbeiroComboBox.getSelectionModel().getSelectedItem();
        if (barbeiroSelecionado == null) {
            avisoServico.setText("Selecione um barbeiro.");
            return;
        }

        // Se o barbeiro escolhido não é o usuário logado → pedir senha do usuário logado
        if (barbeiroSelecionado.getId() != Sessao.usuarioLogado.getId()) {

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Autenticação necessária");

            String nome = Sessao.usuarioLogado.getNome();
            nome = nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();
            dialog.setHeaderText(nome + ", Voce esta transferindo o servico, Digite sua senha:");

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

            // Solicitar foco no campo senha
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

            var resultado = dialog.showAndWait();

            if (resultado.isEmpty()) {
                avisoServico.setText("Atendimento cancelado: autenticação não realizada.");
                return;
            }

            String senhaDigitada = resultado.get();
            boolean senhaCorreta = UsuarioDAO.validarSenha(Sessao.usuarioLogado.getNome(), senhaDigitada);
            if (!senhaCorreta) {
                avisoServico.setText("Senha incorreta. Atendimento não registrado.");
                return;
            }
        }

        // Registrar serviços usando o barbeiro selecionado
        for (Servico servico : servicosSelecionados) {
            Servico servicoCompleto = ServicoDAO.buscarPorId(servico.getId());
            ServicoRealizado realizado = new ServicoRealizado(
                    clienteSelecionado,
                    servicoCompleto,
                    LocalDateTime.now(),
                    barbeiroSelecionado
            );
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
