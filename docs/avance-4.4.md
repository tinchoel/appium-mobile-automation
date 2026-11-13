# Avance 4.4 – POM Avanzado para Mobile

## Objetivo

Que todas las pantallas hereden de `BaseScreen` y reutilicen sus métodos de espera y clic, sin duplicar código. Con este avance se completa la Entrega 2.

## Estado de cada requisito

| Requisito | Estado | Detalle |
|-----------|--------|---------|
| 1. `LoginScreen`, `HomeScreen`, `CartScreen` y `CheckoutScreen` extienden `BaseScreen` | Cumplido | Las cuatro declaran `extends BaseScreen` (se hizo en los avances 4.2 y 4.3). |
| 2. Constructor que envía el driver a la clase padre con `super(...)` | Cumplido, con una variante | Cada pantalla recibe `(AndroidDriver driver, WebDriverWait wait)` y llama a `super(driver, wait)`. |
| 3. Reemplazar esperas y clics duplicados por los métodos heredados | Cumplido | Ninguna pantalla crea un `WebDriverWait` ni llama a `.click()` directamente: todo pasa por `tap`, `type` y `waitVisible` de `BaseScreen`. |
| 4. Mantener localizadores y métodos específicos de cada pantalla | Cumplido | Cada pantalla conserva sus `@AndroidFindBy` y sus métodos de negocio. |
| 5. Verificar que los flujos existentes siguen funcionando | Se verifica con la suite completa | Ver "Cómo verificar". |

## Equivalencias con la consigna

La consigna usa nombres y firmas genéricos. En el proyecto se mantienen los nombres que ya usan todas las pantallas:

| Consigna | En el proyecto |
|----------|----------------|
| `waitForVisibility(By)` | `waitVisible(By)` y `waitVisible(WebElement)` |
| `click(By)` | `tap(By)` y `tap(WebElement)` (esperan que el elemento sea clickeable y hacen el clic) |
| `new BaseScreen(AppiumDriver driver)` | `BaseScreen(AndroidDriver driver, WebDriverWait wait)` |

Por qué el constructor recibe también el `wait`:
- Se comparte una sola espera explícita (la que crea `BaseTest`) en todas las pantallas, en vez de crear un `WebDriverWait` nuevo en cada método.
- Se usa `AndroidDriver` porque `BaseScreen` necesita operaciones propias de Android (por ejemplo, `isKeyboardShown()` y el scroll con UiAutomator).

Además, `BaseScreen` inicializa los elementos de Page Factory, así que las pantallas no repiten esa lógica.

## Cómo se ve en una pantalla

```text
public class LoginScreen extends BaseScreen {

    @AndroidFindBy(id = ID_PREFIX + "loginBtn")
    private WebElement loginButton;

    public LoginScreen(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    public void clickLogin() {
        hideKeyboard();
        tap(loginButton);      // método heredado: espera que sea clickeable y hace clic
    }
}
```

## Cómo verificar

Búsqueda de lógica duplicada en las pantallas (no debe encontrar nada fuera de `BaseScreen`):

```text
findstr /S /N "new WebDriverWait Thread.sleep .click()" src\test\java\screens\*.java
```

Suite completa:

```text
mvn clean test
```

Resultado: todos los tests en verde, igual que antes de este avance (`Tests run: 16, Failures: 0, Errors: 0, Skipped: 1`).

Evidencia: `capturas/15-suite-completa-4-4.png`.

## Entregables

- `LoginScreen`, `HomeScreen`, `CartScreen` y `CheckoutScreen` extendiendo `BaseScreen`
- Clics y esperas resueltos con los métodos heredados
- Proyecto compilando y con los flujos existentes funcionando

## Checklist de la Entrega 2

| Entregable de la Entrega 2 | Dónde está |
|----------------------------|------------|
| Estructura de proyecto Maven configurada | `pom.xml`, paquetes `config`, `screens`, `tests` |
| Todas las Screen Objects con Page Factory | `LoginScreen`, `HomeScreen`, `CartScreen`, `CheckoutScreen` (avances 4.3 y 4.3b) |
| `BaseScreen` con elementos y métodos comunes | `BaseScreen` (esperas, tap, type, scroll, `initElements`) |
| Pruebas completas del flujo con JUnit 5 | `CompraPomTest`, `FlujoPomTest` y el resto de `tests` |
| Configuración de Appium y Desired Capabilities | `config.properties`, `ConfigReader`, `DriverFactory` |
