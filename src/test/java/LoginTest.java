import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de Login: un caso exitoso y dos inválidos (usuario bloqueado y usuario vacío).
 */
public class LoginTest extends BaseTest {

    @Test
    void loginExitosoLlevaAlCatalogo() {
        irAlLogin();
        completarLogin(USUARIO_VALIDO, CLAVE);

        esperarTexto(id(ID_TITULO), "Products");
        Assertions.assertTrue(esperarVisible(id("productRV")).isDisplayed(),
                "Después del login debe verse el catálogo");

        // Con sesión iniciada el menú ofrece Log Out en lugar de Log In
        abrirMenu();
        Assertions.assertTrue(esperarVisible(AppiumBy.accessibilityId("Logout Menu Item")).isDisplayed(),
                "Con sesión iniciada debe aparecer la opción Log Out");
    }

    @Test
    void loginConUsuarioBloqueadoMuestraError() {
        irAlLogin();
        completarLogin(USUARIO_BLOQUEADO, CLAVE);

        esperarTexto(id("passwordErrorTV"), "Sorry this user has been locked out.");
        Assertions.assertTrue(driver.findElement(id("loginBtn")).isDisplayed(),
                "Con un usuario bloqueado se debe permanecer en la pantalla de login");
    }

    @Test
    void loginSinUsuarioMuestraError() {
        irAlLogin();
        completarLogin("", "");

        esperarTexto(id("nameErrorTV"), "Username is required");
        Assertions.assertTrue(driver.findElement(id("loginBtn")).isDisplayed(),
                "Sin usuario se debe permanecer en la pantalla de login");
    }
}