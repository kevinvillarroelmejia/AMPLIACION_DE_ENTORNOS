import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProcesadorFacturacionOptimizado {

    //constantes para que se vea mas claro
    private static final double DESCUENTO_VIP = 0.15;
    private static final double UMBRAL_INFORME = 100;

    // ggenera el informe de facturación y resumen final
    public String generarInforme(List<String> pedidos, List<String> clientesVip, double impuesto) {

        //un hashset fuera del bucle, creo que si lo meto dentro del for se perderia la mejora
        Set<String> vipSet = new HashSet<>(clientesVip);

        //seria como el cuaderno donde se va escribiendo el informe
        StringBuilder resultado = new StringBuilder(); //un StringBuilder para que sea mutable
        int contador = 0;
        double totalGlobal = 0;

        for (int i = 0; i < pedidos.size(); i++) {
            String[] datos = pedidos.get(i).split(";");
            String idCliente = datos[0];
            double importe = Double.parseDouble(datos[1]);

                //sacamos el calculo y el formato para que el metodo generarInforme solo cuente el flujo()
            double importeFinal = calcularImporteFinal(importe, vipSet.contains(idCliente), impuesto);
            totalGlobal += importeFinal;

            if (importeFinal > UMBRAL_INFORME) {
                contador++;
                resultado.append(formatearLinea(i, idCliente, importeFinal));
            }
        }

        resultado.append(formatearResumen(contador, totalGlobal));
        return resultado.toString();
    }

    // aplica el descuento VIP si le toca y después el impuesto al importe
    // es como la de legacy
    private double calcularImporteFinal(double importe, boolean esVip, double impuesto) {
        double importeFinal = importe;
        if (esVip) {
            importeFinal = importe - (importe * DESCUENTO_VIP);
        }
        return importeFinal + (importeFinal * impuesto);
    }

    // construye la línea de detalle de un pedido
    private String formatearLinea(int indice, String idCliente, double importeFinal) {
        return "Pedido [" + indice + "] Cliente: " + idCliente + " -> Total: " + importeFinal + "\n";
    }

    // Construye la línea final del resumen
    private String formatearResumen(int contador, double totalGlobal) {
        return "RESUMEN: " + contador + " pedidos superiores a 100€. Total: " + totalGlobal;
    }
}
