# Avance 5.2 – Configuración de Cucumber para framework mobile

## Objetivo

Configurar el Test Runner para que JUnit 5 descubra y ejecute los escenarios `.feature` con Cucumber. En este avance los Step Definitions son temporales (solo imprimen en consola): no se usa Appium todavía.

## 1. Dependencias en el `pom.xml`

Agregar dentro de `<properties>`:

```text
<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
<cucumber.version>7.20.1</cucumber.version>
<junit.platform.version>1.13.4</junit.platform.version>
```

Y dentro de `<dependencies>`:

```text
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-java</artifactId>
    <version>${cucumber.version}</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-junit-platform-engine</artifactId>
    <version>${cucumber.version}</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.platform</groupId>
    <artifactId>junit-platform-suite</artifactId>
    <version>${junit.platform.version}</version>
    <scope>test</scope>
</dependency>
```

Sobre las versiones:
- `junit-platform-suite` usa `1.13.4` y no `1.11.4` como dice el material, porque el proyecto usa JUnit Jupiter 5.13.4 (que corresponde a la plataforma 1.13.4). Conviene que coincidan.
- `sourceEncoding` en UTF-8 es necesario porque los pasos tienen tildes y eñes dentro de las anotaciones.

## 2. Estructura

```text
src/test/
├── java/
│   ├── runners/
│   │   └── CucumberTestRunner.java
│   └── steps/
│       ├── LoginSteps.java
│       ├── ProductoSteps.java
│       ├── CarritoSteps.java
│       └── CheckoutSteps.java
└── resources/
    └── features/
        ├── Login.feature
        ├── Productos.feature
        ├── Carrito.feature
        └── Checkout.feature
```

## 3. El Runner

```text
@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "steps")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty")
public class CucumberTestRunner { }
```

| Anotación | Para qué sirve |
|-----------|----------------|
| `@Suite` | Define una suite de JUnit Platform |
| `@IncludeEngines("cucumber")` | Usa el motor de Cucumber |
| `@SelectClasspathResource("features")` | Busca los `.feature` en `src/test/resources/features` |
| `GLUE_PROPERTY_NAME = "steps"` | Busca los Step Definitions en el paquete `steps` |
| `PLUGIN_PROPERTY_NAME = "pretty"` | Muestra el detalle de cada escenario en la consola |

## 4. Features

| Archivo | Escenarios |
|---------|-----------|
| `Login.feature` | 6 (el exitoso y cinco inválidos, ver avance 5.1) |
| `Productos.feature` | 2 (ver catálogo, ver detalle de un producto) |
| `Carrito.feature` | 1 (agregar un producto al carrito) |
| `Checkout.feature` | 1 (compra completa) |

Todos llevan el tag `@Android` y uno propio de la funcionalidad (`@login`, `@productos`, `@carrito`, `@checkout`).

## 5. Cómo ejecutar

Desde IntelliJ: abrir `CucumberTestRunner.java` y hacer Run.

Desde la terminal:

```text
mvn clean test "-Dtest=CucumberTestRunner"
```

`mvn clean test` a secas no lo ejecuta, porque Maven solo toma por defecto las clases cuyo nombre termina en `Test` (el Runner termina en `Runner`). Así los tests de Appium y los de Cucumber no se mezclan.

## 6. Resultado esperado

```text
10 Scenarios (10 passed)
54 Steps (54 passed)
```

Pasan porque los pasos solo imprimen en consola. Este resultado demuestra que Cucumber encontró los `.feature` y los vinculó con sus Step Definitions.

## 7. Evidencia

`capturas/16-cucumber-descubre-escenarios.png`: consola con los escenarios descubiertos y el resumen final.

## Errores comunes

| Síntoma | Causa probable |
|---------|----------------|
| `No tests found` | Falta una dependencia, o el motor `cucumber` no está en el classpath |
| Pasos "undefined" | El texto del paso no coincide con la anotación, o el paquete de `GLUE_PROPERTY_NAME` está mal |
| Caracteres raros en la consola | Falta `sourceEncoding` UTF-8 en el `pom.xml` |
| No encuentra los features | La carpeta no es `src/test/resources/features` |

## Entregables

- `CucumberTestRunner.java` ejecutable desde JUnit 5
- Estructura de paquetes organizada para BDD
- Cuatro archivos `.feature` con las funcionalidades principales
- Evidencia de la ejecución con los escenarios descubiertos
