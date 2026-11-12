# Reducciones polinómicas: CLIQUE → VERTEX COVER

**Proyecto Integrador — Unidad 2: Complejidad Computacional**
Asignatura: Estructura de Datos y Algoritmos II · Grupo 6 · Tema 7

---

## ¿Qué hace este proyecto?

Creamos una herramienta en Java que transforma una instancia del problema CLIQUE en una instancia equivalente de VERTEX COVER y comprueba que ambas tienen la misma respuesta .

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
| 1 | [James Caiño] | Núcleo de la reducción (`Grafo`, `Reduccion`), generador de grafos, menú, validación e integración |
| 2 | [Emanuel Encalada] | Algoritmos de CLIQUE y VERTEX COVER, verificador y pruebas de equivalencia |
| 3 | [Eduardo Fogacho] | Análisis de complejidad, medición de tiempos y experimentos |
| 4 | [Kevin Ramirez] | Fundamentos teóricos, NotebookLM, informe final y presentación |

---

## Estructura del repositorio

```
├── src/
│   ├── app/            Menú principal (Main)
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

## Estado del proyecto

- [x] Estructura del repositorio y reglas del equipo
- [ ] `Grafo` — Int. 1
- [ ] `Reduccion` y `GeneradorGrafos` — James God
- [ ] Menú `Main` y validación — James Gog
- [ ] `ResolvedorClique`, `ResolvedorVertexCover`, `Verificador` — Emanuel Encalada
- [ ] Pruebas de equivalencia — Emanuel Encalada
- [ ] `Benchmark` y experimentos — Eduardo Fogacho
- [ ] Informe y presentación — Kevin Ramirez