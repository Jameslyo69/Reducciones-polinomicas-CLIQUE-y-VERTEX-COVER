# Reducciones polinómicas: CLIQUE → VERTEX COVER

**Proyecto Integrador — Unidad 2: Complejidad Computacional**
Asignatura: Estructura de Datos y Algoritmos II · Grupo 6 · Tema 7
Lenguaje: Java (estándar, sin bibliotecas externas)

---

## ¿Qué hace este proyecto?

Creamos una herramienta en Java que transforma una instancia del problema CLIQUE en una instancia equivalente de VERTEX COVER y comprueba que ambas tienen la misma respuesta (SÍ/NO).

La reducción se basa en este resultado:

> **G tiene un clique de tamaño ≥ k ⇔ el grafo complemento Ḡ tiene una cobertura de vértices de tamaño ≤ |V| − k**

Flujo del programa:

```
Grafo G y valor k ──► construir complemento Ḡ ──► transformar k' = |V| − k
        │                                                  │
        ▼                                                  ▼
 resolver CLIQUE(G, k)                      resolver VERTEX COVER(Ḡ, k')
        │                                                  │
        └──────────────► comparar respuestas SÍ/NO ◄───────┘
```

---

## Integrantes y responsabilidades

| # | Integrante | Responsabilidad |
|---|---|---|
| 1 | James Caiño | Núcleo de la reducción (`Grafo`, `Reduccion`), generador de grafos, menú, validación e integración |
| 2 | Emanuel Encalada | Algoritmos de CLIQUE y VERTEX COVER, verificador y pruebas de equivalencia |
| 3 | Eduardo Fogacho | Análisis de complejidad, medición de tiempos y experimentos |
| 4 | Matías Chushig | Fundamentos teóricos, NotebookLM, informe final y presentación |

---

## Estructura del repositorio

```
├── src/
│   ├── app/            Main (menú), DemoGrafo, DemoReduccion
│   ├── modelo/         Grafo (matriz de adyacencia)
│   ├── reduccion/      Reduccion (complemento y k' = n − k)
│   ├── util/           GeneradorGrafos, Validador
│   ├── algoritmos/     ResolvedorClique, ResolvedorVertexCover, Verificador, Resolvedor
│   ├── pruebas/        PruebasFuncionales, PruebasEquivalencia, OraculoExhaustivo, ResultadoPruebas
│   └── experimentos/   Experimento (medición de tiempos)
├── resultados/         experimento.csv e instrucciones de los experimentos
├── docs/
│   ├── informe/        Secciones del informe técnico
│   └── diagramas/      Diagramas de flujo
├── CONTRIBUTING.md     Reglas de trabajo del equipo
└── README.md
```

---

## Cómo empezar (primera vez)

1. Instala **Git**: https://git-scm.com/download/win
2. Instala **Java Temurin 25 LTS**: https://adoptium.net (activa la opción *Set JAVA_HOME variable*).
3. Instala **Visual Studio Code**: https://code.visualstudio.com y, dentro de él, la extensión **Extension Pack for Java** (Microsoft).
4. En VS Code: `Ctrl + Shift + P` → **Git: Clone** → pega la URL de este repositorio.
5. **Lee [CONTRIBUTING.md](CONTRIBUTING.md) antes de programar.**

---

## Cómo ejecutar

**Desde VS Code:** abrir el archivo y presionar **Run** encima de `main`.

**Desde la terminal (PowerShell, en la carpeta del proyecto):** primero compilar todo una vez:
```powershell
javac -d bin (Get-ChildItem -Recurse src -Filter *.java).FullName
```
Luego ejecutar lo que se necesite:

| Comando | Qué hace |
|---|---|
| `java -cp bin app.Main` | Menú principal (reducción, resolver y comparar, complejidad) |
| `java -cp bin pruebas.PruebasFuncionales` | 14 casos con resultado esperado (SÍ, NO, vacío, completo, k = 0, k > n) |
| `java -cp bin pruebas.PruebasEquivalencia` | 4950 grafos aleatorios: comprueba que CLIQUE y VERTEX COVER responden igual |
| `java -cp bin experimentos.Experimento` | Mide tiempos y guarda `resultados/experimento.csv` (ver `resultados/HOW_TO_MAKE_EXPERIMENTS.md`) |

**Mac/Linux:** compilar con `javac -d bin $(find src -name "*.java")` y usar los mismos comandos `java`.

> Si VS Code marca errores en rojo después de un Pull: `Ctrl + Shift + P` → **Java: Clean Java Language Server Workspace** → **Reload and delete**.

---

## Resultados de las pruebas

- `PruebasFuncionales`: **14 de 14** casos correctos.
- `PruebasEquivalencia`: **4950 instancias, 0 discrepancias**. La reducción preservó la respuesta en todos los casos.

---

## Estado del proyecto

- [x] Estructura del repositorio y reglas del equipo — James Caiño
- [x] `Grafo`, `Reduccion`, `GeneradorGrafos` — James Caiño
- [x] Menú `Main` y `Validador` — James Caiño
- [x] `ResolvedorClique`, `ResolvedorVertexCover`, `Verificador` — Emanuel Encalada
- [x] Pruebas funcionales y de equivalencia — Emanuel Encalada
- [x] Experimentos y medición de tiempos — Eduardo Fogacho
- [ ] Informe técnico y presentación — todo el equipo (coordina Matías Chushig)