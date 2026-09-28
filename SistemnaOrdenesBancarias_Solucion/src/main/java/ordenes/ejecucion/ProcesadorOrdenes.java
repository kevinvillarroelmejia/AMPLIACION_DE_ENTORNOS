package ordenes.ejecucion;

import ordenes.documentos.Documento;
import ordenes.documentos.Movimientos;
import ordenes.documentos.Talon;
import ordenes.transferencia.OrdenDTO;
import ordenes.transferencia.SolicitudDTO;

import java.util.ArrayList;
import java.util.List;


//esta clase seria para validar la ordenDTO, si es correcta devuelve un List con los datos
public class ProcesadorOrdenes {

    public SolicitudDTO procesar(OrdenDTO orden) {
        validarBanco(orden.getBanco());
        validarSucursal(orden.getSucursal());
        validarCuenta(orden.getCuenta());
        validarClave(orden.getClave());
        validarTipoOrden(orden.getTipoOrden());

        List<Documento> documentos = generarDocumentos(orden.getTipoOrden());
        return new SolicitudDTO(documentos);
    }

    //funcion que genera/devuelve el documento que pide si el String tipoOrden como pedia en las reglas se añaden las dos
    //si es talonario se crea y añade un talonario
    // lo mismo con movimientos
    private List<Documento> generarDocumentos(String tipoOrden) {
        List<Documento> documentos = new ArrayList<>();
        if (tipoOrden == null || tipoOrden.isEmpty()) {
            documentos.add(new Talon());
            documentos.add(new Movimientos());
        } else if (tipoOrden.equals("Talonario")) {
            documentos.add(new Talon());
        } else if (tipoOrden.equals("Movimientos")) {
            documentos.add(new Movimientos());
        }
        return documentos;
    }
    //==========================LAS SIGUIENTES FUNCIONES SON PARA VALIDAR LOS ATRIBUTOS DE LA ORDEN==========================
    // UTILIZAMOS throw PARA QUE SALTE EL MENSAJE DE ERROR Y AHORRARNOS LINEAS DE CODIGO CON EL TRY CACTH
    private void validarBanco(String banco) {
        if (banco == null || banco.isEmpty()) {
            return;
        }
        if (banco.length() != 3 || !soloDigitos(banco)) {
            throw new OrdenNoValida("CODIGO_BANCO_INVALIDO");
        }
        int primerDigito = Character.getNumericValue(banco.charAt(0));
        if (primerDigito <= 1) {
            throw new OrdenNoValida("CODIGO_BANCO_INVALIDO");
        }
    }

    private void validarSucursal(int sucursal) {

        if (sucursal < 1000 || sucursal > 9999) {
            throw new OrdenNoValida("CODIGO_SUCURSAL_INVALIDO");
        }
    }

    private void validarCuenta(int cuenta) {
        // numero de 5 digitos, sin restriccion sobre el primero -> 0 a 99999
        if (cuenta < 0 || cuenta > 99999) {
            throw new OrdenNoValida("NUMERO_CUENTA_INVALIDO");
        }
    }

    private void validarClave(String clave) {
        if (clave == null || clave.length() != 5 || !esAlfanumerica(clave)) {
            throw new OrdenNoValida("CLAVE_INVALIDA");
        }
    }

    private void validarTipoOrden(String tipoOrden) {
        if (tipoOrden == null || tipoOrden.isEmpty()) {
            return; // en blanco: valido, se envian ambos documentos
        }
        if (!tipoOrden.equals("Talonario") && !tipoOrden.equals("Movimientos")) {
            throw new OrdenNoValida("ORDEN_INVALIDA");
        }
    }



    // Recorre la cadena con un for normal y usa una bandera booleana,
    // igual que se hace al comprobar el fin de fichero con un boolean.
    private boolean soloDigitos(String texto) {
        boolean esValido = true;
        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);
            if (!Character.isDigit(caracter)) {
                esValido = false;
            }
        }
        return esValido;
    }

    private boolean esAlfanumerica(String texto) {
        boolean esValido = true;
        for (int i = 0; i < texto.length(); i++) {
            char caracter = texto.charAt(i);
            //SI ES LETRA O NUMERO LA FUNCION DEVUELVE FALSE
            if (!Character.isLetterOrDigit(caracter)) {
                esValido = false;
            }
        }
        return esValido;
    }
}
