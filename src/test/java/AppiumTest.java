import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("Smoke")
@DisplayName("Verificación de la aplicación abierta")
public class AppiumTest extends BaseTest {

    @Test
    @DisplayName("La sesión se crea y la app abierta es My Demo App")
    void verificarAplicacionAbierta() {
        // Arrange: la sesión ya fue creada por BaseTest.

        // Act
        Object sesion = driver.getSessionId();
        String paquete = driver.getCurrentPackage();

        // Assert
        assertAll("Aplicación abierta",
                () -> assertNotNull(sesion, "No se creó la sesión de Appium"),
                () -> assertEquals("com.saucelabs.mydemoapp.android", paquete));
    }
}