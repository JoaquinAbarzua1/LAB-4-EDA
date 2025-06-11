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
class Paciente implements Comparable<Paciente> {
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

    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getId() { return id; }
    public int getCategoria() { return categoria; }
    public long getTiempoLlegada() { return tiempoLlegada; }
    public String getEstado() { return estado; }
    public String getArea() { return area; }
    public Stack<String> getHistorialCambios() { return historialCambios; }

    public void setCategoria(int nuevaCategoria) {
        if (!historialCambios.empty()) {
            historialCambios.push("Cambio categoría de C" + categoria + " a C" + nuevaCategoria);
        } else {
            historialCambios.push("Entra con categoría C" + nuevaCategoria);
        }
        this.categoria = nuevaCategoria;
    }

    public long tiempoEsperaActual() {
        long tiempoActual = Instant.now().getEpochSecond();
        return (tiempoActual - this.tiempoLlegada) / 60;
    }

    public void registrarCambio(String descripcion) {
        historialCambios.push(descripcion);
    }

    public String obtenerUltimoCambio() {
        return historialCambios.peek();
    }

    @Override
    public int compareTo(Paciente otro) {
        if (this.categoria != otro.categoria) {
            return Integer.compare(this.categoria, otro.categoria);
        } else {
            return Long.compare(this.tiempoLlegada, otro.tiempoLlegada);
        }
    }
}

//-----------------------Clase AreaAtencion--------------------------
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
        if (!saturada()) {
            pacientesHeap.add(P);
        }
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
class Hospital {
    private Map<String, Paciente> pacientesTotales;
    private PriorityQueue<Paciente> colaAtencion;
    private Map<String, AreaAtencion> areasAtencion;
    private List<Paciente> pacientesAtendidos;

    public Hospital() {
        this.pacientesTotales = new HashMap<>();
        this.colaAtencion = new PriorityQueue<>();
        this.areasAtencion = new HashMap<>();
        this.pacientesAtendidos = new ArrayList<>();

        areasAtencion.put("SAPU", new AreaAtencion("SAPU", 10));
        areasAtencion.put("infantil", new AreaAtencion("infantil", 5));
        areasAtencion.put("urgencia_adulto", new AreaAtencion("urgencia_adulto", 7));
    }

    public void registrarPaciente(Paciente p) {
        colaAtencion.add(p);
        pacientesTotales.put(p.getId(), p);
    }

    public void reasignarCategoria(String id, int nuevaCategoria) {
        pacientesTotales.get(id).setCategoria(nuevaCategoria);
    }

    public Paciente atenderSiguiente() {
        if (colaAtencion.isEmpty())
            return null;

        Paciente siguiente = colaAtencion.peek();
        AreaAtencion area = areasAtencion.get(siguiente.getArea());

        if (area != null && !area.saturada()) {
            siguiente = colaAtencion.poll();
            siguiente.registrarCambio("Paciente atendido en área: " + siguiente.getArea());
            area.ingresarPaciente(siguiente);
            siguiente.registrarCambio("Estado cambiado a atendido");
            pacientesAtendidos.add(siguiente);
            return siguiente;
        } else {
            return null;
        }
    }

    public List<Paciente> obtenerPacientesPorCategoria(int categoria) {
        PriorityQueue<Paciente> Aux = new PriorityQueue<>(colaAtencion);
        List<Paciente> ordenados = new ArrayList<>();
        while (!Aux.isEmpty()) {
            Paciente p = Aux.poll();
            if (p.getCategoria() == categoria) {
                ordenados.add(p);
            }
        }
        return ordenados;
    }

    public AreaAtencion obtenerArea(String nombre) {
        return areasAtencion.get(nombre);
    }
}

//-----------------------Clase GeneradorPacientes------------------------
class GeneradorPacientes {
    private static final String[] nombres = {"María", "Pedro", "Ana", "Luis", "Sofía", "Rodrigo", "Benjamin"};
    private static final String[] apellidos = {"Vargas", "Pérez", "López", "Soto", "Torres", "Muños"};
    private static final String[] areas = {"SAPU", "infantil", "urgencia_adulto"};

    private static int generarCategoria(Random rand) {
        int r = rand.nextInt(100) + 1;
        if (r <= 10) return 1;
        else if (r <= 25) return 2;
        else if (r <= 43) return 3;
        else if (r <= 70) return 4;
        else return 5;
    }

    public static List<Paciente> generarPacientes(int N, long timestampInicio) {
        List<Paciente> lista = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < N; i++) {
            String nombre = nombres[rand.nextInt(nombres.length)];
            String apellido = apellidos[rand.nextInt(apellidos.length)];
            String id = "ID" + (1000 + i);
            int categoria = generarCategoria(rand);
            long tiempoLlegada = timestampInicio + i * 600;
            String estado = "en_espera";
            String area = areas[rand.nextInt(areas.length)];
            Stack<String> historialCambios = new Stack<>();

            Paciente p = new Paciente(nombre, apellido, id, categoria, tiempoLlegada, estado, area, historialCambios);
            lista.add(p);
        }
        return lista;
    }

    public static void guardarPacientesEnArchivo(List<Paciente> pacientes, String archivo) {
        try (FileWriter writer = new FileWriter(archivo)) {
            for (Paciente p : pacientes) {
                writer.write(p.getId() + "," + p.getNombre() + "," + p.getApellido() + "," + p.getCategoria()
                        + "," + p.getTiempoLlegada() + "," + p.getArea() + "," + p.getEstado() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//-----------------------Clase SimuladorUrgencia------------------------
class SimuladorUrgencia {
    private Hospital hospital;
    private List<Paciente> pacientesSimulados;
    private int pacientesAtendidos;
    private Map<Integer, List<Long>> tiemposEsperaPorCategoria;
    private List<Paciente> pacientesFueraDeTiempo;

    public SimuladorUrgencia() {
        this.hospital = new Hospital();
        this.pacientesAtendidos = 0;
        this.tiemposEsperaPorCategoria = new HashMap<>();
        this.pacientesFueraDeTiempo = new ArrayList<>();
        for (int i = 1; i <= 5; i++) tiemposEsperaPorCategoria.put(i, new ArrayList<>());
    }

    public void simular(int pacientesPorDia) {
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

        System.out.println("SIMULACIÓN COMPLETA");
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
        return switch (categoria) {
            case 1 -> esperaMinutos > 0;
            case 2 -> esperaMinutos > 30;
            case 3 -> esperaMinutos > 90;
            case 4 -> esperaMinutos > 180;
            case 5 -> false;
            default -> false;
        };
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
class Main {
    public static void main(String[] args) {
        int cantidadPacientes = 200;

        SimuladorUrgencia simulador = new SimuladorUrgencia();
        System.out.println("Iniciando simulación de urgencia hospitalaria con " + cantidadPacientes + " pacientes:");
        simulador.simular(cantidadPacientes);

        long timestampInicio = Instant.now().getEpochSecond();
        List<Paciente> pacientesGenerados = GeneradorPacientes.generarPacientes(cantidadPacientes, timestampInicio);
        String archivoSalida = "Pacientes_24h.txt";
        GeneradorPacientes.guardarPacientesEnArchivo(pacientesGenerados, archivoSalida);
        System.out.println("Pacientes generados guardados en: " + archivoSalida);
    }
}