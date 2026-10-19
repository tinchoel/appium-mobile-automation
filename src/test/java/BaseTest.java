import config.DriverFactory;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Clase base de todos los tests: crea y cierra la sesión de Appium y reúne los
 * métodos de ayuda compartidos (esperas explícitas, escritura y scroll).
 * Los tests solo extienden esta clase y escriben el flujo.
 */
public abstract class BaseTest {

    protected static final String ID_PREFIX = "com.saucelabs.mydemoapp.android:id/";

    protected static final String USUARIO_VALIDO = "bod@example.com";
    protected static final String USUARIO_BLOQUEADO = "alice@example.com";
    protected static final String CLAVE = "10203040";

    protected static final String ID_TITULO = "productTV";

    protected AndroidDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void iniciarSesion() throws Exception {
        driver = DriverFactory.crearDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        cerrarDialogoInicialSiAparece();
    }

    @AfterEach
    void cerrarSesion() {
        if (driver != null) {
            driver.quit();
        }
    }

    /** Algunos dispositivos muestran un mensaje de Android al abrir la app. Si no aparece, se sigue normalmente. */
    private void cerrarDialogoInicialSiAparece() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(AppiumBy.id("android:id/button1")))
                    .click();
        } catch (RuntimeException ignorada) {
            // No hay diálogo: continuar
        }
    }

    // ---------- Localizadores ----------

    /** Atajo para un resource-id de la app: id("productTV"). */
    protected By id(String nombre) {
        return AppiumBy.id(ID_PREFIX + nombre);
    }

    // ---------- Esperas explícitas ----------

    protected WebElement esperarVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement esperarClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void esperarTexto(By locator, String texto) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, texto));
    }

    // ---------- Acciones ----------

    protected void tap(By locator) {
        esperarClickable(locator).click();
    }

    /** Espera que el campo sea visible y escribe el texto. */
    protected void escribir(By locator, String texto) {
        WebElement campo = esperarVisible(locator);
        campo.clear();
        campo.sendKeys(texto);
    }

    protected void ocultarTeclado() {
        if (driver.isKeyboardShown()) {
            driver.hideKeyboard();
        }
    }

    // ---------- Menú, login y logout (compartidos por varios tests) ----------

    /** Abre el menú lateral (☰) desde cualquier pantalla que tenga el encabezado. */
    protected void abrirMenu() {
        tap(id("menuIV"));
    }

    /** Menú → Log In. Deja abierta la pantalla de login. */
    protected void irAlLogin() {
        abrirMenu();
        tap(AppiumBy.accessibilityId("Login Menu Item"));
        esperarVisible(id("loginBtn"));
    }

    /** Escribe usuario y contraseña en la pantalla de login y toca Login. */
    protected void completarLogin(String usuario, String clave) {
        if (!usuario.isEmpty()) {
            escribir(id("nameET"), usuario);
        }
        if (!clave.isEmpty()) {
            escribir(id("passwordET"), clave);
        }
        ocultarTeclado();
        tap(id("loginBtn"));
    }

    /** Login completo con el usuario válido; termina en el catálogo. */
    protected void loginExitoso() {
        irAlLogin();
        completarLogin(USUARIO_VALIDO, CLAVE);
        esperarTexto(id(ID_TITULO), "Products");
    }

    /** Menú → Log Out → confirmar en el diálogo. */
    protected void hacerLogout() {
        abrirMenu();
        tap(AppiumBy.accessibilityId("Logout Menu Item"));
        esperarTexto(id("alertTitle"), "Log Out");
        tap(AppiumBy.id("android:id/button1"));
    }

    // ---------- Scroll ----------

    /**
     * Scroll vertical hacia abajo hasta encontrar un elemento por su texto exacto.
     *
     * @return el WebElement encontrado, ya visible
     */
    protected WebElement scrollAbajoHasta(String texto) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true))"
                        + ".setAsVerticalList()"
                        + ".scrollIntoView(new UiSelector().text(\"" + texto + "\"))"));
        return esperarVisible(AppiumBy.androidUIAutomator("new UiSelector().text(\"" + texto + "\")"));
    }

    /** Scroll hasta un elemento por resource-id. Si la pantalla no tiene contenedor desplazable, no hace nada. */
    protected void scrollHastaId(String resourceId) {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true))"
                            + ".setAsVerticalList()"
                            + ".scrollIntoView(new UiSelector().resourceId(\"" + resourceId + "\"))"));
        } catch (RuntimeException ignorada) {
            // Nada que desplazar: la espera posterior confirma que el elemento está disponible
        }
    }
}