package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import support.TestContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Pasos del catálogo y del detalle de producto, resueltos con HomeScreen. */
public class ProductoSteps {

    private final TestContext context;

    public ProductoSteps(TestContext context) {
        this.context = context;
    }

    @Given("que el usuario se encuentra en el catálogo de productos")
    public void usuarioEnElCatalogo() {
        assertTrue(context.home().isProductScreenDisplayed(), "La app debe abrir en el catálogo de productos");
    }

    @Then("debería visualizar el título {string}")
    public void visualizaElTitulo(String titulo) {
        assertEquals(titulo, context.home().getProductTitle(), "Título de la pantalla");
    }

    @When("selecciona el producto {string}")
    public void seleccionaElProducto(String producto) {
        context.setCurrentProduct(producto);
        context.home().selectProduct(producto);
    }

    @Then("debería visualizar el detalle del producto {string}")
    public void visualizaElDetalle(String producto) {
        assertEquals(producto, context.home().getProductTitle(), "El detalle debe mostrar el producto elegido");
    }
}
