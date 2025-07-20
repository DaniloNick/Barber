package org.example.barber.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.barber.Application;
import org.example.barber.DAO.AgendamentoDAO;
import org.example.barber.DAO.ClienteDAO;
import org.example.barber.entities.Agendamento;
import org.example.barber.entities.Cliente;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class AgendarClienteController {

    @FXML
    private TextField nomeClienteField;

    @FXML
    private DatePicker dataPicker;

    @FXML
    private Spinner<LocalTime> horaSpinner;

    @FXML
    private ListView<Cliente> agendarClienteLista; // ListView para mostrar os clientes encontrados

    @FXML
    private Label avisoAgendarCliente;

    @FXML
    private void initialize() {
        // Spinner de horário (hora:minuto)
        SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<LocalTime>() {
            {
                setConverter(new StringConverter<>() {
                    @Override
                    public String toString(LocalTime time) {
                        return time != null ? time.toString() : "";
                    }

                    @Override
                    public LocalTime fromString(String s) {
                        return LocalTime.parse(s);
                    }
                });
                setValue(LocalTime.of(9, 0));
            }

            @Override
            public void decrement(int steps) {
                setValue(getValue().minusMinutes(steps * 15));
            }

            @Override
            public void increment(int steps) {
                setValue(getValue().plusMinutes(steps * 15));
            }
        };

        horaSpinner.setValueFactory(valueFactory);
        horaSpinner.setEditable(true);

        // Definindo comportamento do ListView de clientes
        agendarClienteLista.getSelectionModel().setSelectionMode(SelectionMode.SINGLE); // Permitir selecionar um único cliente

        // Adicionando listener para o TextField
        nomeClienteField.textProperty().addListener((observable, oldValue, newValue) -> {
            atualizarListaClientes(newValue);
        });
    }

    private void atualizarListaClientes(String nome) {
        if (nome.isEmpty()) {
            agendarClienteLista.getItems().clear(); // Limpar lista se campo estiver vazio
            return;
        }

        // Busca clientes por nome parcial
        List<Cliente> clientes = ClienteDAO.buscarPorNomeParcial(nome);

        agendarClienteLista.getItems().clear(); // Limpa a lista antes de adicionar os novos resultados
        agendarClienteLista.getItems().addAll(clientes); // Adiciona os clientes encontrados
    }

    @FXML
    private void agendarCliente() {
        Cliente clienteSelecionado = agendarClienteLista.getSelectionModel().getSelectedItem();

        if (clienteSelecionado == null) {
            avisoAgendarCliente.setText("Selecione um cliente da lista.");
            return;
        }

        LocalDate data = dataPicker.getValue();
        LocalTime hora = horaSpinner.getValue();

        if (data == null || hora == null) {
            avisoAgendarCliente.setText("Preencha todos os campos.");
            return;
        }

        Agendamento novoAgendamento = new Agendamento(clienteSelecionado, LocalDateTime.of(data, hora));
        AgendamentoDAO.inserir(novoAgendamento);

        avisoAgendarCliente.setText("Agendamento realizado com sucesso!");
        limparCampos();
    }

    private void limparCampos() {
        nomeClienteField.clear();
        dataPicker.setValue(null);
        horaSpinner.getValueFactory().setValue(LocalTime.of(9, 0));
        agendarClienteLista.getItems().clear(); // Limpa a lista de clientes
    }

    @FXML
    private void voltarMenu() {
        Application.trocarTela("agendamento.fxml", "Barber Shop - Agendamento");
    }
}
