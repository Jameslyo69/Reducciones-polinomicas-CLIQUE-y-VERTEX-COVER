package experimentos;

import algoritmos.ResolvedorClique;
import algoritmos.ResolvedorVertexCover;
import algoritmos.Verificador;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import modelo.Grafo;
import reduccion.Reduccion;
import util.GeneradorGrafos;

/**
 * Experimento fijo: mide por separado la reducción y ambos resolvedores.
 */
public final class Experimento {

    private static final int[] TAMANOS = {10, 20, 30, 40, 50};
    private static final double DENSIDAD = 0.8;
    private static final long SEMILLA = 10;
    private static final int CALENTAMIENTOS = 5;
    private static final int REPETICIONES = 15;
    private static final Path SALIDA = Path.of("resultados", "experimento.csv");

    /**
     * Genera un grafo por tamaño, calienta los métodos, mide cada operación y guarda
     * las repeticiones en CSV. Muestra los promedios solo en consola.
     * El tiempo total depende de los resolvedores exactos para los tamaños elegidos.
     *
     * @param args no se utilizan argumentos; la configuración está arriba
     * @throws IOException si no se puede escribir el CSV
     */
    public static void main(String[] args) throws IOException {
        Files.createDirectories(SALIDA.getParent());
        try (BufferedWriter csv = Files.newBufferedWriter(SALIDA, StandardCharsets.UTF_8)) {
            csv.write("prueba,tamano_entrada_n,tiempo_ejecucion_ms,memoria_estimada,"
                    + "resultado,operacion,repeticion,k,densidad_objetivo,semilla");
            csv.newLine();

            System.out.println("Promedio de tiempo por operación (ms)");
            System.out.printf("%5s %5s %14s %14s %17s %10s%n",
                    "n", "k", "Reducción", "CLIQUE", "VERTEX COVER", "Resultado");

            for (int prueba = 0; prueba < TAMANOS.length; prueba++) {
                int n = TAMANOS[prueba];
                int k = (n + 1) / 2;
                int kPrima = n - k;
                Grafo grafo = GeneradorGrafos.aleatorio(n, DENSIDAD, SEMILLA);
                int aristasComplemento = n * (n - 1) / 2 - grafo.numAristas();
                int candidatos = 0;
                for (int v = 0; v < n; v++) {
                    if (grafo.grado(v) >= k - 1) {
                        candidatos++;
                    }
                }
                String memoriaReduccion = (long) n * n + " celdas booleanas; O(n) referencias";
                String memoriaClique = (n + candidatos + k) + " celdas int; O(k) pila y respuesta";
                String memoriaCobertura = (2L * aristasComplemento)
                        + " celdas int para aristas; " + (2 * n)
                        + " booleanas; O(m'+k') referencias y pila";

                for (int i = 0; i < CALENTAMIENTOS; i++) {
                    Grafo complemento = Reduccion.construirComplemento(grafo);
                    Reduccion.transformarK(n, k);
                    ResolvedorClique.buscarClique(grafo, k);
                    ResolvedorVertexCover.buscarCobertura(complemento, kPrima);
                }

                double sumaReduccion = 0;
                double sumaClique = 0;
                double sumaCobertura = 0;
                String resultado = "";
                for (int repeticion = 1; repeticion <= REPETICIONES; repeticion++) {
                    long inicio = System.nanoTime();
                    Grafo complemento = Reduccion.construirComplemento(grafo);
                    int parametroReducido = Reduccion.transformarK(n, k);
                    long tiempoReduccion = System.nanoTime() - inicio;

                    inicio = System.nanoTime();
                    List<Integer> clique = ResolvedorClique.buscarClique(grafo, k);
                    long tiempoClique = System.nanoTime() - inicio;

                    inicio = System.nanoTime();
                    List<Integer> cobertura = ResolvedorVertexCover.buscarCobertura(
                            complemento, parametroReducido);
                    long tiempoCobertura = System.nanoTime() - inicio;

                    if (clique != null && !Verificador.esClique(grafo, clique, k)) {
                        throw new IllegalStateException("CLIQUE devolvió una solución inválida");
                    }
                    if (cobertura != null && !Verificador.esCobertura(
                            complemento, cobertura, kPrima)) {
                        throw new IllegalStateException("VERTEX COVER devolvió una solución inválida");
                    }
                    if ((clique == null) != (cobertura == null)) {
                        throw new IllegalStateException("Las respuestas de ambos problemas no coinciden");
                    }
                    resultado = clique == null ? "NO" : "SI";

                    sumaReduccion += tiempoReduccion;
                    sumaClique += tiempoClique;
                    sumaCobertura += tiempoCobertura;
                    escribirFila(csv, prueba + 1, n, k, repeticion, "REDUCCION",
                            tiempoReduccion, memoriaReduccion, "COMPLEMENTO_GENERADO");
                    escribirFila(csv, prueba + 1, n, k, repeticion, "CLIQUE",
                            tiempoClique, memoriaClique, resultado);
                    escribirFila(csv, prueba + 1, n, k, repeticion, "VERTEX_COVER",
                            tiempoCobertura, memoriaCobertura, resultado);
                }

                System.out.printf("%5d %5d %14.6f %14.6f %17.6f %10s%n", n, k,
                        sumaReduccion / REPETICIONES / 1_000_000.0,
                        sumaClique / REPETICIONES / 1_000_000.0,
                        sumaCobertura / REPETICIONES / 1_000_000.0, resultado);
            }
        }
        System.out.println("Mediciones individuales: " + SALIDA.toAbsolutePath());
    }

    private static void escribirFila(BufferedWriter csv, int prueba, int n, int k,
                                    int repeticion, String operacion, long tiempoNanos,
                                    String memoria, String resultado) throws IOException {
        csv.write(String.format(Locale.ROOT, "%d,%d,%.6f,%s,%s,%s,%d,%d,%.6f,%d%n",
                prueba, n, tiempoNanos / 1_000_000.0, memoria, resultado,
                operacion, repeticion, k, DENSIDAD, SEMILLA));
    }
}
