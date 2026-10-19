import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Avance 2.5: Scroll en el catálogo hasta un producto que no está visible al inicio,
 * seleccionarlo y validar su detalle.
 */
public class ScrollHastaProductoTest extends BaseTest {

    // Producto buscado: está al final del catálogo, así que requiere scroll para verse.
    private static final String PRODUCTO = "Sauce Labs Backpack (violet)";

    private static final By TITULO_PANTALLA = AppiumBy.id(ID_PREFIX + "productTV"); // "Products" en el catálogo; nombre en el detalle

    @Test
    void scrollSeleccionarProductoYValidarDetalle() {
        // Esperar que cargue el catálogo
        esperarVisible(TITULO_PANTALLA);

        // Scroll hasta encontrar el producto y verificar que quedó visible
        WebElement producto = scrollAbajoHasta(PRODUCTO);
        assertTrue(producto.isDisplayed(), PRODUCTO + " no quedó visible después del scroll");
        System.out.println("Encontrado: " + producto.getText());

        // Seleccionarlo con click(). En la tarjeta del catálogo solo la imagen tiene el click
        // asociado (el título no), así que se hace click sobre la imagen que acompaña a ese nombre.
        tap(AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + PRODUCTO + "\")"
                        + ".fromParent(new UiSelector().description(\"Product Image\"))"));

        // Validar el nombre en la pantalla de detalle
        // (productTV también existe en el catálogo, por eso se espera por el texto)
        esperarTexto(TITULO_PANTALLA, PRODUCTO);
        String nombreDetalle = driver.findElement(TITULO_PANTALLA).getText();
        assertTrue(nombreDetalle.contains(PRODUCTO),
                "El detalle no corresponde a " + PRODUCTO + ". Se vio: " + nombreDetalle);
        System.out.println("Detalle: " + nombreDetalle);
    }
}