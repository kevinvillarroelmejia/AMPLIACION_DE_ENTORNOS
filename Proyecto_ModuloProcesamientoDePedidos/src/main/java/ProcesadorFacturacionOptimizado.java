import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProcesadorFacturacionOptimizado {

    private static final double DESCUENTO_VIP = 0.15;
    private static final double UMBRAL_INFORME = 100;

    // Genera el informe de facturación con el detalle de pedidos grandes y el resumen final
    public String generarInforme(List<String> pedidos, List<String> clientesVip, double impuesto) {
        Set<String> vipSet = new HashSet<>(clientesVip);
        StringBuilder resultado = new StringBuilder();
        int contador = 0;
        double totalGlobal = 0;

        for (int i = 0; i < pedidos.size(); i++) {
            String[] datos = pedidos.get(i).split(";");
            String idCliente = datos[0];
            double importe = Double.parseDouble(datos[1]);

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

    // Aplica el descuento VIP (si corresponde) y después el impuesto al importe
    private double calcularImporteFinal(double importe, boolean esVip, double impuesto) {
        double importeFinal = importe;
        if (esVip) {
            importeFinal = importe - (importe * DESCUENTO_VIP);
        }
        return importeFinal + (importeFinal * impuesto);
    }

    // Construye la línea de detalle de un pedido
    private String formatearLinea(int indice, String idCliente, double importeFinal) {
        return "Pedido [" + indice + "] Cliente: " + idCliente + " -> Total: " + importeFinal + "\n";
    }

    // Construye la línea final de resumen del informe
    private String formatearResumen(int contador, double totalGlobal) {
        return "RESUMEN: " + contador + " pedidos superiores a 100€. Total: " + totalGlobal;
    }
}
