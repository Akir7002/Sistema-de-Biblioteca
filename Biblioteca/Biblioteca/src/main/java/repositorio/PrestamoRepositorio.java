package repositorio;

import modelo.Prestamo;
import modelo.Usuario;
import modelo.Libro;
import modelo.EstadoPrestamo;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PrestamoRepositorio implements IRepositorio<Prestamo> {
    private final File archivoCsv;
    private int contadorId;
    private static final String RUTA = "data/prestamos.csv";
    private boolean inicializado = false;
    
    // Referencias a otros repositorios para resolver las dependencias
    private UsuarioRepositorio usuarioRepositorio;
    private BibliotecaRepositorio bibliotecaRepositorio;
    
    public PrestamoRepositorio() {
        this.archivoCsv = new File(RUTA);
        this.contadorId = 1; // Valor por defecto, se actualizará cuando se inicialice
    }
    
    // Métodos para inyectar dependencias
    public void setUsuarioRepositorio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
        inicializarSiEsNecesario();
    }
    
    public void setBibliotecaRepositorio(BibliotecaRepositorio bibliotecaRepositorio) {
        this.bibliotecaRepositorio = bibliotecaRepositorio;
        inicializarSiEsNecesario();
    }
    
    // Inicializa el contador ID una vez que las dependencias estén disponibles
    private void inicializarSiEsNecesario() {
        if (!inicializado && usuarioRepositorio != null && bibliotecaRepositorio != null) {
            this.contadorId = obtenerMaximoId() + 1;
            this.inicializado = true;
        }
    }
    
    private void asegurarDirectorio() {
        File parent = archivoCsv.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
    
    private void guardarTodos(List<Prestamo> prestamos) throws IOException {
        asegurarDirectorio();
        
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivoCsv, false), StandardCharsets.UTF_8))) {
            
            // Cabecera
            bw.write("id;usuarioId;libroId;fechaPrestamo;fechaDevolucionEsperada;fechaDevolucionReal;estado;notas");
            bw.newLine();
            
            for (Prestamo prestamo : prestamos) {
                bw.write(prestamo.toCsv());
                bw.newLine();
            }
        }
    }
    
    private List<Prestamo> cargarTodos() throws IOException {
        List<Prestamo> lista = new ArrayList<>();
        if (!archivoCsv.exists()) {
            return lista; // Si no existe, devuelve lista vacía
        }
        
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivoCsv), StandardCharsets.UTF_8))) {
            
            String linea;
            boolean primera = true;
            
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                
                // Saltar cabecera si es la primera línea
                if (primera) {
                    primera = false;
                    if (linea.toLowerCase().startsWith("id;")) {
                        continue;
                    }
                }
                
                try {
                    // Parsear la línea para obtener IDs
                    String[] p = linea.split(";", -1);
                    if (p.length >= 3) {
                        int usuarioId = Integer.parseInt(p[1].trim());
                        int libroId = Integer.parseInt(p[2].trim());
                        
                        // Buscar las entidades referenciadas
                        Usuario usuario = usuarioRepositorio != null ? usuarioRepositorio.obtenerPorId(usuarioId) : null;
                        Libro libro = bibliotecaRepositorio != null ? bibliotecaRepositorio.obtenerPorId(libroId) : null;
                        
                        if (usuario != null && libro != null) {
                            lista.add(Prestamo.fromCsv(linea, usuario, libro));
                        } else {
                            System.err.println("No se pudieron resolver las referencias para el préstamo: " + linea);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error al procesar línea CSV: " + linea + " - " + e.getMessage());
                }
            }
        }
        return lista;
    }
    
    private int obtenerMaximoId() {
        try {
            List<Prestamo> prestamos = cargarTodos();
            return prestamos.stream()
                           .mapToInt(Prestamo::getId)
                           .max()
                           .orElse(0);
        } catch (IOException e) {
            return 0; // Si hay error al cargar, empezamos desde 0
        }
    }
    
    @Override
    public boolean crear(Prestamo prestamo) {
        try {
            // Asegurar que el repositorio esté inicializado
            inicializarSiEsNecesario();
            
            prestamo.setId(contadorId++);
            List<Prestamo> prestamos = cargarTodos();
            prestamos.add(prestamo);
            guardarTodos(prestamos);
            return true;
        } catch (IOException e) {
            System.err.println("Error al crear préstamo: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Prestamo obtenerPorId(int id) {
        try {
            List<Prestamo> prestamos = cargarTodos();
            return prestamos.stream()
                           .filter(prestamo -> prestamo.getId() == id)
                           .findFirst()
                           .orElse(null);
        } catch (IOException e) {
            System.err.println("Error al obtener préstamo por ID: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Prestamo> obtenerTodos() {
        try {
            // Asegurar que el repositorio esté inicializado
            inicializarSiEsNecesario();
            
            return new ArrayList<>(cargarTodos());
        } catch (IOException e) {
            System.err.println("Error al obtener todos los préstamos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public boolean actualizar(Prestamo prestamo) {
        try {
            List<Prestamo> prestamos = cargarTodos();
            for (int i = 0; i < prestamos.size(); i++) {
                if (prestamos.get(i).getId() == prestamo.getId()) {
                    prestamos.set(i, prestamo);
                    guardarTodos(prestamos);
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            System.err.println("Error al actualizar préstamo: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean eliminar(int id) {
        try {
            List<Prestamo> prestamos = cargarTodos();
            boolean eliminado = prestamos.removeIf(prestamo -> prestamo.getId() == id);
            if (eliminado) {
                guardarTodos(prestamos);
            }
            return eliminado;
        } catch (IOException e) {
            System.err.println("Error al eliminar préstamo: " + e.getMessage());
            return false;
        }
    }
    
    // Métodos específicos
    public List<Prestamo> obtenerPorUsuario(Usuario usuario) {
        try {
            List<Prestamo> prestamos = cargarTodos();
            return prestamos.stream()
                           .filter(prestamo -> prestamo.getUsuario().getId() == usuario.getId())
                           .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        } catch (IOException e) {
            System.err.println("Error al obtener préstamos por usuario: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Prestamo> obtenerPorLibro(Libro libro) {
        try {
            List<Prestamo> prestamos = cargarTodos();
            return prestamos.stream()
                           .filter(prestamo -> prestamo.getLibro().getId() == libro.getId())
                           .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        } catch (IOException e) {
            System.err.println("Error al obtener préstamos por libro: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Prestamo> obtenerPorEstado(EstadoPrestamo estado) {
        try {
            List<Prestamo> prestamos = cargarTodos();
            return prestamos.stream()
                           .filter(prestamo -> prestamo.getEstado() == estado)
                           .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        } catch (IOException e) {
            System.err.println("Error al obtener préstamos por estado: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Prestamo> obtenerPrestamosActivos() {
        return obtenerPorEstado(EstadoPrestamo.ACTIVO);
    }
    
    public List<Prestamo> obtenerPrestamosVencidos() {
        try {
            List<Prestamo> prestamos = cargarTodos();
            return prestamos.stream()
                           .filter(Prestamo::estaVencido)
                           .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        } catch (IOException e) {
            System.err.println("Error al obtener préstamos vencidos: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public Prestamo buscarPrestamoActivoPorLibroYUsuario(Libro libro, Usuario usuario) {
        try {
            List<Prestamo> prestamos = cargarTodos();
            return prestamos.stream()
                           .filter(prestamo -> prestamo.getLibro().getId() == libro.getId() &&
                                             prestamo.getUsuario().getId() == usuario.getId() &&
                                             prestamo.getEstado() == EstadoPrestamo.ACTIVO)
                           .findFirst()
                           .orElse(null);
        } catch (IOException e) {
            System.err.println("Error al buscar préstamo activo: " + e.getMessage());
            return null;
        }
    }
    
    public String rutaAbsoluta() {
        try {
            return archivoCsv.getCanonicalPath();
        } catch (IOException e) {
            return archivoCsv.getAbsolutePath();
        }
    }
}
