package org.example.barber;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Application extends javafx.application.Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        BancoInicializador.inicializar();
        trocarTela("login.fxml", "Barber Shop - Login");
        stage.show();

    }public static void trocarTela(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(Application.class.getResource(fxml));
            Parent root = loader.load();
            primaryStage.setTitle(titulo);
            primaryStage.setScene(new Scene(root, 854, 500));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}