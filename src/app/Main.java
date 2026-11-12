package app;

import java.util.Scanner;
import modelo.Grafo;
import reduccion.Reduccion;
import util.GeneradorGrafos;
import util.Validador;

/**
 * Programa principal
 * Flujo: el usuario crea un grafo G (a mano, al azar o el de ejemplo), elige k,
 * y el programa lo transforma en (complemento de G, k' = n - k).
 *
 * La opción 6 (resolver y comparar) se completa cuando se una
 * el trabajo del Integrante 2 (ResolvedorClique, ResolvedorVertexCover, Verificador).
 */
public class Main {

    /** Máximo de vértices al ingresar un grafo a mano (más sería muy largo de escribir). */
    private static final int MAX_N_MANUAL = 30;

    /** Máximo de vértices para grafos aleatorios (la matriz de 2000 × 2000 ocupa unos 4 MB). */
    private static final int MAX_N_ALEATORIO = 2000;

    /** Hasta este tamaño se imprime la matriz completa; con más vértices solo se muestra un resumen. */
    private static final int MAX_N_MOSTRAR = 20;

    private static final Scanner sc = new Scanner(System.in);

    /** El grafo con el que se está trabajando (null si todavía no se ha creado ninguno). */
    private static Grafo grafoActual = null;

    public static void main(String[] args) {
        System.out.println("==============================================================");
        System.out.println("  REDUCCIÓN POLINÓMICA: CLIQUE -> VERTEX COVER  (Grupo 6, Tema 7)");
        System.out.println("==============================================================");

        int opcion;
        do {
            mostrarMenu();
            opcion = Validador.leerEntero(sc, "Elija una opción: ", 0, 7);
            System.out.println();
            switch (opcion) {
                case 1: ingresarGrafoManual(); break;
                case 2: generarGrafoAleatorio(); break;
                case 3: cargarEjemplo(); break;
                case 4: mostrarGrafoActual(); break;
                case 5: aplicarReduccion(); break;
                case 6: resolverYComparar(); break;
                case 7: mostrarComplejidad(); break;
                case 0: System.out.println("¡Hasta luego!"); break;
                default: break;
            }
        } while (opcion != 0);
    }

    /** Imprime las opciones del menú. */
    private static void mostrarMenu() {
        System.out.println();
        System.out.println("--------------------------- MENÚ ---------------------------");
        System.out.println(" 1. Ingresar un grafo manualmente");
        System.out.println(" 2. Generar un grafo aleatorio");
        System.out.println(" 3. Cargar el grafo de ejemplo (4 vértices)");
        System.out.println(" 4. Mostrar el grafo actual");
        System.out.println(" 5. Aplicar la reducción CLIQUE -> VERTEX COVER");
        System.out.println(" 6. Resolver ambos problemas y comparar respuestas");
        System.out.println(" 7. Ver la complejidad teórica");
        System.out.println(" 0. Salir");
        System.out.println("------------------------------------------------------------");
    }

    /** Opción 1: el usuario escribe el número de vértices y luego cada arista ("u v"). */
    private static void ingresarGrafoManual() {
        int n = Validador.leerEntero(sc, "Número de vértices (1 a " + MAX_N_MANUAL + "): ", 1, MAX_N_MANUAL);
        int maxAristas = n * (n - 1) / 2; // máximo de aristas posibles sin repetir ni lazos
        int m = Validador.leerEntero(sc, "Número de aristas (0 a " + maxAristas + "): ", 0, maxAristas);

        Grafo g = new Grafo(n);
        System.out.println("Escriba cada arista como dos vértices separados por espacio (ej: 0 1).");
        // Se repite hasta tener m aristas distintas; las repetidas se rechazan.
        while (g.numAristas() < m) {
            int[] a = Validador.leerArista(sc, "  Arista " + (g.numAristas() + 1) + ": ", n);
            if (g.hayArista(a[0], a[1])) {
                System.out.println("  [X] Esa arista ya fue ingresada. Escriba otra.");
            } else {
                g.agregarArista(a[0], a[1]);
            }
        }
        grafoActual = g;
        System.out.println("[OK] Grafo creado.");
        mostrarGrafo(grafoActual);
    }

    /** Opción 2: crea un grafo al azar con el tamaño, densidad y semilla que elija el usuario. */
    private static void generarGrafoAleatorio() {
        int n = Validador.leerEntero(sc, "Número de vértices (1 a " + MAX_N_ALEATORIO + "): ", 1, MAX_N_ALEATORIO);
        double densidad = Validador.leerDecimal(sc, "Densidad (0.0 a 1.0, ej: 0.5): ", 0.0, 1.0);
        long semilla = Validador.leerLong(sc, "Semilla (cualquier entero, ej: 42): ");
        grafoActual = GeneradorGrafos.aleatorio(n, densidad, semilla);
        System.out.println("[OK] Grafo aleatorio creado.");
        mostrarGrafo(grafoActual);
    }

    /** Opción 3: carga el grafo de ejemplo del proyecto (aristas 0-1, 0-2, 1-2, 2-3). */
    private static void cargarEjemplo() {
        grafoActual = GeneradorGrafos.ejemploBase();
        System.out.println("[OK] Grafo de ejemplo cargado (pruebe con k = 3).");
        mostrarGrafo(grafoActual);
    }

    /** Opción 4: muestra el grafo actual, o avisa si todavía no hay uno. */
    private static void mostrarGrafoActual() {
        if (hayGrafo()) {
            mostrarGrafo(grafoActual);
        }
    }

    /**
     * Opción 5: pide k, construye el complemento y calcula k' = n - k.
     * Mide solo el tiempo de la reducción (no incluye leer datos ni imprimir).
     */
    private static void aplicarReduccion() {
        if (!hayGrafo()) {
            return;
        }
        int n = grafoActual.getN();
        int k = Validador.leerEntero(sc, "Tamaño de clique buscado k (1 a " + n + "): ", 1, n);

        long inicio = System.nanoTime();
        Grafo complemento = Reduccion.construirComplemento(grafoActual);
        int kPrima = Reduccion.transformarK(n, k);
        long fin = System.nanoTime();

        System.out.println();
        System.out.println("=== INSTANCIA DE CLIQUE: (G, k = " + k + ") ===");
        mostrarGrafo(grafoActual);
        System.out.println("=== INSTANCIA DE VERTEX COVER: (complemento de G, k' = " + n + " - " + k + " = " + kPrima + ") ===");
        mostrarGrafo(complemento);
        System.out.printf("Tiempo de la reducción: %.4f ms  (complejidad Theta(n^2))%n", (fin - inicio) / 1_000_000.0);
        System.out.println("Pregunta equivalente: ¿G tiene un clique de tamaño >= " + k
                + "?  <=>  ¿el complemento tiene una cobertura de tamaño <= " + kPrima + "?");
    }

    /** Opción 6: se completa al integrar el trabajo del Integrante 2. */
    private static void resolverYComparar() {
        System.out.println("Esta opción estará disponible cuando se integren los algoritmos");
        System.out.println("de CLIQUE y VERTEX COVER (Integrante 2).");
    }

    /** Opción 7: muestra un resumen de la complejidad de cada parte. */
    private static void mostrarComplejidad() {
        System.out.println("=== COMPLEJIDAD TEÓRICA (n = vértices) ===");
        System.out.println("Reducción (construir complemento): tiempo Theta(n^2), espacio Theta(n^2)");
        System.out.println("   -> revisa cada uno de los n(n-1)/2 pares de vértices una vez.");
        System.out.println("Transformar k en k' = n - k:        tiempo Theta(1)");
        System.out.println("Consultar una arista (matriz):      tiempo Theta(1)");
        System.out.println("Resolver CLIQUE por fuerza bruta:   O(C(n,k) * k^2) -> en el peor caso exponencial, O(2^n * n^2)");
        System.out.println("Resolver VERTEX COVER (fuerza bruta): O(2^n * n^2) en el peor caso");
        System.out.println("Verificar una solución dada:        tiempo polinómico, O(n^2)");
        System.out.println();
        System.out.println("Conclusión: transformar es barato (polinómico); resolver es caro (exponencial).");
    }

    // Métodos de apoyo 

    /** Devuelve true si ya hay un grafo cargado; si no, avisa al usuario. */
    private static boolean hayGrafo() {
        if (grafoActual == null) {
            System.out.println("[X] Primero cree o cargue un grafo (opciones 1, 2 o 3).");
            return false;
        }
        return true;
    }

    /** Imprime la matriz si el grafo es pequeño; si es grande, solo un resumen. */
    private static void mostrarGrafo(Grafo g) {
        if (g.getN() <= MAX_N_MOSTRAR) {
            System.out.println(g);
        } else {
            System.out.println("Grafo con " + g.getN() + " vértices y " + g.numAristas()
                    + " aristas (la matriz no se muestra porque tiene más de " + MAX_N_MOSTRAR + " vértices).");
        }
    }
}