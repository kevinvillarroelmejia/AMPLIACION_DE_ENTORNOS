package modelo;

import cumpleanos.modelo.Amigo;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AmigoTest {

    private final Amigo amigoBisiesto = new Amigo("Lopez", "Ana", LocalDate.of(2000, 2, 29), "ana.lopez@foobar.com");

    private final Amigo amigo = new Amigo("Doe", "John", LocalDate.of(1982, 10, 8), "john.doe@foobar.com");



    //este test tiene que salir TRUE por que se cumple tanto el dia como el mes
    @Test
    void esSuCumpleanosCuandoCoincidenDiaYMes() {
        //toenm
        assertTrue(amigo.esSuCumpleanos(LocalDate.of(2026, 10, 8)));
    }


    //tiene que dar FALSe por que se cumple el mes pero no el dia
    @Test
    void noEsSuCumpleanosOtroDia() {
        assertFalse(amigo.esSuCumpleanos(LocalDate.of(2026, 10, 9)));
    }


    //FALSE por quee se cumple el dia pero no el mes
    @Test
    void noEsSuCumpleanosOtroMes() {
        assertFalse(amigo.esSuCumpleanos(LocalDate.of(2026, 11, 8)));
    }


    //test para los años BIESTOS
    @Test
    void nacidoElBisiestoSeFelicitaEl29EnAnioBisiesto() {
        assertTrue(amigoBisiesto.esSuCumpleanos(LocalDate.of(2028, 2, 29)));
    }
    @Test
    void nacidoElBisiestoSeFelicitaEl28EnAnioNoBisiesto() {
        assertTrue(amigoBisiesto.esSuCumpleanos(LocalDate.of(2027, 2, 28)));
    }
}


