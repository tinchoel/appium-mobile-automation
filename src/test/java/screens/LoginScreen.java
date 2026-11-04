package screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

/** Pantalla de Login: ingreso de credenciales y autenticación. */
public class LoginScreen extends BaseScreen {

    private static final By USERNAME_FIELD = AppiumBy.id(ID_PREFIX + "nameET");
    private static final By PASSWORD_FIELD = AppiumBy.id(ID_PREFIX + "passwordET");
    private static final By LOGIN_BUTTON = AppiumBy.id(ID_PREFIX + "loginBtn");

    public LoginScreen(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void enterUsername(String username) {
        type(USERNAME_FIELD, username);
    }

    public void enterPassword(String password) {
        type(PASSWORD_FIELD, password);
    }

    public void clickLogin() {
        hideKeyboard();
        tap(LOGIN_BUTTON);
    }

    /** Completa el proceso de autenticación reutilizando los tres métodos anteriores. */
    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
}