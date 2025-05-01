package org.example.barber;

import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    private void initialize() {
        agendamentosListView.setItems(Repositorio.getAgendamentos());

        // Formata como os agendamentos são exibidos
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

// Preenche a ComboBox com horas formatadas (08:00 até 23:00)
        LocalTime hora = LocalTime.of(8, 0);
        while (!hora.isAfter(LocalTime.of(20, 0))) {
            horaComboBox.getItems().add(hora);
            hora = hora.plusMinutes(30);
        }

// Define como os horários serão exibidos (formato HH:mm)
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

    }

    @FXML
    private void editarAgendamento() {
        Agendamento selecionado = agendamentosListView.getSelectionModel().getSelectedItem();
        LocalDate novaData = dataPicker.getValue();
        LocalTime novaHora = horaComboBox.getValue();

        if (selecionado != null && novaData != null && novaHora != null) {
            selecionado.setDataHora(LocalDateTime.of(novaData, novaHora));
            agendamentosListView.refresh();
            mostrarAlerta("Sucesso", "Agendamento atualizado com sucesso.");
        } else {
            mostrarAlerta("Erro", "Selecione um agendamento e preencha data e hora.");
        }
    }

    @FXML
    private void removerAgendamento() {
        Agendamento selecionado = agendamentosListView.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            Repositorio.removerAgendamento(selecionado);
            mostrarAlerta("Sucesso", "Agendamento removido.");
        } else {
            mostrarAlerta("Erro", "Selecione um agendamento para remover.");
        }
    }

    @FXML
    private void voltarMenu() {
        Application.trocarTela("agendamento.fxml", "Barber Shop - Agendamento");
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
