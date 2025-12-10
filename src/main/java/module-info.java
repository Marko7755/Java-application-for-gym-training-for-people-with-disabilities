module hr.java.projekt.projekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;
    requires java.desktop;



    opens hr.java.projekt to javafx.fxml;
    exports hr.java.projekt;
    exports hr.java.projekt.controller;
    exports hr.java.projekt.entity.login;
    exports hr.java.projekt.exceptions;
    exports hr.java.projekt.entity.model;
    exports hr.java.projekt.entity.enums;
    opens hr.java.projekt.controller to javafx.fxml;
}