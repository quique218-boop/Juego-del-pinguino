package vista;

import javafx.fxml.FXML;

public class Configuracion {

    private boolean sAmbiental = true;
    
    private double volumen = 0.5; // 50%
   
    private static boolean sObjetos = true;
    
    private static boolean sFoca = true;
    
    private static boolean mVictory = true;
    
    @FXML
    
    private void actAmbiental() {
    	
        sAmbiental = true;

        if (Menu.musicaAmbiente != null) {
        	
            Menu.musicaAmbiente.setVolume(volumen);
            
            Menu.musicaAmbiente.play();
            
        }
        
    }

    @FXML
    
    private void desAmbiental() {
    	
        sAmbiental = false;

        if (Menu.musicaAmbiente != null) {
        	
            Menu.musicaAmbiente.stop();
            
        }
        
    }

    @FXML
    
    private void actSonidosObjetos() {

    	sObjetos = true;
    	
    }

    @FXML
    
    private void desSonidosObjetos() {

    	sObjetos = false;
    	
    }

    @FXML
    
    private void actSonidosFoca() {

    	sFoca = true;
    	
    }

    @FXML
    
    private void desSonidosFoca() {

    	sFoca = false;
    	
    }

    @FXML
    
    private void MVictory() {

    	mVictory = true;
    	
    }

    @FXML
    
    private void desMVictory() {

    	mVictory = false;
    	
    }

    @FXML
    
    private void volumen0() {
    	
        volumen = 0.0;

        if (Menu.musicaAmbiente != null) {
        	
            Menu.musicaAmbiente.setVolume(volumen);
            
        }
        
    }

    @FXML
    
    private void volumen25() {
    	
        volumen = 0.25;

        if (Menu.musicaAmbiente != null) {
        	
            Menu.musicaAmbiente.setVolume(volumen);
            
        }
        
    }

    @FXML
    
    private void volumen50() {
    	
        volumen = 0.5;

        if (Menu.musicaAmbiente != null) {
        	
            Menu.musicaAmbiente.setVolume(volumen);
            
        }
        
    }

    @FXML
    
    private void volumen75() {
    	
        volumen = 0.75;

        if (Menu.musicaAmbiente != null) {
        	
            Menu.musicaAmbiente.setVolume(volumen);
            
        }
        
    }

    @FXML
    
    private void volumen100() {
    	
        volumen = 1.0;

        if (Menu.musicaAmbiente != null) {
        	
            Menu.musicaAmbiente.setVolume(volumen);
            
        }
        
    }
    
}