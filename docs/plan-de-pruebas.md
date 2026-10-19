# Plan de pruebas: automatización mobile Android

**Proyecto:** `appium-mobile-automation`
**App bajo prueba:** My Demo App de Sauce Labs 2.2.0 (`mda-2.2.0-25.apk`, paquete `com.saucelabs.mydemoapp.android`)
**Responsable:** Tincho

## 1. Objetivo

Automatizar el flujo de e-commerce de la aplicación móvil nativa: selección de artículo, carrito, login, checkout y logout, de modo que se pueda verificar con un comando (`mvn clean test`) que el flujo principal sigue funcionando.

## 2. Alcance

**Incluido**
- Catálogo de productos y navegación hasta el detalle.
- Agregar productos al carrito y verlos en el carrito.
- Login exitoso, login con usuario bloqueado y login sin usuario.
- Checkout completo: envío, pago, revisión y confirmación.
- Logout con confirmación.

**Fuera de alcance (por ahora)**
- Pruebas en dispositivos físicos, iOS y otros tamaños de pantalla.
- Pruebas de rendimiento, seguridad, accesibilidad y localización.
- Productos del catálogo que no sean *Sauce Labs Backpack* (ver hallazgo H1).

## 3. Entorno de pruebas

| Componente | Detalle |
|---|---|
| Lenguaje y build | Java 17 (`maven.compiler.release`), compilado con JDK 18; Maven con Surefire 3.5.2 |
| Framework de pruebas | JUnit 5.13.4 |
| Cliente Appium | Appium Java Client 10.1.1 |
| Servidor Appium | 3.8.0, en `http://127.0.0.1:4723` |
| Driver | UiAutomator2 8.7.0 |
| Dispositivo | Emulador Android (AVD Pixel_9), Android 16 (API 36), `emulator-5554` |
| Sistema operativo del equipo | Windows 11 |

## 4. Estrategia

- **Tipo de pruebas:** funcionales de interfaz (extremo a extremo) sobre la aplicación nativa.
- **Estructura:** todas las clases extienden `BaseTest`, que crea y cierra la sesión de Appium y reúne los métodos de ayuda (esperas explícitas, escritura, scroll, login y logout).
- **Localizadores:** primero `resource-id`; si está repetido o compartido, Accessibility ID; XPath solo como último recurso (hasta ahora no hizo falta).
- **Esperas:** siempre explícitas (`WebDriverWait`), sin `Thread.sleep`. Cuando un identificador se comparte entre pantallas, la espera confirma el cambio por texto.
- **Independencia:** cada test abre su propia sesión y la cierra al terminar; ninguno depende del resultado de otro.
- **Próximas etapas:** Screen Objects con Page Factory (Entrega 2) y escenarios Cucumber con datos externos y reporte Allure (Entrega final).

## 5. Datos de prueba

| Dato | Valor | Uso |
|---|---|---|
| Usuario válido | `bod@example.com` / `10203040` | Login exitoso, checkout, logout |
| Usuario bloqueado | `alice@example.com` / `10203040` | Login inválido |
| Producto | *Sauce Labs Backpack* y su variante *(violet)* | Carrito, scroll, checkout |
| Dirección de envío y tarjeta | Datos ficticios definidos en `CheckoutTest` | Checkout |

Son los usuarios y datos de demostración que publica la propia aplicación.

## 6. Casos de prueba automatizados

| ID | Caso | Resultado esperado | Clase y método |
|---|---|---|---|
| CP-01 | Abrir la aplicación | Se crea la sesión y el paquete activo es `com.saucelabs.mydemoapp.android` | `AppiumTest.verificarAplicacionAbierta` |
| CP-02 | Ver el catálogo | Se muestra la lista; hay al menos un producto con nombre y precio que empieza con `$` | `CatalogoTest.catalogoMuestraProductosConNombreYPrecio` |
| CP-03 | Acceso al carrito desde el catálogo | El ícono del carrito está visible | `CatalogoTest.catalogoPermiteAccederAlCarrito` |
| CP-04 | Agregar un producto al carrito | El detalle muestra *Backpack*, el botón *Add to cart* está disponible y el producto aparece en el carrito | `AgregarAlCarritoTest` |
| CP-05 | Scroll hasta un producto del final del catálogo | Se encuentra *Sauce Labs Backpack (violet)*, se abre su detalle y el título coincide | `ScrollHastaProductoTest` |
| CP-06 | Login exitoso | Se llega al catálogo y el menú ofrece *Log Out* | `LoginTest.loginExitosoLlevaAlCatalogo` |
| CP-07 | Login con usuario bloqueado | Mensaje *Sorry this user has been locked out.* y se permanece en el login | `LoginTest.loginConUsuarioBloqueadoMuestraError` |
| CP-08 | Login sin usuario | Mensaje *Username is required* y se permanece en el login | `LoginTest.loginSinUsuarioMuestraError` |
| CP-09 | Checkout completo | Pasa por login, envío, pago y revisión; termina en *Checkout Complete* con *Thank you for your order* | `CheckoutTest` |
| CP-10 | Logout | Con sesión iniciada, el logout confirmado vuelve a la pantalla de Login | `LogoutTest.logoutVuelveALaPantallaDeLogin` |

**Última ejecución completa:** 10 de 10 pruebas en verde (`Tests run: 10, Failures: 0, Errors: 0`).

## 7. Casos planificados (pendientes de automatizar)

| ID | Caso | Observación |
|---|---|---|
| CP-11 | Login con contraseña incorrecta | Falta confirmar en la app el mensaje exacto |
| CP-12 | Login con contraseña vacía | Mensaje esperado `Enter Password` |
| CP-13 | Aumentar y disminuir la cantidad de un producto en el carrito | Usa `plusIV` y `minusIV`, y verifica `itemsTV` y `totalPriceTV` |
| CP-14 | Eliminar un producto del carrito | Usa `removeBt`; debe aparecer el carrito vacío con *Go Shopping* |
| CP-15 | Campos obligatorios del envío | Full Name, Address Line 1, City, Zip Code y Country |
| CP-16 | Total del pedido | El total de la revisión es el del carrito más el costo de envío |
| CP-17 | Cancelar el logout | El diálogo se cierra y la sesión sigue iniciada |

## 8. Hallazgos y riesgos

| ID | Descripción | Impacto |
|---|---|---|
| H1 | Al abrir el detalle de productos que no son *Backpack* (por ejemplo *Onesie*) la aplicación se cierra | Esos productos quedan sin cubrir. Los tests usan *Backpack* |
| H2 | En la pantalla *Checkout Complete* el contador del carrito (`cartTV`) siguió mostrando `1` | A confirmar si es un defecto o si se actualiza al volver al catálogo |
| H3 | En el emulador, después del login la aplicación llegó a ponerse en negro y cerrarse hasta hacer un *cold boot* | Riesgo de falsos fallos por inestabilidad del entorno. Si se repite, conviene probar un AVD con una versión de Android más antigua |
| H4 | Una ejecución falló con `SessionNotCreated` (la app no arrancó a tiempo) y pasó al repetirla | Riesgo de falsos fallos por lentitud al iniciar; posible ajuste con `appium:appWaitDuration` |
| H5 | Los identificadores se repiten dentro de las listas y entre pantallas | Se resuelve con esperas por texto y selectores por jerarquía |

## 9. Criterios

- **Entrada:** servidor Appium en ejecución, emulador iniciado y visible en `adb devices`, Appium Inspector cerrado, APK en `src/test/resources/apks/`.
- **Salida:** todas las pruebas en verde con `mvn clean test`. Un fallo se confirma repitiendo el test solo (`mvn clean test -Dtest=NombreDelTest`) antes de darlo por defecto.

## 10. Ejecución

```bash
mvn clean test                    # todas las pruebas
mvn clean test -Dtest=LoginTest   # una clase
```

## 11. Desired Capabilities propuestas

Se leen de `src/test/resources/config.properties` y las arma `DriverFactory` con `UiAutomator2Options`.

| Capability | Valor |
|---|---|
| `platformName` | `Android` |
| `appium:automationName` | `UiAutomator2` |
| `appium:deviceName` | `emulator-5554` |
| `appium:udid` | `emulator-5554` |
| `appium:app` | `src/test/resources/apks/mda-2.2.0-25.apk` (ruta relativa al proyecto) |
| `appium:appPackage` | `com.saucelabs.mydemoapp.android` |
| `appium:appActivity` | `com.saucelabs.mydemoapp.android.view.activities.SplashActivity` |
| `appium:appWaitActivity` | `*` |

La URL del servidor (`http://127.0.0.1:4723`) también está en `config.properties`.
