package org.example.barber.DAO;

import org.example.barber.entities.ServicoRealizado;
import org.example.barber.entities.Servico;
import org.example.barber.entities.Cliente;
import org.example.barber.database.ConexaoSQLite;

import java.sql.*;
import java.time.Instant;
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
                String barbeiro = rs.getString("barbeiro");
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
            stmt.setString(4, realizado.getBarbeiro());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao salvar serviço realizado:");
            e.printStackTrace();
        }
    }

}
