# Reglas de trabajo del equipo 

Estas reglas existen para que el código de nosotros se una sin errores.

---

## 1. Herramientas 

| Herramienta | Versión |
|---|---|
| Java | **Temurin 25 LTS** (https://adoptium.net) — mínimo Java 17 |
| Editor | Visual Studio Code + extensión **Extension Pack for Java** (Microsoft) |
| Control de versiones | Git + GitHub |

No usar Maven, Gradle ni bibliotecas externas. Solo Java estándar.

---

## 2. Ramas: quién trabaja dónde

- **`main`** es la versión oficial y estable. **Nadie hace push directo a `main`**, excepto el líder técnico (yo jamesito papi hvo de oro).
- Cada integrante trabaja **solo en su rama**:

| Integrante | Rama | Trabaja en |
|---|---|---|
| James Caino — Núcleo y aplicación | `main` | `src/app`, `src/modelo`, `src/reduccion`, `src/util`, `docs/diagramas` |
| Emanuel Encalada— Algoritmos y pruebas | `int2-algoritmos` | `src/algoritmos`, `src/pruebas` |
| Eduardo fogacho — Complejidad y experimentos | `int3-experimentos` | `src/experimentos`, `resultados` |
| Kevin Ramirez — Teoría, informe y presentación | `int4-documentacion` | `docs/informe` |

- Para unir tu trabajo a `main` se abre un **Pull Request (PR)**, y james-godsito lo revisa y lo aprueba.
- **No modifiques archivos de otro integrante.** Si necesitas un cambio en ellos, pídeselo a su dueño por el grupo.

---

## 3. Flujo de trabajo de cada día

1. **Antes de empezar:** cámbiate a tu rama (abajo a la izquierda en VS Code) y trae lo último de `main`.
2. **Programa** solo en tus carpetas.
3. **Commit** cada vez que termines algo pequeño que funcione, con un mensaje claro (ver sección 4).
4. **Push** (botón *Sync Changes*) para subirlo a GitHub.
5. Cuando una parte esté completa y probada, **abre un Pull Request** hacia `main`.

---

## 4. Mensajes de commit

Formato: `tipo: qué hiciste (en presente, en español)`

| Tipo | Cuándo usarlo | Ejemplo |
|---|---|---|
| `feat` | Agregas una funcionalidad nueva | `feat: agrega búsqueda de clique con backtracking` |
| `fix` | Corriges un error | `fix: corrige índice fuera de rango en agregarArista` |
| `docs` | Documentación, README, comentarios | `docs: agrega pseudocódigo de la reducción` |
| `test` | Pruebas | `test: agrega casos de grafo vacío y completo` |
| `refactor` | Reorganizas código sin cambiar qué hace | `refactor: separa la validación en otro método` |
| `chore` | Configuración o estructura | `chore: actualiza .gitignore` |

❌ Mal: `cambios`, `asdf`, `ya funciona`, `final final v2`

---

## 5. Contratos: firmas acordadas (NO cambiarlas sin avisar al grupo)

Todos programamos usando exactamente estas firmas. Así el código encaja al unirlo.

### Convenciones obligatorias
- Los vértices se numeran de **0 a n−1**.
- El grafo es **no dirigido** y **sin lazos**: no existe la arista (v, v).
- Los resolvedores devuelven la **lista de vértices** de la solución, o **`null` si no existe solución**.
- Casos borde (para que CLIQUE y VERTEX COVER respondan igual):
  - `buscarClique` con `k <= 0` devuelve una lista vacía (SÍ); con `k > n` devuelve `null` (NO).
  - `buscarCobertura` con `k < 0` devuelve `null` (NO); con `k >= n` siempre existe (SÍ).
- **Las clases de algoritmos NO imprimen nada** (`System.out`). Solo devuelven resultados. Imprimir dentro de ellas arruina las mediciones de tiempo.

---

## 6. Estilo de código

- Nombres en español, **sin tildes ni ñ** en clases, métodos o variables (`numAristas`, no `númAristas`). En comentarios sí se pueden usar tildes.
- Clases en `PascalCase` (`ResolvedorClique`) y métodos/variables en `camelCase` (`buscarClique`).
- Indentación de 4 espacios.
- Cada clase y cada método público lleva un comentario **Javadoc** (`/** ... */`) que diga **qué hace, qué recibe, qué devuelve y su complejidad**.
- Los comentarios dentro del código explican **por qué** se hace algo, no lo obvio.

---

## 7. Checklist antes de abrir un Pull Request

- [ ] El proyecto compila sin errores (no hay subrayados rojos en VS Code).
- [ ] Probé mi código con al menos un caso SÍ y un caso NO.
- [ ] Solo modifiqué archivos de mis carpetas.
- [ ] Mis métodos respetan las firmas de la sección 5.
- [ ] No subí archivos `.class` ni la carpeta `bin/`.
- [ ] Los mensajes de commit siguen el formato de la sección 4.