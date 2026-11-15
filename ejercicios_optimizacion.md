# Optimización: ejercicios 11 a 15

## Ejercicio 11: Switch vs Pattern Matching

### Problemas del código original

- **Datos mezclados con la lógica:** los números (1, 2, 3, 4) y los nombres ("Admin", "Gestor"...) están dentro del `switch`.
- Si se añade un tipo nuevo, hay que tocar el `switch`.

### Código original

```java
public String getTipo(Usuario usuario) {
    return switch (usuario.getTipo()) {
        case 1 -> "Admin";
        case 2 -> "Gestor";
        case 3 -> "Usuario";
        case 4 -> "Invitado";
        default -> "Desconocido";
    };
}
```

### Código optimizado

```java
public enum TipoUsuario {
    ADMIN(1, "Admin"),
    GESTOR(2, "Gestor"),
    USUARIO(3, "Usuario"),
    INVITADO(4, "Invitado");

    private final int codigo;
    private final String nombre;

    TipoUsuario(int codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    // Devuelve el nombre del tipo que tiene ese código, o "Desconocido" si no existe.
    public static String nombrePorCodigo(int codigo) {
        String resultado = "Desconocido";
        for (TipoUsuario tipo : values()) {
            if (tipo.codigo == codigo) {
                resultado = tipo.nombre;
            }
        }
        return resultado;
    }
}

// Devuelve el nombre del tipo de un usuario.
public String getTipo(Usuario usuario) {
    return TipoUsuario.nombrePorCodigo(usuario.getTipo());
}
```

### Qué se ha hecho

- Cada tipo pasa a ser una opción del enum con su código y su nombre.
- `getTipo` ya no tiene `switch`: pregunta al enum.
- Si no existe el código, sigue devolviendo "Desconocido", igual que el `default` original.

---

## Ejercicio 12: Bucles vs Streams

### Problemas del código original

- **Bucle largo para algo simple:** hay que crear la lista, el índice `i` y el `if` a mano.
- Se llama a `numeros.get(i)` dos veces.

### Código original

```java
public List<Integer> getPares(List<Integer> numeros) {
    List<Integer> resultado = new ArrayList<>();
    for (int i = 0; i < numeros.size(); i++) {
        if (numeros.get(i) % 2 == 0) {
            resultado.add(numeros.get(i));
        }
    }
    return resultado;
}
```

### Código optimizado

```java
import java.util.List;
import java.util.stream.Collectors;

// Devuelve una lista solo con los números pares.
public List<Integer> getPares(List<Integer> numeros) {
    return numeros.stream()
            .filter(numero -> numero % 2 == 0)
            .collect(Collectors.toList());
}
```

### Qué se ha hecho

- `stream()` recorre la lista, `filter` se queda con los pares y `collect` los junta en una lista nueva.
- Se usa `Collectors.toList()` y no `.toList()` para que la lista devuelta siga siendo modificable, como la del original.

---

## Ejercicio 13: Map vs Switch

### Problemas del código original

- **Switch largo con datos:** el `switch` es una tabla de país → precio escrita como código.
- Añadir un país obliga a modificar el método.

### Código original

```java
public int getPrecioEnvio(String pais) {
    return switch (pais.toLowerCase()) {
        case "españa" -> 5;
        case "francia" -> 8;
        case "alemania" -> 10;
        case "italia" -> 7;
        case "portugal" -> 4;
        case "reino unido" -> 12;
        case "belgica" -> 9;
        case "paises bajos" -> 11;
        default -> 15;
    };
}
```

### Código optimizado

```java
import java.util.Map;

private static final int PRECIO_ENVIO_DEFECTO = 15;

private static final Map<String, Integer> PRECIOS_ENVIO = Map.of(
        "españa", 5,
        "francia", 8,
        "alemania", 10,
        "italia", 7,
        "portugal", 4,
        "reino unido", 12,
        "belgica", 9,
        "paises bajos", 11
);

// Devuelve el precio de envío del país, o el precio por defecto si no está en la tabla.
public int getPrecioEnvio(String pais) {
    return PRECIOS_ENVIO.getOrDefault(pais.toLowerCase(), PRECIO_ENVIO_DEFECTO);
}
```

### Qué se ha hecho

- Los países y sus precios pasan a un `Map`, que se declara una sola vez.
- `getOrDefault` busca el país y, si no está, devuelve el 15 de antes (`default`).
- `Map.of` admite hasta 10 pares, y aquí hay 8.

---

## Ejercicio 14: Null Checking

### Problemas del código original

- **Varios `if` seguidos** solo para comprobar nulls.
- Hay dos casos distintos: usuario null ("Anónimo") y nombre null ("Sin nombre").

### Código original

```java
public String getNombre(Usuario usuario) {
    if (usuario == null) {
        return "Anónimo";
    }

    if (usuario.getNombre() == null) {
        return "Sin nombre";
    }

    return usuario.getNombre();
}
```

### Código optimizado

```java
import java.util.Optional;

// Devuelve el nombre del usuario, "Anónimo" si el usuario es null o "Sin nombre" si no tiene nombre.
public String getNombre(Usuario usuario) {
    return Optional.ofNullable(usuario)
            .map(u -> Optional.ofNullable(u.getNombre()).orElse("Sin nombre"))
            .orElse("Anónimo");
}
```

### Qué se ha hecho

- `Optional.ofNullable(usuario)` envuelve al usuario: si es null, queda vacío y el `orElse("Anónimo")` del final se aplica.
- Si el usuario existe, `map` mira su nombre con otro `Optional`: si el nombre es null devuelve "Sin nombre".
- No sirve `map(Usuario::getNombre).orElse(...)` con un solo mensaje, porque se perdería la diferencia entre "Anónimo" y "Sin nombre".

---

## Ejercicio 15: Construcción de cadenas

### Problemas del código original

- **Muchas concatenaciones con `+`:** la frase es difícil de leer y es fácil olvidar un espacio.

### Código original

```java
String mensaje = "El usuario " + usuario.getNombre() + " con ID " + usuario.getId()
        + " ha realizado " + pedido.getCantidad() + " compras por un total de " + pedido.getTotal() + " euros.";
```

### Código optimizado con String.format

```java
String mensaje = String.format(
        "El usuario %s con ID %s ha realizado %s compras por un total de %s euros.",
        usuario.getNombre(), usuario.getId(), pedido.getCantidad(), pedido.getTotal());
```

### Código optimizado con StringBuilder

```java
StringBuilder mensaje = new StringBuilder();
mensaje.append("El usuario ").append(usuario.getNombre())
        .append(" con ID ").append(usuario.getId())
        .append(" ha realizado ").append(pedido.getCantidad())
        .append(" compras por un total de ").append(pedido.getTotal())
        .append(" euros.");
```

### Qué se ha hecho

- Con `String.format` la frase se lee entera y los datos van al final, en el mismo orden que los `%s`.
- Se usa `%s` en todos los huecos para que el resultado sea idéntico a la concatenación, sea cual sea el tipo de cada dato.
- Con `StringBuilder`, cada `append` añade un trozo y no se crean cadenas intermedias. Para obtener el texto final hay que usar `mensaje.toString()`.
