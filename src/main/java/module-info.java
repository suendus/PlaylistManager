module com.example.playlistmanagerserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.playlistmanager to javafx.fxml;
    exports com.example.playlistmanager.client;
    exports com.example.playlistmanager.common;
    exports com.example.playlistmanager.server;
    opens com.example.playlistmanager.server to javafx.fxml;
}