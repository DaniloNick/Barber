package org.example.barber.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.barber.Application;
import org.example.barber.DAO.ClienteDAO;
import org.example.barber.entities.Cliente;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CadastroController {

    @FXML private TextField nomeField;
    @FXML private TextField telefoneField;
    @FXML private TextField dataNascimentoField;
    @FXML private Label avisoCadastroCliente;

    @FXML
    public void initialize() {
        // Formatar automaticamente a data no formato DD/MM/AAAA
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
    }

    @FXML
    protected void salvarCliente() {
        String nome = nomeField.getText();
        String telefone = telefoneField.getText();
        String dataStr = dataNascimentoField.getText();

        if (nome.isEmpty() || telefone.isEmpty() || dataStr.isEmpty()) {
            avisoCadastroCliente.setText("Todos os campos devem ser preenchidos.");
            return;
        }

        LocalDate data;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            data = LocalDate.parse(dataStr, formatter);
        } catch (DateTimeParseException e) {
            avisoCadastroCliente.setText("Por favor, insira a data no formato correto (DD/MM/AAAA).");
            return;
        }

        Cliente novo = new Cliente(nome, telefone, data);

        boolean sucesso = ClienteDAO.inserir(novo);
        if (sucesso) {
            avisoCadastroCliente.setText("Cliente cadastrado com sucesso!");
            nomeField.clear();
            telefoneField.clear();
            dataNascimentoField.clear();
        } else {
            avisoCadastroCliente.setText("Erro ao cadastrar cliente.");
        }
    }

    @FXML
    protected void voltarMenu() {
        Application.trocarTela("menu.fxml", "Barber Shop - Menu Principal");
    }
}
