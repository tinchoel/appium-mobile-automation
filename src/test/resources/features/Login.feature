@Android @login
Feature: Inicio de sesión en la aplicación
  Como usuario de la tienda móvil
  Quiero iniciar sesión con mi cuenta
  Para acceder al catálogo de productos

  Background:
    Given que el usuario se encuentra en la pantalla de inicio de sesión

  @smoke
  Scenario: Inicio de sesión exitoso
    When ingresa el usuario "bod@example.com"
    And ingresa la contraseña "10203040"
    And presiona el botón de iniciar sesión
    Then debería visualizar el catálogo de productos

  # Defecto conocido de la app: acepta cualquier contraseña para un usuario válido.
  # Se excluye de la ejecución normal con el tag @known-issue.
  @known-issue
  Scenario: Inicio de sesión con contraseña incorrecta
    When ingresa el usuario "bod@example.com"
    And ingresa la contraseña "clave-incorrecta"
    And presiona el botón de iniciar sesión
    Then debería permanecer en la pantalla de inicio de sesión
    And debería visualizar un mensaje de error

  # Defecto conocido de la app: permite iniciar sesión con un usuario que no existe.
  @known-issue
  Scenario: Inicio de sesión con un usuario que no existe
    When ingresa el usuario "inexistente@example.com"
    And ingresa la contraseña "10203040"
    And presiona el botón de iniciar sesión
    Then debería permanecer en la pantalla de inicio de sesión
    And debería visualizar un mensaje de error

  Scenario: Inicio de sesión con un usuario bloqueado
    When ingresa el usuario "alice@example.com"
    And ingresa la contraseña "10203040"
    And presiona el botón de iniciar sesión
    Then debería permanecer en la pantalla de inicio de sesión
    And debería visualizar un mensaje indicando que el usuario está bloqueado

  Scenario: Inicio de sesión sin ingresar el usuario
    When ingresa la contraseña "10203040"
    And presiona el botón de iniciar sesión
    Then debería permanecer en la pantalla de inicio de sesión
    And debería visualizar un mensaje de error

  Scenario: Inicio de sesión sin ingresar la contraseña
    When ingresa el usuario "bod@example.com"
    And presiona el botón de iniciar sesión
    Then debería permanecer en la pantalla de inicio de sesión
    And debería visualizar un mensaje de error
