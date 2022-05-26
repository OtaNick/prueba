module com.example.spotify {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.spotify to javafx.fxml;
    exports com.example.spotify;
    opens com.example.spotify.img;
}