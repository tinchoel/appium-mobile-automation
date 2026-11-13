@Android @carrito
Feature: Carrito de compras
  Como usuario de la tienda móvil
  Quiero agregar productos al carrito
  Para revisarlos antes de comprar

  Background:
    Given que el usuario se encuentra en el catálogo de productos

  @smoke
  Scenario: Agregar un producto al carrito
    When selecciona el producto "Sauce Labs Backpack"
    And agrega el producto al carrito
    And abre el carrito
    Then debería visualizar el producto "Sauce Labs Backpack" en el carrito
    And el carrito debería contener 1 ítem
