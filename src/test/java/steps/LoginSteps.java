package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/** Pasos del login. Por ahora son temporales: solo imprimen en consola. */
public class LoginSteps {

    @Given("que el usuario se encuentra en la pantalla de inicio de sesión")
    public void usuarioEnPantallaDeLogin() {
        System.out.println("[login] el usuario está en la pantalla de inicio de sesión");
    }

    @When("ingresa el usuario {string}")
    public void ingresaElUsuario(String usuario) {
        System.out.println("[login] usuario: " + usuario);
    }

    @When("ingresa la contraseña {string}")
    public void ingresaLaContrasena(String contrasena) {
        System.out.println("[login] contraseña ingresada");
    }

    @When("presiona el botón de iniciar sesión")
    public void presionaIniciarSesion() {
        System.out.println("[login] presiona iniciar sesión");
    }

    @Then("debería visualizar el catálogo de productos")
    public void visualizaElCatalogo() {
        System.out.println("[login] verifica el catálogo de productos");
    }

    @Then("debería permanecer en la pantalla de inicio de sesión")
    public void permaneceEnLogin() {
        System.out.println("[login] verifica que sigue en el login");
    }

    @Then("debería visualizar un mensaje de error")
    public void visualizaMensajeDeError() {
        System.out.println("[login] verifica el mensaje de error");
    }

    @Then("debería visualizar un mensaje indicando que el usuario está bloqueado")
    public void visualizaMensajeDeBloqueo() {
        System.out.println("[login] verifica el mensaje de usuario bloqueado");
    }
}
