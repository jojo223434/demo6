module org.example.demo6 {
    requires javafx.fxml;
    requires eu.hansolo.medusa;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.sql;


    opens org.example.demo6 to javafx.fxml;
    exports org.example.demo6;
}