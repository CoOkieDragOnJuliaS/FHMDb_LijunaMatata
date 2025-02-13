module org.fhmdb.fhmdb_lijunamatata {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens org.fhmdb.fhmdb_lijunamatata to javafx.fxml;
    exports org.fhmdb.fhmdb_lijunamatata;
    exports org.fhmdb.fhmdb_lijunamatata.controller;
    opens org.fhmdb.fhmdb_lijunamatata.controller to javafx.fxml;
    exports org.fhmdb.fhmdb_lijunamatata.models;
    opens org.fhmdb.fhmdb_lijunamatata.models to javafx.fxml;
}