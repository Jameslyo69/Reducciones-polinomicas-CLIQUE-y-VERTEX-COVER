package pruebas;

import java.util.List;

/**
 * Resultado de una tanda de pruebas: cuántas instancias se probaron y qué fallos hubo.
 *
 * Sirve para separar dos responsabilidades: quien ejecuta las pruebas solo produce este
 * resultado, y quien quiera mostrarlo (consola, archivo, informe) decide cómo hacerlo.
 *
 * @param totalInstancias cantidad de instancias (G, k) probadas
 * @param discrepancias   una descripción por cada fallo encontrado (vacía si todo salió bien)
 */
public record ResultadoPruebas(int totalInstancias, List<String> discrepancias) {

    /** Copia la lista para que el resultado no pueda modificarse desde afuera. */
    public ResultadoPruebas {
        discrepancias = List.copyOf(discrepancias);
    }

    /** Indica si todas las instancias pasaron las pruebas. */
    public boolean esExitoso() {
        return discrepancias.isEmpty();
    }
}
