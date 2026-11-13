package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import support.TestContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Pasos del login, resueltos con HomeScreen y LoginScreen. */
public class LoginSteps {

    private final TestContext context;

    public LoginSteps(TestContext context) {
        this.context = context;
    }

    @Given("que el usuario se encuentra en la pantalla de inicio de sesión")
    public void usuarioEnPantallaDeLogin() {
        context.home().goToLogin();
        assertTrue(context.login().isDisplayed(), "Debe verse la pantalla de inicio de sesión");
    }

    @When("ingresa el usuario {string}")
    public void ingresaElUsuario(String usuario) {
        context.login().enterUsername(usuario);
    }

    @When("ingresa la contraseña {string}")
    public void ingresaLaContrasena(String contrasena) {
        context.login().enterPassword(contrasena);
    }

    @When("presiona el botón de iniciar sesión")
    public void presionaIniciarSesion() {
        context.login().clickLogin();
    }

    @Then("debería visualizar el catálogo de productos")
    public void visualizaElCatalogo() {
        assertTrue(context.home().isProductScreenDisplayed(), "Debe verse el catálogo de productos");
        assertEquals("Products", context.home().getProductTitle(), "Título del catálogo");
    }

    @Then("debería permanecer en la pantalla de inicio de sesión")
    public void permaneceEnLogin() {
        assertTrue(context.login().isDisplayed(), "Debe seguir visible la pantalla de inicio de sesión");
    }

    @Then("debería visualizar un mensaje de error")
    public void visualizaMensajeDeError() {
        String mensaje = context.login().getErrorMessage();
        assertFalse(mensaje.isBlank(), "Debe mostrarse un mensaje de error");
    }

    @Then("debería visualizar un mensaje indicando que el usuario está bloqueado")
    public void visualizaMensajeDeBloqueo() {
        String mensaje = context.login().getErrorMessage();
        assertTrue(mensaje.contains("locked out"), "Mensaje de usuario bloqueado. Se vio: " + mensaje);
    }
}
