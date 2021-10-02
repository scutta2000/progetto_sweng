module org.openjfx {
    requires javafx.controls;
    requires ormlite.core;
    requires ormlite.jdbc;
    requires java.sql;
    requires sqlite.jdbc;
    requires javafx.base;
    requires javafx.fxml;

    exports org.pietroscuttari;
    exports org.pietroscuttari.data;
    exports org.pietroscuttari.managers;
    exports org.pietroscuttari.controllers;
    exports org.pietroscuttari.utils;

    opens org.pietroscuttari.data;
    opens org.pietroscuttari.managers;
    opens org.pietroscuttari.controllers;
    opens org.pietroscuttari.utils;
}