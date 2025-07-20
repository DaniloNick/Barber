package org.example.barber.DAO;

import org.example.barber.database.ConexaoSQLite;
import org.example.barber.entities.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Retorna o usuário completo se login válido, ou null caso contrário
    public static Usuario validarLogin(String nome, String senha) {
        String sql = "SELECT * FROM usuarios WHERE nome = ? AND senha = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                boolean isAdmin = rs.getBoolean("isAdmin");
                return new Usuario(id, nome, senha, isAdmin);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // login inválido ou erro
    }

    // Verifica se um usuário já existe
    public static boolean usuarioExiste(String nome) {
        String sql = "SELECT 1 FROM usuarios WHERE nome = ? LIMIT 1";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Insere um novo usuário
    public static void inserir(String nome, String senha, boolean isAdmin) {
        String sql = "INSERT INTO usuarios(nome, senha, isAdmin) VALUES(?, ?, ?)";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, senha);
            stmt.setBoolean(3, isAdmin);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Atualiza usuário incluindo isAdmin
    public static boolean atualizarUsuario(String nomeAtual, String novoNome, String novaSenha, boolean isAdmin) {
        String sql = "UPDATE usuarios SET nome = ?, senha = ?, isAdmin = ? WHERE nome = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoNome);
            stmt.setString(2, novaSenha);
            stmt.setBoolean(3, isAdmin);
            stmt.setString(4, nomeAtual);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método opcional para checar se usuário é admin, caso precise
    public static boolean isUsuarioAdmin(String nome) {
        String sql = "SELECT isAdmin FROM usuarios WHERE nome = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("isAdmin");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Verifica se há ao menos um usuário no banco
    public static boolean tabelaUsuariosExistente() {
        String sql = "SELECT 1 FROM usuarios LIMIT 1";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String senha = rs.getString("senha");
                boolean isAdmin = rs.getBoolean("isAdmin");

                usuarios.add(new Usuario(id, nome, senha, isAdmin));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    public Usuario buscarPorNome(String nome) {
        String sql = "SELECT * FROM usuarios WHERE nome = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String senha = rs.getString("senha");
                boolean isAdmin = rs.getBoolean("isAdmin");

                return new Usuario(id, nome, senha, isAdmin);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean excluir(int id) {
        String verificarAdminSQL = "SELECT isAdmin FROM usuarios WHERE id = ?";
        String excluirSQL = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = ConexaoSQLite.conectar()) {
            // Verifica se o usuário é admin
            try (PreparedStatement verificarStmt = conn.prepareStatement(verificarAdminSQL)) {
                verificarStmt.setInt(1, id);
                ResultSet rs = verificarStmt.executeQuery();

                if (rs.next() && rs.getBoolean("isAdmin")) {
                    // É administrador: não pode excluir
                    System.out.println("Impossível excluir Administrador.");
                    return false;
                }
            }

            // Não é admin: pode excluir
            try (PreparedStatement excluirStmt = conn.prepareStatement(excluirSQL)) {
                excluirStmt.setInt(1, id);
                return excluirStmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



}
