# Ejecutar el experimento

Desde la raíz del proyecto, en PowerShell:

```powershell
javac -d bin (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp bin experimentos.Experimento
```

La configuración está al inicio de [`Experimento.java`](../src/experimentos/Experimento.java): tamaños, densidad, semilla, 5 calentamientos y 15 repeticiones. Los promedios aparecen en consola; las mediciones individuales se guardan en `resultados/experimento.csv`. **Cada ejecución reemplaza ese CSV.**
