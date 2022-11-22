module mypack {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.java;


    opens mypack to javafx.fxml;
    exports mypack;
    exports mypack.controller;
    exports mypack.connection;
    opens mypack.controller to javafx.fxml;
    exports mypack.model;
    opens mypack.model to javafx.fxml;
}