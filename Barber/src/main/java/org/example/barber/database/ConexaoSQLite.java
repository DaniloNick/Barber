package org.example.barber.database;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConexaoSQLite {
    private static final String DB_NAME = "barber.db";

    private static String getJarDir() {
        try {
            // Obtem o arquivo jar do código atual (this class)
            File jarFile = new File(ConexaoSQLite.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            // Retorna o diretório onde o jar está
            return jarFile.getParentFile().getPath();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            // fallback para diretório atual
            return System.getProperty("user.dir");
        }
    }

    private static final String DB_PATH = getJarDir() + File.separator + DB_NAME;

    private static final String URL = "jdbc:sqlite:" + DB_PATH;

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
