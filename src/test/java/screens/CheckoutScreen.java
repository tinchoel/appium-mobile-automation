package screens;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Checkout: envío, pago, revisión del pedido y confirmación. Elementos declarados con Page Factory.
 * Los campos se buscan recién cuando se usan, así que varias pantallas pueden compartir esta clase
 * aunque algunos identificadores se repitan (nameET, paymentBtn).
 */
public class CheckoutScreen extends BaseScreen {

    // --- Envío ---
    @AndroidFindBy(id = ID_PREFIX + "fullNameET")
    private WebElement fullNameField;

    @AndroidFindBy(id = ID_PREFIX + "address1ET")
    private WebElement addressField;

    @AndroidFindBy(id = ID_PREFIX + "cityET")
    private WebElement cityField;

    @AndroidFindBy(id = ID_PREFIX + "stateET")
    private WebElement stateField;

    @AndroidFindBy(id = ID_PREFIX + "zipET")
    private WebElement zipField;

    @AndroidFindBy(id = ID_PREFIX + "countryET")
    private WebElement countryField;

    // --- Pago (nameET es el titular de la tarjeta; en el Login es el usuario) ---
    @AndroidFindBy(id = ID_PREFIX + "nameET")
    private WebElement cardHolderField;

    @AndroidFindBy(id = ID_PREFIX + "cardNumberET")
    private WebElement cardNumberField;

    @AndroidFindBy(id = ID_PREFIX + "expirationDateET")
    private WebElement expirationField;

    @AndroidFindBy(id = ID_PREFIX + "securityCodeET")
    private WebElement securityCodeField;

    // --- Botón principal: es el mismo en las tres pantallas y solo cambia su texto ---
    @AndroidFindBy(id = ID_PREFIX + "paymentBtn")
    private WebElement paymentButton;

    // --- Revisión del pedido ---
    @AndroidFindBy(id = ID_PREFIX + "fullNameTV")
    private WebElement reviewName;

    @AndroidFindBy(id = ID_PREFIX + "addressTV")
    private WebElement reviewAddress;

    @AndroidFindBy(id = ID_PREFIX + "cityTV")
    private WebElement reviewCity;

    @AndroidFindBy(id = ID_PREFIX + "countryTV")
    private WebElement reviewCountry;

    @AndroidFindBy(id = ID_PREFIX + "cardHolderTV")
    private WebElement reviewCardHolder;

    @AndroidFindBy(id = ID_PREFIX + "itemNumberTV")
    private WebElement reviewItems;

    @AndroidFindBy(id = ID_PREFIX + "totalAmountTV")
    private WebElement reviewTotal;

    // --- Confirmación ---
    @AndroidFindBy(id = ID_PREFIX + "completeTV")
    private WebElement completeTitle;

    @AndroidFindBy(id = ID_PREFIX + "thankYouTV")
    private WebElement thankYouMessage;

    public CheckoutScreen(AndroidDriver driver, WebDriverWait wait) {
        super(driver, wait);
    }

    // ---------- Envío ----------

    public void enterShippingAddress(String fullName, String address, String city,
                                     String state, String zip, String country) {
        type(fullNameField, fullName);
        type(addressField, address);
        type(cityField, city);
        type(stateField, state);
        type(zipField, zip);
        type(countryField, country);
        hideKeyboard();
    }

    public void clickToPayment() {
        goToNextStep("To Payment");
    }

    // ---------- Pago ----------

    public void enterPaymentDetails(String holder, String cardNumber, String expiry, String securityCode) {
        type(cardHolderField, holder);
        type(cardNumberField, cardNumber);
        type(expirationField, expiry);
        type(securityCodeField, securityCode);
        hideKeyboard();
    }

    public void clickReviewOrder() {
        goToNextStep("Review Order");
    }

    // ---------- Revisión del pedido ----------

    public String getShippingName() {
        return readReview(reviewName, "fullNameTV");
    }

    public String getShippingAddress() {
        return readReview(reviewAddress, "addressTV");
    }

    public String getShippingCity() {
        return readReview(reviewCity, "cityTV");
    }

    public String getShippingCountry() {
        return readReview(reviewCountry, "countryTV");
    }

    public String getCardHolder() {
        return readReview(reviewCardHolder, "cardHolderTV");
    }

    public String getItemCount() {
        return readReview(reviewItems, "itemNumberTV");
    }

    public String getTotalAmount() {
        return readReview(reviewTotal, "totalAmountTV");
    }

    public void clickPlaceOrder() {
        goToNextStep("Place Order");
    }

    // ---------- Confirmación ----------

    public String getConfirmationTitle() {
        return textOf(completeTitle);
    }

    public String getThankYouMessage() {
        return textOf(thankYouMessage);
    }

    // ---------- Métodos internos ----------

    /**
     * paymentBtn es el mismo ID en las tres pantallas: se espera por el texto esperado (confirma que
     * cargó la pantalla correcta), se hace scroll por si queda fuera de pantalla y se toca.
     */
    private void goToNextStep(String buttonText) {
        waitForText(paymentButton, buttonText);
        scrollToId("paymentBtn");
        tap(paymentButton);
    }

    /** Los datos de la revisión pueden quedar fuera de pantalla: scroll hasta el elemento y lectura de su texto. */
    private String readReview(WebElement element, String idName) {
        scrollToId(idName);
        return textOf(element);
    }
}