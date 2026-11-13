package pruebas;

import algoritmos.ResolvedorClique;
import algoritmos.ResolvedorVertexCover;
import algoritmos.Verificador;
import java.util.List;
import java.util.function.Supplier;
import modelo.Grafo;
import reduccion.Reduccion;
import util.GeneradorGrafos;

/**
 * Casos de prueba con resultado esperado conocido. Imprime una tabla (entrada, esperado,
 * obtenido) que se puede capturar para la sección "Pruebas funcionales" del informe.
 *
 * Cubre: un caso SÍ, un caso NO, grafo vacío, grafo completo, k = 0 y k > n.
 * Para agregar un caso nuevo basta con añadir una línea en construirCasos().
 */
public final class PruebasFuncionales {

    /** Un caso de prueba: qué se ejecuta, qué texto se espera y cómo ejecutarlo. */
    private record Caso(String entrada, String esperado, Supplier<Object> ejecucion) {
    }

    // No se crean objetos de esta clase: sus métodos se usan así: PruebasFuncionales.metodo(...)
    private PruebasFuncionales() {
    }

    /** Ejecuta todos los casos e imprime la tabla con el veredicto de cada uno. */
    public static void main(String[] args) {
        List<Caso> casos = construirCasos();
        int correctos = 0;

        System.out.println("=== PRUEBAS FUNCIONALES ===");
        System.out.printf("%-3s %-52s %-20s %-20s %s%n", "#", "Entrada", "Esperado", "Obtenido", "Resultado");
        for (int i = 0; i < casos.size(); i++) {
            Caso caso = casos.get(i);
            String obtenido = String.valueOf(caso.ejecucion().get()); // "[0, 1, 2]", "[]", "null", "true"...
            boolean correcto = obtenido.equals(caso.esperado());
            if (correcto) {
                correctos++;
            }
            System.out.printf("%-3d %-52s %-20s %-20s %s%n",
                    i + 1, caso.entrada(), caso.esperado(), obtenido, correcto ? "OK" : "FALLA");
        }
        System.out.println("Casos correctos: " + correctos + " de " + casos.size());
        if (correctos != casos.size()) {
            System.exit(1);
        }
    }

    /** Define los casos. Las listas se muestran como [a, b, c]; "null" significa que no hay solución. */
    private static List<Caso> construirCasos() {
        Grafo base = GeneradorGrafos.ejemploBase();                  // clique {0, 1, 2}
        Grafo complemento = Reduccion.construirComplemento(base);    // aristas 0-3 y 1-3
        Grafo vacio = GeneradorGrafos.vacio(5);
        Grafo completo = GeneradorGrafos.completo(5);

        return List.of(
                new Caso("buscarClique(ejemploBase, k=3)  [caso SÍ]", "[0, 1, 2]",
                        () -> ResolvedorClique.buscarClique(base, 3)),
                new Caso("buscarCobertura(complemento, k'=1)  [caso SÍ]", "[3]",
                        () -> ResolvedorVertexCover.buscarCobertura(complemento, 1)),
                new Caso("buscarClique(ejemploBase, k=4)  [caso NO]", "null",
                        () -> ResolvedorClique.buscarClique(base, 4)),
                new Caso("buscarCobertura(complemento, k'=0)  [caso NO]", "null",
                        () -> ResolvedorVertexCover.buscarCobertura(complemento, 0)),
                new Caso("buscarClique(grafo vacío de 5, k=2)", "null",
                        () -> ResolvedorClique.buscarClique(vacio, 2)),
                new Caso("buscarClique(grafo completo de 5, k=5)", "[0, 1, 2, 3, 4]",
                        () -> ResolvedorClique.buscarClique(completo, 5)),
                new Caso("buscarCobertura(grafo vacío de 5, k=0)", "[]",
                        () -> ResolvedorVertexCover.buscarCobertura(vacio, 0)),
                new Caso("buscarClique(ejemploBase, k=9)  [k > n]", "null",
                        () -> ResolvedorClique.buscarClique(base, 9)),
                new Caso("buscarClique(ejemploBase, k=0)  [k = 0]", "[]",
                        () -> ResolvedorClique.buscarClique(base, 0)),
                new Caso("buscarCobertura(complemento, k=-1)  [k < 0]", "null",
                        () -> ResolvedorVertexCover.buscarCobertura(complemento, -1)),
                new Caso("esClique(ejemploBase, [0, 1, 2], 3)", "true",
                        () -> Verificador.esClique(base, List.of(0, 1, 2), 3)),
                new Caso("esClique(ejemploBase, [0, 1, 3], 3)  [falta 0-3]", "false",
                        () -> Verificador.esClique(base, List.of(0, 1, 3), 3)),
                new Caso("esCobertura(complemento, [3], 1)", "true",
                        () -> Verificador.esCobertura(complemento, List.of(3), 1)),
                new Caso("esCobertura(complemento, [0], 1)  [falta 1-3]", "false",
                        () -> Verificador.esCobertura(complemento, List.of(0), 1))
        );
    }
}
