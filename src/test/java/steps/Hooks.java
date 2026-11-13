package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import support.TestContext;

import java.net.MalformedURLException;

/** Abre la sesión de Appium antes de cada escenario y la cierra al terminar. */
public class Hooks {

    private final TestContext context;

    public Hooks(TestContext context) {
        this.context = context;
    }

    @Before
    public void abrirApp() throws MalformedURLException {
        context.start();
    }

    @After
    public void cerrarApp(Scenario scenario) {
        if (scenario.isFailed() && context.hasDriver()) {
            byte[] captura = ((TakesScreenshot) context.driver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(captura, "image/png", "Captura al fallar");
        }
        context.quit();
    }
}
