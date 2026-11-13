package pruebas;

import algoritmos.Resolvedor;
import algoritmos.ResolvedorClique;
import algoritmos.ResolvedorVertexCover;
import algoritmos.Verificador;
import java.util.ArrayList;
import java.util.List;
import modelo.Grafo;
import reduccion.Reduccion;
import util.GeneradorGrafos;

/**
 * Prueba masiva de que la reducción CLIQUE -> VERTEX COVER preserva la respuesta SÍ/NO.
 *
 * Para muchos grafos aleatorios pequeños y todos los k de 1 a n, comprueba tres cosas:
 *  1. CLIQUE(G, k) y VERTEX COVER(complemento de G, n - k) responden lo mismo.
 *  2. Cada solución devuelta es realmente válida (la revisa el Verificador).
 *  3. La respuesta de CLIQUE coincide con la de un oráculo exhaustivo independiente.
 *
 * Los algoritmos se reciben como Resolvedor, así que esta prueba sirve igual para cualquier
 * algoritmo nuevo que se agregue en el futuro, sin modificarla.
 *
 * Ejecución: correr el main. Al final imprime cuántas instancias se probaron y cuántas
 * fallaron (deben ser 0).
 */
public final class PruebasEquivalencia {

    // CONFIGURACIÓN POR DEFECTO (todo en un solo lugar para poder ajustarla fácil)
    private static final int N_MINIMO = 1;
    private static final int N_MAXIMO = 10;
    private static final double[] DENSIDADES = {0.2, 0.5, 0.8};
    private static final int SEMILLAS_POR_CONFIGURACION = 30;
    private static final int MAX_FALLOS_MOSTRADOS = 10;

    // No se crean objetos de esta clase: sus métodos se usan así: PruebasEquivalencia.metodo(...)
    private PruebasEquivalencia() {
    }

    /** Ejecuta la prueba con los algoritmos del proyecto e imprime el informe. */
    public static void main(String[] args) {
        ResultadoPruebas resultado = ejecutar(
                ResolvedorClique::buscarClique,
                ResolvedorVertexCover::buscarCobertura,
                N_MINIMO, N_MAXIMO, DENSIDADES, SEMILLAS_POR_CONFIGURACION);
        imprimirInforme(resultado);
        if (!resultado.esExitoso()) {
            System.exit(1); // código de salida distinto de 0: útil si algún día se automatiza
        }
    }

    /**
     * Prueba todas las combinaciones de n, densidad, semilla y k, y devuelve el resultado
     * sin imprimir nada (quien lo llame decide cómo mostrarlo).
     *
     * @param clique    algoritmo que resuelve CLIQUE
     * @param cobertura algoritmo que resuelve VERTEX COVER
     * @param nMinimo   menor cantidad de vértices a probar
     * @param nMaximo   mayor cantidad de vértices a probar
     * @param densidades probabilidades de arista a probar
     * @param semillas  cuántas semillas distintas se usan por cada (n, densidad)
     * @return total de instancias probadas y la lista de fallos
     *
     * Costo: depende de los algoritmos recibidos; con los del proyecto y n <= 10 termina en segundos.
     */
    public static ResultadoPruebas ejecutar(Resolvedor clique, Resolvedor cobertura,
                                            int nMinimo, int nMaximo,
                                            double[] densidades, int semillas) {
        List<String> fallos = new ArrayList<>();
        int total = 0;
        for (int n = nMinimo; n <= nMaximo; n++) {
            for (double densidad : densidades) {
                for (long semilla = 1; semilla <= semillas; semilla++) {
                    Grafo g = GeneradorGrafos.aleatorio(n, densidad, semilla);
                    Grafo complemento = Reduccion.construirComplemento(g);
                    for (int k = 1; k <= n; k++) {
                        total++;
                        String problema = evaluarInstancia(g, complemento, k, clique, cobertura);
                        if (problema != null) {
                            fallos.add("n=" + n + ", densidad=" + densidad + ", semilla=" + semilla
                                    + ", k=" + k + " -> " + problema);
                        }
                    }
                }
            }
        }
        return new ResultadoPruebas(total, fallos);
    }

    /**
     * Revisa una sola instancia.
     *
     * @return null si todo está bien, o un texto que describe el primer problema encontrado
     */
    private static String evaluarInstancia(Grafo g, Grafo complemento, int k,
                                           Resolvedor clique, Resolvedor cobertura) {
        int kPrima = Reduccion.transformarK(g.getN(), k);
        List<Integer> solucionClique = clique.resolver(g, k);
        List<Integer> solucionCobertura = cobertura.resolver(complemento, kPrima);
        boolean siClique = solucionClique != null;
        boolean siCobertura = solucionCobertura != null;

        // 1) La reducción debe preservar la respuesta.
        if (siClique != siCobertura) {
            return "respuestas distintas (CLIQUE=" + respuesta(siClique)
                    + ", VERTEX COVER=" + respuesta(siCobertura) + ")";
        }
        // 2) Toda solución devuelta debe ser válida.
        if (siClique && !Verificador.esClique(g, solucionClique, k)) {
            return "el clique devuelto no es válido: " + solucionClique;
        }
        if (siCobertura && !Verificador.esCobertura(complemento, solucionCobertura, kPrima)) {
            return "la cobertura devuelta no es válida: " + solucionCobertura;
        }
        // 3) La respuesta de CLIQUE debe coincidir con la del oráculo independiente.
        if (siClique != OraculoExhaustivo.existeClique(g, k)) {
            return "CLIQUE contradice al oráculo exhaustivo (respondió " + respuesta(siClique) + ")";
        }
        return null;
    }

    /** Convierte un booleano en "SÍ" o "NO" para los mensajes. */
    private static String respuesta(boolean existe) {
        return existe ? "SÍ" : "NO";
    }

    /** Imprime el resumen final (esto es lo que se captura para el informe). */
    private static void imprimirInforme(ResultadoPruebas resultado) {
        System.out.println("=== PRUEBA DE EQUIVALENCIA: CLIQUE(G, k) vs VERTEX COVER(complemento, n - k) ===");
        System.out.println("Instancias probadas : " + resultado.totalInstancias());
        System.out.println("Discrepancias       : " + resultado.discrepancias().size());
        int mostrar = Math.min(MAX_FALLOS_MOSTRADOS, resultado.discrepancias().size());
        for (int i = 0; i < mostrar; i++) {
            System.out.println("  [X] " + resultado.discrepancias().get(i));
        }
        System.out.println(resultado.esExitoso()
                ? "[OK] La reducción preservó la respuesta en todas las instancias."
                : "[X] Hay discrepancias: revisar los algoritmos.");
    }
}