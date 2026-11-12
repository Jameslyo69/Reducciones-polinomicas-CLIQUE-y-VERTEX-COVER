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
| 4 | Kevin Ramirez | Fundamentos teóricos, NotebookLM, informe final y presentación |

---

## Estructura del repositorio

```
├── src/
│   ├── app/            Menú principal (Main) y demos de prueba
│   ├── modelo/         Grafo con matriz de adyacencia
│   ├── reduccion/      Transformación CLIQUE → VERTEX COVER
│   ├── util/           Generador de grafos y validación de entradas
│   ├── algoritmos/     Resolvedores exactos y verificador
│   ├── pruebas/        Pruebas de equivalencia de la reducción
│   └── experimentos/   Medición de tiempos y experimentos
├── resultados/         Resultados experimentales (CSV, gráficas)
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

**Desde VS Code:** abrir `src/app/Main.java` y presionar **Run** encima de `main`.

**Desde la terminal (PowerShell, en la carpeta del proyecto):**
```powershell
javac -d bin (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp bin app.Main
```

**Desde la terminal (Mac/Linux):**
```bash
javac -d bin $(find src -name "*.java")
java -cp bin app.Main
```

Demos rápidas (sin menú): `app.DemoGrafo` y `app.DemoReduccion`.

---

## Estado del proyecto

- [x] Estructura del repositorio y reglas del equipo — James Caiño
- [x] `Grafo` — James Caiño
- [x] `Reduccion` y `GeneradorGrafos` — James Caiño
- [ ] Menú `Main` y `Validador` — James Caiño (listo; falta la opción 6, que se completa al integrar el trabajo de Emanuel)
- [ ] `ResolvedorClique`, `ResolvedorVertexCover`, `Verificador` — Emanuel Encalada
- [ ] Pruebas de equivalencia — Emanuel Encalada
- [ ] `Benchmark` y experimentos — Eduardo Fogacho
- [ ] Informe y presentación — Kevin Ramirez