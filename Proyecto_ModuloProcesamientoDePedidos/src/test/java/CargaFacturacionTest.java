import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CargaFacturacionTest {

    ////
    /// constantes para fijar valores que no cambian
    //
    private static final int NUM_PEDIDOS = 50_000; //he visto que podemos poner _ para que se lea mejor
    private static final int NUM_CLIENTES_VIP = 10_000;
    private static final int NUM_CLIENTES_TOTAL = 20_000;
    private static final double IMPUESTO = 0.21;

    // Genera los datos de prueba, ejecuta legacy y/o optimizado segun el argumento y compara sus informes
    // Uso: java CargaFacturacionTest [legacy|optimizado|ambos]
    public static void main(String[] args) {
        String modo = args.length > 0 ? args[0] : "ambos";
        Random rnd = new Random(42); //genera siempre lo mismos numeros

        //cada pedido es un texto con formato idCliente;importe que es el lo que se parte con ;
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

        // permite saber cuantos bytes de memoria ha reservado el hilo
        com.sun.management.ThreadMXBean tb =
                (com.sun.management.ThreadMXBean) java.lang.management.ManagementFactory.getThreadMXBean();
        long idHilo = Thread.currentThread().getId();

        String salidaLegacy = null;
        String salidaOptimizada = null;

        //para comparar lo que ponemos al final del comando puede ser legacy optimizado o ambos
        if (modo.equals("legacy") || modo.equals("ambos")) {
            ProcesadorFacturacionLegacy legacy = new ProcesadorFacturacionLegacy();
            // lo siguiente seria para guardar la memoria y la hora antes de ejecutar
            long memAntes = tb.getThreadAllocatedBytes(idHilo);
            long inicio = System.nanoTime();

            salidaLegacy = legacy.generarInforme(pedidos, clientesVip, IMPUESTO);

            // tiempo y memoria = valor de despues menos valor de antes
            long ms = (System.nanoTime() - inicio) / 1_000_000;
            long memBytes = tb.getThreadAllocatedBytes(idHilo) - memAntes;
            imprimirResultado("LEGACY", ms, memBytes, salidaLegacy.length());
        }

        //esto seria lo siguiente pero con optimizado
        if (modo.equals("optimizado") || modo.equals("ambos")) {
            ProcesadorFacturacionOptimizado optimizado = new ProcesadorFacturacionOptimizado();
            // misma medicion que en legacy
            long memAntes = tb.getThreadAllocatedBytes(idHilo);
            long inicio = System.nanoTime();
            salidaOptimizada = optimizado.generarInforme(pedidos, clientesVip, IMPUESTO);
            long ms = (System.nanoTime() - inicio) / 1_000_000;
            long memBytes = tb.getThreadAllocatedBytes(idHilo) - memAntes;
            imprimirResultado("OPTIMIZADO", ms, memBytes, salidaOptimizada.length());
        }
        // si se ejecutaron las dos versiones, comprobamos que el texto del informe es igual
        if (salidaLegacy != null && salidaOptimizada != null) {
            System.out.println("Salida identica: " + salidaLegacy.equals(salidaOptimizada));
        }
    }

    // Imprime el tiempo, la memoria asignada (lo multiplicamos por 1024 para pasarlo a  MB)
    private static void imprimirResultado(String nombre, long ms, long memBytes, int longitud) {
        System.out.println(nombre + " -> tiempo: " + ms + " ms | memoria asignada: "
                + memBytes / (1024 * 1024) + " MB | longitud informe: " + longitud);
    }
}