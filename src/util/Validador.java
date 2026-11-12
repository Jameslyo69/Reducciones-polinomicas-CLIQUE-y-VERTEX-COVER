package util;

import java.util.Scanner;

/**
 * Lee datos del teclado de forma segura: si el usuario escribe letras,
 * números fuera de rango o deja la línea vacía, muestra un mensaje y vuelve a preguntar.
 * Así el programa nunca se cae por una entrada incorrecta.
 *
 */
public class Validador {

    // No se crean objetos de esta clase: sus métodos se usan así: Validador.metodo(...)
    private Validador() {
    }

    /**
     * Lee una línea del teclado. Si ya no hay más entrada (por ejemplo, el usuario
     * presionó Ctrl+Z / Ctrl+D), cierra el programa de forma ordenada en vez de caerse.
     */
    private static String leerLinea(Scanner sc) {
        if (!sc.hasNextLine()) {
            System.out.println();
            System.out.println("No hay más datos de entrada. Programa terminado.");
            System.exit(0);
        }
        return sc.nextLine().trim();
    }

    /** Pide un número entero entre min y max (incluidos). Repite hasta que sea válido. */
    public static int leerEntero(Scanner sc, String mensaje, int min, int max) {
        while (true) {
            System.out.print(mensaje);
            String linea = leerLinea(sc);
            try {
                int valor = Integer.parseInt(linea);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("  [X] Debe estar entre " + min + " y " + max + ". Intente de nuevo.");
            } catch (NumberFormatException e) {
                System.out.println("  [X] \"" + linea + "\" no es un número entero. Intente de nuevo.");
            }
        }
    }

    /** Pide un número decimal entre min y max (acepta punto o coma: 0.5 o 0,5). */
    public static double leerDecimal(Scanner sc, String mensaje, double min, double max) {
        while (true) {
            System.out.print(mensaje);
            String linea = leerLinea(sc).replace(',', '.');
            try {
                double valor = Double.parseDouble(linea);
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.println("  [X] Debe estar entre " + min + " y " + max + ". Intente de nuevo.");
            } catch (NumberFormatException e) {
                System.out.println("  [X] \"" + linea + "\" no es un número válido. Intente de nuevo.");
            }
        }
    }

    /** Pide un número entero largo (para la semilla). Acepta cualquier valor numérico. */
    public static long leerLong(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String linea = leerLinea(sc);
            try {
                return Long.parseLong(linea);
            } catch (NumberFormatException e) {
                System.out.println("  [X] \"" + linea + "\" no es un número entero. Intente de nuevo.");
            }
        }
    }

    /**
     * Pide una arista escrita como dos números separados por espacio, por ejemplo "0 3".
     * Revisa que ambos vértices estén entre 0 y n-1 y que no sean iguales (sin lazos).
     * Devuelve la arista como {u, v}.
     */
    public static int[] leerArista(Scanner sc, String mensaje, int n) {
        while (true) {
            System.out.print(mensaje);
            String[] partes = leerLinea(sc).split("\\s+");
            if (partes.length != 2) {
                System.out.println("  [X] Escriba dos números separados por un espacio, por ejemplo: 0 1");
                continue;
            }
            try {
                int u = Integer.parseInt(partes[0]);
                int v = Integer.parseInt(partes[1]);
                if (u < 0 || u >= n || v < 0 || v >= n) {
                    System.out.println("  [X] Los vértices deben estar entre 0 y " + (n - 1) + ".");
                } else if (u == v) {
                    System.out.println("  [X] No se permiten lazos (un vértice consigo mismo).");
                } else {
                    return new int[] {u, v};
                }
            } catch (NumberFormatException e) {
                System.out.println("  [X] Solo se permiten números enteros, por ejemplo: 0 1");
            }
        }
    }
}