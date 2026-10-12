import config.DriverFactory;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Avance 2.4: flujo Listado -> Backpack -> Detalle -> Agregar al carrito -> Carrito -> Verificar.
 */
public class AgregarAlCarritoTest {

    private static final String ID_PREFIX = "com.saucelabs.mydemoapp.android:id/";

    // --- Localizadores (documentados en docs/avance-2.4.md) ---
    private static final String TITULO_PANTALLA_ID = ID_PREFIX + "productTV";   // "Products" en el listado; nombre en el detalle
    private static final String IMAGEN_PRODUCTO_ACC = "Product Image";           // Accessibility ID (se repite por producto)
    private static final String BOTON_AGREGAR_ACC = "Tap to add product to cart"; // Accessibility ID
    private static final String ACCESO_CARRITO_ACC = "View cart";                // Accessibility ID
    private static final String NOMBRE_EN_CARRITO_ID = ID_PREFIX + "titleTV";    // nombre del producto en el carrito

    private static final String PRODUCTO = "Backpack";

    private AndroidDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void iniciarSesion() throws Exception {
        driver = DriverFactory.crearDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterEach
    void cerrarSesion() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void agregarBackpackAlCarrito() {
        // c. Esperar el listado de productos
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(TITULO_PANTALLA_ID)));

        // d. Seleccionar el primer producto (Backpack)
        wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.accessibilityId(IMAGEN_PRODUCTO_ACC))).click();

        // e/f. Detalle: esperar que el título muestre Backpack y validarlo
        // (se espera por el texto porque productTV también existe en el listado)
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                AppiumBy.id(TITULO_PANTALLA_ID), PRODUCTO));
        String nombreDetalle = driver.findElement(AppiumBy.id(TITULO_PANTALLA_ID)).getText();
        assertTrue(nombreDetalle.contains(PRODUCTO),
                "No se ve el detalle de Backpack. Se vio: " + nombreDetalle);
        System.out.println("Detalle: " + nombreDetalle);

        // g. Verificar que el botón Agregar al carrito esté disponible
        WebElement agregar = wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.accessibilityId(BOTON_AGREGAR_ACC)));
        assertTrue(agregar.isDisplayed() && agregar.isEnabled(),
                "El botón Agregar al carrito no está disponible");
        System.out.println("Botón agregar al carrito disponible");

        // h. Click en el botón
        agregar.click();

        // i. Abrir el carrito
        wait.until(ExpectedConditions.elementToBeClickable(
                AppiumBy.accessibilityId(ACCESO_CARRITO_ACC))).click();

        // j. Verificar que Backpack aparece en el carrito
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                AppiumBy.id(NOMBRE_EN_CARRITO_ID), PRODUCTO));
        String nombreCarrito = driver.findElement(AppiumBy.id(NOMBRE_EN_CARRITO_ID)).getText();
        assertTrue(nombreCarrito.contains(PRODUCTO),
                "El carrito no muestra Backpack. Se vio: " + nombreCarrito);
        System.out.println("Carrito: " + nombreCarrito);
    }
}