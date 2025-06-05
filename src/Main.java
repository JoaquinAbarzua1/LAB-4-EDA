import java.security.PrivateKey;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;
import java.util.Comparator;
import java.time.Instant;
import java.io.FileWriter;
import java.io.IOException;
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

    public void setCategoria(int nuevaCategoria) { //setter de categoría que agrega el cambio al historial de Cambios
        if (!historialCambios.empty()) {
            historialCambios.push("Cambio categoria de C" + categoria + " a C" + nuevaCategoria); //quizás también haya que agregar la hora en que se hizo el cambio
        }
        else {historialCambios.push("Entra con categoría C"+nuevaCategoria);} //¿colocar hora de ingreso en el historial?
        this.categoria = nuevaCategoria;
    }

    //----------Metodos Obligatorios---------
    public long tiempoEsperaActual(){}

    public void registrarCambio(String descripcion){ historialCambios.push(descripcion); }

    public String obtenerUltimoCambio(){ return historialCambios.peek(); }



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

    public void ingresarPaciente(Paciente P) {
        if (!saturada()){
            pacientesHeap.add(P);}
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
        }
        return ordenados;
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

    public void registrarPaciente(Paciente p) {
        colaAtencion.add(p);
        pacientesTotales.put(p.getId(), p);
    }

    public void reasignarCategoria(String id, int nuevaCategoria){
        pacientesTotales.get(id).setCategoria(nuevaCategoria); //en el set se agrega el cambio en el historial
    }

    public Paciente atenderSiguiente(){
        //¿el area corresponde al area que tiene el paciente? ¿o sea que debo conseguir su area, usarla como llave para
        // conseguir el valor (objeto) AreaAtencion, para luego agregarlo a su fila pacientesHeap?
    }

    public List<Paciente> obtenerPacientesPorCategoria(int categoria){
        PriorityQueue<Paciente> Aux = new PriorityQueue<>(colaAtencion);
        List<Paciente> ordenados = new ArrayList<>();
        while (!Aux.isEmpty()) {
            if (Aux.peek().getCategoria() == categoria){
                ordenados.add(Aux.poll());
            }
            else {
                Aux.poll();
            }
        }
        return ordenados;
    }

    public AreaAtencion obtenerArea(String nombre){}


}


//-----------------------Clase GeneradorPacientes------------------------

    class GeneradorPacientes{
        private static final String[] nombres = { "María", "Pedro", "Ana", "Luis", "Sofía","Rodrigo","Benjamin"} ;             //Datos fijos 
        private static final String[] apellidos = {"Vargas","Pérez","López","Soto","Torres","Muños"} ;
        private static final String[] areas = {"SAPU","infantil","urgencia_adulto"} ;
        private static int generarCategoria(Random rand) {         //genera categorias al azar
            int r = rand.nextInt(100) + 1;
            if (r <= 10) return 1;       // C1
            else if (r <= 25) return 2;  // C2
            else if (r <= 43) return 3;  // C3
            else if (r <= 70) return 4;  // C4
            else return 5;               // C5
        }
    
        public static List<Paciente> generarPacientes(int N, long timestampInicio) {         // lista de pacientes
            List<Paciente> lista = new ArrayList<>();
            Random rand = new Random();

            for (int i = 0; i < N; i++) {               //bucles para crear pacientes
                String nombre = nombres[rand.nextInt(nombres.length)];     //Nombre y apellidos al azar
                String apellido = apellidos[rand.nextInt(apellidos.length)];
                String id = "ID" + (1000 + i);                           // ID de paciente
                int categoria = generarCategoria(rand);                      // categoria random
                long tiempoLlegada = timestampInicio + i * 600;             // Simula llegada cada 10 mins
                String area = areas[rand.nextInt(areas.length)];                  //área al azar

                //crear "estado" e "historialCambios"
                Paciente p = new Paciente(nombre, apellido, id, categoria, tiempoLlegada, estado, area, historialCambios);
                lista.add(p);                                             //crea paciente y lo agrega a la lista
                }
                return lista;
            }



    public static void guardarPacientesEnArchivo(List<Paciente> pacientes, String archivo) {                    //Recibe lista de paciente  y archivo
        try (FileWriter writer = new FileWriter(archivo)) {                                           //Abre el archivo usando try-with-resources, lo que garantiza que se cerrará correctamente al final.
            for (Paciente p : pacientes) {
                writer.write(p.getId() + "," + p.getNombre() + "," + p.getApellido() + "," + p.getCategoria()
                    + "," + p.getTiempoLlegada() + "," + p.getArea() + "," + p.getEstado() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();                               //Escribe cada paciente en una línea del archivo CSV.

        }
    }
    

}


//-----------------------Clase SimuladorUrgencia------------------------

class SimuladorUrgencia {                                   // crear clase 
    private Hospital hospital;
    private List<Paciente> pacientesSimulados;
    private int pacientesAtendidos;
    private Map<Integer, List<Long>> tiemposEsperaPorCategoria;
    private List<Paciente> pacientesFueraDeTiempo;

    public SimuladorUrgencia() {                            // constructor 
        this.hospital = new Hospital(); 
        this.pacientesAtendidos = 0;
        this.tiemposEsperaPorCategoria = new HashMap<>();
        this.pacientesFueraDeTiempo = new ArrayList<>();
        for (int i = 1; i <= 5; i++) tiemposEsperaPorCategoria.put(i, new ArrayList<>());
    }

    public void simular(int pacientesPorDia) {                                         //simula pacientes por dia 
        long timestampInicio = Instant.now().getEpochSecond();
        pacientesSimulados = GeneradorPacientes.generarPacientes(pacientesPorDia, timestampInicio);
        Queue<Paciente> colaLlegadas = new LinkedList<>(pacientesSimulados);

        int minuto = 0;
        int nuevosDesdeUltimaAtencion = 0;

        while (minuto < 24 * 60) {
            long tiempoActual = timestampInicio + minuto * 60;

            if (minuto % 10 == 0 && !colaLlegadas.isEmpty()) {
                Paciente nuevo = colaLlegadas.poll();
                hospital.registrarPaciente(nuevo);
                nuevosDesdeUltimaAtencion++;
            }

            if (minuto % 15 == 0 || nuevosDesdeUltimaAtencion >= 3) {
                atenderPacientes(tiempoActual, nuevosDesdeUltimaAtencion >= 3 ? 2 : 1);
                nuevosDesdeUltimaAtencion = 0;
            }
            minuto++;
        }

        System.out.println(" SIMULACIÓN COMPLETA ");
        mostrarResultados();
    }

    private void atenderPacientes(long tiempoActual, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            Paciente p = hospital.atenderSiguiente();
            if (p == null) return;

            long espera = (tiempoActual - p.getTiempoLlegada()) / 60;
            tiemposEsperaPorCategoria.get(p.getCategoria()).add(espera);
            if (excedeTiempoMaximo(p.getCategoria(), espera)) pacientesFueraDeTiempo.add(p);
            pacientesAtendidos++;
        }
    }

    private boolean excedeTiempoMaximo(int categoria, long esperaMinutos) {
        switch (categoria) {
            case 1: return esperaMinutos > 0;
            case 2: return esperaMinutos > 30;
            case 3: return esperaMinutos > 90;
            case 4: return esperaMinutos > 180;
            case 5: return false;
        }
        return false;
    }

    private void mostrarResultados() {
        System.out.println("Total pacientes atendidos: " + pacientesAtendidos);

        for (int cat = 1; cat <= 5; cat++) {
            List<Long> tiempos = tiemposEsperaPorCategoria.get(cat);
            double promedio = tiempos.isEmpty() ? 0 : tiempos.stream().mapToLong(Long::longValue).average().orElse(0);
            System.out.printf("Categoría C%d: atendidos=%d, espera promedio=%.2f min\n", cat, tiempos.size(), promedio);
        }

        System.out.println("Pacientes que excedieron el tiempo máximo: " + pacientesFueraDeTiempo.size());
        for (Paciente p : pacientesFueraDeTiempo) {
            System.out.println("- " + p.getId() + ", C" + p.getCategoria() + ", área: " + p.getArea());
        }
    }
}

//-----------------------Clase Main-------------------------

public class Main {
    public static void main(String[] args) {


    }
}
