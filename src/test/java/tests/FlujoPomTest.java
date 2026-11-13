package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import screens.CartScreen;
import screens.HomeScreen;
import screens.LoginScreen;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Avance 4.2: flujo con Screen Objects. La prueba no usa localizadores: solo llama a los métodos
 * de LoginScreen, HomeScreen y CartScreen.
 */
@Tag("POM")
@DisplayName("Flujo con Screen Objects")
public class FlujoPomTest extends BaseTest {

    private static final String PRODUCTO = "Sauce Labs Backpack";

    @Test
    @DisplayName("Login válido, agregar un producto y abrir el carrito")
    void loginAgregarProductoYAbrirCarrito() {
        // Arrange: app abierta en el catálogo; se llega al login desde el menú
        HomeScreen home = new HomeScreen(driver, wait);
        LoginScreen login = new LoginScreen(driver, wait);
        CartScreen cart = new CartScreen(driver, wait);
        home.goToLogin();

        // Act
        login.login(USUARIO_VALIDO, CLAVE);
        boolean catalogoVisible = home.isProductScreenDisplayed();
        String tituloCatalogo = home.getProductTitle();
        home.selectProduct(PRODUCTO);
        String tituloDetalle = home.getProductTitle();
        home.addProductToCart(PRODUCTO);
        home.clickCart();
        boolean productoEnCarrito = cart.isProductInCart(PRODUCTO);
        String items = cart.getTotalItems();

        // Assert
        assertAll("Flujo login -> catálogo -> carrito",
                () -> assertTrue(catalogoVisible, "Después del login debe verse el catálogo"),
                () -> assertEquals("Products", tituloCatalogo, "Título del catálogo"),
                () -> assertEquals(PRODUCTO, tituloDetalle, "El detalle debe mostrar el producto elegido"),
                () -> assertTrue(productoEnCarrito, "El carrito debe mostrar " + PRODUCTO),
                () -> assertTrue(items.contains("1"), "El carrito debe tener 1 ítem. Se vio: " + items));
    }
}