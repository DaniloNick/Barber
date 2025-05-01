package org.example.barber;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AgendarClienteController {

    @FXML
    private TextField nomeClienteField;

    @FXML
    private DatePicker dataPicker;

    @FXML
    private Spinner<LocalTime> horaSpinner;

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
    }

    @FXML
    private void agendarCliente() {
        String nome = nomeClienteField.getText().trim();
        LocalDate data = dataPicker.getValue();
        LocalTime hora = horaSpinner.getValue();

        if (nome.isEmpty() || data == null || hora == null) {
            mostrarAlerta("Erro", "Preencha todos os campos.");
            return;
        }

        Cliente cliente = Repositorio.buscarClientePorNome(nome);
        if (cliente == null) {
            mostrarAlerta("Erro", "Cliente não encontrado.");
            return;
        }

        Agendamento novoAgendamento = new Agendamento(cliente, LocalDateTime.of(data, hora));
        Repositorio.adicionarAgendamento(novoAgendamento);

        mostrarAlerta("Sucesso", "Agendamento realizado com sucesso!");
        limparCampos();
    }

    private void limparCampos() {
        nomeClienteField.clear();
        dataPicker.setValue(null);
        horaSpinner.getValueFactory().setValue(LocalTime.of(9, 0));
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    @FXML
    private void voltarMenu() {
        Application.trocarTela("agendamento.fxml", "Barber Shop - Agendamento");
    }
}
