package org.example.barber.DAO;

import org.example.barber.controllers.RelatoriosController;
import org.example.barber.entities.*;
import org.example.barber.database.ConexaoSQLite;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ServicoRealizadoDAO {

    // Método para listar todos os serviços realizados por um cliente específico
    public static List<ServicoRealizado> listarPorCliente(int clienteId) {
        List<ServicoRealizado> lista = new ArrayList<>();
        String sql = """
                    SELECT sr.id, sr.data_hora, sr.barbeiro,
                           s.id AS servico_id, s.nome AS servico_nome, s.preco AS servico_preco, s.comissao AS servico_comissao
                    FROM servicos_realizados sr
                    JOIN servicos s ON sr.servico_id = s.id
                    WHERE sr.cliente_id = ?
                    ORDER BY sr.data_hora DESC
                
                
                """;

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");

                // Ajuste aqui: Leitura do timestamp Unix (milissegundos)
                long timestampMillis = rs.getLong("data_hora"); // Recebe como número (timestamp Unix)

                // Converte o timestamp Unix para LocalDateTime
                LocalDateTime dataHora = Instant.ofEpochMilli(timestampMillis)
                        .atZone(ZoneId.systemDefault())  // Ajuste de fuso horário
                        .toLocalDateTime();

                int servicoId = rs.getInt("servico_id");
                String servicoNome = rs.getString("servico_nome");
                double servicoPreco = rs.getDouble("servico_preco");
                String nomeBarbeiro = rs.getString("barbeiro");
                Usuario barbeiro = UsuarioDAO.buscarPorNome(nomeBarbeiro);
                double servicoComissao = rs.getDouble("servico_comissao");

                // Criando o objeto Servico
                Servico servico = new Servico(servicoId, servicoNome, servicoPreco, servicoComissao);

                // Buscando o Cliente pelo ID (clienteId)
                Cliente cliente = ClienteDAO.buscarPorId(clienteId); // Alteração aqui: buscamos o Cliente completo

                // Criando o objeto ServicoRealizado e adicionando à lista
                ServicoRealizado servicoRealizado = new ServicoRealizado(cliente, servico, dataHora, barbeiro);
                lista.add(servicoRealizado);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar serviços realizados para o cliente:");
            e.printStackTrace();
        }

        return lista;
    }


    public static void salvar(ServicoRealizado realizado) {
        String sql = "INSERT INTO servicos_realizados (cliente_id, servico_id, data_hora, barbeiro) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, realizado.getCliente().getId());
            stmt.setInt(2, realizado.getServico().getId());
            stmt.setTimestamp(3, Timestamp.valueOf(realizado.getDataHora()));
            stmt.setString(4, realizado.getBarbeiro().getNome());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar serviço realizado:");
            e.printStackTrace();
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM servicos_realizados WHERE id = ?";
        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ServicoRealizado> buscarPorBarbeiro(String termo) {
        List<ServicoRealizado> lista = new ArrayList<>();
        String sql = """
        SELECT sr.id, sr.data_hora, sr.barbeiro, sr.servico_id, 
               s.nome AS nomeServico, s.preco, 
               u.nome AS nomeBarbeiro, c.nome AS nomeCliente
        FROM servicos_realizados sr
        JOIN servicos s ON sr.servico_id = s.id
        JOIN usuarios u ON sr.barbeiro = u.nome
        JOIN clientes c ON sr.cliente_id = c.id
        WHERE u.nome LIKE ?
        ORDER BY sr.data_hora DESC
    """;

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + termo + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ServicoRealizado sr = new ServicoRealizado();
                sr.setId(rs.getInt("id"));

                long timestampMillis = rs.getLong("data_hora");
                LocalDateTime dataHora = Instant.ofEpochMilli(timestampMillis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                sr.setDataHora(dataHora);

                Usuario barbeiro = new Usuario();
                barbeiro.setNome(rs.getString("nomeBarbeiro"));
                sr.setBarbeiro(barbeiro);

                Servico servico = new Servico();
                servico.setNome(rs.getString("nomeServico"));
                servico.setPreco(rs.getDouble("preco"));
                sr.setServico(servico);

                Cliente cliente = new Cliente();
                cliente.setNome(rs.getString("nomeCliente"));
                sr.setCliente(cliente);

                lista.add(sr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }




    public static RelatorioItem gerarRelatorio(String barbeiro, LocalDate dataInicial, LocalDate dataFinal, String periodoNome) {
        String sql = """
            SELECT 
                COUNT(*) AS atendimentos,
                SUM(s.preco) AS faturamento,
                SUM(s.preco * s.comissao / 100) AS comissao
            FROM servicos_realizados sr
            JOIN servicos s ON sr.servico_id = s.id
            WHERE sr.data_hora BETWEEN ? AND ?
        """;

        if (!barbeiro.equals("Todos")) {
            sql += " AND sr.barbeiro = ?";
        }

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Converte data para timestamp em milissegundos
            long inicio = dataInicial.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long fim = dataFinal.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli() - 1;

            stmt.setLong(1, inicio);
            stmt.setLong(2, fim);

            if (!barbeiro.equals("Todos")) {
                stmt.setString(3, barbeiro);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int atendimentos = rs.getInt("atendimentos");
                double faturamento = rs.getDouble("faturamento");
                double comissao = rs.getDouble("comissao");

                return new RelatorioItem(periodoNome, atendimentos, faturamento, comissao);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Caso não encontre dados, retorna zerado
        return new RelatorioItem(periodoNome, 0, 0.0, 0.0);
    }

    public List<ServicoRealizado> listarTodos() {
        List<ServicoRealizado> lista = new ArrayList<>();
        String sql = "SELECT sr.id, sr.data_hora, sr.barbeiro, sr.servico_id, " +
                "s.nome AS nomeServico, s.preco, " +
                "u.nome AS nomeBarbeiro " +
                "FROM servicos_realizados sr " +
                "JOIN servicos s ON sr.servico_id = s.id " +
                "JOIN usuarios u ON sr.barbeiro = u.id " +
                "ORDER BY sr.data_hora DESC";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ServicoRealizado sr = new ServicoRealizado();
                sr.setId(rs.getInt("id"));
                sr.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());

                Servico servico = new Servico();
                servico.setId(rs.getInt("servico_id"));

                sr.getServico().setNome(rs.getString("nomeServico"));
                sr.getServico().setPreco(rs.getDouble("preco"));
                sr.setServico(servico);

                Usuario barbeiro = new Usuario();
                barbeiro.setId(rs.getInt("barbeiro")); // idem
                sr.getBarbeiro().setNome(rs.getString("nomeBarbeiro"));
                sr.setBarbeiro(barbeiro);

                lista.add(sr);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
