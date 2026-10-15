package cumpleanos.mensajeria;

import cumpleanos.modelo.Mensaje;


//por donde saldra el mensaje,
public interface ServicioMensajeria {


    void enviar(Mensaje mensaje);
}