package screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Pantalla de Login: ingreso de credenciales y autenticación. Elementos declarados con Page Factory. */
public class LoginScreen extends BaseScreen {

    @AndroidFindBy(id = ID_PREFIX + "nameET")
    private WebElement usernameField;

    @AndroidFindBy(id = ID_PREFIX + "passwordET")
    private WebElement passwordField;

    @AndroidFindBy(id = ID_PREFIX + "loginBtn")
    private WebElement loginButton;

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
}