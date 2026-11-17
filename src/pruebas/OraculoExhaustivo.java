package pruebas;

import modelo.Grafo;

/**
 * Respuesta de referencia para CLIQUE, calculada de la forma más simple posible (probar TODOS
 * los subconjuntos de k vértices) y SIN usar los resolvedores ni el Verificador.
 *
 * Sirve para detectar un error que la prueba de equivalencia sola no vería: si los dos
 * resolvedores fallaran de la misma manera, igual coincidirían entre sí. Como el oráculo es
 * independiente, esa falla compartida sí se detecta. Solo es práctico con grafos pequeños.
 */
final class OraculoExhaustivo {

    // No se crean objetos de esta clase: sus métodos se usan así: OraculoExhaustivo.metodo(...)
    private OraculoExhaustivo() {
    }

    /**
     * Indica si G tiene un clique de al menos k vértices.
     * Costo: O(C(n, k) * k²), porque prueba cada subconjunto de k vértices.
     */
    static boolean existeClique(Grafo g, int k) {
        if (k <= 0) {
            return true;  // el conjunto vacío siempre sirve
        }
        if (k > g.getN()) {
            return false; // no hay k vértices distintos
        }
        return probar(g, k, 0, new int[k], 0);
    }

    /** Recorre todas las combinaciones de k vértices en orden creciente. */
    private static boolean probar(Grafo g, int k, int desde, int[] elegidos, int tam) {
        if (tam == k) {
            return true;
        }
        for (int v = desde; g.getN() - v >= k - tam; v++) {
            if (estaConectadoConTodos(g, v, elegidos, tam)) {
                elegidos[tam] = v;
                if (probar(g, k, v + 1, elegidos, tam + 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    /** Indica si v tiene arista con cada uno de los primeros tam vértices elegidos. */
    private static boolean estaConectadoConTodos(Grafo g, int v, int[] elegidos, int tam) {
        for (int i = 0; i < tam; i++) {
            if (!g.hayArista(elegidos[i], v)) {
                return false;
            }
        }
        return true;
    }
}

