# Datos de prueba desde un archivo CSV

## Objetivo

Que los datos del `Scenario Outline` de login (usuario, contraseña y resultado esperado) salgan de un archivo externo y no estén escritos dentro del `.feature`.

## El archivo de datos

`src/test/resources/data/usuarios.csv`

```text
caso,usuario,contrasena,resultado
usuario_valido,bod@example.com,10203040,catálogo
usuario_bloqueado,alice@example.com,10203040,usuario bloqueado
sin_usuario,,10203040,usuario obligatorio
sin_contrasena,bod@example.com,,mensaje de error
clave_incorrecta,bod@example.com,clave-incorrecta,mensaje de error
```

- Cada fila es un caso identificado por la columna `caso`.
- Una celda vacía significa "no completar ese campo".
- El archivo está en UTF-8 (tiene tildes).

## Cómo lo usa el escenario

El `Examples` del `Scenario Outline` solo lista los nombres de los casos. Los datos se buscan en el CSV durante la ejecución:

```text
Scenario Outline: Inicio de sesión con múltiples usuarios
  When ingresa las credenciales del caso "<caso>"
  And presiona el botón de iniciar sesión
  Then debería ver el resultado esperado del caso "<caso>"

  Examples: Casos que la app resuelve correctamente
    | caso              |
    | usuario_valido    |
    | usuario_bloqueado |
    | sin_usuario       |
    | sin_contrasena    |
```

## Cómo se lee

`support/CsvReader` abre el archivo desde `resources`, toma la primera línea como nombres de columna y devuelve cada fila como un mapa columna → valor:

```text
Map<String, String> datos = CsvReader.row("data/usuarios.csv", "caso", "sin_usuario");
datos.get("usuario");     // ""
datos.get("contrasena");  // "10203040"
datos.get("resultado");   // "usuario obligatorio"
```

Si el archivo o el caso no existen, falla con un mensaje claro. No necesita dependencias nuevas.

## Pasos

| Paso | Qué hace |
|------|----------|
| `ingresa las credenciales del caso {string}` | Lee el caso del CSV y completa usuario y contraseña (omite los vacíos) |
| `debería ver el resultado esperado del caso {string}` | Lee el resultado del CSV y lo verifica (catálogo, usuario bloqueado, usuario obligatorio o mensaje de error) |

## Agregar un caso nuevo

1. Agregar una fila al CSV con un nombre de caso nuevo.
2. Agregar ese nombre a la tabla `Examples`.

No hace falta tocar código mientras el resultado sea uno de los cuatro existentes.

## Cómo ejecutar

```text
mvn clean test "-Dtest=CucumberTestRunner"
```

Resultado esperado: los mismos 13 escenarios en verde y 3 salteados (`@known-issue`).

## Evidencia

`capturas/19-cucumber-datos-csv.png`: consola con los escenarios en verde.
