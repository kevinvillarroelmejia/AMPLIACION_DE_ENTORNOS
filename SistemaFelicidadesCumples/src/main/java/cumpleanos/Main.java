package cumpleanos;

import cumpleanos.mensajeria.ServicioEmail;
import cumpleanos.repositorio.RepositorioAmigosFichero;
import cumpleanos.servicio.ServicioFelicitaciones;

import java.time.LocalDate;



/*
* pongo a luis perez que cumple hoy para ver si funciona
* */
public class Main {

    public static void main(String[] args) {

        //lista de amigos
        RepositorioAmigosFichero repositorio = new RepositorioAmigosFichero("amigos.txt");

        //servicio de mensajeria
        ServicioEmail mensajeria = new ServicioEmail();



        //objeto creado con la lista de amigos y el servicio de mesajeria
        ServicioFelicitaciones servicio = new ServicioFelicitaciones(repositorio, mensajeria);

        //recorre el fichero comprueba que amigos cumplen hoy años
        servicio.enviarFelicitaciones(LocalDate.now());
    }
}