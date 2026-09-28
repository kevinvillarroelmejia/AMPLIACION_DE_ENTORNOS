package ordenes.ejecucion;

import ordenes.documentos.Documento;
import ordenes.documentos.Movimientos;
import ordenes.documentos.Talon;
import ordenes.transferencia.OrdenDTO;
import ordenes.transferencia.SolicitudDTO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class ProcesadorOrdenesTest {


    //procesador creado para utilizar las funciones de VALIDACION Y GENERADOR DE ORDENES
    private final ProcesadorOrdenes procesador = new ProcesadorOrdenes();

    //assertEquals()
    //se utiliza cuando para comprobar dos valores lo que esperas y lo que optiones

    //assertTrue(condición)
    //basicamente comprueba si la condicion es true si es False el TEST falla

    //fail lo utilizamoc cuando probocar un excepcion a posta cuando llegue a su linea
    //es decir que si el se llega a su linea ya algo deberia estar mal por que hemos probocado algo a posta
    //SI CAPTURA LA EXCEPCION JUnit lo marca en rojo para saber donde salto


    //ESTRUCTURA CODIGO: se crean los objetos (orden) y se validan

    // ---------- Casos validos ----------
    @Test
    void ordenEnBlancoDevuelveTalonYMovimientos() {
        OrdenDTO orden = new OrdenDTO("", 1000, 0, "A1b2C", "");
        try {
            SolicitudDTO solicitud = procesador.procesar(orden);
            List<Documento> documentos = solicitud.getDocumentos();
            assertEquals(2, documentos.size());
            assertTrue(contieneTalon(documentos));
            assertTrue(contieneMovimientos(documentos));
        } catch (OrdenNoValida e) {
            fail("No se esperaba excepcion: " + e.getMessage());
        }
    }

    @Test
    void bancoValorMedioConTalonarioDevuelveSoloTalon() {
        OrdenDTO orden = new OrdenDTO("500", 5000, 50000, "B3c4D", "Talonario");
        try {
            SolicitudDTO solicitud = procesador.procesar(orden);
            List<Documento> documentos = solicitud.getDocumentos();
            assertEquals(1, documentos.size());
            assertTrue(documentos.get(0) instanceof Talon);
        } catch (OrdenNoValida e) {
            fail("excepcion N1: " + e.getMessage());
        }
    }

    @Test
    void bancoFrontera200ConMovimientosDevuelveSoloMovimientos() {
        OrdenDTO orden = new OrdenDTO("200", 1001, 1, "C5d6E", "Movimientos");
        try {
            SolicitudDTO solicitud = procesador.procesar(orden);
            List<Documento> documentos = solicitud.getDocumentos();
            assertEquals(1, documentos.size());
            assertTrue(documentos.get(0) instanceof Movimientos);
        } catch (OrdenNoValida e) {
            fail("excepcion N2:  " + e.getMessage());
        }
    }

    @Test
    void bancoFrontera201EsValido() {
        OrdenDTO orden = new OrdenDTO("201", 9998, 99998, "D7e8F", "Talonario");
        try {
            procesador.procesar(orden);
        } catch (OrdenNoValida e) {
            fail("excepcion N3:  " + e.getMessage());
        }
    }









    @Test
    void bancoFrontera998EsValido() {
        OrdenDTO orden = new OrdenDTO("998", 9999, 99999, "E9f0G", "Movimientos");
        try {
            procesador.procesar(orden);
        } catch (OrdenNoValida e) {
            fail("excepcion N4: " + e.getMessage());
        }
    }



    @Test
    void bancoFrontera999EsValido() {
        OrdenDTO orden = new OrdenDTO("999", 1000, 0, "A1b2C", "");
        try {
            procesador.procesar(orden);
        } catch (OrdenNoValida e) {
            fail("excepcion N5:  " + e.getMessage());
        }
    }

    // ---------- Casos invalidos: codigo de banco ----------

    @Test
    void bancoConPrimerDigitoMenorOIgualA1EsInvalido() {
        OrdenDTO orden = new OrdenDTO("150", 5000, 50000, "A1b2C", "Talonario");
        try {
            procesador.procesar(orden);
            fail("Se esperaba OrdenNoValida");
        } catch (OrdenNoValida e) {
            assertEquals("CODIGO_BANCO_INVALIDO", e.getMessage());
        }
    }

    @Test
    void bancoConMasDeTresDigitosEsInvalido() {
        OrdenDTO orden = new OrdenDTO("1500", 5000, 50000, "A1b2C", "Talonario");
        try {
            procesador.procesar(orden);
            fail("Se esperaba OrdenNoValida");
        } catch (OrdenNoValida e) {
            assertEquals("CODIGO_BANCO_INVALIDO", e.getMessage());
        }
    }










    @Test
    void bancoConMenosDeTresDigitosEsInvalido() {
        OrdenDTO orden = new OrdenDTO("50", 5000, 50000, "A1b2C", "Talonario");
        try {
            procesador.procesar(orden);
            fail("Se esperaba OrdenNoValida");
        } catch (OrdenNoValida e) {
            assertEquals("CODIGO_BANCO_INVALIDO", e.getMessage());
        }
    }



    @Test
    void bancoFrontera199EsInvalido() {
        OrdenDTO orden = new OrdenDTO("199", 5000, 50000, "A1b2C", "Talonario");
        try {
            procesador.procesar(orden);
            fail("Se esperaba OrdenNoValida");
        } catch (OrdenNoValida e) {
            assertEquals("CODIGO_BANCO_INVALIDO", e.getMessage());
        }
    }

    // ---------- Casos invalidos con  codigos de sucursal INVALIDOS ----------

    @Test
    void sucursalConMenosDeCuatroDigitosEsInvalida() {
        OrdenDTO orden = new OrdenDTO("500", 500, 50000, "A1b2C", "Talonario");
        try {
            procesador.procesar(orden);
            fail("Se esperaba OrdenNoValida");
        } catch (OrdenNoValida e) {
            assertEquals("CODIGO_SUCURSAL_INVALIDO", e.getMessage());
        }
    }

    @Test
    void sucursalConMasDeCuatroDigitosEsInvalida() {
        OrdenDTO orden = new OrdenDTO("500", 50000, 50000, "A1b2C", "Talonario");
        try {
            procesador.procesar(orden);
            fail("Se esperaba OrdenNoValida");
        } catch (OrdenNoValida e) {
            assertEquals("CODIGO_SUCURSAL_INVALIDO", e.getMessage());
        }
    }








    // ---------- Casos invalidos: numero de cuenta INVALIDOS ----------

    @Test
    void cuentaPorEncimaDeCincoDigitosEsInvalida() {
        OrdenDTO orden = new OrdenDTO("500", 5000, 500000, "A1b2C", "Talonario");
        try {
            procesador.procesar(orden);
            fail("Se esperaba OrdenNoValida");
        } catch (OrdenNoValida e) {
            assertEquals("NUMERO_CUENTA_INVALIDO", e.getMessage());
        }
    }



    @Test
    void cuentaNegativaEsInvalida() {
        OrdenDTO orden = new OrdenDTO("500", 5000, -1, "A1b2C", "Talonario");
        try {
            procesador.procesar(orden);
            fail("Se esperaba OrdenNoValida");
        } catch (OrdenNoValida e) {
            assertEquals("NUMERO_CUENTA_INVALIDO", e.getMessage());
        }
    }
    // ---------- Casos invalidos: clave personal ===================
    @Test
    void claveConMenosDeCincoCaracteresEsInvalida() {
        OrdenDTO orden = new OrdenDTO("500", 5000, 50000, "A1b2", "Talonario");
        try {
            procesador.procesar(orden);
            fail("Se esperaba OrdenNoValida");
        } catch (OrdenNoValida e) {
            assertEquals("CLAVE_INVALIDA", e.getMessage());
        }
    }



    @Test
    void claveConMasDeCincoCaracteresEsInvalida() {
        OrdenDTO orden = new OrdenDTO("500", 5000, 50000, "A1b2C3", "Talonario");
        try {
            procesador.procesar(orden);
            fail("Se esperaba OrdenNoValida");
        } catch (OrdenNoValida e) {
            assertEquals("CLAVE_INVALIDA", e.getMessage());
        }
    }






    @Test
    void claveConCaracterNoAlfanumericoEsInvalida() {
        OrdenDTO orden = new OrdenDTO("500", 5000, 50000, "A1b2!", "Talonario");
        try {
            procesador.procesar(orden);
            fail("Se esperaba OrdenNoValida");
        } catch (OrdenNoValida e) {
            assertEquals("CLAVE_INVALIDA", e.getMessage());
        }
    }

    // ---------- Casos invalidos: orden ----------

    @Test
    void tipoOrdenDesconocidoEsInvalido() {
        OrdenDTO orden = new OrdenDTO("500", 5000, 50000, "A1b2C", "Cheques");
        try {
            procesador.procesar(orden);
            fail("Se esperaba OrdenNoValida");
        } catch (OrdenNoValida e) {
            assertEquals("ORDEN_INVALIDA", e.getMessage());
        }
    }

    // ---------- Metodos de apoyo (evitan usar streams para recorrer la lista) ----------

    private boolean contieneTalon(List<Documento> documentos) {
        boolean encontrado = false;
        for (Documento documento : documentos) {
            if (documento instanceof Talon) {
                encontrado = true;
            }
        }
        return encontrado;
    }










    private boolean contieneMovimientos(List<Documento> documentos) {
        boolean encontrado = false;
        for (Documento documento : documentos) {
            if (documento instanceof Movimientos) {
                encontrado = true;
            }
        }
        return encontrado;
    }
}
