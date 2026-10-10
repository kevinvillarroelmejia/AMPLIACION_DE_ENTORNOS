package tddmicroexercises.telemetrysystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TelemetryDiagnosticControlsTest
{



    // TEST que conecta a la primera, se envia el mensaje y se guarda la respuesta
    @Test
    public void CheckTransmission_EnviarMensajeDiagnosticoYGuardaRespuesta()
    {
        // Creamos un cliente falso con  mock para no depender del modem real
        TelemetryClient cliente = mock(TelemetryClient.class);
        // 1.ª vez que se pregunta: offline. Desde la 2.ª: online
        when(cliente.getOnlineStatus()).thenReturn(false, true);

        // Utilizando mockito para que cuando le pidan la respuesta, devuelve este texto de prueba
        when(cliente.receive()).thenReturn("respuesta de diagnostico");


        // Le pasamos el cliente falso a la clase que queremos probar
        TelemetryDiagnosticControls controles = new TelemetryDiagnosticControls(cliente);
        try {
            controles.checkTransmission();
            verify(cliente).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
            //Con lo siguientee Comprobamos que la respuesta se guardo en diagnosticInfo
            assertEquals("respuesta de diagnostico", controles.getDiagnosticInfo());
        } catch (Exception e) {
            fail("No se esperaba excepcion: " + e.getMessage());
        }
    }







    //TEST que Nunca conecta, hace 3 intentos y error
    @Test
    public void CheckTransmission_ejecutaExcepcionSiNoConecta()
    {
        TelemetryClient cliente = mock(TelemetryClient.class);
        // El cliente siempre responde que esta desconectad
        when(cliente.getOnlineStatus()).thenReturn(false);

        TelemetryDiagnosticControls controles = new TelemetryDiagnosticControls(cliente);

        try {
            controles.checkTransmission();
            // Si llega aqui es que no salto la excepcion y el test debe fallar
            fail("Se esperaba Exception");
        } catch (Exception e) {
            assertEquals("Unable to connect.", e.getMessage());
            // Debe haber intentado conectat 3 veces
            verify(cliente, times(3)).connect("*111#");
            // Y como nunca conecto, no envio nada
            verify(cliente, never()).send(anyString());
        }
    }
    //TEST que conecta al tercer intento si falla reintenta y acaba funcionando
    @Test
    public void CheckTransmission_should_retry_until_connected()
    {
        TelemetryClient cliente = mock(TelemetryClient.class);
        // Esto seria = Desconectado, desconectado y por fin conectado
        when(cliente.getOnlineStatus()).thenReturn(false, false, true);
        when(cliente.receive()).thenReturn("respuesta de diagnostico");

        TelemetryDiagnosticControls controles = new TelemetryDiagnosticControls(cliente);

        try {
            controles.checkTransmission();
            // Fallo dos veces entonces debio llamar a connect 2 veces
            verify(cliente, times(2)).connect("*111#");
            assertEquals("respuesta de diagnostico", controles.getDiagnosticInfo());
        } catch (Exception e) {
            fail("No se esperaba excepcion: " + e.getMessage());
        }
    }
}
