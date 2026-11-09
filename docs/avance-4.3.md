# Avance 4.3 – Page Factory Pattern para Mobile

## Objetivo

Migrar `CartScreen` y `CheckoutScreen` a **Page Factory**, declarando los elementos con `@AndroidFindBy` sobre campos `WebElement`, e inicializándolos de forma centralizada en `BaseScreen`. Con eso se automatiza el flujo completo de compra usando Screen Objects.

## Qué cambió

| Clase | Cambio |
|-------|--------|
| `BaseScreen` | Inicializa los elementos con `PageFactory.initElements(new AppiumFieldDecorator(driver), this)` en el constructor. Se agregaron métodos sobrecargados que reciben `WebElement` (`waitVisible`, `waitClickable`, `waitForText`, `tap`, `type`, `textOf`) y `scrollToId`. |
| `LoginScreen` | Migrada a Page Factory (prerrequisito: el checkout sin sesión pasa por el Login). |
| `CartScreen` | Migrada: títulos del carrito como `List<WebElement>`, total de ítems, precio total y botón de checkout. |
| `CheckoutScreen` | Nueva, con Page Factory: dirección de envío, pago, revisión del pedido y confirmación. |
| `HomeScreen` | Se mantiene con `By` (localizadores dinámicos por nombre de producto). Solo se agregó una espera inicial en `selectProduct`. |
| `CompraPomTest` | Nueva prueba del flujo de compra completo con Screen Objects. |

## Inicialización centralizada

```text
protected BaseScreen(AndroidDriver driver, WebDriverWait wait) {
    this.driver = driver;
    this.wait = wait;
    PageFactory.initElements(new AppiumFieldDecorator(driver), this);
}
```

Todas las pantallas heredan de `BaseScreen`, así que ninguna llama a `initElements` por su cuenta. El constructor de cada pantalla mantiene la firma `(driver, wait)`.

## Elementos → anotaciones

### CartScreen

| Campo | Anotación |
|-------|-----------|
| `productTitles` (lista) | `@AndroidFindBy(id = ID_PREFIX + "titleTV")` |
| `totalItems` | `@AndroidFindBy(id = ID_PREFIX + "itemsTV")` |
| `totalPrice` | `@AndroidFindBy(id = ID_PREFIX + "totalPriceTV")` |
| `checkoutButton` | `@AndroidFindBy(accessibility = "Confirms products for checkout")` |

### CheckoutScreen

| Paso | Campos (resource-id) |
|------|----------------------|
| Envío | `fullNameET`, `address1ET`, `cityET`, `stateET`, `zipET`, `countryET` |
| Pago | `nameET` (titular), `cardNumberET`, `expirationDateET`, `securityCodeET` |
| Botón de avance | `paymentBtn` (cambia de texto: To Payment / Review Order / Place Order) |
| Revisión | `fullNameTV`, `addressTV`, `cityTV`, `countryTV`, `cardHolderTV`, `itemNumberTV`, `totalAmountTV` |
| Confirmación | `completeTV`, `thankYouTV` |

## Antes y después

```text
// Antes (By + búsqueda en cada uso)
private static final By TOTAL_PRICE = AppiumBy.id(ID_PREFIX + "totalPriceTV");
String total = textOf(TOTAL_PRICE);

// Después (Page Factory)
@AndroidFindBy(id = ID_PREFIX + "totalPriceTV")
private WebElement totalPrice;
String total = textOf(totalPrice);
```

## Decisiones de diseño

- **Esperas explícitas:** se mantienen. Los proxies de Page Factory son perezosos (buscan el elemento al usarlo) y `WebDriverWait` ignora `NotFoundException`, por lo que `ExpectedConditions.visibilityOf(WebElement)` funciona. No se usa `Thread.sleep`.
- **Ids compartidos entre pantallas:** `paymentBtn` se usa en tres pasos, así que se espera por el texto del botón antes de tocarlo. `nameET` es el usuario en Login y el titular en Pago; cada pantalla lo declara por separado.
- **`List<WebElement>` en el carrito:** permite verificar que el producto esté en el carrito sin depender de la posición.
- **`HomeScreen` con `By`:** sus localizadores dependen del nombre del producto (dinámicos), que Page Factory no resuelve. Puede migrarse en un avance posterior.

## Cómo ejecutar

```text
mvn clean test "-Dtest=CompraPomTest"
mvn clean test "-Dgroups=POM"
mvn clean test
```

Orden de arranque: emulador → `adb devices` → `appium` en su propia terminal → `mvn`.

## Evidencia

`capturas/14-compra-pom-exitosa.png`: ejecución de `CompraPomTest` en verde.

## Entregables

- `CartScreen` y `CheckoutScreen` migradas a Page Factory
- Declaraciones `@AndroidFindBy`
- Integración en `BaseScreen`
- Métodos públicos actualizados
- Ejecución y validación del flujo de compra (`CompraPomTest`)
- Código versionado en GitHub
