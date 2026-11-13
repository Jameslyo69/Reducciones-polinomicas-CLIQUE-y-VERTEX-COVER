package modelo;

import java.util.ArrayList;
import java.util.List;

/**
 *  CLASE GRAFO 
 * guarda un grafo
 *
 * ¿CÓMO LO GUARDA?
 *   Con una MATRIZ DE ADYACENCIA: una tabla de n filas y n columnas de true/false.
 *     adyacencia[u][v] = true   --- los vértices u y v están conectados.
 *     adyacencia[u][v] = false  --- no están conectados.
 *
 * REGLAS DE ESTE GRAFO
 *   - Los vértices se numeran de 0 a n-1.
 *   - Es NO DIRIGIDO: si 0 está conectado con 1, entonces 1 está conectado con 0.
 *     Por eso la matriz siempre es simétrica (espejo respecto a la diagonal).
 *   - NO tiene lazos: un vértice nunca se conecta consigo mismo (diagonal en 0).
 *
 *   - Emanuel usa hayArista() y getAristas() para comprobar soluciones.
 */
public class Grafo {

    // ATRIBUTOS
    /** Cantidad de vértices (|V|). Es "final" porque no cambia después de crear el grafo. */
    private final int n;
    /** La matriz de adyacencia: adyacencia[u][v] es true si existe la arista u-v. */
    private final boolean[][] adyacencia;
    /**
     * Cantidad de aristas (|E|). Se suma 1 al agregar y se resta 1 al eliminar,
     * así siempre está actualizada y no hay que contar la matriz completa cada vez.
     */
    private int numAristas;

    // CONSTRUCTOR DEL GRAFO
    /**
     * Crea un grafo con n vértices y SIN aristas (todos desconectados).
     * Después se agregan las aristas una por una con agregarArista().
     * @param n cantidad de vértices (0 o más)
     * @throws IllegalArgumentException si n es negativo
     */
    public Grafo(int n) {
        // Un grafo no puede tener una cantidad negativa de vértices.
        if (n < 0) {
            throw new IllegalArgumentException("El número de vértices no puede ser negativo: " + n);
        }
        this.n = n;
        this.adyacencia = new boolean[n][n]; // todas las casillas empiezan en false
        this.numAristas = 0;                 // al inicio no hay aristas
    }

    // MÉTODOS PARA MODIFICAR EL GRAFO
    /**
     * Conecta los vértices u y v (agrega la arista u-v), si la arista ya existía, no hace nada (no se cuenta dos veces).
     * Marca las DOS casillas [u][v] y [v][u] porque el grafo es no dirigido.
     * @param u un extremo de la arista (entre 0 y n-1)
     * @param v el otro extremo (entre 0 y n-1)
     * @throws IllegalArgumentException si u o v no existen, o si u == v (lazo)
     */
    public void agregarArista(int u, int v) {
        // 1) Comprobamos que ambos vértices existan.
        validarVertice(u);
        validarVertice(v);

        // 2) Rechazamos lazos: un vértice no puede conectarse consigo mismo.
        if (u == v) {
            throw new IllegalArgumentException("No se permiten lazos (arista de un vértice consigo mismo): " + u);
        }

        // 3) Agregar la arista solo si todavía no existe.
        if (!adyacencia[u][v]) {
            adyacencia[u][v] = true; // u → v
            adyacencia[v][u] = true; // v → u (espejo, porque es no dirigido)
            numAristas++;            // hay una arista más
        }
    }

    /**
     * Desconecta los vértices u y v (elimina la arista u-v).
     * Si la arista no existía, no hace nada.

     * @param u un extremo de la arista (entre 0 y n-1)
     * @param v el otro extremo (entre 0 y n-1)
     * @throws IllegalArgumentException si u o v no existen
     */
    public void eliminarArista(int u, int v) {
        validarVertice(u);
        validarVertice(v);

        // Solo se elimina si existe; así el contador no queda en negativo.
        if (adyacencia[u][v]) {
            adyacencia[u][v] = false; // se borran las dos casillas (espejo)
            adyacencia[v][u] = false;
            numAristas--;             // hay una arista menos
        }
    }

    // MÉTODOS PARA CONSULTAR EL GRAFO 

    /**
     * Verifica que los vértices u y v estén conectados comprobando la existencia de la arista u-v.

     * @param u un vértice (entre 0 y n-1)
     * @param v otro vértice (entre 0 y n-1)
     * @return true si existe la arista u-v; false si no existe (o si u == v)
     * @throws IllegalArgumentException si u o v no existen
     */
    public boolean hayArista(int u, int v) {
        validarVertice(u);
        validarVertice(v);
        return adyacencia[u][v];
    }

    /**
     * Devuelve cuántos vértices tiene el grafo (n = |V|).
     *
     * @return la cantidad de vértices
     */
    public int getN() {
        return n;
    }

    /**
     * Devuelve cuántas aristas tiene el grafo (|E|).
     * @return la cantidad de aristas
     */
    public int numAristas() {
        return numAristas;
    }

    /**
     * Devuelve el grado del vértice v: cuántos vecinos (conexiones) tiene.
     * 
     * @param v el vértice a consultar (entre 0 y n-1)
     * @return cuántas aristas tocan a v
     * @throws IllegalArgumentException si v no existe
     */
    public int grado(int v) {
        validarVertice(v);
        int grado = 0;
        // Se recorre la fila v: cada true es un vecino.
        for (int w = 0; w < n; w++) {
            if (adyacencia[v][w]) {
                grado++;
            }
        }
        return grado;
    }

    /**
     * Devuelve todas las aristas en una lista. Cada arista es un arreglo de 2 números {u, v}.
     * 
     * @return una lista nueva con las aristas (cambiarla no modifica el grafo)
     */
    public List<int[]> getAristas() {
        List<int[]> aristas = new ArrayList<>(numAristas);

        // Solo se mira la parte de la matriz por ENCIMA de la diagonal (v > u).
        // Si se recorriera toda, cada arista saldría dos veces: {0,1} y {1,0}.
        for (int u = 0; u < n; u++) {
            for (int v = u + 1; v < n; v++) {
                if (adyacencia[u][v]) {
                    aristas.add(new int[] {u, v});
                }
            }
        }
        return aristas;
    }

    /**
     * Convierte el grafo en texto para mostrarlo en pantalla.
     * @return el grafo como texto
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Línea de resumen.
        sb.append("Grafo con ").append(n).append(" vértices y ")
          .append(numAristas).append(" aristas\n");

        // Encabezado: los números de las columnas.
        sb.append("    ");
        for (int v = 0; v < n; v++) {
            sb.append(String.format("%3d", v));
        }
        sb.append('\n');

        // Cada fila: el número del vértice y luego sus 1 y 0.
        for (int u = 0; u < n; u++) {
            sb.append(String.format("%3d ", u));
            for (int v = 0; v < n; v++) {
                sb.append(String.format("%3d", adyacencia[u][v] ? 1 : 0));
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    // MÉTODO AUXILIAR PRIVADO 

    /**
     * Revisa que v sea un vértice que existe (entre 0 y n-1).
     *
     * Todos los métodos que reciben vértices lo llaman primero. Así, si alguien
     * pasa un vértice inválido, recibe un mensaje claro en vez de un error confuso
     * de Java (ArrayIndexOutOfBoundsException).
     *
     * @param v el vértice a revisar
     * @throws IllegalArgumentException si v es menor que 0 o mayor o igual que n
     */
    private void validarVertice(int v) {
        if (v < 0 || v >= n) {
            throw new IllegalArgumentException(
                "Vértice fuera de rango: " + v + " (debe estar entre 0 y " + (n - 1) + ")");
        }
    }
}