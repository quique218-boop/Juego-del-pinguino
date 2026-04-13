package modelo;

import java.util.HashMap;

import java.util.Map;

import javafx.scene.media.AudioClip;

public class efectos_de_sonido {

    private static final Map<String, AudioClip> sonidos = new HashMap<>();

    public static void init() {
    	
        cargar("bola", "/recursos/BoladeNieve.mp3");
        
        cargar("agujero", "/recursos/Agujero.mp3");
        
        cargar("dados", "/recursos/Dados.mp3");
        
        cargar("foca", "/recursos/Foca.mp3");
        
        cargar("golpe", "/recursos/Golpe.mp3");
        
        cargar("moto", "/recursos/MotodeNieve.mp3");
        
        cargar("pez", "/recursos/Pez.mp3");
        
        cargar("trineo", "/recursos/Trineo.mp3");
        
    }

    private static void cargar(String nombre, String ruta) {
    	
        try {
        	
            String rutaC = efectos_de_sonido.class.getResource(ruta).toExternalForm();
            
            AudioClip sonido = new AudioClip(rutaC);
            
            sonido.setVolume(0.75);
            
            sonidos.put(nombre, sonido);
            
        } catch (Exception e) {
        	
            System.out.println("Error al cargar el sonido: " + nombre);
            
            e.printStackTrace();
            
        }
        
    }

    public static void reproducir(String nombre) {
    	
        try {
        	
            AudioClip sonido = sonidos.get(nombre);

            if (sonido != null) {
            	
                sonido.play();
                
            } else {
            	
                System.out.println("No existe el sonido: " + nombre);
                
            }
            
        } catch (Exception e) {
        	
            System.out.println("Error al reproducir el sonido: " + nombre);
            
            e.printStackTrace();
            
        }
        
    }

    public static void sonidoBola() {
    	
        reproducir("bola");
        
    }
    
    public static void sonidoAgujero() {
    	
    	reproducir("agujero");
    	
    }
    
    public static void sonidoDados() {
    	
    	reproducir("dados");
    	
    }
    
    public static void sonidoFoca() {
    	
    	reproducir("foca");
    	
    }
    
    public static void sonidoGolpe() {
    	
    	reproducir("golpe");
    	
    }
    
    public static void sonidoMoto() {
    	
    	reproducir("moto");
    	
    }
    
    public static void sonidoPez() {
    	
    	reproducir("pez");
    	
    }
    
    public static void sonidoTrineo() {
    	
    	reproducir("trineo");
    	
    }
}