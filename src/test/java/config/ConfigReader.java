package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final String ARCHIVO = "config.properties";
    private static final Properties properties = cargar();

    private ConfigReader() {
    }

    private static Properties cargar() {
        Properties props = new Properties();
        try (InputStream entrada = ConfigReader.class.getClassLoader().getResourceAsStream(ARCHIVO)) {
            if (entrada == null) {
                throw new IllegalStateException("No se encontró " + ARCHIVO + " en src/test/resources");
            }
            props.load(entrada);
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer " + ARCHIVO, e);
        }
        return props;
    }

    public static String get(String clave) {
        String valor = properties.getProperty(clave);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Falta la propiedad '" + clave + "' en " + ARCHIVO);
        }
        return valor.trim();
    }
}