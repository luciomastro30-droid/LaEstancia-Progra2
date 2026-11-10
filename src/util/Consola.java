package util;

import java.util.Scanner;

public class Consola {

    private static final Scanner SCAN = new Scanner(System.in);

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
