package app;

import modelo.Grafo;

/**
 * Demostramos rápidamente la clase Grafo con el ejemplo de 4 vértices del proyecto:
 * vértices A=0, B=1, C=2, D=3 y aristas A-B, B-C, A-C, C-D.
 *
 * <p>Sirve para comprobar que Grafo funciona antes de tener el menú principal.</p>
 */
public class DemoGrafo {

    public static void main(String[] args) {
        Grafo g = new Grafo(4);
        g.agregarArista(0, 1); // A-B
        g.agregarArista(1, 2); // B-C
        g.agregarArista(0, 2); // A-C
        g.agregarArista(2, 3); // C-D
        g.agregarArista(1, 0); // repetida (B-A = A-B): no debe contarse dos veces

        System.out.println(g);
        System.out.println("Número de aristas : " + g.numAristas());
        System.out.println("¿Hay arista A-C? : " + g.hayArista(0, 2));
        System.out.println("¿Hay arista A-D? : " + g.hayArista(0, 3));
        System.out.println("Grado de C : " + g.grado(2));

        System.out.print("Lista de aristas: ");
        for (int[] arista : g.getAristas()) {
            System.out.print("(" + arista[0] + "," + arista[1] + ") ");
        }
        System.out.println();

        // Prueba de validación: un vértice fuera de rango debe producir un error controlado.
        try {
            g.agregarArista(0, 7);
        } catch (IllegalArgumentException e) {
            System.out.println("Error controlado (esperado): " + e.getMessage());
        }
        // Prueba de validación: un lazo debe producir un error controlado.
        try {
            g.agregarArista(2, 2);
        } catch (IllegalArgumentException e) {
            System.out.println("Error controlado (esperado): " + e.getMessage());
        }
    }
}