import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import org.openqa.selenium.WebElement;

/**
 * Pruebas del catálogo de productos al abrir la aplicación.
 */
public class CatalogoTest extends BaseTest {

    @Test
    void catalogoMuestraProductosConNombreYPrecio() {
        esperarTexto(id(ID_TITULO), "Products");
        Assertions.assertTrue(esperarVisible(id("productRV")).isDisplayed(),
                "Debe verse la lista de productos");

        List<WebElement> nombres = driver.findElements(id("titleTV"));
        List<WebElement> precios = driver.findElements(id("priceTV"));

        Assertions.assertFalse(nombres.isEmpty(), "El catálogo debe mostrar al menos un producto");
        Assertions.assertEquals(nombres.size(), precios.size(),
                "Cada producto visible debe tener un precio");
        Assertions.assertTrue(nombres.get(0).getText().contains("Sauce Labs"),
                "El primer producto debe ser de Sauce Labs pero es: " + nombres.get(0).getText());
        Assertions.assertTrue(precios.get(0).getText().startsWith("$"),
                "El precio debe empezar con $ pero es: " + precios.get(0).getText());
    }

    @Test
    void catalogoPermiteAccederAlCarrito() {
        esperarTexto(id(ID_TITULO), "Products");
        Assertions.assertTrue(esperarVisible(id("cartRL")).isDisplayed(),
                "El acceso al carrito debe estar visible en el catálogo");
    }
}