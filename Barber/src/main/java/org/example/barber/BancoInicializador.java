package org.example.barber;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BancoInicializador {
    public static void inicializar() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nome TEXT NOT NULL," +
                "senha TEXT NOT NULL)";

        try (Connection conn = ConexaoSQLite.conectar();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

            // Usuário padrão
            String sqlInsert = "INSERT INTO usuarios (nome, senha) " +
                    "SELECT 'admin', 'admin' " +
                    "WHERE NOT EXISTS (SELECT 1 FROM usuarios WHERE nome = 'admin')";
            stmt.execute(sqlInsert);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
