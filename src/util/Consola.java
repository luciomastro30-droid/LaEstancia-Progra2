package util;

import java.util.Scanner;
import java.io.IOException;

public class Consola {

    private static final Scanner SCAN = new Scanner(System.in);
    // lineas en blanco que se imprimen para "limpiar" la pantalla
    private static final int LINEAS_LIMPIAR = 60;

    /*
     * "Limpia" la pantalla imprimiendo muchas lineas en blanco:
     * lo anterior sube y queda fuera de la vista.
     * Se hace asi porque la consola de IntelliJ no tiene
     * un comando para borrar la pantalla.
     */
    public static void limpiarPantalla() {
        System.out.print("\n".repeat(LINEAS_LIMPIAR));
        System.out.flush();
    }

    private Consola() {
    }

    public static String leerTexto(String mensaje) {
        while (true){
            System.out.print(mensaje);
            String linea = SCAN.nextLine().trim();
            if (!linea.isBlank()) {
                return linea;
            }
            System.out.println("No puede estar vacio");
        }
    }


    public static String leerTextoOpcional(String mensaje){

        System.out.print(mensaje);
        String linea = SCAN.nextLine().trim();
        if(linea.isBlank())return null;

        return linea;
    }

    public static int leerEntero (String mensaje, int min, int max){

        while (true){
            try {
                System.out.print(mensaje);
                String linea = SCAN.nextLine().trim();
                int num = Integer.parseInt(linea);
                if(num >= min && num <= max){
                    return num;

                }
                System.out.println("Ingrese un numero entre " + min + " y " + max);

            }catch (NumberFormatException e){
                System.out.println("Ingrese un numero valido ");
            }
        }

    }

    public static boolean confirmar(String mensaje){

        while (true){
           System.out.print(mensaje + " (s/n): ");
           String linea = SCAN.nextLine().trim();
           if(linea.equalsIgnoreCase("s")) return true;
           if(linea.equalsIgnoreCase("n")) return false;
            System.out.println("Opcion invalida");
       }
    }

    public static void pausar(){
        System.out.print("Presiona Enter para continuar...");
        SCAN.nextLine();
    }
}
