package reduccion;

import modelo.Grafo;

/**
 * Convierte un problema de CLIQUE (G, k) en uno de VERTEX COVER (complemento de G, n - k).
 *
 * G tiene un clique de k vértices  ⇔  el complemento de G
 * tiene una cobertura de n - k vértices (los que quedan fuera del clique).
 *
 * lo usa emanuel y fogacho
 */
public class Reduccion {

    // No se crean objetos de esta clase: sus métodos se usan así: Reduccion.metodo(...)
    private Reduccion() {
    }

    /**
     * Crea el grafo complemento: mismos vértices, pero las aristas al revés
     * (donde había arista ya no hay, y donde no había ahora sí hay).
     * No modifica el grafo original.
     * Costo: Θ(n²), porque revisa cada par de vértices una vez.
     */
    public static Grafo construirComplemento(Grafo g) {
        if (g == null) {
            throw new IllegalArgumentException("El grafo no puede ser null");
        }

        int n = g.getN();
        Grafo complemento = new Grafo(n); // mismos vértices, sin aristas

        // Revisa cada par (u, v) una sola vez; si NO están conectados en g, se conectan en el complemento.
        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (!g.hayArista(u, v)) {
                    complemento.agregarArista(u, v);
                }
            }
        }
        return complemento;
    }

    /**
     * Calcula el nuevo k para VERTEX COVER: k' = n - k.
     * Si k > n el resultado es negativo, y la respuesta será NO en ambos problemas.
     * Costo: Θ(1).
     */
    public static int transformarK(int n, int k) {
        if (n < 0) {
            throw new IllegalArgumentException("El número de vértices no puede ser negativo: " + n);
        }
        return n - k;
    }
}