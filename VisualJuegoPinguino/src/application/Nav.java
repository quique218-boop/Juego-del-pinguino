package application;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Nav {
    private static Stage stage;

    public static void init(Stage primaryStage) {
        stage = primaryStage;
    }

    /**
     * Cambia de pantalla cargando un FXML que está en el MISMO paquete "application".
     * Ejemplo: Nav.goTo("login.fxml", "Login");
     */
    public static void goTo(String fxml, String title) {
        try {
            // Si tus FXML están en src/application/ (mismo paquete), esta ruta funciona:
            FXMLLoader loader = new FXMLLoader(Nav.class.getResource(fxml));

            Parent root = loader.load();

            if (stage.getScene() == null) {
                stage.setScene(new Scene(root, 900, 600));
            } else {
                stage.getScene().setRoot(root);
            }

            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar: " + fxml, e);
        } catch (NullPointerException e) {
            // Esto pasa cuando getResource(...) devuelve null (ruta mal puesta / archivo no encontrado)
            throw new RuntimeException(
                    "No se encontró el FXML '" + fxml + "'. Asegúrate de que esté en src/application/ y se llame igual.",
                    e
            );
        }
    }
}