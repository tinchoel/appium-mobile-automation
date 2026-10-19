import org.junit.jupiter.api.Test;
import io.appium.java_client.AppiumBy;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Avance 2.6: flujo de Checkout con esperas explícitas (WebDriverWait + ExpectedConditions).
 * No se usa Thread.sleep() en ningún punto.
 * Flujo: Catálogo -> Backpack -> Agregar al carrito -> Carrito -> Proceed To Checkout
 *        -> Login -> Envío -> Pago -> Revisión -> Place Order -> Checkout Complete
 */
public class CheckoutTest extends BaseTest {

    // --- Datos de prueba ---
    private static final String USUARIO = "bod@example.com";
    private static final String PASSWORD = "10203040";
    private static final String NOMBRE = "Tincho Test";
    private static final String DIRECCION = "Av. Colon 123";
    private static final String CIUDAD = "Cordoba";
    private static final String PROVINCIA = "Cordoba";
    private static final String CODIGO_POSTAL = "5000";
    private static final String PAIS = "Argentina";
    private static final String TARJETA = "3258125675687891";
    private static final String VENCIMIENTO = "03/27";
    private static final String CODIGO_SEGURIDAD = "123";

    @Test
    void completarCheckout() {
        // 1. Catálogo cargado
        esperarVisible(id("productTV"));

        // 2. Seleccionar el primer producto (Backpack): la imagen es lo que recibe el click
        tap(AppiumBy.accessibilityId("Product Image"));

        // 3. Detalle: esperar por el nombre (productTV también existe en el catálogo) y agregar al carrito
        esperarTexto(id("productTV"), "Backpack");
        tap(AppiumBy.accessibilityId("Tap to add product to cart"));

        // 4. Abrir el carrito y verificar el producto
        tap(AppiumBy.accessibilityId("View cart"));
        esperarTexto(id("titleTV"), "Backpack");

        // 5. Iniciar el checkout. Sin sesión iniciada la app pide login primero.
        tap(AppiumBy.accessibilityId("Confirms products for checkout"));

        // 6. Login
        escribir(id("nameET"), USUARIO);
        escribir(id("passwordET"), PASSWORD);
        ocultarTeclado();
        tap(id("loginBtn"));

        // 7. Envío: la pantalla se detecta por su primer campo
        escribir(id("fullNameET"), NOMBRE);
        escribir(id("address1ET"), DIRECCION);
        escribir(id("cityET"), CIUDAD);
        escribir(id("stateET"), PROVINCIA);
        escribir(id("zipET"), CODIGO_POSTAL);
        escribir(id("countryET"), PAIS);
        ocultarTeclado();
        irAlPasoSiguiente("To Payment");

        // 8. Pago
        escribir(id("nameET"), NOMBRE);
        escribir(id("cardNumberET"), TARJETA);
        escribir(id("expirationDateET"), VENCIMIENTO);
        escribir(id("securityCodeET"), CODIGO_SEGURIDAD);
        ocultarTeclado();
        irAlPasoSiguiente("Review Order");

        // 9. Revisión: esperar el resumen y validar sus datos antes de confirmar
        esperarVisible(id("placeOrderRV"));
        String enviadoA = esperarVisible(id("fullNameTV")).getText();
        assertTrue(enviadoA.contains(NOMBRE), "El resumen no muestra el nombre de envío. Se vio: " + enviadoA);
        String items = esperarVisible(id("itemNumberTV")).getText();
        assertTrue(items.contains("1"), "El resumen no muestra 1 ítem. Se vio: " + items);
        irAlPasoSiguiente("Place Order");

        // 10. Confirmación
        esperarTexto(id("completeTV"), "Checkout Complete");
        String gracias = esperarVisible(id("thankYouTV")).getText();
        assertTrue(gracias.contains("Thank you"), "No se mostró el agradecimiento. Se vio: " + gracias);
        System.out.println("Checkout: " + gracias);
    }

    /**
     * paymentBtn es el mismo ID en las tres pantallas finales y solo cambia su texto.
     * Se espera por el texto esperado (confirma que cargó la pantalla correcta), se hace
     * scroll por si el botón queda fuera de pantalla y se hace click cuando es clickeable.
     */
    private void irAlPasoSiguiente(String textoBoton) {
        esperarTexto(id("paymentBtn"), textoBoton);
        scrollHastaId(ID_PREFIX + "paymentBtn");
        tap(id("paymentBtn"));
    }
}