package support;

import config.DriverFactory;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import screens.CartScreen;
import screens.CheckoutScreen;
import screens.HomeScreen;
import screens.LoginScreen;

import java.net.MalformedURLException;
import java.time.Duration;

/**
 * Estado compartido de un escenario: el driver de Appium, la espera explícita y los Screen Objects.
 * Cucumber (PicoContainer) crea una instancia nueva por escenario y la entrega a todas las clases de
 * pasos que la pidan en su constructor, así todos trabajan con la misma sesión.
 */
public class TestContext {

    private AndroidDriver driver;
    private WebDriverWait wait;

    private HomeScreen home;
    private LoginScreen login;
    private CartScreen cart;
    private CheckoutScreen checkout;

    /** Producto con el que se está trabajando en el escenario (el último seleccionado). */
    private String currentProduct;

    /** Abre la sesión de Appium y deja la app lista. */
    public void start() throws MalformedURLException {
        driver = DriverFactory.crearDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        closeInitialDialogIfPresent();
    }

    /** Cierra la sesión de Appium. */
    public void quit() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    public boolean hasDriver() {
        return driver != null;
    }

    public AndroidDriver driver() {
        return driver;
    }

    public HomeScreen home() {
        if (home == null) {
            home = new HomeScreen(driver, wait);
        }
        return home;
    }

    public LoginScreen login() {
        if (login == null) {
            login = new LoginScreen(driver, wait);
        }
        return login;
    }

    public CartScreen cart() {
        if (cart == null) {
            cart = new CartScreen(driver, wait);
        }
        return cart;
    }

    public CheckoutScreen checkout() {
        if (checkout == null) {
            checkout = new CheckoutScreen(driver, wait);
        }
        return checkout;
    }

    public String currentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(String product) {
        this.currentProduct = product;
    }

    /** Algunos dispositivos muestran un mensaje de Android al abrir la app. Si no aparece, se sigue normalmente. */
    private void closeInitialDialogIfPresent() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(AppiumBy.id("android:id/button1")))
                    .click();
        } catch (RuntimeException ignorada) {
            // No hay diálogo: continuar
        }
    }
}
