package vista;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import modelo.Foca;
import modelo.Jugador;
import modelo.Pinguino;

public class Victoria {

    @FXML
    private Text tituloVictoria;

    @FXML
    private Text mensajeVictoria;

    @FXML
    private Text textoGanador;

    @FXML
    private Text textoPuntuacion;

    @FXML
    private ImageView imagenResultado;
    @FXML
    private Button volver;

    private Jugador ganador;

    public void setGanador(Jugador ganador) { //Recibe el ganador desde otra pantalla
        this.ganador = ganador;
    }

    /*
     * Inicializamos la pantalla de resultados.
     * Comprobamos si el ganador es una Foca o un Pingüino
     * dependiendo del ganador se cambiarán los textos e imagen.
     */
    
    public void inicio() {

        if (ganador instanceof Foca) {

            tituloVictoria.setText("DERROTA");

            mensajeVictoria.setText(
                "La foca ha dominado el hielo..."
            );

            textoGanador.setText("La foca ha ganado");

            textoPuntuacion.setText("Inténtalo de nuevo");

            imagenResultado.setImage(
                new Image("/recursos/foca_victoria.jpg")
            );

        } else { //Si la foca no ha ganado entonces es el pingüino

            Pinguino p = (Pinguino) ganador;

            tituloVictoria.setText("VICTORIA");

            mensajeVictoria.setText(
                "¡Has conquistado el tablero helado!"
            );

            textoGanador.setText(
                "Ganador: " + p.getNombre()
            );

            textoPuntuacion.setText(
                "Puntuación total: " + p.getPuntuacion()
            );

            imagenResultado.setImage(
                new Image("/recursos/pinguino_victoria.jpg")
            );
        }
    }

    @FXML
    private void volver() { //Metodo que se ejecutara al pulsar el botón volver cargando el menú principal

        try {

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/recursos/Menu.fxml")
            );

            Parent root = loader.load();

            Stage stage =
                (Stage) tituloVictoria.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}