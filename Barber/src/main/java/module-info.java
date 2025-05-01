module org.example.barber {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;
    requires java.sql;


    opens org.example.barber to javafx.fxml;
    exports org.example.barber;
}