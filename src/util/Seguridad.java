package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Se encarga de transformar contraseñas en un hash utilizando SHA-256,
 * evitando trabajar con contraseñas almacenadas directamente en texto plano.
 */
public class Seguridad {

    public static String hashear(String pass){
        if (pass == null || pass.isBlank()) {
            throw new IllegalArgumentException(
                    "La contraseña no puede estar vacia."
            );
        }

        try {

            MessageDigest dig = MessageDigest.getInstance("SHA-256");

            // SHA-256 trabaja con bytes, por eso la contraseña se convierte
            // utilizando UTF-8 antes de calcular el hash.
            byte[] bytes = pass.getBytes(StandardCharsets.UTF_8);

            byte[] hash = dig.digest(bytes);

            // Convierte el hash en texto hexadecimal para poder almacenarlo
            // y compararlo fácilmente en la base de datos.
            return HexFormat.of().formatHex(hash);

        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no esta disponible", e);
        }

    }

}