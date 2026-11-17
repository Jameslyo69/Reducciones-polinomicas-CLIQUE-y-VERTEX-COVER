package util;

import java.util.Random;
import modelo.Grafo;

/**
 * Crea grafos de prueba automáticamente, para no escribirlos a mano.
 *
 * Lo usa papi james, emanuel y fogachinhio
 * Uso:  Grafo g = GeneradorGrafos.aleatorio(10, 0.5, 42);
 */
public class GeneradorGrafos {

    // No se crean objetos de esta clase: sus métodos se usan así: GeneradorGrafos.metodo(...)
    private GeneradorGrafos() {
    }
    /**
     * Crea un grafo al azar con n vértices.
     * - densidad: probabilidad de que exista cada arista (0.0 = ninguna, 1.0 = todas).
     * - semilla: con la misma semilla sale siempre el mismo grafo (sirve para repetir pruebas).
     * Costo: Θ(n²).
     */
    public static Grafo aleatorio(int n, double densidad, long semilla) {
        if (n < 0) {
            throw new IllegalArgumentException("El número de vértices no puede ser negativo: " + n);
        }
        if (densidad < 0.0 || densidad > 1.0) {
            throw new IllegalArgumentException("La densidad debe estar entre 0.0 y 1.0: " + densidad);
        }

        Random azar = new Random(semilla);
        Grafo g = new Grafo(n);

        // Para cada par (u, v) se "lanza una moneda": si sale menor que la densidad, se agrega la arista.
        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (azar.nextDouble() < densidad) {
                    g.agregarArista(u, v);
                }
            }
        }
        return g;
    }

    /**
     * Crea el grafo de ejemplo del proyecto: 4 vértices y aristas 0-1, 0-2, 1-2, 2-3.
     * Tiene un clique de tamaño 3: {0, 1, 2}.
     */
    public static Grafo ejemploBase() {
        Grafo g = new Grafo(4);
        g.agregarArista(0, 1);
        g.agregarArista(0, 2);
        g.agregarArista(1, 2);
        g.agregarArista(2, 3);
        return g;
    }

    /** Crea un grafo vacío: n vértices sin ninguna arista (caso borde para pruebas). */
    public static Grafo vacio(int n) {
        return new Grafo(n);
    }

    /** Crea un grafo completo: n vértices, todos conectados con todos (caso borde para pruebas). */
    public static Grafo completo(int n) {
        Grafo g = new Grafo(n);
        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                g.agregarArista(u, v);
            }
        }
        return g;
    }
}