import java.util.List;

public class ProcesadorFacturacionLegacy {

    public String generarInforme(List<String> pedidos, List<String> clientesVip, double impuesto) {
        String resultado = "";
        int contador = 0;
        double totalGlobal = 0;
        String variableSinUso = "DEBUG_MODE";

        for (int i = 0; i < pedidos.size(); i++) {
            String[] datos = pedidos.get(i).split(";");
            String idCliente = datos[0];
            double importe = Double.parseDouble(datos[1]);

            // Cálculo de descuento si es VIP
            boolean esVip = false;
            for (int j = 0; j < clientesVip.size(); j++) {
                if (clientesVip.get(j).equals(idCliente)) {
                    esVip = true;
                }
            }

            double importeFinal = importe;
            if (esVip == true) {
                importeFinal = importe - (importe * 0.15);
            }

            importeFinal = importeFinal + (importeFinal * impuesto);
            totalGlobal = totalGlobal + importeFinal;

            if (importeFinal > 100) {
                contador++;
                resultado += "Pedido [" + i + "] Cliente: " + idCliente + " -> Total: " + importeFinal + "\n";
            }
        }

        resultado += "RESUMEN: " + contador + " pedidos superiores a 100€. Total: " + totalGlobal;
        return resultado;
    }
}