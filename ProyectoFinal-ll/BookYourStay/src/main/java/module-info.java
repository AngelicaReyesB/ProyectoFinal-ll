module co.edu.uniquindio.bookyourstay {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires static lombok;
    requires org.simplejavamail.core;
    requires org.simplejavamail;
    requires java.desktop;
    requires jakarta.mail;

    opens co.edu.uniquindio.bookyourstay to javafx.fxml;
    exports co.edu.uniquindio.bookyourstay;
    exports co.edu.uniquindio.bookyourstay.modelo;
    exports co.edu.uniquindio.bookyourstay.controlador;
    exports co.edu.uniquindio.bookyourstay.controlador.observador;
    exports co.edu.uniquindio.bookyourstay.factory;
    exports co.edu.uniquindio.bookyourstay.servicio;
    exports co.edu.uniquindio.bookyourstay.utils;
    exports co.edu.uniquindio.bookyourstay.modelo.enums;
    opens co.edu.uniquindio.bookyourstay.controlador to javafx.fxml;
}
