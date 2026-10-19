import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppiumTest extends BaseTest {

    @Test
    void verificarAplicacionAbierta() {
        assertNotNull(driver.getSessionId(), "No se creó la sesión de Appium");
        assertEquals("com.saucelabs.mydemoapp.android", driver.getCurrentPackage());
    }
}