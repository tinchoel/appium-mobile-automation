@Android @logout
Feature: Cierre de sesión
  Como usuario de la tienda móvil
  Quiero cerrar mi sesión
  Para proteger mi cuenta cuando termino de usar la app

  Background:
    Given que el usuario inició sesión con el usuario "bod@example.com" y la contraseña "10203040"

  @smoke
  Scenario: Cierre de sesión exitoso
    When cierra la sesión desde el menú
    Then debería volver a la pantalla de inicio de sesión
    But no debería visualizar el catálogo de productos
