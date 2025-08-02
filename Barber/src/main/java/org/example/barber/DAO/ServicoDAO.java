package org.example.barber.DAO;

import org.example.barber.database.ConexaoSQLite;
import org.example.barber.entities.Servico;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicoDAO {

    public static void criarTabela() {
        String sql = """
            CREATE TABLE IF NOT EXISTS servicos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                preco REAL NOT NULL,
                comissao REAL NOT NULL
            )
        """;

        try (Connection conn = ConexaoSQLite.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabela de serviços:");
            e.printStackTrace();
        }
    }

    public static void inserir(Servico servico) {
        String sql = "INSERT INTO servicos(nome, preco, comissao) VALUES(?, ?, ?)";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, servico.getNome());
            stmt.setDouble(2, servico.getPreco());
            stmt.setDouble(3,servico.getComissao());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir serviço:");
            e.printStackTrace();
        }
    }

    public static List<Servico> buscarTodos() {
        List<Servico> lista = new ArrayList<>();
        String sql = "SELECT * FROM servicos ORDER BY nome COLLATE NOCASE";

        try (Connection conn = ConexaoSQLite.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                double comissao = rs.getDouble("comissao");
                Servico s = new Servico(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        comissao
                );
                lista.add(s);
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os serviços:");
            e.printStackTrace();
        }

        return lista;
    }


    public static void atualizar(Servico servico) {
        String sql = "UPDATE servicos SET nome = ?, preco = ?, comissao = ? WHERE id = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, servico.getNome());
            stmt.setDouble(2, servico.getPreco());
            stmt.setDouble(3,servico.getComissao());
            stmt.setInt(4, servico.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar serviço:");
            e.printStackTrace();
        }
    }

    public static void excluir(int id) {
        String sql = "DELETE FROM servicos WHERE id = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao excluir serviço:");
            e.printStackTrace();
        }
    }

    public static Servico buscarPorId(int id) {
        String sql = "SELECT * FROM servicos WHERE id = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idServico = rs.getInt("id");
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                double comissao = rs.getDouble("comissao");

                return new Servico(idServico, nome, preco,comissao); // Retorna um objeto Servico
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar serviço por ID:");
            e.printStackTrace();
        }

        return null;
    }





}
