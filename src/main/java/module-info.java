module org.fhmdb.fhmdb_lijunamatata {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires okhttp3;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires ormlite.jdbc;
    requires java.sql;
    requires com.h2database;

    opens org.fhmdb.fhmdb_lijunamatata to javafx.fxml;
    opens org.fhmdb.fhmdb_lijunamatata.controller to javafx.fxml;
    opens org.fhmdb.fhmdb_lijunamatata.models to javafx.fxml, com.google.gson;

    exports org.fhmdb.fhmdb_lijunamatata;
    exports org.fhmdb.fhmdb_lijunamatata.controller;
    exports org.fhmdb.fhmdb_lijunamatata.models;
    exports org.fhmdb.fhmdb_lijunamatata.api;
    exports org.fhmdb.fhmdb_lijunamatata.database;
    opens org.fhmdb.fhmdb_lijunamatata.database to com.google.gson, javafx.fxml, ormlite.jdbc;
}