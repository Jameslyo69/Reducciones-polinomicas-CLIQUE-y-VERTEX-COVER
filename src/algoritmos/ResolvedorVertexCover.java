package algoritmos;

import java.util.ArrayList;
import java.util.List;
import modelo.Grafo;

/**
 * Resuelve el problema VERTEX COVER: ¿existe un grupo de a lo sumo k vértices que toque todas las aristas?
 *
 * Es un algoritmo EXACTO basado en RAMIFICACIÓN: se toma una arista que todavía no está cubierta;
 * para cubrirla hay que elegir uno de sus dos extremos, así que se prueban las dos opciones
 * (y se retrocede si ninguna funciona). Esta clase solo devuelve resultados.
 *
 * Mejora que no cambia la respuesta, solo el tiempo: antes de buscar se calcula una cota inferior
 * (un emparejamiento maximal). Si ya necesita más de k vértices, se responde NO sin ramificar.
 */
public final class ResolvedorVertexCover {

    // No se crean objetos de esta clase: sus métodos se usan así: ResolvedorVertexCover.metodo(...)
    private ResolvedorVertexCover() {
    }

    /**
     * Busca una cobertura de vértices de a lo sumo k vértices.
     *
     * @param g grafo donde se busca
     * @param k cantidad máxima de vértices permitida
     * @return los vértices de la cobertura (en orden creciente), una lista vacía si el grafo
     *         no tiene aristas, o null si no existe cobertura de ese tamaño (incluye k < 0)
     * @throws IllegalArgumentException si g es null
     *
     * Costo: tiempo exponencial en k, O(2^k * m), con m = cantidad de aristas: el árbol de
     * ramificación tiene a lo sumo 2^(k+1) nodos y cada uno recorre las aristas. Como k <= n,
     * en el peor caso es O(2^n * n²). Espacio: O(n + m) (lista de aristas y recursión de
     * profundidad a lo sumo k).
     */
    public static List<Integer> buscarCobertura(Grafo g, int k) {
        if (g == null) {
            throw new IllegalArgumentException("El grafo no puede ser null");
        }
        // Caso borde acordado con el equipo: con k negativo nunca hay solución.
        if (k < 0) {
            return null;
        }
        // Se pide la lista de aristas UNA sola vez (cada llamada a getAristas() crea una lista nueva).
        List<int[]> aristas = g.getAristas();
        if (aristas.isEmpty()) {
            return new ArrayList<>(); // sin aristas no hay nada que cubrir
        }
        // Cota inferior: cada arista del emparejamiento exige un vértice distinto en la cobertura.
        if (tamanoEmparejamientoMaximal(g.getN(), aristas) > k) {
            return null;
        }
        return new BusquedaCobertura(g.getN(), aristas, k).ejecutar();
    }

    /**
     * Cuenta las aristas de un emparejamiento maximal (aristas que no comparten vértices),
     * elegidas con un recorrido voraz. Cualquier cobertura necesita al menos ese tamaño.
     * Costo: O(m + n).
     */
    private static int tamanoEmparejamientoMaximal(int n, List<int[]> aristas) {
        boolean[] usado = new boolean[n];
        int tamano = 0;
        for (int[] arista : aristas) {
            if (!usado[arista[0]] && !usado[arista[1]]) {
                usado[arista[0]] = true;
                usado[arista[1]] = true;
                tamano++;
            }
        }
        return tamano;
    }

    /**
     * Guarda el estado de una búsqueda (aristas, vértices elegidos y presupuesto) para no pasar
     * muchos parámetros entre llamadas recursivas. Cada búsqueda crea su propio objeto,
     * así que no hay estado compartido entre llamadas.
     */
    private static final class BusquedaCobertura {

        private final List<int[]> aristas;
        private final int k;
        /** enCobertura[v] es true si el vértice v está elegido en la solución parcial. */
        private final boolean[] enCobertura;

        BusquedaCobertura(int n, List<int[]> aristas, int k) {
            this.aristas = aristas;
            this.k = k;
            this.enCobertura = new boolean[n];
        }

        /** Lanza la búsqueda y devuelve la cobertura encontrada, o null si no hay. */
        List<Integer> ejecutar() {
            if (!cubrir(0, k)) {
                return null;
            }
            List<Integer> cobertura = new ArrayList<>();
            for (int v = 0; v < enCobertura.length; v++) {
                if (enCobertura[v]) {
                    cobertura.add(v);
                }
            }
            return cobertura;
        }

        /**
         * Intenta cubrir todas las aristas desde la posición indicada.
         *
         * @param desde       primera arista que todavía no se ha revisado (las anteriores ya están cubiertas)
         * @param presupuesto cuántos vértices más se pueden elegir
         * @return true si se logró cubrir todo (la solución queda marcada en enCobertura)
         */
        private boolean cubrir(int desde, int presupuesto) {
            // Se saltan las aristas que ya quedaron cubiertas por los vértices elegidos.
            int i = desde;
            while (i < aristas.size() && estaCubierta(aristas.get(i))) {
                i++;
            }
            if (i == aristas.size()) {
                return true; // todas las aristas están cubiertas
            }
            if (presupuesto == 0) {
                return false; // hay una arista sin cubrir y no quedan vértices por elegir
            }
            // La arista i está sin cubrir: hay que elegir uno de sus dos extremos. Se prueban ambos.
            for (int extremo : aristas.get(i)) {
                enCobertura[extremo] = true;
                if (cubrir(i + 1, presupuesto - 1)) {
                    return true;
                }
                enCobertura[extremo] = false; // retrocede y prueba el otro extremo
            }
            return false;
        }

        /** Indica si alguno de los dos extremos de la arista ya está elegido. Costo: Θ(1). */
        private boolean estaCubierta(int[] arista) {
            return enCobertura[arista[0]] || enCobertura[arista[1]];
        }
    }
}
