import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CargaFacturacionTest {

    private static final int NUM_PEDIDOS = 50_000;
    private static final int NUM_CLIENTES_VIP = 10_000;
    private static final int NUM_CLIENTES_TOTAL = 20_000;
    private static final double IMPUESTO = 0.21;

    // Uso: java CargaFacturacionTest [legacy|optimizado|ambos]
    public static void main(String[] args) {
        String modo = args.length > 0 ? args[0] : "ambos";
        Random rnd = new Random(42);

        List<String> clientesVip = new ArrayList<>();
        for (int i = 0; i < NUM_CLIENTES_VIP; i++) {
            clientesVip.add("C" + String.format("%06d", i));
        }
        List<String> pedidos = new ArrayList<>();
        for (int i = 0; i < NUM_PEDIDOS; i++) {
            int cliente = rnd.nextInt(NUM_CLIENTES_TOTAL);
            double importe = Math.round((20 + rnd.nextDouble() * 180) * 100.0) / 100.0;
            pedidos.add("C" + String.format("%06d", cliente) + ";" + importe);
        }

        String salidaLegacy = null;
        String salidaOptimizada = null;

        if (modo.equals("legacy") || modo.equals("ambos")) {
            salidaLegacy = medir("LEGACY", () -> new ProcesadorFacturacionLegacy().generarInforme(pedidos, clientesVip, IMPUESTO));
        }
        if (modo.equals("optimizado") || modo.equals("ambos")) {
            salidaOptimizada = medir("OPTIMIZADO", () -> new ProcesadorFacturacionOptimizado().generarInforme(pedidos, clientesVip, IMPUESTO));
        }
        if (salidaLegacy != null && salidaOptimizada != null) {
            System.out.println("Salida identica: " + salidaLegacy.equals(salidaOptimizada));
        }
    }

    // Ejecuta el procesador, mide tiempo y memoria asignada por el hilo y devuelve el informe
    private static String medir(String nombre, java.util.function.Supplier<String> tarea) {
        com.sun.management.ThreadMXBean tb =
                (com.sun.management.ThreadMXBean) java.lang.management.ManagementFactory.getThreadMXBean();
        long id = Thread.currentThread().getId();
        long memAntes = tb.getThreadAllocatedBytes(id);
        long inicio = System.nanoTime();
        String salida = tarea.get();
        long ms = (System.nanoTime() - inicio) / 1_000_000;
        long memAsignada = (tb.getThreadAllocatedBytes(id) - memAntes) / (1024 * 1024);
        System.out.println(nombre + " -> tiempo: " + ms + " ms | memoria asignada: "
                + memAsignada + " MB | longitud informe: " + salida.length());
        return salida;
    }
}