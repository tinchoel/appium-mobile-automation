package config;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Paths;

public class DriverFactory {

    private DriverFactory() {
    }

    public static UiAutomator2Options crearOpciones() {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName(ConfigReader.get("platformName"));
        options.setAutomationName(ConfigReader.get("automationName"));
        options.setDeviceName(ConfigReader.get("deviceName"));
        options.setUdid(ConfigReader.get("udid"));
        options.setApp(Paths.get(ConfigReader.get("app")).toAbsolutePath().toString());
        options.setAppPackage(ConfigReader.get("appPackage"));
        options.setAppActivity(ConfigReader.get("appActivity"));
        options.setAppWaitActivity(ConfigReader.get("appWaitActivity"));
        return options;
    }

    public static AndroidDriver crearDriver() throws MalformedURLException {
        return new AndroidDriver(URI.create(ConfigReader.get("appiumUrl")).toURL(), crearOpciones());
    }
}