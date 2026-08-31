module org.example.projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.eclipse.yasson;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.slf4j;
    requires jakarta.json.bind;
    requires java.desktop;
    requires java.sql;

    opens org.example to javafx.fxml;
    exports org.example.enteties;
    opens org.example.enteties to jakarta.json.bind, org.eclipse.yasson;
    exports org.example.screen;
    opens org.example.screen to javafx.fxml;
    exports org.example.enteties.json;
    opens org.example.enteties.json to jakarta.json.bind, org.eclipse.yasson;

}