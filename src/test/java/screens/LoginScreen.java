package screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/** Pantalla de Login: ingreso de credenciales y autenticación. Elementos declarados con Page Factory. */
public class LoginScreen extends BaseScreen {

    @AndroidFindBy(id = ID_PREFIX + "nameET")
    private WebElement usernameField;

    @AndroidFindBy(id = ID_PREFIX + "passwordET")
    private WebElement passwordField;

    @AndroidFindBy(id = ID_PREFIX + "loginBtn")
    private WebElement loginButton;

    // Mensajes de error: el de usuario aparece bajo el campo Username; el resto, bajo Password
    @AndroidFindBy(id = ID_PREFIX + "nameErrorTV")
    private WebElement usernameError;

    @AndroidFindBy(id = ID_PREFIX + "passwordErrorTV")
    private WebElement passwordError;

    public LoginScreen(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void enterUsername(String username) {
        type(usernameField, username);
    }

    public void enterPassword(String password) {
        type(passwordField, password);
    }

    public void clickLogin() {
        hideKeyboard();
        tap(loginButton);
    }

    /** Completa el proceso de autenticación reutilizando los tres métodos anteriores. */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    /** true si la pantalla de login se muestra (se ve el botón Login) dentro del tiempo de espera. */
    public boolean isDisplayed() {
        try {
            return waitVisible(loginButton).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Espera a que aparezca algún mensaje de error y lo devuelve; si no aparece, devuelve texto vacío. */
    public String getErrorMessage() {
        try {
            return wait.until((ExpectedCondition<String>) d -> {
                for (WebElement error : List.of(usernameError, passwordError)) {
                    try {
                        String text = error.getText();
                        if (error.isDisplayed() && !text.isBlank()) {
                            return text;
                        }
                    } catch (RuntimeException ignorada) {
                        // El elemento todavía no está: se sigue esperando
                    }
                }
                return null;
            });
        } catch (TimeoutException e) {
            return "";
        }
    }
}
