module VisualJuegoPinguino {
    requires javafx.controls;
    requires javafx.fxml;

    // JavaFX necesita poder acceder a tu clase Application (Main)
    exports application to javafx.graphics;

    // FXML necesita acceso reflectivo a tus controllers
    opens application to javafx.fxml;
}