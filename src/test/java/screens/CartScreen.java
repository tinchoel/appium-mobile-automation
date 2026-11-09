package screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/** Carrito de compras: productos agregados, totales y acceso al checkout. Elementos con Page Factory. */
public class CartScreen extends BaseScreen {

    // Un nombre por cada producto del carrito: Page Factory también admite listas de elementos
    @AndroidFindBy(id = ID_PREFIX + "titleTV")
    private List<WebElement> productTitles;

    @AndroidFindBy(id = ID_PREFIX + "itemsTV")
    private WebElement totalItems;

    @AndroidFindBy(id = ID_PREFIX + "totalPriceTV")
    private WebElement totalPrice;

    @AndroidFindBy(accessibility = "Confirms products for checkout")
    private WebElement checkoutButton;

    public CartScreen(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /** true si el carrito muestra un producto con ese nombre dentro del tiempo de espera. */
    public boolean isProductInCart(String productName) {
        try {
            return wait.until((ExpectedCondition<Boolean>) d ->
                    productTitles.stream().anyMatch(title -> title.getText().equals(productName)));
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Texto con la cantidad de ítems, por ejemplo "1 Items". */
    public String getTotalItems() {
        return textOf(totalItems);
    }

    /** Texto con el precio total, por ejemplo "$ 29.99". */
    public String getTotalPrice() {
        return textOf(totalPrice);
    }

    public void proceedToCheckout() {
        tap(checkoutButton);
    }
}