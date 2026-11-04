package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/** Catálogo de productos (pantalla principal) y detalle del producto. */
public class HomeScreen extends BaseScreen {

    // productTV es el título del catálogo ("Products") y también el nombre en el detalle del producto
    private static final By TITLE = AppiumBy.id(ID_PREFIX + "productTV");
    private static final By MENU_BUTTON = AppiumBy.id(ID_PREFIX + "menuIV");
    private static final By LOGIN_MENU_ITEM = AppiumBy.accessibilityId("Login Menu Item");
    private static final By ADD_TO_CART_BUTTON = AppiumBy.accessibilityId("Tap to add product to cart");
    private static final By CART_ICON = AppiumBy.accessibilityId("View cart");

    public HomeScreen(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /** Menú lateral -> Log In. */
    public void goToLogin() {
        tap(MENU_BUTTON);
        tap(LOGIN_MENU_ITEM);
    }

    /** true si el catálogo se muestra dentro del tiempo de espera. */
    public boolean isProductScreenDisplayed() {
        try {
            waitForText(TITLE, "Products");
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Texto del título: "Products" en el catálogo; el nombre del producto en el detalle. */
    public String getProductTitle() {
        return waitVisible(TITLE).getText();
    }

    /**
     * Abre el detalle de un producto por su nombre. Hace scroll hasta encontrarlo y toca su imagen,
     * porque en las tarjetas del catálogo solo la imagen recibe el click.
     */
    public void selectProduct(String productName) {
        scrollToText(productName);
        tap(AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + productName + "\")"
                        + ".fromParent(new UiSelector().description(\"Product Image\"))"));
        waitForText(TITLE, productName); // confirma que cambió a la pantalla de detalle
    }

    /** Agrega el producto al carrito. Si todavía no está en su detalle, lo abre primero. */
    public void addProductToCart(String productName) {
        if (!isOnDetailOf(productName)) {
            selectProduct(productName);
        }
        tap(ADD_TO_CART_BUTTON);
    }

    public void clickCart() {
        tap(CART_ICON);
    }

    private boolean isOnDetailOf(String productName) {
        List<WebElement> titles = driver.findElements(TITLE);
        return !titles.isEmpty() && productName.equals(titles.get(0).getText());
    }
}