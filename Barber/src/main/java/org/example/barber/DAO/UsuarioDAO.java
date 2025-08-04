package org.example.barber.DAO;

import org.example.barber.database.ConexaoSQLite;
import org.example.barber.entities.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Valida login (retorna usuario se correto, senão null)
    public static Usuario validarLogin(String nome, String senha) {
        String sql = "SELECT * FROM usuarios WHERE nome = ? AND senha = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, senha);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("senha"),
                        rs.getBoolean("isAdmin")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Verifica se usuário já existe
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

    // Insere novo usuario
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

    // Atualiza dados do usuario pelo nome
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

    // Retorna true se for admin
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

    // Retorna todos os usuarios
    public static List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("senha"),
                        rs.getBoolean("isAdmin")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

    // Buscar por nome
    public static Usuario buscarPorNome(String nome) {
        String sql = "SELECT * FROM usuarios WHERE nome = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("senha"),
                        rs.getBoolean("isAdmin")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Validar senha de um usuário específico
    public static boolean validarSenha(String nome, String senhaDigitada) {
        String sql = "SELECT 1 FROM usuarios WHERE nome = ? AND senha = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, senhaDigitada);

            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Listar barbeiros (quem não é admin)
    public static List<Usuario> listarBarbeiros() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("senha"),
                        rs.getBoolean("isAdmin")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Excluir usuario por id (não pode excluir admin)
    public static boolean excluir(int id) {
        String sqlCheck = "SELECT isAdmin FROM usuarios WHERE id = ?";
        String sqlDelete = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = ConexaoSQLite.conectar()) {
            try (PreparedStatement checkStmt = conn.prepareStatement(sqlCheck)) {
                checkStmt.setInt(1, id);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next() && rs.getBoolean("isAdmin")) {
                    System.out.println("Impossível excluir administrador.");
                    return false;
                }
            }

            try (PreparedStatement delStmt = conn.prepareStatement(sqlDelete)) {
                delStmt.setInt(1, id);
                return delStmt.executeUpdate() > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean atualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, senha = ?, isAdmin = ? WHERE id = ?";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getSenha());
            stmt.setBoolean(3, usuario.isAdmin());
            stmt.setInt(4, usuario.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    // Método para verificar se há pelo menos um usuário cadastrado (para inicializar admin)
    public static boolean tabelaUsuariosExistente() {
        String sql = "SELECT 1 FROM usuarios LIMIT 1";

        try (Connection conn = ConexaoSQLite.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
