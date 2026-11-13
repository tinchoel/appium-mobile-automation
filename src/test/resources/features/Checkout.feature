@Android @checkout
Feature: Checkout
  Como usuario de la tienda móvil
  Quiero completar la compra de los productos de mi carrito
  Para recibir mi pedido

  Background:
    Given que el usuario se encuentra en el catálogo de productos

  @smoke
  Scenario: Compra completa de un producto
    When selecciona el producto "Sauce Labs Backpack"
    And agrega el producto al carrito
    And abre el carrito
    And inicia el proceso de compra
    And inicia sesión con el usuario "bod@example.com" y la contraseña "10203040"
    And completa la dirección de envío
    And completa los datos de pago
    And confirma el pedido
    Then debería visualizar la confirmación de la compra
