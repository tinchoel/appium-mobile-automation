package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/** Pasos del catálogo y del detalle de producto. Temporales: solo imprimen en consola. */
public class ProductoSteps {

    @Given("que el usuario se encuentra en el catálogo de productos")
    public void usuarioEnElCatalogo() {
        System.out.println("[productos] el usuario está en el catálogo");
    }

    @Then("debería visualizar el título {string}")
    public void visualizaElTitulo(String titulo) {
        System.out.println("[productos] verifica el título: " + titulo);
    }

    @When("selecciona el producto {string}")
    public void seleccionaElProducto(String producto) {
        System.out.println("[productos] selecciona: " + producto);
    }

    @Then("debería visualizar el detalle del producto {string}")
    public void visualizaElDetalle(String producto) {
        System.out.println("[productos] verifica el detalle de: " + producto);
    }
}
