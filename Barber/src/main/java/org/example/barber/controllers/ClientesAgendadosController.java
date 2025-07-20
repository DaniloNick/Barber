package org.example.barber.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.barber.Application;
import org.example.barber.entities.Agendamento;
import org.example.barber.DAO.AgendamentoDAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClientesAgendadosController {

    @FXML
    private ListView<Agendamento> agendamentosListView;

    @FXML
    private DatePicker dataPicker;

    @FXML
    private ComboBox<LocalTime> horaComboBox;

    @FXML
    private Button editarButton;

    @FXML
    private Button removerButton;

    @FXML
    private Label avisoClientesAgendados;

    @FXML
    private TextField buscarNomeTextField;

    private ObservableList<Agendamento> agendamentos = FXCollections.observableArrayList(); // Lista observável para filtrar
    private ObservableList<Agendamento> agendamentosFiltrados = FXCollections.observableArrayList(); // Lista filtrada

    @FXML
    private void initialize() {
        // Carregar todos os agendamentos da base de dados
        agendamentos.setAll(AgendamentoDAO.listarTodos()); // Assumindo que você tem um método para buscar todos os agendamentos

        // Configurar a ListView para exibir os agendamentos
        agendamentosListView.setItems(agendamentos);

        // Formatação da ListView para exibir os agendamentos
        agendamentosListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Agendamento agendamento, boolean empty) {
                super.updateItem(agendamento, empty);
                if (empty || agendamento == null) {
                    setText(null);
                } else {
                    String dataFormatada = agendamento.getDataHora().toLocalDate()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    String horaFormatada = agendamento.getDataHora().toLocalTime().toString();

                    setText("Cliente: " + agendamento.getCliente().getNome() +
                            " - Data: " + dataFormatada +
                            " - Hora: " + horaFormatada);
                }
            }
        });

        // Preenche a ComboBox de horas (de 08:00 às 20:00)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime hora = LocalTime.of(8, 0);
        while (!hora.isAfter(LocalTime.of(20, 0))) {
            horaComboBox.getItems().add(hora);
            hora = hora.plusMinutes(30);
        }

        horaComboBox.setCellFactory(cb -> new ListCell<>() {
            @Override
            protected void updateItem(LocalTime time, boolean empty) {
                super.updateItem(time, empty);
                setText(empty || time == null ? null : time.format(formatter));
            }
        });
        horaComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(LocalTime time, boolean empty) {
                super.updateItem(time, empty);
                setText(empty || time == null ? null : time.format(formatter));
            }
        });

        // Listener para filtrar a lista conforme o usuário digita no campo de busca
        buscarNomeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarAgendamentosPorNome(newValue);
        });
    }

    // Método para filtrar os agendamentos pela parte do nome
    private void filtrarAgendamentosPorNome(String nomeParcial) {
        agendamentosFiltrados.clear();

        // Se não houver texto no campo, exibe todos os agendamentos
        if (nomeParcial.isEmpty()) {
            agendamentosFiltrados.addAll(agendamentos);
        } else {
            // Caso contrário, filtra os agendamentos com base no nome
            for (Agendamento agendamento : agendamentos) {
                if (agendamento.getCliente().getNome().toLowerCase().contains(nomeParcial.toLowerCase())) {
                    agendamentosFiltrados.add(agendamento);
                }
            }
        }

        // Atualiza a ListView com a lista filtrada
        agendamentosListView.setItems(agendamentosFiltrados);
    }

    @FXML
    private void editarAgendamento() {
        Agendamento selecionado = agendamentosListView.getSelectionModel().getSelectedItem();
        LocalDate novaData = dataPicker.getValue();
        LocalTime novaHora = horaComboBox.getValue();

        if (selecionado != null && novaData != null && novaHora != null) {
            // Atualiza a data e hora do agendamento
            selecionado.setDataHora(LocalDateTime.of(novaData, novaHora));

            // Atualiza na base de dados
            AgendamentoDAO.atualizar(selecionado);

            // Atualiza a ListView e a mensagem de aviso
            agendamentosListView.refresh();
            avisoClientesAgendados.setText("Agendamento atualizado com sucesso.");
        } else {
            avisoClientesAgendados.setText("Selecione um agendamento e preencha data e hora.");
        }
    }

    @FXML
    private void removerAgendamento() {
        Agendamento selecionado = agendamentosListView.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            // Remover o agendamento
            AgendamentoDAO.remover(selecionado);

            // Atualizar a ListView
            agendamentos.remove(selecionado);
            avisoClientesAgendados.setText("Agendamento removido.");
        } else {
            avisoClientesAgendados.setText("Selecione um agendamento para remover.");
        }
    }

    @FXML
    private void voltarMenu() {
        Application.trocarTela("agendamento.fxml", "Barber Shop - Agendamento");
    }
}
