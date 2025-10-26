package repositorio;

import modelo.Libro;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BibliotecaRepositorio implements IRepositorio<Libro> {
    private final File archivoCsv;
    private int contadorId;
    private static final String RUTA = "data/libros.csv";
    
    public BibliotecaRepositorio() {
        this.archivoCsv = new File(RUTA);
        this.contadorId = obtenerMaximoId() + 1;
    }
    
    private void asegurarDirectorio() {
        File parent = archivoCsv.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
    
    private void guardarTodos(List<Libro> libros) throws IOException {
        asegurarDirectorio();
        
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivoCsv, false), StandardCharsets.UTF_8))) {
            
            // Cabecera
            bw.write("id;titulo;autor;cantidadDisponible;cantidadTotal");
            bw.newLine();
            
            for (Libro libro : libros) {
                bw.write(libro.toCsv());
                bw.newLine();
            }
        }
    }
    
    private List<Libro> cargarTodos() throws IOException {
        List<Libro> lista = new ArrayList<>();
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
                    lista.add(Libro.fromCsv(linea));
                } catch (Exception e) {
                    System.err.println("Error al procesar línea CSV: " + linea + " - " + e.getMessage());
                }
            }
        }
        return lista;
    }
    
    private int obtenerMaximoId() {
        try {
            List<Libro> libros = cargarTodos();
            return libros.stream()
                        .mapToInt(Libro::getId)
                        .max()
                        .orElse(0);
        } catch (IOException e) {
            return 0; // Si hay error al cargar, empezamos desde 0
        }
    }
    
    @Override
    public boolean crear(Libro libro) {
        try {
            libro.setId(contadorId++);
            List<Libro> libros = cargarTodos();
            libros.add(libro);
            guardarTodos(libros);
            return true;
        } catch (IOException e) {
            System.err.println("Error al crear libro: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Libro obtenerPorId(int id) {
        try {
            List<Libro> libros = cargarTodos();
            return libros.stream()
                        .filter(libro -> libro.getId() == id)
                        .findFirst()
                        .orElse(null);
        } catch (IOException e) {
            System.err.println("Error al obtener libro por ID: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Libro> obtenerTodos() {
        try {
            return new ArrayList<>(cargarTodos());
        } catch (IOException e) {
            System.err.println("Error al obtener todos los libros: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    @Override
    public boolean actualizar(Libro libro) {
        try {
            List<Libro> libros = cargarTodos();
            for (int i = 0; i < libros.size(); i++) {
                if (libros.get(i).getId() == libro.getId()) {
                    libros.set(i, libro);
                    guardarTodos(libros);
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public boolean eliminar(int id) {
        try {
            List<Libro> libros = cargarTodos();
            boolean eliminado = libros.removeIf(libro -> libro.getId() == id);
            if (eliminado) {
                guardarTodos(libros);
            }
            return eliminado;
        } catch (IOException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }
    
    // Métodos específicos
    public List<Libro> buscarPorTitulo(String titulo) {
        try {
            List<Libro> libros = cargarTodos();
            return libros.stream()
                        .filter(libro -> libro.getTitulo().toLowerCase()
                                             .contains(titulo.toLowerCase()))
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        } catch (IOException e) {
            System.err.println("Error al buscar por título: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Libro> buscarPorAutor(String autor) {
        try {
            List<Libro> libros = cargarTodos();
            return libros.stream()
                        .filter(libro -> libro.getAutor().toLowerCase()
                                             .contains(autor.toLowerCase()))
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        } catch (IOException e) {
            System.err.println("Error al buscar por autor: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Libro> obtenerLibrosDisponibles() {
        try {
            List<Libro> libros = cargarTodos();
            return libros.stream()
                        .filter(Libro::estaDisponible)
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        } catch (IOException e) {
            System.err.println("Error al obtener libros disponibles: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public List<Libro> getLibros() {
        return obtenerTodos();
    }
    
    public String rutaAbsoluta() {
        try {
            return archivoCsv.getCanonicalPath();
        } catch (IOException e) {
            return archivoCsv.getAbsolutePath();
        }
    }
}