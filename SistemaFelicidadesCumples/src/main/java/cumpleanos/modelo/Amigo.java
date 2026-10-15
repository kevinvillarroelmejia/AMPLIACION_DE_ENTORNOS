package cumpleanos.modelo;

import java.time.LocalDate;



public class Amigo {

    //
    private  String apellido;
    private  String nombre;
    private  LocalDate fechaNacimiento;
    private  String email;


    //constructor para crear el objeto
    public Amigo(String apellido, String nombre, LocalDate fechaNacimiento, String email) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.fechaNacimiento = fechaNacimiento;
        this.email = email;
    }




    //getters
    public String getApellido() {
        return apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getEmail() {
        return email;
    }


    //funcion booleana que comprueba si es su cumple
    //comprueba si HOY es su cumpleaños
    //comprobar si el año es bisesto
    public boolean esSuCumpleanos(LocalDate hoy) {
        boolean bisiesto = fechaNacimiento.getMonthValue() == 2 && fechaNacimiento.getDayOfMonth() == 29;

        //isLeapYear comprueba si el año es bisiesto
        if (bisiesto && !hoy.isLeapYear()) {
            //AÑO NO BISIESTO: NO EXISTE EL 29, ASI QUE SE FELICITA EL 28 DE FEBRERO
            return hoy.getMonthValue() == 2 && hoy.getDayOfMonth() == 28;
        }

        //comparando dia del mes y mes
        return fechaNacimiento.getDayOfMonth() == hoy.getDayOfMonth() && fechaNacimiento.getMonthValue() == hoy.getMonthValue();
    }
}