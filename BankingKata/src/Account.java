import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Account {
    private int saldo;
    private static final String CABECERA="Date         Amount  Balance";
    private ArrayList<String> listaStatement=new ArrayList<>();
    //FUNCION QUE AÑADE DINERO A TU CUENTA
    public void deposit(int ingreso){
        saldo = saldo + ingreso;
        anadirLinea("+" + ingreso);
    }
    private void anadirLinea(String ingreso) {
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("d.M.yyyy"));
        listaStatement.add(fecha + "   " + ingreso + "      " + saldo);
    }
    //FUNCION QUE QUITA DINERO DE TU CUENTA
    public void withdraw(int retirada){
        saldo = saldo - retirada;
        anadirLinea("-" + retirada);
    }

    //DEVUELVE EL EXTRACTO
    public String printStatement(){
        String extracto = CABECERA;
        for (String linea : listaStatement) {
            extracto = extracto + "\n" + linea;
        }
        return extracto;
    }
}
