import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Avance 2.4: Listado -> Backpack -> Detalle -> Agregar al carrito -> Carrito -> Verificar.
 */
public class AgregarAlCarritoTest extends BaseTest {

    private static final String PRODUCTO = "Backpack";

    private static final By TITULO_PANTALLA = AppiumBy.id(ID_PREFIX + "productTV"); // "Products" en el listado; nombre en el detalle
    private static final By IMAGEN_PRODUCTO = AppiumBy.accessibilityId("Product Image");
    private static final By BOTON_AGREGAR = AppiumBy.accessibilityId("Tap to add product to cart");
    private static final By ACCESO_CARRITO = AppiumBy.accessibilityId("View cart");
    private static final By NOMBRE_EN_CARRITO = AppiumBy.id(ID_PREFIX + "titleTV");

    @Test
    void agregarBackpackAlCarrito() {
        // Esperar el listado de productos
        esperarVisible(TITULO_PANTALLA);

        // Seleccionar el primer producto (Backpack)
        tap(IMAGEN_PRODUCTO);

        // Detalle: esperar por el texto (productTV también existe en el listado) y validar
        esperarTexto(TITULO_PANTALLA, PRODUCTO);
        String nombreDetalle = driver.findElement(TITULO_PANTALLA).getText();
        assertTrue(nombreDetalle.contains(PRODUCTO), "No se ve el detalle de Backpack. Se vio: " + nombreDetalle);
        System.out.println("Detalle: " + nombreDetalle);

        // Verificar que el botón Agregar al carrito esté disponible y hacer click
        WebElement agregar = esperarClickable(BOTON_AGREGAR);
        assertTrue(agregar.isDisplayed() && agregar.isEnabled(), "El botón Agregar al carrito no está disponible");
        System.out.println("Botón agregar al carrito disponible");
        agregar.click();

        // Abrir el carrito y verificar el producto
        tap(ACCESO_CARRITO);
        esperarTexto(NOMBRE_EN_CARRITO, PRODUCTO);
        String nombreCarrito = driver.findElement(NOMBRE_EN_CARRITO).getText();
        assertTrue(nombreCarrito.contains(PRODUCTO), "El carrito no muestra Backpack. Se vio: " + nombreCarrito);
        System.out.println("Carrito: " + nombreCarrito);
    }
}