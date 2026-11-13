# Entrega Final: escenarios e implementación Cucumber para E-commerce Mobile

Repositorio: https://github.com/tinchoel/appium-mobile-automation (rama `master`)

## Objetivo

Escribir los escenarios BDD en Gherkin del e-commerce móvil, implementar los Step Definitions que los conectan con la automatización con Appium, alimentar los escenarios con datos externos y generar el reporte de Allure.

## Escenarios pedidos

| Escenario de la consigna | Dónde está |
|--------------------------|-----------|
| Login exitoso | `Login.feature`: "Inicio de sesión exitoso" |
| Login con credenciales inválidas | `Login.feature`: usuario bloqueado, sin usuario y sin contraseña (más los casos `@known-issue`) |
| Agregar producto al carrito | `Carrito.feature`: "Agregar un producto al carrito" |
| Flujo completo de compra | `Checkout.feature`: "Compra completa de un producto" |
| Logout | `Logout.feature`: "Cierre de sesión exitoso" |
| Scenario Outline: login con múltiples usuarios | `Login.feature`: "Inicio de sesión con múltiples usuarios" |
| Lectura de datos desde un archivo | `usuarios.csv` leído con `CsvReader` |

## Entregables

| Entregable | Estado | Evidencia |
|------------|--------|-----------|
| Mínimo 5 escenarios en Gherkin | 13 escenarios ejecutados (más 3 `@known-issue`) | `src/test/resources/features` |
| Keywords `Given`, `When`, `Then`, `And`, `But` | `But` se usa en `Logout.feature` | `Logout.feature` |
| Al menos un `Scenario Outline` | Login con múltiples usuarios | `Login.feature` |
| Tags para organización (Android) | `@Android` en todos los features, más un tag por funcionalidad | Todos los `.feature` |
| `Background` cuando sea necesario | Cada feature declara su precondición: pantalla de login, catálogo o sesión iniciada | Todos los `.feature` |
| Step Definitions para todos los escenarios | Pasos resueltos con los Screen Objects | `src/test/java/steps` |
| Archivo CSV como fuente de datos | `usuarios.csv` | `src/test/resources/data`, `docs/bdd-datos-csv.md` |
| Captura del reporte de Allure con las métricas | 13 casos, 100 % exitosos, 5 suites | `docs/capturas/20-allure-reporte.png` |

## Resultado de la ejecución

```text
Tests run: 16, Failures: 0, Errors: 0, Skipped: 3
```

Los 3 salteados son los escenarios `@known-issue`, que documentan defectos de la app (acepta cualquier contraseña y permite iniciar sesión con un usuario inexistente).

## Cómo reproducirlo

```text
mvn clean test "-Dtest=CucumberTestRunner"
mvn allure:serve
```

Requiere emulador encendido y Appium en marcha. Los detalles están en el `README.md`.
