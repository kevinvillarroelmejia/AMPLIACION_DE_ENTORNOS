package servicio;

import cumpleanos.mensajeria.ServicioMensajeria;
import cumpleanos.modelo.Amigo;
import cumpleanos.modelo.Mensaje;
import cumpleanos.repositorio.RepositorioAmigos;
import cumpleanos.servicio.ServicioFelicitaciones;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ServicioFelicitacionesTest {

    //dejamos la fecha en la fecha de hoy 14/10/2000
    private final LocalDate hoy = LocalDate.of(2026, 10, 14);

    //objetos mocks de las interfaces -- creando objetos falsos
    private final RepositorioAmigos repositorio = mock(RepositorioAmigos.class);
    private final ServicioMensajeria mensajeria = mock(ServicioMensajeria.class);


    private final ServicioFelicitaciones servicio = new ServicioFelicitaciones(repositorio, mensajeria);

    // este amigo cumple hoy -- basicamente un metodo para no repetir el constructor
    private Amigo luis() {
        return new Amigo("Perez", "Luis", LocalDate.of(2000, 10, 14), "luis.perez@foobar.com");
    }

    // este no cumple hoy --- con este lo mismo que el anterior
    private Amigo john() {
        return new Amigo("Doe", "John", LocalDate.of(1982, 10, 8), "john.doe@foobar.com");
    }


    /*==TESTING===*/


    @Test
    void LuisCumple_seEnviaUnMensaje() {
        List<Amigo> amigos = new ArrayList<>();

        amigos.add(luis());

        when(repositorio.cargarTodos()).thenReturn(amigos);

        servicio.enviarFelicitaciones(hoy);

        //verificamos que se llamo  SOLO UNA vez a enviar
        verify(mensajeria, times(1)).enviar(any(Mensaje.class));
    }

    @Test
    void nadieCumpleHoy_noEnviarNada() {
        List<Amigo> amigos = new ArrayList<>();
        amigos.add(john());

        when(repositorio.cargarTodos()).thenReturn(amigos);

        servicio.enviarFelicitaciones(hoy);

        //esto seria lo mismo que el anterior, solo verificamos que nunca se uso --> enviar
        verify(mensajeria, never()).enviar(any(Mensaje.class));
    }





    @Test
    void dosAmigosCumplenElMismoDia_seEnvianDosMensajes() {
        List<Amigo> amigos = new ArrayList<>();
        amigos.add(luis());
        amigos.add(new Amigo("Ann", "Mary", LocalDate.of(1990, 10, 14), "mary.ann@foobar.com"));
        amigos.add(john());
        when(repositorio.cargarTodos()).thenReturn(amigos);

        servicio.enviarFelicitaciones(hoy);

        //cumplen dos, verificamos que se felicita dos veces
        verify(mensajeria, times(2)).enviar(any(Mensaje.class));
    }



    @Test
    void listaVacia_noSeEnviaNada() {
        ArrayList<Amigo> listaVacia=new ArrayList<>();
        when(repositorio.cargarTodos()).thenReturn(listaVacia);

        servicio.enviarFelicitaciones(hoy);

        //como la lista esta vacia NUNCA se deberia felicitar-enviar
        verify(mensajeria, never()).enviar(any(Mensaje.class));
    }

    //Se me ocurri este test
    //Que comprueba si el formato del mensaje es el correcto
    @Test
    void elMensajeTieneElFormatoCorrecto() {
        List<Amigo> amigos = new ArrayList<>();
        amigos.add(luis());
        when(repositorio.cargarTodos()).thenReturn(amigos);

        servicio.enviarFelicitaciones(hoy);

        //se atrapa el mensaje con ArgumentCaptor
        ArgumentCaptor<Mensaje> captor = ArgumentCaptor.forClass(Mensaje.class);
        verify(mensajeria).enviar(captor.capture());
        Mensaje enviado = captor.getValue();

        //verificamos que el mensaje enviado tiene el formato correcto
        assertEquals("luis.perez@foobar.com", enviado.getDestinatario());
        assertEquals("¡Feliz cumpleaños!", enviado.getAsunto());
        assertEquals("¡Feliz cumpleaños, querido/a Luis!", enviado.getCuerpo());
    }
}