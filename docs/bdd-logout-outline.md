# Logout y Scenario Outline de login

## Objetivo

Completar los escenarios de la Entrega Final: el cierre de sesión y el login con múltiples usuarios mediante un `Scenario Outline`.

## Logout

Archivo: `src/test/resources/features/Logout.feature`

```text
@Android @logout
Feature: Cierre de sesión

  Background:
    Given que el usuario inició sesión con el usuario "bod@example.com" y la contraseña "10203040"

  @smoke
  Scenario: Cierre de sesión exitoso
    When cierra la sesión desde el menú
    Then debería volver a la pantalla de inicio de sesión
    But no debería visualizar el catálogo de productos
```

- El `Background` deja al usuario con la sesión iniciada antes del escenario.
- `But` se usa para expresar un resultado negativo que acompaña al anterior.
- `HomeScreen.logout()` es nuevo: abre el menú, toca Log Out y confirma el diálogo.

## Scenario Outline: login con múltiples usuarios

Archivo: `src/test/resources/features/Login.feature`

```text
Scenario Outline: Inicio de sesión con múltiples usuarios
  When ingresa el usuario "<usuario>"
  And ingresa la contraseña "<contraseña>"
  And presiona el botón de iniciar sesión
  Then debería ver el resultado "<resultado>"

  Examples: Casos que la app resuelve correctamente
    | usuario           | contraseña | resultado           |
    | bod@example.com   | 10203040   | catálogo            |
    | alice@example.com | 10203040   | usuario bloqueado   |
    |                   | 10203040   | usuario obligatorio |
    | bod@example.com   |            | mensaje de error    |
```

Cada fila se ejecuta como un escenario independiente. Una celda vacía significa "no completar ese campo".

| Resultado | Qué se verifica |
|-----------|-----------------|
| `catálogo` | Se ve el catálogo de productos |
| `usuario bloqueado` | Sigue el login y el mensaje contiene "locked out" |
| `usuario obligatorio` | Sigue el login y el mensaje es "Username is required" |
| `mensaje de error` | Sigue el login y se muestra algún mensaje de error |

Hay un segundo bloque de `Examples` con el tag `@known-issue` (contraseña incorrecta), que se excluye de la ejecución normal por el defecto de la app descrito en `bdd-step-definitions.md`.

## Keywords de Gherkin usadas

`Given`, `When`, `Then`, `And` y `But` (en `Logout.feature`), más `Background`, `Scenario Outline` y `Examples`.

## Cómo ejecutar

```text
mvn clean test "-Dtest=CucumberTestRunner"
```

Resultado esperado: 13 escenarios en verde (8 anteriores, 1 de logout y 4 filas del Outline). Quedan afuera los 3 con `@known-issue`.

Solo logout o solo el Outline:

```text
mvn clean test "-Dtest=CucumberTestRunner" "-Dcucumber.filter.tags=@logout"
mvn clean test "-Dtest=CucumberTestRunner" "-Dcucumber.filter.tags=@multiusuario and not @known-issue"
```

## Evidencia

`capturas/18-cucumber-logout-outline.png`: consola con los 13 escenarios en verde.
