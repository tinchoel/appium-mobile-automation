package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Catálogo de productos (pantalla principal) y detalle del producto. */
public class HomeScreen extends BaseScreen {

    // productTV es el título del catálogo ("Products") y también el nombre en el detalle del producto
    @AndroidFindBy(id = ID_PREFIX + "productTV")
    private WebElement title;

    @AndroidFindBy(id = ID_PREFIX + "menuIV")
    private WebElement menuButton;

    @AndroidFindBy(accessibility = "Login Menu Item")
    private WebElement loginMenuItem;

    @AndroidFindBy(accessibility = "Tap to add product to cart")
    private WebElement addToCartButton;

    @AndroidFindBy(accessibility = "View cart")
    private WebElement cartIcon;

    public HomeScreen(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    /** Menú lateral -> Log In. */
    public void goToLogin() {
        tap(menuButton);
        tap(loginMenuItem);
    }

    /** true si el catálogo se muestra dentro del tiempo de espera. */
    public boolean isProductScreenDisplayed() {
        try {
            waitForText(title, "Products");
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Texto del título: "Products" en el catálogo; el nombre del producto en el detalle. */
    public String getProductTitle() {
        return waitVisible(title).getText();
    }

    /**
     * Abre el detalle de un producto por su nombre. Hace scroll hasta encontrarlo y toca su imagen,
     * porque en las tarjetas del catálogo solo la imagen recibe el click.
     */
    public void selectProduct(String productName) {
        waitVisible(title); // el catálogo debe estar cargado antes de buscar con scroll
        scrollToText(productName);
        // Localizador dinámico: depende del nombre del producto, por eso no se declara con @AndroidFindBy
        tap(AppiumBy.androidUIAutomator(
                "new UiSelector().text(\"" + productName + "\")"
                        + ".fromParent(new UiSelector().description(\"Product Image\"))"));
        waitForText(title, productName); // confirma que cambió a la pantalla de detalle
    }

    /** Agrega el producto al carrito. Si todavía no está en su detalle, lo abre primero. */
    public void addProductToCart(String productName) {
        if (!isOnDetailOf(productName)) {
            selectProduct(productName);
        }
        tap(addToCartButton);
    }

    public void clickCart() {
        tap(cartIcon);
    }

    private boolean isOnDetailOf(String productName) {
        try {
            return productName.equals(title.getText());
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}