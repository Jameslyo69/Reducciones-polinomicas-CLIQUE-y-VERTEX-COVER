package algoritmos;

import java.util.ArrayList;
import java.util.List;
import modelo.Grafo;

/**
 * Resuelve el problema CLIQUE: ¿existe en G un grupo de k vértices todos conectados entre sí?
 *
 * Es un algoritmo EXACTO (siempre responde bien) basado en backtracking: se construye el clique
 * agregando vértices de uno en uno y se retrocede en cuanto un vértice no está conectado con
 * los ya elegidos. Esta clase  solo devuelve resultados, para no alterar
 * las mediciones de tiempo.
 *
 * Mejoras que no cambian la respuesta, solo el tiempo:
 *  - Preselección por grado: un vértice con menos de k-1 vecinos nunca puede estar en un clique de k.
 *  - Poda por cantidad: se corta la rama si ya no quedan vértices suficientes para completar k.
 */
public final class ResolvedorClique {
    // No se crean objetos de esta clase: sus métodos se usan así: ResolvedorClique.metodo(...)
    private ResolvedorClique() {
    }
    /**
     * Busca un clique de exactamente k vértices (si existe uno de tamaño mayor, también existe uno de k).
     *
     * @param g grafo donde se busca
     * @param k tamaño del clique buscado
     * @return los vértices del clique (en orden creciente), una lista vacía si k <= 0
     *         (el conjunto vacío siempre sirve), o null si no existe (incluye k > n)
     * @throws IllegalArgumentException si g es null
     *
     * Costo: tiempo exponencial en el peor caso, O(2^n * k): se exploran a lo sumo los
     * subconjuntos de vértices que forman un clique parcial y cada uno cuesta O(k) comprobar.
     * La preselección por grado cuesta Θ(n²). Espacio: O(n + k).
     */
    public static List<Integer> buscarClique(Grafo g, int k) {
        if (g == null) {
            throw new IllegalArgumentException("El grafo no puede ser null");
        }
        // Casos borde acordados con el equipo (para que CLIQUE y VERTEX COVER respondan igual).
        if (k <= 0) {
            return new ArrayList<>();
        }
        if (k > g.getN()) {
            return null;
        }
        return new BusquedaClique(g, k).ejecutar();
    }

    /**
     * Guarda el estado de una búsqueda (grafo, candidatos y clique parcial) para no pasar
     * muchos parámetros entre llamadas recursivas. Cada búsqueda crea su propio objeto,
     * así que no hay estado compartido entre llamadas (es seguro repetir mediciones).
     */
    private static final class BusquedaClique {

        private final Grafo g;
        private final int k;
        /** Vértices que todavía pueden formar parte de un clique de tamaño k (en orden creciente). */
        private final int[] candidatos;
        /** Clique parcial: las primeras posiciones ocupadas son los vértices ya elegidos. */
        private final int[] actual;

        BusquedaClique(Grafo g, int k) {
            this.g = g;
            this.k = k;
            this.candidatos = preseleccionarPorGrado();
            this.actual = new int[k];
        }

        /** Lanza la búsqueda y devuelve el clique encontrado, o null si no hay. */
        List<Integer> ejecutar() {
            if (candidatos.length < k || !extender(0, 0)) {
                return null;
            }
            List<Integer> clique = new ArrayList<>(k);
            for (int v : actual) {
                clique.add(v);
            }
            return clique;
        }

        /**
         * Intenta completar el clique parcial.
         *
         * @param tam   cuántos vértices ya hay elegidos
         * @param desde desde qué posición de candidatos se sigue (así no se repiten combinaciones)
         * @return true si se logró un clique de k vértices (queda guardado en actual)
         */
        private boolean extender(int tam, int desde) {
            if (tam == k) {
                return true;
            }
            int faltan = k - tam;
            // Poda: si los candidatos restantes no alcanzan para completar k, esta rama no sirve.
            for (int i = desde; candidatos.length - i >= faltan; i++) {
                int v = candidatos[i];
                if (esCompatible(v, tam)) {
                    actual[tam] = v;
                    if (extender(tam + 1, i + 1)) {
                        return true;
                    }
                }
            }
            return false; // retrocede: ninguna opción de este nivel funcionó
        }

        /** Indica si v está conectado con todos los vértices ya elegidos. Costo: O(k). */
        private boolean esCompatible(int v, int tam) {
            for (int i = 0; i < tam; i++) {
                if (!g.hayArista(actual[i], v)) {
                    return false;
                }
            }
            return true;
        }

        /** Se queda solo con los vértices que tienen al menos k-1 vecinos. Costo: Θ(n²). */
        private int[] preseleccionarPorGrado() {
            int[] temporal = new int[g.getN()];
            int cantidad = 0;
            for (int v = 0; v < g.getN(); v++) {
                if (g.grado(v) >= k - 1) {
                    temporal[cantidad++] = v;
                }
            }
            return java.util.Arrays.copyOf(temporal, cantidad);
        }
    }
}
