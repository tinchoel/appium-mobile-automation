package tests;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de Checkout, una por etapa. Cada método es independiente: arma su propio estado
 * (Arrange), realiza la acción que se prueba (Act) y verifica con aserciones agrupadas (Assert).
 */
@Tag("Checkout")
@DisplayName("Checkout")
public class CheckoutTest extends BaseTest {

    // --- Datos de prueba ---
    private static final String NOMBRE = "Tincho Test";
    private static final String DIRECCION = "Av. Colon 123";
    private static final String CIUDAD = "Cordoba";
    private static final String PROVINCIA = "Cordoba";
    private static final String CODIGO_POSTAL = "5000";
    private static final String PAIS = "Argentina";
    private static final String TARJETA = "3258125675687891";
    private static final String VENCIMIENTO = "03/27";
    private static final String CODIGO_SEGURIDAD = "123";
    private static final String TOTAL_ESPERADO = "35.98"; // $ 29.99 del producto + envío

    /** Datos que muestra la pantalla de revisión del pedido. */
    private record Resumen(String nombre, String direccion, String ciudad, String pais,
                           String titular, String items, String total) { }

    @Test
    @DisplayName("Envío con datos válidos lleva a la pantalla de pago")
    void envioConDatosValidosLlevaAlPago() {
        // Arrange: Backpack en el carrito, checkout iniciado y sesión abierta
        iniciarCheckoutConBackpack();

        // Act
        completarEnvio();
        boolean tarjetaVisible = esperarVisible(id("cardNumberET")).isDisplayed();
        esperarTexto(id("paymentBtn"), "Review Order");
        String boton = esperarVisible(id("paymentBtn")).getText();

        // Assert
        assertAll("Pantalla de pago",
                () -> assertTrue(tarjetaVisible, "Debe verse el campo Card Number"),
                () -> assertTrue(boton.contains("Review Order"), "El botón debe decir Review Order. Se vio: " + boton));
    }

    @Test
    @DisplayName("La revisión del pedido muestra los datos ingresados")
    void revisionMuestraLosDatosIngresados() {
        // Arrange: checkout hasta el pago
        iniciarCheckoutConBackpack();
        completarEnvio();

        // Act: completar el pago y leer el resumen
        completarPago();
        Resumen resumen = leerResumen();

        // Assert: todas las comprobaciones se ejecutan aunque alguna falle
        assertAll("Resumen del pedido",
                () -> assertTrue(resumen.nombre().contains(NOMBRE), "Nombre de envío. Se vio: " + resumen.nombre()),
                () -> assertTrue(resumen.direccion().contains(DIRECCION), "Dirección. Se vio: " + resumen.direccion()),
                () -> assertTrue(resumen.ciudad().contains(CIUDAD), "Ciudad. Se vio: " + resumen.ciudad()),
                () -> assertTrue(resumen.pais().contains(PAIS), "País. Se vio: " + resumen.pais()),
                () -> assertTrue(resumen.titular().contains(NOMBRE), "Titular de la tarjeta. Se vio: " + resumen.titular()),
                () -> assertTrue(resumen.items().contains("1"), "Cantidad de ítems. Se vio: " + resumen.items()),
                () -> assertTrue(resumen.total().contains(TOTAL_ESPERADO), "Total del pedido. Se vio: " + resumen.total()));
    }

    @Test
    @DisplayName("Confirmar el pedido muestra Checkout Complete")
    void compraCompletaMuestraConfirmacion() {
        // Arrange: checkout hasta la revisión
        llegarALaRevision();

        // Act
        irAlPasoSiguiente("Place Order");
        esperarTexto(id("completeTV"), "Checkout Complete");
        String titulo = esperarVisible(id("completeTV")).getText();
        String gracias = esperarVisible(id("thankYouTV")).getText();

        // Assert
        assertAll("Confirmación de la compra",
                () -> assertTrue(titulo.contains("Checkout Complete"), "Título de la confirmación. Se vio: " + titulo),
                () -> assertTrue(gracias.contains("Thank you"), "Agradecimiento. Se vio: " + gracias));
    }

    /**
     * Demostración para la evidencia del Avance 3.1: se ejecuta solo con -DdemoFallo=true y compara el
     * resumen con valores incorrectos a propósito. assertAll informa los tres errores en una sola corrida.
     */
    @Test
    @Tag("Demo")
    @EnabledIfSystemProperty(named = "demoFallo", matches = "true")
    @DisplayName("DEMO: assertAll informa varios fallos a la vez")
    void demoRevisionConValoresIncorrectos() {
        // Arrange
        llegarALaRevision();

        // Act
        Resumen resumen = leerResumen();

        // Assert: tres valores esperados incorrectos a propósito
        assertAll("Resumen con valores esperados incorrectos",
                () -> assertTrue(resumen.nombre().contains("Nombre Equivocado"), "Nombre de envío. Se vio: " + resumen.nombre()),
                () -> assertTrue(resumen.ciudad().contains("Rosario"), "Ciudad. Se vio: " + resumen.ciudad()),
                () -> assertTrue(resumen.total().contains("99.99"), "Total del pedido. Se vio: " + resumen.total()));
    }

    // ---------- Pasos de preparación y lectura ----------

    /** Catálogo -> Backpack -> carrito -> Proceed To Checkout -> login. Termina en la pantalla de envío. */
    private void iniciarCheckoutConBackpack() {
        esperarVisible(id(ID_TITULO));
        tap(AppiumBy.accessibilityId("Product Image"));
        esperarTexto(id(ID_TITULO), "Backpack"); // productTV también existe en el catálogo
        tap(AppiumBy.accessibilityId("Tap to add product to cart"));
        tap(AppiumBy.accessibilityId("View cart"));
        esperarTexto(id("titleTV"), "Backpack");
        tap(AppiumBy.accessibilityId("Confirms products for checkout"));
        completarLogin(USUARIO_VALIDO, CLAVE); // sin sesión, la app pide login antes del envío
    }

    private void completarEnvio() {
        escribir(id("fullNameET"), NOMBRE);
        escribir(id("address1ET"), DIRECCION);
        escribir(id("cityET"), CIUDAD);
        escribir(id("stateET"), PROVINCIA);
        escribir(id("zipET"), CODIGO_POSTAL);
        escribir(id("countryET"), PAIS);
        ocultarTeclado();
        irAlPasoSiguiente("To Payment");
    }

    private void completarPago() {
        escribir(id("nameET"), NOMBRE);
        escribir(id("cardNumberET"), TARJETA);
        escribir(id("expirationDateET"), VENCIMIENTO);
        escribir(id("securityCodeET"), CODIGO_SEGURIDAD);
        ocultarTeclado();
        irAlPasoSiguiente("Review Order");
    }

    /** Checkout completo hasta dejar abierta la pantalla de revisión. */
    private void llegarALaRevision() {
        iniciarCheckoutConBackpack();
        completarEnvio();
        completarPago();
        esperarVisible(id("placeOrderRV"));
    }

    private Resumen leerResumen() {
        return new Resumen(
                leerTexto("fullNameTV"),
                leerTexto("addressTV"),
                leerTexto("cityTV"),
                leerTexto("countryTV"),
                leerTexto("cardHolderTV"),
                leerTexto("itemNumberTV"),
                leerTexto("totalAmountTV"));
    }

    /** Hace scroll hasta el elemento (puede quedar fuera de pantalla) y devuelve su texto. */
    private String leerTexto(String idElemento) {
        scrollHastaId(ID_PREFIX + idElemento);
        return esperarVisible(id(idElemento)).getText();
    }

    /**
     * paymentBtn es el mismo ID en las tres pantallas finales y solo cambia su texto:
     * se espera por el texto, se hace scroll por si queda fuera de pantalla y se toca.
     */
    private void irAlPasoSiguiente(String textoBoton) {
        esperarTexto(id("paymentBtn"), textoBoton);
        scrollHastaId(ID_PREFIX + "paymentBtn");
        tap(id("paymentBtn"));
    }
}