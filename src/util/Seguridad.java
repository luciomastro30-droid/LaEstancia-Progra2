package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class Seguridad {

    public static String hashear(String pass){
        try {

            //se pide q calcule el algoritmo
            MessageDigest dig = MessageDigest.getInstance("SHA-256");
            //el algoritmo trabaja con bytes, no con texto. Indicar UTF-8
            //asegura que la misma contraseña dé siempre los mismos bytes, en cualquier computadora.
            byte[] bytes = pass.getBytes(StandardCharsets.UTF_8);
            //calcula el hash. Siempre devuelve 32 bytes, sin importar el largo del texto.
            byte[] hash = dig.digest(bytes);
            //pasa esos bytes a un texto de 64 caracteres hexadecimales,
            // que es lo que se guarda en la columna contrasenia_hash.
            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no esta disponible", e);
        }

    }

}
