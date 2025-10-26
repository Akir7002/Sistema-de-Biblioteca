package controlador;

import repositorio.BibliotecaRepositorio;
import repositorio.IRepositorio;
import modelo.Libro;
import vista.VistaUsuarioExterno;

/**
 * Clase que representa la aplicación pública para usuarios externos.
 * Permite a los usuarios consultar el catálogo de libros y realizar búsquedas.
 */
public class AplicacionUsuarioExterno {

    /**
     * Método principal que sirve como punto de entrada para la aplicación pública.
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        iniciarCatalogoPublico();
    }

    /**
     * Método que inicializa los componentes necesarios para el catálogo público,
     * incluyendo el repositorio, la vista y el controlador.
     */
    private static void iniciarCatalogoPublico() {
        // Crear repositorio (solo lectura)
        IRepositorio<Libro> repositorioLibros = new BibliotecaRepositorio();

        // Crear vista específica para usuarios externos
        VistaUsuarioExterno vista = new VistaUsuarioExterno();

        // Crear controlador específico
        ControladorUsuarioExterno controlador = new ControladorUsuarioExterno(
                repositorioLibros, vista);

        // Cargar datos de ejemplo
        cargarDatosEjemplo(repositorioLibros);

        // Ejecutar aplicación pública
        controlador.ejecutar();
    }

    /**
     * Método que carga datos de ejemplo en el repositorio de libros.
     * @param repositorio Repositorio donde se almacenarán los libros de ejemplo.
     */
    private static void cargarDatosEjemplo(IRepositorio<Libro> repositorio) {
        // Libros de ejemplo para consulta pública
        repositorio.crear(new Libro("Don Quijote de la Mancha", "Miguel de Cervantes", 3));
        repositorio.crear(new Libro("Cien Años de Soledad", "Gabriel García Márquez", 2));
        repositorio.crear(new Libro("1984", "George Orwell", 4));
        repositorio.crear(new Libro("El Principito", "Antoine de Saint-Exupéry", 0)); // Agotado
        repositorio.crear(new Libro("Orgullo y Prejuicio", "Jane Austen", 3));
        repositorio.crear(new Libro("Crimen y Castigo", "Fiódor Dostoyevski", 1));
        repositorio.crear(new Libro("La Odisea", "Homero", 2));
        repositorio.crear(new Libro("Hamlet", "William Shakespeare", 0)); // Agotado
        repositorio.crear(new Libro("El Gran Gatsby", "F. Scott Fitzgerald", 2));
        repositorio.crear(new Libro("Rayuela", "Julio Cortázar", 1));
    }
}

