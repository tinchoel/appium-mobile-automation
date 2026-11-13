package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import support.TestContext;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Pasos del checkout, resueltos con CartScreen, LoginScreen y CheckoutScreen. */
public class CheckoutSteps {

    // Datos de envío y pago de la compra de prueba
    private static final String NOMBRE = "Tincho Test";
    private static final String DIRECCION = "Av. Colon 123";
    private static final String CIUDAD = "Cordoba";
    private static final String PROVINCIA = "Cordoba";
    private static final String CODIGO_POSTAL = "5000";
    private static final String PAIS = "Argentina";
    private static final String TARJETA = "3258125675687891";
    private static final String VENCIMIENTO = "03/27";
    private static final String CODIGO_SEGURIDAD = "123";

    private final TestContext context;

    public CheckoutSteps(TestContext context) {
        this.context = context;
    }

    @When("inicia el proceso de compra")
    public void iniciaElProcesoDeCompra() {
        context.cart().proceedToCheckout();
    }

    @When("inicia sesión con el usuario {string} y la contraseña {string}")
    public void iniciaSesion(String usuario, String contrasena) {
        context.login().login(usuario, contrasena);
    }

    @When("completa la dirección de envío")
    public void completaLaDireccionDeEnvio() {
        context.checkout().enterShippingAddress(NOMBRE, DIRECCION, CIUDAD, PROVINCIA, CODIGO_POSTAL, PAIS);
        context.checkout().clickToPayment();
    }

    @When("completa los datos de pago")
    public void completaLosDatosDePago() {
        context.checkout().enterPaymentDetails(NOMBRE, TARJETA, VENCIMIENTO, CODIGO_SEGURIDAD);
        context.checkout().clickReviewOrder();
    }

    @When("confirma el pedido")
    public void confirmaElPedido() {
        context.checkout().clickPlaceOrder();
    }

    @Then("debería visualizar la confirmación de la compra")
    public void visualizaLaConfirmacion() {
        String titulo = context.checkout().getConfirmationTitle();
        String gracias = context.checkout().getThankYouMessage();
        assertAll("Confirmación de la compra",
                () -> assertTrue(titulo.contains("Checkout Complete"), "Título de la confirmación. Se vio: " + titulo),
                () -> assertTrue(gracias.contains("Thank you"), "Agradecimiento. Se vio: " + gracias));
    }
}
