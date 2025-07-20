package org.example.barber.DAO;

import org.example.barber.database.ConexaoSQLite;
import org.example.barber.entities.Agendamento;
import org.example.barber.entities.Cliente;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AgendamentoDAO {

    // Método para criar a tabela no banco de dados
    public static void criarTabela() {
        String sql = """
            CREATE TABLE IF NOT EXISTS agendamentos (
                cliente_id INTEGER NOT NULL,
                data TEXT NOT NULL, -- formato: yyyy-MM-dd
                hora TEXT NOT NULL, -- formato: HH:mm:ss
                FOREIGN KEY (cliente_id) REFERENCES clientes(id)
            )
        """;

        try (Connection conn = ConexaoSQLite.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela de agendamentos:");
            e.printStackTrace();
        }
    }

    // Método para inserir um novo agendamento
    public static void inserir(Agendamento agendamento) {
        String sql = "INSERT INTO agendamentos(cliente_id, data, hora) VALUES(?, ?, ?)";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, agendamento.getCliente().getId());
            stmt.setString(2, agendamento.getDataHora().toLocalDate().toString());
            stmt.setString(3, agendamento.getDataHora().toLocalTime().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir agendamento:");
            e.printStackTrace();
        }
    }

    // Método para listar todos os agendamentos
    public static List<Agendamento> listarTodos() {
        List<Agendamento> agendamentos = new ArrayList<>();

        String sql = """
            SELECT a.*, c.id AS cliente_id, c.nome, c.telefone, c.data_nascimento
            FROM agendamentos a
            JOIN clientes c ON a.cliente_id = c.id
            ORDER BY a.data, a.hora
        """;

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("cliente_id"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        LocalDate.parse(rs.getString("data_nascimento"))
                );

                LocalDate data = LocalDate.parse(rs.getString("data"));
                LocalTime hora = LocalTime.parse(rs.getString("hora"));
                LocalDateTime dataHora = LocalDateTime.of(data, hora);

                Agendamento agendamento = new Agendamento(cliente, dataHora);
                agendamentos.add(agendamento);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao listar agendamentos:");
            e.printStackTrace();
        }

        return agendamentos;
    }

    // Método para buscar agendamentos por nome do cliente
    public static List<Agendamento> buscarPorCliente(String nomeCliente) {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sql = """
            SELECT a.*, c.id AS cliente_id, c.nome, c.telefone, c.data_nascimento
            FROM agendamentos a
            JOIN clientes c ON a.cliente_id = c.id
            WHERE c.nome LIKE ?
            ORDER BY a.data, a.hora
        """;

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomeCliente + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("cliente_id"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        LocalDate.parse(rs.getString("data_nascimento"))
                );

                LocalDate data = LocalDate.parse(rs.getString("data"));
                LocalTime hora = LocalTime.parse(rs.getString("hora"));
                LocalDateTime dataHora = LocalDateTime.of(data, hora);

                Agendamento agendamento = new Agendamento(cliente, dataHora);
                agendamentos.add(agendamento);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar agendamentos por cliente:");
            e.printStackTrace();
        }

        return agendamentos;
    }

    // Método para atualizar o agendamento
    public static void atualizar(Agendamento agendamento) {
        String sql = "UPDATE agendamentos SET data = ?, hora = ? WHERE cliente_id = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, agendamento.getDataHora().toLocalDate().toString());
            stmt.setString(2, agendamento.getDataHora().toLocalTime().toString());
            stmt.setInt(3, agendamento.getCliente().getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar agendamento:");
            e.printStackTrace();
        }
    }

    // Método para remover um agendamento
    public static void remover(Agendamento agendamento) {
        String sql = "DELETE FROM agendamentos WHERE cliente_id = ? AND data = ? AND hora = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, agendamento.getCliente().getId());
            stmt.setString(2, agendamento.getDataHora().toLocalDate().toString());
            stmt.setString(3, agendamento.getDataHora().toLocalTime().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao remover agendamento:");
            e.printStackTrace();
        }
    }
}
