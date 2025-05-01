package org.example.barber;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class Repositorio {
    // Lista de clientes e serviços utilizando ObservableList para facilitar a atualização na interface gráfica
    private static final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private static final ObservableList<Servico> servicos = FXCollections.observableArrayList();

    // Retorna a lista de clientes
    public static ObservableList<Cliente> getClientes() {
        return clientes;
    }

    // Retorna a lista de serviços
    public static ObservableList<Servico> getServicos() {
        return servicos;
    }

    // Adiciona um novo cliente à lista
    public static void adicionarCliente(Cliente cliente) {
        clientes.add(cliente);
    }

    // Adiciona um novo serviço à lista
    public static void adicionarServico(Servico servico) {
        servicos.add(servico);
    }

    // Método para buscar um cliente por nome
    public static Cliente buscarClientePorNome(String nome) {
        for (Cliente cliente : clientes) {
            if (cliente.getNome().toLowerCase().contains(nome.toLowerCase().trim())) {
                return cliente; // Retorna um único cliente
            }
        }
        return null; // Retorna null se não encontrar o cliente
    }

    private static final ObservableList<Agendamento> agendamentos = FXCollections.observableArrayList();

    public static ObservableList<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    public static void adicionarAgendamento(Agendamento agendamento) {
        agendamentos.add(agendamento);
    }

    public static void removerAgendamento(Agendamento agendamento) {
        agendamentos.remove(agendamento);
    }


}
