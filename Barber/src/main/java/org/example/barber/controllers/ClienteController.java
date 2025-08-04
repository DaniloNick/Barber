package org.example.barber.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import org.example.barber.Application;
import org.example.barber.DAO.ClienteDAO;
import org.example.barber.DAO.UsuarioDAO;
import org.example.barber.entities.Cliente;
import org.example.barber.entities.Usuario;
import org.example.barber.entities.Sessao;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class ClienteController {

    @FXML private TextField buscaNomeField;
    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableColumn<Cliente, String> colunaNome;
    @FXML private TableColumn<Cliente, String> colunaTelefone;
    @FXML private TableColumn<Cliente, String> colunaNascimento;

    @FXML private TextField nomeField;
    @FXML private TextField telefoneField;
    @FXML private TextField dataNascimentoField;
    @FXML private Label avisoLabel;

    private ObservableList<Cliente> clientesObservable = FXCollections.observableArrayList();

    private Cliente clienteSelecionado = null;

    @FXML
    public void initialize() {
        // Configura as colunas da tabela
        colunaNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
        colunaTelefone.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTelefone()));
        colunaNascimento.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        tabelaClientes.setItems(clientesObservable);

        carregarTodosClientes();

        // Configura seleção na tabela para carregar dados para edição
        tabelaClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                clienteSelecionado = newSel;
                carregarDadosCliente(clienteSelecionado);
                avisoLabel.setText("");
            }
        });

        // Formatação automática da data
        dataNascimentoField.textProperty().addListener((obs, oldText, newText) -> {
            String somenteNumeros = newText.replaceAll("[^0-9]", "");
            StringBuilder formatado = new StringBuilder();

            int maxLength = Math.min(somenteNumeros.length(), 8);
            for (int i = 0; i < maxLength; i++) {
                formatado.append(somenteNumeros.charAt(i));
                if (i == 1 || i == 3) {
                    formatado.append("/");
                }
            }

            if (!newText.equals(formatado.toString())) {
                dataNascimentoField.setText(formatado.toString());
                dataNascimentoField.positionCaret(formatado.length());
            }
        });

        // Atualiza busca ao digitar nome parcial
        buscaNomeField.textProperty().addListener((obs, oldText, newText) -> {
            buscarClientes(newText);
        });
    }

    private void carregarTodosClientes() {
        List<Cliente> clientes = ClienteDAO.listarTodos();
        clientesObservable.setAll(clientes);
    }

    private void buscarClientes(String nomeParcial) {
        if (nomeParcial == null || nomeParcial.isEmpty()) {
            carregarTodosClientes();
            return;
        }
        List<Cliente> encontrados = ClienteDAO.buscarPorNomeParcial(nomeParcial);
        clientesObservable.setAll(encontrados);
    }

    private void carregarDadosCliente(Cliente cliente) {
        nomeField.setText(cliente.getNome());
        telefoneField.setText(cliente.getTelefone());
        dataNascimentoField.setText(cliente.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    @FXML
    protected void salvarCliente() {
        String nome = nomeField.getText();
        String telefone = telefoneField.getText();
        String dataStr = dataNascimentoField.getText();

        if (nome.isEmpty() || telefone.isEmpty() || dataStr.isEmpty()) {
            avisoLabel.setText("Todos os campos devem ser preenchidos.");
            return;
        }

        LocalDate data;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            data = LocalDate.parse(dataStr, formatter);
        } catch (DateTimeParseException e) {
            avisoLabel.setText("Data inválida. Use o formato DD/MM/AAAA.");
            return;
        }

        if (clienteSelecionado == null) {
            // Cadastrar novo cliente
            Cliente novo = new Cliente(nome, telefone, data);
            boolean inserido = ClienteDAO.inserir(novo);
            if (inserido) {
                avisoLabel.setText("Cliente cadastrado com sucesso!");
                nomeField.clear();
                telefoneField.clear();
                dataNascimentoField.clear();
                carregarTodosClientes();
            } else {
                avisoLabel.setText("Erro ao cadastrar cliente.");
            }
        } else {
            // Atualizar cliente existente
            clienteSelecionado.setNome(nome);
            clienteSelecionado.setTelefone(telefone);
            clienteSelecionado.setDataNascimento(data);

            boolean atualizado = ClienteDAO.atualizar(clienteSelecionado);
            if (atualizado) {
                avisoLabel.setText("Cliente atualizado com sucesso!");
                carregarTodosClientes();
            } else {
                avisoLabel.setText("Erro ao atualizar cliente.");
            }
        }
    }


    @FXML
    protected void excluirCliente() {
        if (clienteSelecionado == null) {
            avisoLabel.setText("Selecione um cliente para excluir.");
            return;
        }

        // Dialog para autenticação do usuário logado
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Confirmação de exclusão");
        dialog.setHeaderText("Digite a senha do usuário logado para confirmar exclusão: " + Sessao.usuarioLogado.getNome());

        ButtonType okButtonType = new ButtonType("Confirmar", ButtonBar.ButtonData.OK_DONE);
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

        Platform.runLater(passwordField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return passwordField.getText();
            }
            return null;
        });

        Optional<String> resultado = dialog.showAndWait();

        if (resultado.isPresent()) {
            String senhaDigitada = resultado.get();
            boolean senhaCorreta = UsuarioDAO.validarSenha(Sessao.usuarioLogado.getNome(), senhaDigitada);
            if (!senhaCorreta) {
                avisoLabel.setText("Senha incorreta. Exclusão cancelada.");
                return;
            }
        } else {
            avisoLabel.setText("Exclusão cancelada.");
            return;
        }

        boolean excluido = ClienteDAO.excluir(clienteSelecionado);
        if (excluido) {
            avisoLabel.setText("Cliente excluído com sucesso.");
            clienteSelecionado = null;
            nomeField.clear();
            telefoneField.clear();
            dataNascimentoField.clear();
            carregarTodosClientes();
        } else {
            avisoLabel.setText("Erro ao excluir cliente.");
        }
    }

    @FXML
    protected void voltarMenu() {
        Application.trocarTela("menu.fxml", "Barber Shop - Menu Principal");
    }

}
