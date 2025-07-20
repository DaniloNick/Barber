package org.example.barber.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class BancoInicializador {

    public static void inicializar() {
        try (Connection conn = ConexaoSQLite.conectar();
             Statement stmt = conn.createStatement()) {

            // Tabela de usuários
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    senha TEXT NOT NULL
                );
            """);

            // Tabela de clientes
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS clientes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    telefone TEXT,
                    data_nascimento TEXT NOT NULL
                );
            """);

            // Tabela de serviços
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS servicos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    preco REAL NOT NULL
                );
            """);

            // Tabela de serviços realizados
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS servicos_realizados (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cliente_id INTEGER NOT NULL,
                    servico_id INTEGER NOT NULL,
                    data_hora TEXT NOT NULL,
                    FOREIGN KEY (cliente_id) REFERENCES clientes(id),
                    FOREIGN KEY (servico_id) REFERENCES servicos(id)
                );
            """);

            // Tabela de agendamentos
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS agendamentos (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cliente_id INTEGER NOT NULL,
                    data TEXT NOT NULL,
                    hora TEXT NOT NULL,
                    FOREIGN KEY (cliente_id) REFERENCES clientes(id)
                );
            """);

        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }
}
