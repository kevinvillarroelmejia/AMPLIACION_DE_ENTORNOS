package cumpleanos.repositorio;

import cumpleanos.modelo.Amigo;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//clase para leer la lista de amigos y devolverlo como lista
public class RepositorioAmigosFichero implements RepositorioAmigos {

    private  String rutaFichero;

    public RepositorioAmigosFichero(String rutaFichero) {
        this.rutaFichero = rutaFichero;
    }


    @Override
    public List<Amigo> cargarTodos() {
        List<Amigo> amigos = new ArrayList<>();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy/M/d");

        try {
            Scanner lector = new Scanner(new File(rutaFichero));
            boolean primeraLinea = true;

            while (lector.hasNextLine()) {
                String linea = lector.nextLine();

                if (primeraLinea==true) {
                    //Como la primera primera linea es la cabecera es decir nunca hay nada la saltamos
                    primeraLinea = false;
                } else if (!linea.trim().isEmpty()) {
                    //como entre cada celda por asi decirlo hay espacio por las comas ponemos trim
                    //separar por comas
                    String[] partes = linea.split(",");

                    //cogemos apellido
                    String apellido = partes[0].trim();

                    //cogemos nombre
                    String nombre = partes[1].trim();

                    //SEPARando fecha
                    LocalDate fecha = LocalDate.parse(partes[2].trim(), formato);
                    String email = partes[3].trim();

                    //añadiendo amigo a la lista
                    Amigo amigo=new Amigo(apellido,nombre,fecha,email);
                    amigos.add(amigo);
                }
            }
            lector.close();
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("No se encuentra el fichero: " + rutaFichero);
        }


        //DEVOLVEMOS LA LISTA
        return amigos;
    }
}