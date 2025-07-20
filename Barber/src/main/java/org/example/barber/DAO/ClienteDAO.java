package org.example.barber.DAO;

import org.example.barber.database.ConexaoSQLite;
import org.example.barber.entities.Cliente;
import org.example.barber.entities.ServicoRealizado;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public static void criarTabela() {
        String sql = """
            CREATE TABLE IF NOT EXISTS clientes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                telefone TEXT,
                dataNascimento TEXT
            )
        """;

        try (Connection conn = ConexaoSQLite.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela de clientes:");
            e.printStackTrace();
        }
    }

    public static boolean inserir(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome, telefone, data_nascimento) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getDataNascimento().toString());
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            System.err.println("Erro ao inserir cliente:");
            e.printStackTrace();
            return false;
        }
    }

    public static List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String telefone = rs.getString("telefone");
                LocalDate dataNascimento = LocalDate.parse(rs.getString("data_nascimento"));

                Cliente cliente = new Cliente(id, nome, telefone, dataNascimento);

                // Carrega histórico de serviços do cliente
                List<ServicoRealizado> historico = ServicoRealizadoDAO.listarPorCliente(id);
                cliente.setHistoricoServicos(historico);

                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }

    public static List<Cliente> buscarPorNomeParcial(String nomeParcial) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE nome LIKE ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nomeParcial + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        LocalDate.parse(rs.getString("data_nascimento"))

                );
                clientes.add(cliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clientes;
    }


    // Método para buscar cliente por ID
    public static Cliente buscarPorId(int id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int clienteId = rs.getInt("id");
                String nome = rs.getString("nome");
                String telefone = rs.getString("telefone");
                Date dataNascimento = rs.getDate("data_nascimento");

                // Retorna o objeto Cliente com os dados do banco
                return new Cliente(clienteId, nome, telefone, dataNascimento.toLocalDate());
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar cliente por ID:");
            e.printStackTrace();
        }

        return null;
    }

    public static List<ServicoRealizado> buscarServicosRealizados(int clienteId) {
        return ServicoRealizadoDAO.listarPorCliente(clienteId);
    }

}
