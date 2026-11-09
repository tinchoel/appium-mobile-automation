package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import screens.CartScreen;
import screens.CheckoutScreen;
import screens.HomeScreen;
import screens.LoginScreen;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Avance 4.3: flujo de compra completo usando solo Screen Objects. Cart, Checkout y Login usan
 * Page Factory (@AndroidFindBy); la prueba no contiene localizadores.
 */
@Tag("POM")
@DisplayName("Compra completa con Screen Objects")
public class CompraPomTest extends BaseTest {

    private static final String PRODUCTO = "Sauce Labs Backpack";

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

    @Test
    @DisplayName("Agregar un producto, completar el checkout y confirmar el pedido")
    void compraCompletaConScreenObjects() {
        // Arrange
        HomeScreen home = new HomeScreen(driver, wait);
        LoginScreen login = new LoginScreen(driver, wait);
        CartScreen cart = new CartScreen(driver, wait);
        CheckoutScreen checkout = new CheckoutScreen(driver, wait);

        // Act: catálogo -> producto -> carrito
        home.addProductToCart(PRODUCTO);
        home.clickCart();
        boolean productoEnCarrito = cart.isProductInCart(PRODUCTO);
        String itemsCarrito = cart.getTotalItems();

        // Act: checkout (sin sesión iniciada la app pide el login primero)
        cart.proceedToCheckout();
        login.login(USUARIO_VALIDO, CLAVE);
        checkout.enterShippingAddress(NOMBRE, DIRECCION, CIUDAD, PROVINCIA, CODIGO_POSTAL, PAIS);
        checkout.clickToPayment();
        checkout.enterPaymentDetails(NOMBRE, TARJETA, VENCIMIENTO, CODIGO_SEGURIDAD);
        checkout.clickReviewOrder();

        // Act: lectura de la revisión y confirmación del pedido
        String nombre = checkout.getShippingName();
        String direccion = checkout.getShippingAddress();
        String ciudad = checkout.getShippingCity();
        String pais = checkout.getShippingCountry();
        String titular = checkout.getCardHolder();
        String items = checkout.getItemCount();
        String total = checkout.getTotalAmount();
        checkout.clickPlaceOrder();
        String tituloFinal = checkout.getConfirmationTitle();
        String gracias = checkout.getThankYouMessage();

        // Assert
        assertAll("Compra completa",
                () -> assertTrue(productoEnCarrito, "El carrito debe mostrar " + PRODUCTO),
                () -> assertTrue(itemsCarrito.contains("1"), "Ítems en el carrito. Se vio: " + itemsCarrito),
                () -> assertTrue(nombre.contains(NOMBRE), "Nombre de envío. Se vio: " + nombre),
                () -> assertTrue(direccion.contains(DIRECCION), "Dirección. Se vio: " + direccion),
                () -> assertTrue(ciudad.contains(CIUDAD), "Ciudad. Se vio: " + ciudad),
                () -> assertTrue(pais.contains(PAIS), "País. Se vio: " + pais),
                () -> assertTrue(titular.contains(NOMBRE), "Titular de la tarjeta. Se vio: " + titular),
                () -> assertTrue(items.contains("1"), "Ítems del pedido. Se vio: " + items),
                () -> assertTrue(total.contains(TOTAL_ESPERADO), "Total del pedido. Se vio: " + total),
                () -> assertTrue(tituloFinal.contains("Checkout Complete"), "Título de la confirmación. Se vio: " + tituloFinal),
                () -> assertTrue(gracias.contains("Thank you"), "Agradecimiento. Se vio: " + gracias));
    }
}