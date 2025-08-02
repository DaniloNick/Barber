package org.example.barber;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.barber.DAO.ClienteDAO;
import org.example.barber.DAO.ServicoDAO;
import org.example.barber.database.BancoInicializador;
import org.example.barber.database.ConexaoSQLite;
import org.example.barber.DAO.ServicoRealizadoDAO;

import java.io.IOException;

public class Application extends javafx.application.Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        BancoInicializador.inicializar();
        ClienteDAO.criarTabela();
        ServicoDAO.criarTabela();
        ConexaoSQLite.criarTabelaUsuarios();
        primaryStage = stage;
        trocarTela("login.fxml", "Barber Shop - Login");
        stage.show();

    }public static void trocarTela(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource("views/" + fxml));
            Parent root = loader.load();
            Scene scene = new Scene(root, 854, 500);
            scene.getStylesheets().add(Application.class.getResource("/styles/style.css").toExternalForm());

            primaryStage.setTitle(titulo);
            primaryStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}