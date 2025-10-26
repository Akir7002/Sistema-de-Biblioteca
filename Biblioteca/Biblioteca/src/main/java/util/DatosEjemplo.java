package util;

import modelo.*;
import repositorio.*;
import java.io.File;

/**
 * Clase utilitaria para manejar la carga de datos de ejemplo de manera centralizada.
 * Solo carga los datos si los archivos CSV no existen.
 */
public class DatosEjemplo {
    
    /**
     * Carga datos de ejemplo solo si los archivos CSV no existen.
     * Esto evita duplicaciones cuando se ejecutan múltiples aplicaciones.
     */
    public static void cargarSiNoExisten(IRepositorio<Libro> repoLibros, 
                                        IRepositorio<Usuario> repoUsuarios, 
                                        IRepositorio<Prestamo> repoPrestamos) {
        
        // Verificar si ya existen archivos CSV
        File librosFile = new File("data/libros.csv");
        File usuariosFile = new File("data/usuarios.csv");
        
        // Solo cargar datos si no existen los archivos principales
        if (!librosFile.exists() && !usuariosFile.exists()) {
            System.out.println("Cargando datos de ejemplo por primera vez...");
            
            // Cargar primero libros y usuarios
            cargarLibrosEjemplo(repoLibros);
            cargarUsuariosEjemplo(repoUsuarios);
            
            // Luego cargar préstamos (que dependen de libros y usuarios)
            cargarPrestamosEjemplo(repoLibros, repoUsuarios, repoPrestamos);
            
            System.out.println("Datos de ejemplo cargados correctamente:");
            System.out.println("- 20 libros en el catálogo");
            System.out.println("- 6 usuarios registrados (3 estudiantes, 2 profesores, 1 administrador)");
            System.out.println("- 2 títulos temporalmente agotados (El Principito y Hamlet)");
            System.out.println("- 2 préstamos activos creados");
        } else {
            System.out.println("Datos existentes encontrados. Cargando desde archivos CSV...");
        }
    }
    
    /**
     * Carga libros de ejemplo.
     */
    private static void cargarLibrosEjemplo(IRepositorio<Libro> repoLibros) {
        // Cargar libros de ejemplo
        repoLibros.crear(new Libro("Don Quijote de la Mancha", "Miguel de Cervantes", 3));
        repoLibros.crear(new Libro("Cien Años de Soledad", "Gabriel García Márquez", 2));
        repoLibros.crear(new Libro("1984", "George Orwell", 4));
        repoLibros.crear(new Libro("El Principito", "Antoine de Saint-Exupéry", 0)); // Agotado
        repoLibros.crear(new Libro("Crimen y Castigo", "Fiódor Dostoyevski", 2));
        repoLibros.crear(new Libro("Orgullo y Prejuicio", "Jane Austen", 3));
        repoLibros.crear(new Libro("El señor de los anillos", "J.R.R. Tolkien", 1));
        repoLibros.crear(new Libro("Hamlet", "William Shakespeare", 0)); // Agotado
        repoLibros.crear(new Libro("La Odisea", "Homero", 3));
        repoLibros.crear(new Libro("Rayuela", "Julio Cortázar", 1));
        repoLibros.crear(new Libro("El Gran Gatsby", "F. Scott Fitzgerald", 2));
        repoLibros.crear(new Libro("Harry Potter y la piedra filosofal", "J.K. Rowling", 4));
        repoLibros.crear(new Libro("El código Da Vinci", "Dan Brown", 2));
        repoLibros.crear(new Libro("Los miserables", "Victor Hugo", 1));
        repoLibros.crear(new Libro("El alquimista", "Paulo Coelho", 5));
        repoLibros.crear(new Libro("Crónica de una muerte anunciada", "Gabriel García Márquez", 2));
        repoLibros.crear(new Libro("La casa de los espíritus", "Isabel Allende", 1));
        repoLibros.crear(new Libro("El perfume", "Patrick Süskind", 3));
        repoLibros.crear(new Libro("Beloved", "Toni Morrison", 1));
        repoLibros.crear(new Libro("El nombre de la rosa", "Umberto Eco", 2));
    }
    
    /**
     * Carga usuarios de ejemplo.
     */
    private static void cargarUsuariosEjemplo(IRepositorio<Usuario> repoUsuarios) {
        // Cargar usuarios de ejemplo
        repoUsuarios.crear(new Usuario("Ana García", "ana.garcia@email.com", "123-456-7890", TipoUsuario.ESTUDIANTE));
        repoUsuarios.crear(new Usuario("Carlos López", "carlos.lopez@email.com", "123-456-7891", TipoUsuario.PROFESOR));
        repoUsuarios.crear(new Usuario("María Rodríguez", "maria.rodriguez@email.com", "123-456-7892", TipoUsuario.ESTUDIANTE));
        repoUsuarios.crear(new Usuario("Dr. Juan Pérez", "juan.perez@email.com", "123-456-7893", TipoUsuario.ADMINISTRADOR));
        repoUsuarios.crear(new Usuario("Laura Martínez", "laura.martinez@email.com", "123-456-7894", TipoUsuario.PROFESOR));
        repoUsuarios.crear(new Usuario("Pedro Sánchez", "pedro.sanchez@email.com", "123-456-7895", TipoUsuario.ESTUDIANTE));
    }
    
    /**
     * Carga préstamos de ejemplo.
     */
    private static void cargarPrestamosEjemplo(IRepositorio<Libro> repoLibros, 
                                              IRepositorio<Usuario> repoUsuarios, 
                                              IRepositorio<Prestamo> repoPrestamos) {
        // Crear algunos préstamos de ejemplo
        try {
            // Obtener algunos libros y usuarios para crear préstamos
            Libro libro1 = repoLibros.obtenerPorId(1); // Don Quijote
            Libro libro2 = repoLibros.obtenerPorId(2); // Cien Años
            Usuario usuario1 = repoUsuarios.obtenerPorId(1); // Ana García
            Usuario usuario2 = repoUsuarios.obtenerPorId(3); // María Rodríguez
            
            if (libro1 != null && usuario1 != null && libro1.prestar()) {
                Prestamo prestamo1 = new Prestamo(usuario1, libro1);
                repoPrestamos.crear(prestamo1);
                usuario1.agregarPrestamo(prestamo1);
                repoUsuarios.actualizar(usuario1);
                repoLibros.actualizar(libro1);
            }
            
            if (libro2 != null && usuario2 != null && libro2.prestar()) {
                Prestamo prestamo2 = new Prestamo(usuario2, libro2);
                repoPrestamos.crear(prestamo2);
                usuario2.agregarPrestamo(prestamo2);
                repoUsuarios.actualizar(usuario2);
                repoLibros.actualizar(libro2);
            }
            
        } catch (Exception e) {
            System.err.println("Error al crear préstamos de ejemplo: " + e.getMessage());
        }
    }
    
    /**
     * Fuerza la carga de datos de ejemplo (para casos especiales o testing).
     */
    public static void forzarCargaDatos(IRepositorio<Libro> repoLibros, 
                                       IRepositorio<Usuario> repoUsuarios, 
                                       IRepositorio<Prestamo> repoPrestamos) {
        cargarLibrosEjemplo(repoLibros);
        cargarUsuariosEjemplo(repoUsuarios);
        cargarPrestamosEjemplo(repoLibros, repoUsuarios, repoPrestamos);
    }
}