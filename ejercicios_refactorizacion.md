# Refactorización: ejercicios 7 a 10

## Ejercicio 7: Código Duplicado

### Code smells

-  se repite el if donde solo cambia el objeto que se le pasa 

### Código original

```java
public void guardarEstudiante(Estudiante estudiante) {
    if (estudiante == null) {
        throw new IllegalArgumentException();
    }
    validarEstudiante(estudiante);
    repositorio.save(estudiante);
    logger.info("Estudiante guardado");
}

public void guardarProfesor(Profesor profesor) {
    if (profesor == null) {
        throw new IllegalArgumentException();
    }
    validarProfesor(profesor);
    repositorio.save(profesor);
    logger.info("Profesor guardado");
}
```

### Código refactorizado

```java
// metodo donde se unifica los demas metodos
public void guardarEstudiante(Estudiante estudiante) {
    validarNoNulo(estudiante);
    validarEstudiante(estudiante);
    guardarYRegistrar(estudiante, "Estudiante guardado");
}

public void guardarProfesor(Profesor profesor) {
    validarNoNulo(profesor);
    validarProfesor(profesor);
    guardarYRegistrar(profesor, "Profesor guardado");
}

// laanza una excepción si el objeto recibido es null.
private void validarNoNulo(Object entidad) {
    if (entidad == null) {
        throw new IllegalArgumentException();
    }
}

// guarda el objeto en el repositorio y escribe el mensaje
private void guardarYRegistrar(Object entidad, String mensaje) {
    repositorio.save(entidad);
    logger.info(mensaje);
}
```

---
## Ejercicio 8: Método Largo

### Code smells

-  el metodo procesarPedido es demasiado largo tiene demasiada responsabilidad
### Código original

```java
public void procesarPedido(Pedido pedido) {
    // Validar cliente
    if (pedido.getCliente() == null) {
        logger.error("Cliente nulo");
        return;
    }

    // Validar dirección
    if (pedido.getDireccion() == null || pedido.getDireccion().isBlank()) {
        logger.error("Dirección vacía");
        return;
    }

    // Calcular totales
    BigDecimal subtotal = BigDecimal.ZERO;
    for (Item item : pedido.getItems()) {
        subtotal = subtotal.add(item.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
    }

    // Aplicar descuento
    BigDecimal descuento = BigDecimal.ZERO;
    if (pedido.getCliente().tieneDescuento()) {
        descuento = subtotal.multiply(new BigDecimal("0.10"));
    }

    // Calcular IVA
    BigDecimal iva = subtotal.subtract(descuento).multiply(new BigDecimal("0.21"));
    BigDecimal total = subtotal.subtract(descuento).add(iva);

    // Guardar pedido
    repositorio.guardar(pedido);

    // Enviar email
    emailService.enviar(pedido.getCliente().getEmail(), "Pedido procesado");
}
```

### Código refactorizado

```java
public void procesarPedido(Pedido pedido) {
    if (!validarPedido(pedido)) {
        return;
    }
    BigDecimal total = calcularTotal(pedido);
    guardarYNotificar(pedido);
}

// Se me hace raro utilizar varios return, pero veo que aqui es muy necesario
private boolean validarPedido(Pedido pedido) {
    if (pedido.getCliente() == null) {
        logger.error("Cliente nulo");
        return false;
    }
    if (pedido.getDireccion() == null || pedido.getDireccion().isBlank()) {
        logger.error("Dirección vacía");
        return false;
    }
    return true;
}

// calcula el total
private BigDecimal calcularSubtotal(Pedido pedido) {
    BigDecimal subtotal = BigDecimal.ZERO;
    for (Item item : pedido.getItems()) {
        subtotal = subtotal.add(item.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));
    }
    return subtotal;
}

// ========= devuelve el 10% del subtotal
private BigDecimal calcularDescuento(Pedido pedido, BigDecimal subtotal) {
    BigDecimal descuento = BigDecimal.ZERO;
    if (pedido.getCliente().tieneDescuento()) {
        descuento = subtotal.multiply(new BigDecimal("0.10"));
    }
    return descuento;
}

// === Calcula el total del pedido menos descuento más el 21% de IVA.
private BigDecimal calcularTotal(Pedido pedido) {
    BigDecimal subtotal = calcularSubtotal(pedido);
    BigDecimal baseImponible = subtotal.subtract(calcularDescuento(pedido, subtotal));
    BigDecimal iva = baseImponible.multiply(new BigDecimal("0.21"));
    return baseImponible.add(iva);
}

// guarda el pedido en el repositorio y envia un email
private void guardarYNotificar(Pedido pedido) {
    repositorio.guardar(pedido);
    emailService.enviar(pedido.getCliente().getEmail(), "Pedido procesado");
}
```

---
## Ejercicio 9: Magic Numbers

### Code smells

-  el  0.21, el 18 y los días 30, 7 y 1 salen sin contexto alguno

### Código original

```java
public class Calculadora {
    public BigDecimal calcularImpuesto(BigDecimal monto) {
        return monto.multiply(new BigDecimal("0.21"));
    }

    public boolean esMayorDeEdad(int edad) {
        return edad >= 18;
    }

    public int obtenerDiasPlazo(String tipo) {
        return switch (tipo) {
            case "normal" -> 30;
            case "urgente" -> 7;
            case "express" -> 1;
            default -> 30;
        };
    }
}
```

### Código refactorizado

```java
import java.math.BigDecimal;

public class Calculadora {

    private static final BigDecimal TIPO_IVA = new BigDecimal("0.21");
    private static final int MAYORIA_EDAD = 18;

    public enum TipoPlazo {
        NORMAL(30),
        URGENTE(7),
        EXPRESS(1);

        private final int dias;

        TipoPlazo(int dias) {
            this.dias = dias;
        }

        public int getDias() {
            return dias;
        }
    }

    // Calcula el impuesto (IVA) de un monto.
    public BigDecimal calcularImpuesto(BigDecimal monto) {
        return monto.multiply(TIPO_IVA);
    }

    // Indica si la edad alcanza la mayoría de edad.
    public boolean esMayorDeEdad(int edad) {
        return edad >= MAYORIA_EDAD;
    }

    // Devuelve los días de plazo que corresponden al tipo indicado.
    public int obtenerDiasPlazo(TipoPlazo tipo) {
        return tipo.getDias();
    }
}
```

### Qué se ha hecho

- Los números sueltos pasan a constantes con nombre (TIPO_IVA, MAYORIA_EDAD).
- Los textos `"normal"`, `"urgente"` y `"express"` pasan a un enum, y cada opción lleva sus días
- El switch desaparece. Con un enum ya no hace falta el default

| Antes                    | Después             |
| ------------------------ | ------------------- |
| `new BigDecimal("0.21")` | `TIPO_IVA`          |
| `18`                     | `MAYORIA_EDAD`      |
| `"normal"` / `30`        | `TipoPlazo.NORMAL`  |
| `"urgente"` / `7`        | `TipoPlazo.URGENTE` |
| `"express"` / `1`        | `TipoPlazo.EXPRESS` |

---

## Ejercicio 10: Nombres Pobres

### Code smells

-  la clase `C`, el método `p` y las variables `a`, `b`, `x` e `y` no dicen qué hacen
- Hay que leer el código entero para entender que calcula.

### Código original

```java
public class C {
    public int p(int a, int b) {
        int x = a + b;
        int y = a - b;
        return x * y;
    }
}
```

### Código refactorizado

```java
public class Operaciones {

    // Multiplica la suma de dos valores por su resta.
    public int multiplicarSumaPorResta(int valorA, int valorB) {
        int suma = valorA + valorB;
        int resta = valorA - valorB;
        return suma * resta;
    }
}
```
