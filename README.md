# Appium Mobile Automation

Automatización de pruebas de la app Android **My Demo App** (Sauce Labs) con Appium, Java, Maven, JUnit 5, Cucumber y Allure Report. Proyecto del curso de QA Automation Mobile de Alkemy.

Cubre el flujo de e-commerce completo: login, selección de productos, carrito, checkout y logout.

## Tecnologías

| Herramienta | Versión |
|-------------|---------|
| Java | 17 |
| Maven | Instalado y en el PATH |
| Appium Server / UiAutomator2 | 3.8.0 / 8.7.0 |
| Appium Java Client | 10.1.1 |
| JUnit 5 | 5.13.4 |
| Cucumber | 7.20.1 |
| Allure Report | 2.29.1 |
| Dispositivo de prueba | Emulador Android 16 (API 36) |

## Estructura

```text
src/test/
├── java/
│   ├── config/      ConfigReader y DriverFactory (capabilities)
│   ├── screens/     Screen Objects con Page Factory (BaseScreen, Login, Home, Cart, Checkout)
│   ├── tests/       Pruebas JUnit 5 sobre los Screen Objects
│   ├── runners/     CucumberTestRunner
│   ├── steps/       Step Definitions y Hooks de Cucumber
│   └── support/     TestContext y CsvReader
└── resources/
    ├── apks/        APK de My Demo App
    ├── features/    Escenarios Gherkin
    ├── data/        Datos de prueba en CSV
    └── config.properties
docs/                Documentación de cada avance y capturas de evidencia
```

Patrón de diseño: **Page Object Model** con **Page Factory** (`@AndroidFindBy`). Todas las pantallas heredan de `BaseScreen`, que reúne las esperas explícitas y las acciones comunes.

## Configuración

Las capabilities están en `src/test/resources/config.properties`: URL de Appium, dispositivo, ruta del APK y actividad de la app. El APK está incluido en el repositorio.

## Cómo ejecutar

Orden de arranque:

1. Encender el emulador.
2. Verificar que aparece con `adb devices`.
3. Iniciar Appium en su propia terminal con `appium`.
4. Ejecutar Maven desde la raíz del proyecto.

Pruebas JUnit (suite completa o por etiqueta):

```text
mvn clean test
mvn clean test "-Dgroups=POM"
```

Escenarios Cucumber:

```text
mvn clean test "-Dtest=CucumberTestRunner"
mvn clean test "-Dtest=CucumberTestRunner" "-Dcucumber.filter.tags=@smoke"
```

Reporte Allure (después de ejecutar Cucumber):

```text
mvn allure:serve
```

## Escenarios BDD

| Feature | Escenarios |
|---------|-----------|
| `Login.feature` | Login exitoso, usuario bloqueado, sin usuario, sin contraseña y un `Scenario Outline` con datos del CSV |
| `Productos.feature` | Ver el catálogo y el detalle de un producto |
| `Carrito.feature` | Agregar un producto al carrito |
| `Checkout.feature` | Compra completa |
| `Logout.feature` | Cierre de sesión |

Tags: `@Android` en todos, más `@login`, `@productos`, `@carrito`, `@checkout`, `@logout`, `@multiusuario` y `@smoke`. Los datos del `Scenario Outline` están en `src/test/resources/data/usuarios.csv`.

## Defectos conocidos de la app

My Demo App acepta cualquier contraseña para un usuario válido y permite iniciar sesión con un usuario inexistente. Los escenarios que describen el comportamiento esperado llevan el tag `@known-issue` y se excluyen de la ejecución normal. Para correrlos:

```text
mvn clean test "-Dtest=CucumberTestRunner" "-Dcucumber.filter.tags=@known-issue"
```

## Documentación

| Tema | Archivo |
|------|---------|
| Análisis del flujo, pantallas y plan de pruebas | `docs/flujo-compra.md`, `docs/mapeo-elementos.md`, `docs/plan-de-pruebas.md` |
| Arquitectura y diagrama de clases | `docs/arquitectura.md`, `docs/avance-4.1.md` |
| Screen Objects y Page Factory | `docs/avance-4.2.md`, `docs/avance-4.3.md`, `docs/avance-4.4.md` |
| BDD y Cucumber | `docs/avance-5.1.md`, `docs/avance-5.2.md`, `docs/bdd-step-definitions.md`, `docs/bdd-logout-outline.md`, `docs/bdd-datos-csv.md` |
| Reporte Allure | `docs/allure-report.md` |
| Entrega Final | `docs/entrega-final.md` |
