package app;

import modelo.Grafo;
import reduccion.Reduccion;
import util.GeneradorGrafos;

/**
 * Prueba rápida de Reduccion y GeneradorGrafos (antes de tener el menú).
 * Cada línea muestra el valor esperado para compararlo con el obtenido.
 */
public class DemoReduccion {

    public static void main(String[] args) {

        // 1) Ejemplo base: el complemento debe tener solo las aristas 0-3 y 1-3, y k' = 1.
        Grafo g = GeneradorGrafos.ejemploBase();
        int k = 3;
        System.out.println("=== GRAFO ORIGINAL G (k = " + k + ") ===");
        System.out.println(g);

        Grafo complemento = Reduccion.construirComplemento(g);
        int kPrima = Reduccion.transformarK(g.getN(), k);

        System.out.println("=== GRAFO COMPLEMENTO (k' = " + kPrima + ") ===");
        System.out.println(complemento);
        System.out.print("Aristas del complemento : ");
        for (int[] arista : complemento.getAristas()) {
            System.out.print("(" + arista[0] + "," + arista[1] + ") ");
        }
        System.out.println();
        System.out.println("k' = n - k : " + kPrima);
        System.out.println();

        // 2) Casos borde: el complemento del vacío es el completo, y al revés.
        System.out.println("Aristas del complemento del VACÍO de 5 : "
                + Reduccion.construirComplemento(GeneradorGrafos.vacio(5)).numAristas());
        System.out.println("Aristas del complemento del COMPLETO de 5 : "
                + Reduccion.construirComplemento(GeneradorGrafos.completo(5)).numAristas());
        System.out.println();

        // 3) Grafo aleatorio: entre G y su complemento tienen todas las aristas posibles, n(n-1)/2.
        int n = 10;
        Grafo aleatorio = GeneradorGrafos.aleatorio(n, 0.5, 42);
        Grafo compAleatorio = Reduccion.construirComplemento(aleatorio);
        System.out.println("Aleatorio n=10: aristas de G + aristas del complemento = "
                + (aleatorio.numAristas() + compAleatorio.numAristas())
                + " (esperado " + (n * (n - 1) / 2) + ")");

        // El complemento del complemento debe ser el grafo original.
        System.out.println("¿Complemento del complemento = original? : "
                + mismasAristas(aleatorio, Reduccion.construirComplemento(compAleatorio)));
        System.out.println();

        // 4) La misma semilla debe generar el mismo grafo.
        System.out.println("¿Misma semilla genera el mismo grafo? : "
                + mismasAristas(aleatorio, GeneradorGrafos.aleatorio(n, 0.5, 42)));
    }

    /** Devuelve true si los dos grafos tienen los mismos vértices y las mismas aristas. */
    private static boolean mismasAristas(Grafo a, Grafo b) {
        if (a.getN() != b.getN()) {
            return false;
        }
        for (int u = 0; u < a.getN(); u++) {
            for (int v = 0; v < a.getN(); v++) {
                if (a.hayArista(u, v) != b.hayArista(u, v)) {
                    return false;
                }
            }
        }
        return true;
    }
}