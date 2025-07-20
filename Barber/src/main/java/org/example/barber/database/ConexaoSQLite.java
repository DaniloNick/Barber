package org.example.barber.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConexaoSQLite {
    private static final String URL = "jdbc:sqlite:barber.db";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }
    public static void criarTabelaUsuarios() {
        String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nome TEXT NOT NULL, " +
                "senha TEXT NOT NULL)";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
