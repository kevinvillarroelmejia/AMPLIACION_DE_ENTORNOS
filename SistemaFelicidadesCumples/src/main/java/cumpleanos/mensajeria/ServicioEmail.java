package cumpleanos.mensajeria;

import cumpleanos.modelo.Mensaje;

/*
* para entenderlo esto es como si se estuviera simulando el servidor de mensajeria
*
* sobreEscribimos la funcion ENVIAR para adaptarlo en cada caso
* */
public class ServicioEmail implements ServicioMensajeria {

    @Override
    public void enviar(Mensaje mensaje) {
        System.out.println("---- CORREO ENVIADO ----");
        System.out.println("Para: " + mensaje.getDestinatario());
        System.out.println("Asunto: " + mensaje.getAsunto());
        System.out.println();
        System.out.println(mensaje.getCuerpo());
        System.out.println("------------------------");
    }
}