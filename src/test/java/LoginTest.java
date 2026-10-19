import io.appium.java_client.AppiumBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("Login")
@DisplayName("Login")
public class LoginTest extends BaseTest {

    @Test
    @DisplayName("Login exitoso lleva al catálogo")
    void loginExitosoLlevaAlCatalogo() {
        // Arrange: abrir la pantalla de login desde el menú
        irAlLogin();

        // Act: iniciar sesión y abrir el menú
        completarLogin(USUARIO_VALIDO, CLAVE);
        esperarTexto(id(ID_TITULO), "Products");
        boolean catalogoVisible = esperarVisible(id("productRV")).isDisplayed();
        abrirMenu();
        boolean logoutVisible = esperarVisible(AppiumBy.accessibilityId("Logout Menu Item")).isDisplayed();

        // Assert
        assertAll("Login exitoso",
                () -> assertTrue(catalogoVisible, "Después del login debe verse el catálogo"),
                () -> assertTrue(logoutVisible, "Con sesión iniciada debe aparecer la opción Log Out"));
    }

    @Test
    @DisplayName("Login con usuario bloqueado muestra error")
    void loginConUsuarioBloqueadoMuestraError() {
        // Arrange
        irAlLogin();

        // Act
        completarLogin(USUARIO_BLOQUEADO, CLAVE);
        esperarTexto(id("passwordErrorTV"), "Sorry this user has been locked out.");

        // Assert: el mensaje ya se esperó arriba; además se permanece en el login
        assertTrue(driver.findElement(id("loginBtn")).isDisplayed(),
                "Con un usuario bloqueado se debe permanecer en la pantalla de login");
    }

    @Test
    @DisplayName("Login sin usuario muestra error")
    void loginSinUsuarioMuestraError() {
        // Arrange
        irAlLogin();

        // Act
        completarLogin("", "");
        esperarTexto(id("nameErrorTV"), "Username is required");

        // Assert
        assertTrue(driver.findElement(id("loginBtn")).isDisplayed(),
                "Sin usuario se debe permanecer en la pantalla de login");
    }
}