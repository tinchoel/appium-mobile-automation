package tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("Logout")
@DisplayName("Logout")
public class LogoutTest extends BaseTest {

    @Test
    @DisplayName("Confirmar el logout vuelve a la pantalla de Login")
    void logoutVuelveALaPantallaDeLogin() {
        // Arrange: usuario con sesión iniciada
        loginExitoso();

        // Act: Menú -> Log Out -> confirmar
        hacerLogout();
        boolean botonLogin = esperarVisible(id("loginBtn")).isDisplayed();
        boolean campoUsuario = esperarVisible(id("nameET")).isDisplayed();
        boolean campoClave = esperarVisible(id("passwordET")).isDisplayed();

        // Assert
        assertAll("Pantalla de Login después del logout",
                () -> assertTrue(botonLogin, "Debe verse el botón Login"),
                () -> assertTrue(campoUsuario, "Debe verse el campo Username"),
                () -> assertTrue(campoClave, "Debe verse el campo Password"));
    }
}