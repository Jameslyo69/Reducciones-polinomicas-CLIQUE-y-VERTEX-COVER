package algoritmos;
import java.util.List;
import modelo.Grafo;

/**
 * Contrato común de cualquier algoritmo que resuelve una instancia (G, k)
 * de CLIQUE o de VERTEX COVER.
 *
 * Existe para que el resto del proyecto (pruebas, benchmark, menú) dependa de
 * esta abstracción y no de una clase concreta: si mañana se agrega otro algoritmo
 * (por ejemplo una versión más rápida o una heurística), basta con pasarlo donde
 * se espera un Resolvedor, sin modificar el código que ya existe.
 *
 * Se puede usar con referencias a métodos:
 *   Resolvedor clique = ResolvedorClique::buscarClique;
 *   Resolvedor cobertura = ResolvedorVertexCover::buscarCobertura;
 */
@FunctionalInterface
public interface Resolvedor {
    /**
     * Resuelve la instancia (g, k).
     *
     * @param g grafo sobre el que se busca la solución
     * @param k tamaño pedido (su significado depende del problema)
     * @return la lista de vértices de la solución, o null si no existe solución
     */
    List<Integer> resolver(Grafo g, int k);
}
