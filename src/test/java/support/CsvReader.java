package support;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Lector simple de archivos CSV de datos de prueba (separados por coma, sin comillas), ubicados en
 * src/test/resources. La primera línea son los nombres de las columnas.
 */
public final class CsvReader {

    private CsvReader() {
    }

    /**
     * Lee el archivo y devuelve una fila por cada valor de la columna clave.
     *
     * @param resource  ruta dentro de resources, por ejemplo "data/usuarios.csv"
     * @param keyColumn columna cuyo valor identifica cada fila, por ejemplo "caso"
     * @return filas indexadas por la clave; cada fila es columna -> valor (vacío si la celda está vacía)
     */
    public static Map<String, Map<String, String>> read(String resource, String keyColumn) {
        try (InputStream in = CsvReader.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalArgumentException("No se encontró el archivo de datos: " + resource);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new IllegalArgumentException("El archivo de datos está vacío: " + resource);
            }
            String[] headers = split(headerLine.replace("﻿", ""));

            Map<String, Map<String, String>> rows = new LinkedHashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] values = split(line);
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i], i < values.length ? values[i].trim() : "");
                }
                rows.put(row.get(keyColumn), row);
            }
            return rows;
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer " + resource, e);
        }
    }

    /** Devuelve la fila de un caso; falla con un mensaje claro si el caso no existe. */
    public static Map<String, String> row(String resource, String keyColumn, String key) {
        Map<String, String> row = read(resource, keyColumn).get(key);
        if (row == null) {
            throw new IllegalArgumentException("No existe el caso '" + key + "' en " + resource);
        }
        return row;
    }

    private static String[] split(String line) {
        return line.split(",", -1); // -1 conserva las celdas vacías del final
    }
}
