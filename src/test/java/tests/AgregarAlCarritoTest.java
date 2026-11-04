package tests;

import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("Cart")
@DisplayName("Carrito")
public class AgregarAlCarritoTest extends BaseTest {

    private static final String PRODUCTO = "Backpack";

    private static final By TITULO_PANTALLA = AppiumBy.id(ID_PREFIX + "productTV"); // "Products" en el listado; nombre en el detalle
    private static final By IMAGEN_PRODUCTO = AppiumBy.accessibilityId("Product Image");
    private static final By BOTON_AGREGAR = AppiumBy.accessibilityId("Tap to add product to cart");
    private static final By ACCESO_CARRITO = AppiumBy.accessibilityId("View cart");

    @Test
    @Tag("Products")
    @DisplayName("El detalle del producto muestra el botón Add to cart disponible")
    void detalleMuestraBotonAgregarDisponible() {
        // Arrange: abrir el detalle de la primera Backpack
        abrirDetalleDeBackpack();

        // Act
        String nombreDetalle = driver.findElement(TITULO_PANTALLA).getText();
        WebElement agregar = esperarClickable(BOTON_AGREGAR);

        // Assert
        assertAll("Detalle del producto",
                () -> assertTrue(nombreDetalle.contains(PRODUCTO), "No se ve el detalle de Backpack. Se vio: " + nombreDetalle),
                () -> assertTrue(agregar.isDisplayed() && agregar.isEnabled(), "El botón Add to cart no está disponible"));
    }

    @Test
    @DisplayName("El producto agregado aparece en el carrito")
    void productoAgregadoApareceEnElCarrito() {
        // Arrange
        abrirDetalleDeBackpack();

        // Act: agregar al carrito y abrir el carrito
        tap(BOTON_AGREGAR);
        tap(ACCESO_CARRITO);
        esperarTexto(id("titleTV"), PRODUCTO);
        String nombreCarrito = driver.findElement(id("titleTV")).getText();
        String items = esperarVisible(id("itemsTV")).getText();
        String total = esperarVisible(id("totalPriceTV")).getText();

        // Assert
        assertAll("Carrito con un producto",
                () -> assertTrue(nombreCarrito.contains(PRODUCTO), "El carrito no muestra Backpack. Se vio: " + nombreCarrito),
                () -> assertTrue(items.contains("1"), "El carrito debe tener 1 ítem. Se vio: " + items),
                () -> assertTrue(total.contains("$"), "El total debe mostrar el precio. Se vio: " + total));
    }

    /** Paso de preparación compartido: catálogo -> detalle de la primera Backpack. */
    private void abrirDetalleDeBackpack() {
        esperarVisible(TITULO_PANTALLA);
        tap(IMAGEN_PRODUCTO);
        esperarTexto(TITULO_PANTALLA, PRODUCTO); // productTV también existe en el listado: se espera por el texto
    }
}