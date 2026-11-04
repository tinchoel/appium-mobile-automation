package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("Products")
@DisplayName("Catálogo de productos")
public class CatalogoTest extends BaseTest {

    @Test
    @DisplayName("El catálogo muestra productos con nombre y precio")
    void catalogoMuestraProductosConNombreYPrecio() {
        // Arrange: la app abre directamente en el catálogo
        esperarTexto(id(ID_TITULO), "Products");

        // Act
        boolean listaVisible = esperarVisible(id("productRV")).isDisplayed();
        List<WebElement> nombres = driver.findElements(id("titleTV"));
        List<WebElement> precios = driver.findElements(id("priceTV"));

        // Assert
        assertTrue(listaVisible, "Debe verse la lista de productos");
        assertFalse(nombres.isEmpty(), "El catálogo debe mostrar al menos un producto");
        assertAll("Primer producto del catálogo",
                () -> assertEquals(nombres.size(), precios.size(), "Cada producto visible debe tener un precio"),
                () -> assertTrue(nombres.get(0).getText().contains("Sauce Labs"),
                        "El primer producto debe ser de Sauce Labs pero es: " + nombres.get(0).getText()),
                () -> assertTrue(precios.get(0).getText().startsWith("$"),
                        "El precio debe empezar con $ pero es: " + precios.get(0).getText()));
    }

    @Test
    @DisplayName("El catálogo permite acceder al carrito")
    void catalogoPermiteAccederAlCarrito() {
        // Arrange
        esperarTexto(id(ID_TITULO), "Products");

        // Act
        boolean carritoVisible = esperarVisible(id("cartRL")).isDisplayed();

        // Assert
        assertTrue(carritoVisible, "El acceso al carrito debe estar visible en el catálogo");
    }
}