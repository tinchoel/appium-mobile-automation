package steps;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/** Pasos del checkout. Temporales: solo imprimen en consola. */
public class CheckoutSteps {

    @When("inicia el proceso de compra")
    public void iniciaElProcesoDeCompra() {
        System.out.println("[checkout] inicia el proceso de compra");
    }

    @When("inicia sesión con el usuario {string} y la contraseña {string}")
    public void iniciaSesion(String usuario, String contrasena) {
        System.out.println("[checkout] inicia sesión con: " + usuario);
    }

    @When("completa la dirección de envío")
    public void completaLaDireccionDeEnvio() {
        System.out.println("[checkout] completa la dirección de envío");
    }

    @When("completa los datos de pago")
    public void completaLosDatosDePago() {
        System.out.println("[checkout] completa los datos de pago");
    }

    @When("confirma el pedido")
    public void confirmaElPedido() {
        System.out.println("[checkout] confirma el pedido");
    }

    @Then("debería visualizar la confirmación de la compra")
    public void visualizaLaConfirmacion() {
        System.out.println("[checkout] verifica la confirmación de la compra");
    }
}
