import java.security.PrivateKey;
import java.util.*;


//-----------------------Clase Paciente-----------------------
class Paciente{
    private String nombre;
    private String apellido;
    private String id;
    private int categoria;
    private long tiempoLlegada;
    private String estado;
    private String area;
    private Stack<String> historialCambios;

    public Paciente(String nombre, String apellido, String id, int categoria, long tiempoLlegada, String estado, String area, Stack<String> historialCambios) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.id = id;
        this.categoria = categoria;
        this.tiempoLlegada = tiempoLlegada;
        this.estado = estado;
        this.area = area;
        this.historialCambios = historialCambios;
    }
    //----------Getters-----------
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getId() { return id; }
    public int getCategoria() { return categoria;}
    public long getTiempoLlegada(){ return tiempoLlegada;}
    public String getEstado() { return estado; }
    public String getArea() { return area; }
    public Stack<String> getHistorialCambios(){ return historialCambios; }
    //---------Setters----------- (de der necesarios)


    //----------Metodos Obligatorios---------
    public long tiempoEsperaActual(){}

    public void registrarCambio(String descripcion){}

    public String obtenerUltimoCambio(){}


}

//-----------------------Clase AreaAtecion--------------------------
class AreaAtencion {
    private String nombre;
    private PriorityQueue<Paciente> pacientesHeap;
    private int capacidadMaxima;

    public AreaAtencion(String nombre, int capacidadMaxima) {
        this.nombre = nombre;
        this.capacidadMaxima = capacidadMaxima;
        this.pacientesHeap = new PriorityQueue<>();

    }

    public void IngresarPaciente(Paciente P) {
        if (!saturada())
            pacientesHeap.add(P);
    }

    public boolean saturada() {
        return pacientesHeap.size() >= capacidadMaxima;
    }

    public Paciente atenderPaciente() {
        return pacientesHeap.poll();
    }

    public List<Paciente> obtenerPacientePorHeapSort() {
        PriorityQueue<Paciente> Aux = new PriorityQueue<>(pacientesHeap);
        List<Paciente> ordenados = new ArrayList<>();
        while (!Aux.isEmpty()) {
            ordenados.add(Aux.poll());
            return ordenados;

        }


    }
}





//-----------------------Clase Hospital-----------------------

class Hospital{
    private Map<String, Paciente> pacientesTotales;
    private PriorityQueue<Paciente> colaAtencion;
    private Map<String, AreaAtencion> areasAtencion;
    private List<Paciente> pacientesAtendidos;

    public Hospital(Map<String, Paciente> pacientesTotales, PriorityQueue<Paciente> colaAtencion, Map<String, AreaAtencion> areasAtencion, List<Paciente> pacientesAtendidos){
        this.pacientesTotales = pacientesTotales;
        this.colaAtencion = colaAtencion;
        this.areasAtencion = areasAtencion;
        this.pacientesAtendidos = pacientesAtendidos;
    }

    //----------Getters---------- (en caso de ser necesarios)

    //----------Metodos obligatorios----------

    public void registrarPaciente(Paciente p) {}

    public void reasignarCategoria(String id, int nuevaCategoria){}

    public Paciente atenderSiguiente(){}

    public List<Paciente> obtenerPacientesPorCategoria(int categoria){}

    public AreaAtencion obtenerArea(String nombre){}


}


//-----------------------Clase GeneradorPacientes------------------------

class GeneradorPacientes{
    private static final String[] nombres = ("NOMBRES ")
    private static final String[] apellidos = ("Apellidos")
    private static final String[] areas = ("SAPU","infantil","urgencia_adulto")

    //para categoria, generdador num aleatorio entre 1 y 5


}


//-----------------------Clase SimuladorUrgencia------------------------


class SimuladorUrgencia{

    public void simular(int pacientesPorDia){}
}


//-----------------------Clase Main-------------------------

public class Main {
    public static void main(String[] args) {


    }
}