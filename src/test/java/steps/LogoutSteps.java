package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import support.TestContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Pasos del cierre de sesión, resueltos con HomeScreen y LoginScreen. */
public class LogoutSteps {

    private final TestContext context;

    public LogoutSteps(TestContext context) {
        this.context = context;
    }

    @When("cierra la sesión desde el menú")
    public void cierraLaSesion() {
        context.home().logout();
    }

    @Then("debería volver a la pantalla de inicio de sesión")
    public void vuelveAlLogin() {
        assertTrue(context.login().isDisplayed(), "Después del logout debe verse la pantalla de inicio de sesión");
    }

    @Then("no debería visualizar el catálogo de productos")
    public void noVisualizaElCatalogo() {
        assertFalse(context.home().isProductScreenDisplayed(), "Sin sesión no debe mostrarse el catálogo");
    }
}
