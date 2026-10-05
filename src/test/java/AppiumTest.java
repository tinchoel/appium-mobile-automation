import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppiumTest {

    private AndroidDriver driver;

    @BeforeEach
    public void configurarSesion() throws Exception {
        // Ruta del APK relativa al proyecto, convertida a absoluta
        Path apk = Paths.get("src", "test", "resources", "apks", "mda-2.2.0-25.apk")
                .toAbsolutePath();

        UiAutomator2Options options = new UiAutomator2Options();
        options.setDeviceName("emulator-5554");
        options.setUdid("emulator-5554");
        options.setApp(apk.toString());
        // La app arranca con un SplashActivity que desaparece enseguida;
        // "*" evita que Appium espere esa pantalla concreta.
        options.setAppWaitActivity("*");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);
    }

    @DisplayName("Debe iniciar la sesión y abrir My Demo App")
    @Test
    public void verificarAplicacionAbierta() {
        assertAll(
                () -> assertNotNull(driver.getSessionId(), "La sesión no se inició"),
                () -> assertEquals("com.saucelabs.mydemoapp.android",
                        driver.getCurrentPackage(),
                        "La app abierta no es My Demo App"));
    }

    @AfterEach
    public void finalizarSesion() {
        if (driver != null) {
            driver.quit();
        }
    }
}