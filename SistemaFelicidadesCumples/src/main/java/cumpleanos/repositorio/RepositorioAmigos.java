package cumpleanos.repositorio;

import cumpleanos.modelo.Amigo;

import java.util.List;



//INTERFAZ -- NO SABEMOS COMO VIENE LOS AMIGOS, ES DECIR PUEDEN SER UN TXT,sqlITE etc...
//
public interface RepositorioAmigos {
    List<Amigo> cargarTodos();
}