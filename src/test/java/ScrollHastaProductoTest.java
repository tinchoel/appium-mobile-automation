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
 * Avance 2.5: Scroll en el catálogo hasta un producto que no está visible al inicio,
 * seleccionarlo y validar su detalle.
 */
public class ScrollHastaProductoTest {

    private static final String ID_PREFIX = "com.saucelabs.mydemoapp.android:id/";
    private static final String TITULO_PANTALLA_ID = ID_PREFIX + "productTV"; // "Products" en el catálogo; nombre en el detalle

    // Producto buscado: está al final del catálogo, así que requiere scroll para verse.
    private static final String PRODUCTO = "Sauce Labs Backpack (violet)";

    private AndroidDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void iniciarSesion() throws Exception {
        driver = DriverFactory.crearDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        // Esperar que cargue el catálogo
        wait.until(ExpectedConditions.visibilityOfElementLocated(AppiumBy.id(TITULO_PANTALLA_ID)));
    }

    @AfterEach
    void cerrarSesion() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void scrollSeleccionarProductoYValidarDetalle() {
        // Scroll hasta encontrar el producto y verificar que quedó visible
        WebElement producto = scrollAbajoHasta(PRODUCTO);
        assertTrue(producto.isDisplayed(), PRODUCTO + " no quedó visible después del scroll");
        System.out.println("Encontrado: " + producto.getText());

        // Seleccionarlo con click(). En la tarjeta del catálogo solo la imagen tiene el click
        // asociado (el título no), así que se hace click sobre la imagen que acompaña a ese nombre.
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + PRODUCTO + "\")"
                        + ".fromParent(new UiSelector().description(\"Product Image\"))"))).click();

        // Validar el nombre en la pantalla de detalle
        // (productTV también existe en el catálogo, por eso se espera por el texto)
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                AppiumBy.id(TITULO_PANTALLA_ID), PRODUCTO));
        String nombreDetalle = driver.findElement(AppiumBy.id(TITULO_PANTALLA_ID)).getText();
        assertTrue(nombreDetalle.contains(PRODUCTO),
                "El detalle no corresponde a " + PRODUCTO + ". Se vio: " + nombreDetalle);
        System.out.println("Detalle: " + nombreDetalle);
    }

    /**
     * Realiza un scroll vertical hacia abajo hasta encontrar un elemento por su texto.
     * Reutilizable con cualquier producto u otro elemento identificable por texto exacto.
     *
     * @param texto texto del elemento que se quiere encontrar
     * @return el WebElement encontrado, ya visible
     */
    private WebElement scrollAbajoHasta(String texto) {
        // Android se encarga de desplazar el contenedor hasta que el texto aparece
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
                        + ".setAsVerticalList()"
                        + ".scrollIntoView(new UiSelector().text(\"" + texto + "\"))"));

        // Esperar que el elemento encontrado esté visible
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                AppiumBy.androidUIAutomator("new UiSelector().text(\"" + texto + "\")")));
    }
}