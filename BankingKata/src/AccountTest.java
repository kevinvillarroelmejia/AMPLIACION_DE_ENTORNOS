import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.junit.jupiter.api.Assertions.*;
class AccountTest {
    private static final String CABECERA = "Date         Amount  Balance";
    private String fechaHoy() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("d.M.yyyy"));
    }
    @Test
    void ImprimeSoloLaCabecera() {
        Account cuenta = new Account();
        assertEquals(CABECERA, cuenta.printStatement());
    }
    @Test
    void deposito500() {
        Account cuenta = new Account();
        cuenta.deposit(500);
        String esperado = CABECERA + "\n" + fechaHoy() + "   +500      500";
        assertEquals(esperado, cuenta.printStatement());
    }
    //depositamos solo 1
    @Test
    void deposito1EsElImporteMinimoValido() {
        Account cuenta = new Account();
        cuenta.deposit(1);
        String esperado = CABECERA + "\n" + fechaHoy() + "   +1      1";
        assertEquals(esperado, cuenta.printStatement());
    }
    @Test
    void deposito500YRetirada100() {
        Account cuenta = new Account();
        cuenta.deposit(500);
        cuenta.withdraw(100);
        String esperado = CABECERA
                + "\n" + fechaHoy() + "   +500      500"
                + "\n" + fechaHoy() + "   -100      400";
        assertEquals(esperado, cuenta.printStatement());
    }
}