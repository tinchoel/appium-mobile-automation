import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Prueba de Logout: iniciar sesión, cerrarla y comprobar que la app vuelve a la pantalla de Login.
 */
public class LogoutTest extends BaseTest {

    @Test
    void logoutVuelveALaPantallaDeLogin() {
        loginExitoso();
        hacerLogout();

        // Al confirmar el logout la app muestra la pantalla de Login
        Assertions.assertTrue(esperarVisible(id("loginBtn")).isDisplayed(),
                "Después del logout debe verse el botón Login");
        Assertions.assertTrue(esperarVisible(id("nameET")).isDisplayed(),
                "Después del logout debe verse el campo Username");
        Assertions.assertTrue(esperarVisible(id("passwordET")).isDisplayed(),
                "Después del logout debe verse el campo Password");
    }
}