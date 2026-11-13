@Android @productos
Feature: Selección de productos
  Como usuario de la tienda móvil
  Quiero explorar el catálogo y ver el detalle de un producto
  Para decidir qué comprar

  Background:
    Given que el usuario se encuentra en el catálogo de productos

  @smoke
  Scenario: Visualizar el catálogo de productos
    Then debería visualizar el título "Products"

  Scenario: Ver el detalle de un producto
    When selecciona el producto "Sauce Labs Backpack"
    Then debería visualizar el detalle del producto "Sauce Labs Backpack"
