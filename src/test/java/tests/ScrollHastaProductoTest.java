package tests;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("Products")
@DisplayName("Scroll en el catálogo")
public class ScrollHastaProductoTest extends BaseTest {

    // Producto del final del catálogo: requiere scroll para verse.
    private static final String PRODUCTO = "Sauce Labs Backpack (violet)";

    private static final By TITULO_PANTALLA = AppiumBy.id(ID_PREFIX + "productTV"); // "Products" en el catálogo; nombre en el detalle

    @Test
    @DisplayName("Scroll hasta un producto, abrir su detalle y validar el nombre")
    void scrollSeleccionarProductoYValidarDetalle() {
        // Arrange: catálogo cargado
        esperarVisible(TITULO_PANTALLA);

        // Act: scroll hasta el producto y abrir su detalle (solo la imagen recibe el click)
        boolean visibleTrasScroll = scrollAbajoHasta(PRODUCTO).isDisplayed();
        tap(AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + PRODUCTO + "\")"
                        + ".fromParent(new UiSelector().description(\"Product Image\"))"));
        esperarTexto(TITULO_PANTALLA, PRODUCTO); // productTV también existe en el catálogo: se espera por el texto
        String nombreDetalle = driver.findElement(TITULO_PANTALLA).getText();

        // Assert
        assertAll("Producto encontrado con scroll",
                () -> assertTrue(visibleTrasScroll, PRODUCTO + " no quedó visible después del scroll"),
                () -> assertTrue(nombreDetalle.contains(PRODUCTO),
                        "El detalle no corresponde a " + PRODUCTO + ". Se vio: " + nombreDetalle));
    }
}