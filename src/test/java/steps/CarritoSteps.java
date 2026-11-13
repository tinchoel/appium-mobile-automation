package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/** Pasos del carrito. Temporales: solo imprimen en consola. */
public class CarritoSteps {

    @When("agrega el producto al carrito")
    public void agregaElProductoAlCarrito() {
        System.out.println("[carrito] agrega el producto al carrito");
    }

    @When("abre el carrito")
    public void abreElCarrito() {
        System.out.println("[carrito] abre el carrito");
    }

    @Then("debería visualizar el producto {string} en el carrito")
    public void visualizaElProductoEnElCarrito(String producto) {
        System.out.println("[carrito] verifica el producto: " + producto);
    }

    @Then("el carrito debería contener {int} ítem")
    public void elCarritoContiene(int cantidad) {
        System.out.println("[carrito] verifica la cantidad: " + cantidad);
    }
}
