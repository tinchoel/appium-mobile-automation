package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Carrito de compras: productos agregados y acceso al checkout. */
public class CartScreen extends BaseScreen {

    private static final By TOTAL_ITEMS = AppiumBy.id(ID_PREFIX + "itemsTV");
    private static final By CHECKOUT_BUTTON = AppiumBy.accessibilityId("Confirms products for checkout");

    public CartScreen(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /** true si el carrito muestra un producto con ese nombre. */
    public boolean isProductInCart(String productName) {
        try {
            waitVisible(AppiumBy.androidUIAutomator(
                    "new UiSelector().resourceId(\"" + ID_PREFIX + "titleTV\").text(\"" + productName + "\")"));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Texto con la cantidad de ítems, por ejemplo "1 Items". */
    public String getTotalItems() {
        return waitVisible(TOTAL_ITEMS).getText();
    }

    public void proceedToCheckout() {
        tap(CHECKOUT_BUTTON);
    }
}