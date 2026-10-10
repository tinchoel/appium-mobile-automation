import config.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppiumTest {

    private AndroidDriver driver;

    @BeforeEach
    public void configurarSesion() throws Exception {
        driver = DriverFactory.crearDriver();
    }

    @DisplayName("Debe iniciar la sesión y abrir My Demo App")
    @Test
    public void verificarAplicacionAbierta() {
        assertAll(
                () -> assertNotNull(driver.getSessionId(), "La sesión no se inició"),
                () -> assertEquals("com.saucelabs.mydemoapp.android", driver.getCurrentPackage(),
                        "La app abierta no es My Demo App"));
    }

    @AfterEach
    public void finalizarSesion() {
        if (driver != null) {
            driver.quit();
        }
    }
}