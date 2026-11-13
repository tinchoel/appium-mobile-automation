package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Clase base de todos los Screen Objects: guarda el driver, inicializa los elementos declarados con
 * Page Factory (@AndroidFindBy) y reúne las acciones comunes (esperas explícitas, tap, escritura y scroll).
 */
public abstract class BaseScreen {

    protected static final String ID_PREFIX = "com.saucelabs.mydemoapp.android:id/";

    protected final AndroidDriver driver;
    protected final WebDriverWait wait;

    protected BaseScreen(AndroidDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        // Prepara los campos anotados con @AndroidFindBy de la pantalla que se está creando.
        // Los elementos se buscan cuando se usan, no al crear la pantalla; si no aparecen, reintenta hasta 10 s.
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    /** Atajo para un resource-id de la app: id("productTV"). */
    protected By id(String name) {
        return AppiumBy.id(ID_PREFIX + name);
    }

    // ---------- Esperas y acciones sobre un localizador (By) ----------

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void waitForText(By locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    protected void tap(By locator) {
        waitClickable(locator).click();
    }

    /** Espera que el campo sea visible, lo limpia y escribe el texto. */
    protected void type(By locator, String text) {
        WebElement field = waitVisible(locator);
        field.clear();
        field.sendKeys(text);
    }

    // ---------- Esperas y acciones sobre un WebElement de Page Factory ----------

    protected WebElement waitVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void waitForText(WebElement element, String text) {
        wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    protected void tap(WebElement element) {
        waitClickable(element).click();
    }

    protected void type(WebElement element, String text) {
        WebElement field = waitVisible(element);
        field.clear();
        field.sendKeys(text);
    }

    /** Espera que el elemento sea visible y devuelve su texto. */
    protected String textOf(WebElement element) {
        return waitVisible(element).getText();
    }

    // ---------- Teclado y scroll ----------

    protected void hideKeyboard() {
        if (driver.isKeyboardShown()) {
            driver.hideKeyboard();
        }
    }

    /** Scroll vertical hacia abajo hasta que aparece un elemento con ese texto exacto. */
    protected void scrollToText(String text) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
                        + ".setAsVerticalList()"
                        + ".scrollIntoView(new UiSelector().text(\"" + text + "\"))"));
    }

    /**
     * Scroll vertical hacia abajo hasta un elemento por su resource-id (sin el prefijo del paquete).
     * Si la pantalla no tiene un contenedor desplazable no hace nada: la espera posterior confirma
     * que el elemento está disponible.
     */
    protected void scrollToId(String idName) {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))"
                            + ".setAsVerticalList()"
                            + ".scrollIntoView(new UiSelector().resourceId(\"" + ID_PREFIX + idName + "\"))"));
        } catch (RuntimeException ignored) {
            // Nada que desplazar
        }
    }
}