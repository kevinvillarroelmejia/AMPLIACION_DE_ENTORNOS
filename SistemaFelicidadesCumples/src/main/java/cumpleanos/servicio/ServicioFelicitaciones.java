package cumpleanos.servicio;

import cumpleanos.mensajeria.ServicioMensajeria;
import cumpleanos.modelo.Amigo;
import cumpleanos.modelo.Mensaje;
import cumpleanos.repositorio.RepositorioAmigos;

import java.time.LocalDate;
import java.util.List;

/*
* la logica seria como-- extraemos la fecha de los cumpleaños y vemos quien cumple hoy
*
* */
public class ServicioFelicitaciones {

    private  RepositorioAmigos repositorio;
    private  ServicioMensajeria mensajeria;


    //constructor que recibe
    //lista de amigos
    //servicio de mensajeria - enviar
    public ServicioFelicitaciones(RepositorioAmigos repositorio, ServicioMensajeria mensajeria) {
        this.repositorio = repositorio;
        this.mensajeria = mensajeria;
    }

    //RECORRE LOS AMIGOS Y FELICITA A LOS QUE CUMPLEN HOY
    public void enviarFelicitaciones(LocalDate hoy) {
        List<Amigo> amigos = repositorio.cargarTodos();
        for (Amigo amigo : amigos) {
            if (amigo.esSuCumpleanos(hoy)) {
                Mensaje mensaje = crearFelicitacion(amigo);
                mensajeria.enviar(mensaje);
            }
        }
    }

    //CONSTRUYE EL MENSAJE CON EL FORMATO QUE PIDE EL ENUNCIADO
    private Mensaje crearFelicitacion(Amigo amigo) {
        String asunto = "¡Feliz cumpleaños!";
        String cuerpo = "¡Feliz cumpleaños, querido/a " + amigo.getNombre() + "!";
        return new Mensaje(amigo.getEmail(), asunto, cuerpo);
    }
}