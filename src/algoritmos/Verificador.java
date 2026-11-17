package algoritmos;

import java.util.List;
import modelo.Grafo;

/**
 * Comprueba si un conjunto de vértices es una solución válida de CLIQUE o de VERTEX COVER.
 *
 * Su única responsabilidad es VERIFICAR: no busca soluciones. Esto ilustra la idea central
 * de la clase NP: verificar una solución es barato (tiempo polinómico), mientras que
 * encontrarla es caro (los resolvedores son exponenciales).
 *
 * Una lista se considera válida solo si no es null, no repite vértices y todos sus
 * vértices existen en el grafo (entre 0 y n-1).
 */
public final class Verificador {

    // No se crean objetos de esta clase: sus métodos se usan así: Verificador.metodo(...)
    private Verificador() {
    }

    /**
     * Indica si la lista es un clique de tamaño al menos k: tiene al menos k vértices
     * y todos están conectados entre sí.
     *
     * @param g        grafo en el que se verifica
     * @param vertices vértices propuestos como clique
     * @param k        tamaño mínimo exigido
     * @return true si es un clique válido de al menos k vértices
     * @throws IllegalArgumentException si g es null
     *
     * Costo: O(s²), con s = cantidad de vértices de la lista (s ≤ n), porque revisa cada par una vez.
     */
    public static boolean esClique(Grafo g, List<Integer> vertices, int k) {
        exigirGrafo(g);
        if (!sonVerticesValidos(g, vertices) || vertices.size() < k) {
            return false;
        }
        // Se revisa cada par (i, j) una sola vez: basta que falte una arista para fallar.
        for (int i = 0; i < vertices.size(); i++) {
            for (int j = i + 1; j < vertices.size(); j++) {
                if (!g.hayArista(vertices.get(i), vertices.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Indica si la lista es una cobertura de vértices de tamaño a lo sumo k: tiene como máximo
     * k vértices y cada arista del grafo tiene al menos uno de sus extremos en la lista.
     *
     * @param g        grafo en el que se verifica
     * @param vertices vértices propuestos como cobertura
     * @param k        tamaño máximo permitido
     * @return true si es una cobertura válida de a lo sumo k vértices
     * @throws IllegalArgumentException si g es null
     *
     * Costo: Θ(n²), porque revisa cada par de vértices del grafo una vez.
     */
    public static boolean esCobertura(Grafo g, List<Integer> vertices, int k) {
        exigirGrafo(g);
        if (!sonVerticesValidos(g, vertices) || vertices.size() > k) {
            return false;
        }
        // Se marca cada vértice de la lista para saber en Θ(1) si una arista queda cubierta.
        boolean[] enCobertura = new boolean[g.getN()];
        for (int v : vertices) {
            enCobertura[v] = true;
        }
        for (int u = 0; u < g.getN(); u++) {
            for (int v = u + 1; v < g.getN(); v++) {
                // Una arista existente sin ningún extremo marcado queda sin cubrir.
                if (g.hayArista(u, v) && !enCobertura[u] && !enCobertura[v]) {
                    return false;
                }
            }
        }
        return true;
    }

    // MÉTODOS AUXILIARES PRIVADOS

    /** Falla con un mensaje claro si el grafo es null (es un error del programador, no del usuario). */
    private static void exigirGrafo(Grafo g) {
        if (g == null) {
            throw new IllegalArgumentException("El grafo no puede ser null");
        }
    }

    /**
     * Comprueba que la lista no sea null, que cada vértice exista y que no haya repetidos.
     * Se rechazan repetidos porque inflarían el tamaño de la solución sin aportar nada.
     * Costo: O(s) con s = tamaño de la lista.
     */
    private static boolean sonVerticesValidos(Grafo g, List<Integer> vertices) {
        if (vertices == null) {
            return false;
        }
        boolean[] visto = new boolean[g.getN()];
        for (Integer v : vertices) {
            if (v == null || v < 0 || v >= g.getN() || visto[v]) {
                return false;
            }
            visto[v] = true;
        }
        return true;
    }
}
