module org.example.barber {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;

    opens org.example.barber to javafx.fxml;
    exports org.example.barber;

    exports org.example.barber.controllers;
    opens org.example.barber.controllers to javafx.fxml;

    exports org.example.barber.entities;
    opens org.example.barber.entities to javafx.fxml;

    exports org.example.barber.DAO;
    opens org.example.barber.DAO to javafx.fxml;

    exports org.example.barber.database;
    opens org.example.barber.database to javafx.fxml;
}
