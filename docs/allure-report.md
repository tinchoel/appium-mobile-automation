# Reporte de ejecución con Allure Report

## Objetivo

Generar el reporte HTML de Allure con los resultados y métricas de los escenarios de Cucumber.

## Qué se agregó

| Archivo | Cambio |
|---------|--------|
| `pom.xml` | Dependencia `allure-cucumber7-jvm` y plugin `allure-maven` |
| `CucumberTestRunner` | El plugin de Cucumber `io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm`, además de `pretty` |
| `src/test/resources/allure.properties` | Carpeta de resultados: `target/allure-results` |

El plugin de Maven descarga Allure por su cuenta, así que no hace falta instalar nada en la computadora.

## Cómo generar el reporte

1. Ejecutar los escenarios (con emulador y `appium` en marcha). `clean` borra los resultados de corridas anteriores:

```text
mvn clean test "-Dtest=CucumberTestRunner"
```

Esto deja los resultados en `target/allure-results`.

2. Abrir el reporte:

```text
mvn allure:serve
```

Levanta un servidor local y abre el reporte en el navegador. Se cierra con `Ctrl + C` en la terminal.

Para generar el reporte como archivos (sin abrirlo):

```text
mvn allure:report
```

Queda en `target/site/allure-maven-plugin`. Esa carpeta no se abre directo con doble clic: hay que usar `allure:serve`.

## Qué muestra el reporte

- **Overview:** total de escenarios, porcentaje de éxito, duración y distribución por estado.
- **Suites / Behaviors:** escenarios agrupados por feature (Login, Productos, Carrito, Checkout, Logout).
- **Graphs y Timeline:** resultados por tag (`@Android`, `@login`, `@smoke`) y tiempos de cada escenario.
- **Detalle de cada escenario:** los pasos Given/When/Then con su resultado y, si falló, la captura de pantalla que adjunta el hook.

Los escenarios con `@known-issue` no aparecen porque se excluyen de la ejecución normal.

## Evidencia

`capturas/20-allure-reporte.png`: pestaña Overview con las métricas de la ejecución.

Los resultados y el reporte se generan dentro de `target`, que no se sube a GitHub.
