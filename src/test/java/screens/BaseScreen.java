package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Clase base de todos los Screen Objects: guarda el driver y reúne las acciones comunes
 * (esperas explícitas, tap, escritura y scroll). Las pantallas no repiten este código.
 */
public abstract class BaseScreen {

    protected static final String ID_PREFIX = "com.saucelabs.mydemoapp.android:id/";

    protected final AndroidDriver driver;
    protected final WebDriverWait wait;

    protected BaseScreen(AndroidDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    /** Atajo para un resource-id de la app: id("productTV"). */
    protected By id(String name) {
        return AppiumBy.id(ID_PREFIX + name);
    }

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
}