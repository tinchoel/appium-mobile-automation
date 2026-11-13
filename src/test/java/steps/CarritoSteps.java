package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import support.TestContext;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Pasos del carrito, resueltos con HomeScreen y CartScreen. */
public class CarritoSteps {

    private final TestContext context;

    public CarritoSteps(TestContext context) {
        this.context = context;
    }

    @When("agrega el producto al carrito")
    public void agregaElProductoAlCarrito() {
        context.home().addProductToCart(context.currentProduct());
    }

    @When("abre el carrito")
    public void abreElCarrito() {
        context.home().clickCart();
    }

    @Then("debería visualizar el producto {string} en el carrito")
    public void visualizaElProductoEnElCarrito(String producto) {
        assertTrue(context.cart().isProductInCart(producto), "El carrito debe mostrar " + producto);
    }

    @Then("el carrito debería contener {int} ítem")
    public void elCarritoContiene(int cantidad) {
        String items = context.cart().getTotalItems();
        assertTrue(items.contains(String.valueOf(cantidad)), "Ítems en el carrito. Se vio: " + items);
    }
}
