package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import support.CsvReader;
import support.TestContext;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Pasos del login, resueltos con HomeScreen y LoginScreen. */
public class LoginSteps {

    private static final String ARCHIVO_USUARIOS = "data/usuarios.csv";

    private final TestContext context;

    public LoginSteps(TestContext context) {
        this.context = context;
    }

    @Given("que el usuario se encuentra en la pantalla de inicio de sesión")
    public void usuarioEnPantallaDeLogin() {
        context.home().goToLogin();
        assertTrue(context.login().isDisplayed(), "Debe verse la pantalla de inicio de sesión");
    }

    @Given("que el usuario inició sesión con el usuario {string} y la contraseña {string}")
    public void usuarioIniciaSesion(String usuario, String contrasena) {
        context.home().goToLogin();
        context.login().login(usuario, contrasena);
        assertTrue(context.home().isProductScreenDisplayed(), "Después del login debe verse el catálogo");
    }

    /** Completa usuario y contraseña con los datos del caso indicado en data/usuarios.csv. */
    @When("ingresa las credenciales del caso {string}")
    public void ingresaLasCredencialesDelCaso(String caso) {
        Map<String, String> datos = CsvReader.row(ARCHIVO_USUARIOS, "caso", caso);
        ingresaElUsuario(datos.get("usuario"));
        ingresaLaContrasena(datos.get("contrasena"));
    }

    @When("ingresa el usuario {string}")
    public void ingresaElUsuario(String usuario) {
        if (!usuario.isEmpty()) { // una celda vacía de un Examples significa "no completar el campo"
            context.login().enterUsername(usuario);
        }
    }

    @When("ingresa la contraseña {string}")
    public void ingresaLaContrasena(String contrasena) {
        if (!contrasena.isEmpty()) {
            context.login().enterPassword(contrasena);
        }
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

    /** Verifica el resultado esperado que figura en data/usuarios.csv para el caso indicado. */
    @Then("debería ver el resultado esperado del caso {string}")
    public void veElResultadoDelCaso(String caso) {
        verificarResultado(CsvReader.row(ARCHIVO_USUARIOS, "caso", caso).get("resultado"));
    }

    /** Resultados posibles: "catálogo", "usuario bloqueado", "usuario obligatorio" y "mensaje de error". */
    private void verificarResultado(String resultado) {
        switch (resultado) {
            case "catálogo" -> visualizaElCatalogo();
            case "usuario bloqueado" -> {
                permaneceEnLogin();
                visualizaMensajeDeBloqueo();
            }
            case "usuario obligatorio" -> {
                permaneceEnLogin();
                String mensaje = context.login().getErrorMessage();
                assertTrue(mensaje.contains("Username is required"), "Mensaje de usuario obligatorio. Se vio: " + mensaje);
            }
            case "mensaje de error" -> {
                permaneceEnLogin();
                visualizaMensajeDeError();
            }
            default -> throw new IllegalArgumentException("Resultado no reconocido: " + resultado);
        }
    }
}
