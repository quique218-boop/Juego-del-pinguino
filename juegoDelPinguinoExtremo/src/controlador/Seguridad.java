package controlador;

import java.security.MessageDigest;

public class Seguridad {

    public static String hashPassword(String password) { 
    	//Metodo estatico que recibe una constraseña en texto normal y la devuelve convertida en Hhash SHA-256

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256"); //Creamos un objeto MessageDigest usando el algoritmo SHA-256.

            byte[] hash = md.digest(password.getBytes("UTF-8")); //Convierte la contraseña a Bytes con UTF-8

            StringBuilder sb = new StringBuilder(); //Usamos Stringbuilder para construir el texto final del hash de forma eficiente

            for (byte b : hash) { //Recorremos cada byte
                sb.append(String.format("%02x", b)); //Convertimos el byte a hexadecimal y usamos al menos 2 caracteres y rellenamos con 0 si hace falta
            }

            return sb.toString(); //Devuelve el hash final como string

        } catch (Exception e) { //Si ocurre algun error lo muestra por consola
            e.printStackTrace();
        }

        return null; //Si el proceso falla devuelve null
    }
}