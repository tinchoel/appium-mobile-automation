# Avance 5.1 – Introducción a BDD y Cucumber

## Objetivo

Diseñar en Gherkin los escenarios del módulo de autenticación. En este avance solo se redactan los escenarios: no hay código Java ni Step Definitions (eso se agrega en el 5.2 y siguientes).

Archivo: `src/test/resources/features/Login.feature`

## Aplicación y credenciales

La consigna habla de "Sauce Demo" con `standard_user` / `secret_sauce`. Esas credenciales son de la web de Sauce Demo. El proyecto automatiza la app Android **My Demo App** de Sauce Labs, que usa estas:

| Tipo de usuario | Usuario | Contraseña |
|-----------------|---------|------------|
| Válido | `bob@example.com` | `10203040` |
| Bloqueado | `alice@example.com` | `10203040` |

## Condición inicial

El usuario está en la pantalla de inicio de sesión. Es igual para todos los escenarios, así que va en un `Background`:

```text
Background:
  Given que el usuario se encuentra en la pantalla de inicio de sesión
```

## Escenarios

| # | Escenario | Acción | Resultado esperado |
|---|-----------|--------|--------------------|
| 1 | Inicio de sesión exitoso | Ingresa usuario y contraseña válidos y presiona iniciar sesión | Ve el catálogo de productos |
| 2 | Contraseña incorrecta | Ingresa un usuario válido con una contraseña errónea | Sigue en el login y ve un mensaje de error |
| 3 | Usuario que no existe | Ingresa un usuario inexistente | Sigue en el login y ve un mensaje de error |
| 4 | Usuario bloqueado | Ingresa las credenciales del usuario bloqueado | Sigue en el login y ve el mensaje de usuario bloqueado |
| 5 | Sin usuario | Ingresa solo la contraseña | Sigue en el login y ve un mensaje de error |
| 6 | Sin contraseña | Ingresa solo el usuario | Sigue en el login y ve un mensaje de error |

## Feature completo

```text
@Android @login
Feature: Inicio de sesión en la aplicación

  Background:
    Given que el usuario se encuentra en la pantalla de inicio de sesión

  @smoke
  Scenario: Inicio de sesión exitoso
    When ingresa el usuario "bob@example.com"
    And ingresa la contraseña "10203040"
    And presiona el botón de iniciar sesión
    Then debería visualizar el catálogo de productos

  Scenario: Inicio de sesión con contraseña incorrecta
    When ingresa el usuario "bob@example.com"
    And ingresa la contraseña "clave-incorrecta"
    And presiona el botón de iniciar sesión
    Then debería permanecer en la pantalla de inicio de sesión
    And debería visualizar un mensaje de error

  ... (los otros cuatro escenarios están en el archivo)
```

## Criterios de redacción

- Cada escenario describe un solo comportamiento y su resultado se puede comprobar con una prueba automática.
- No hay localizadores, métodos de Appium ni código: solo lo que hace y ve el usuario.
- Se usan las palabras clave `Given`, `When`, `Then` y `And`, con el texto de los pasos en español, como en el material del curso.
- Los mensajes de error se describen sin copiar su texto exacto; el texto se valida contra la app al implementar los pasos.

## Entregables

- Escenarios de login exitoso e inválido en Gherkin
- Precondiciones, acciones y resultados esperados de cada escenario (tabla de arriba)
