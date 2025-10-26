package controlador;

import modelo.Libro;
import repositorio.BibliotecaRepositorio;
import repositorio.IRepositorio;
import vista.VistaUsuarioExterno;
import java.util.List;

/**
 * Clase que implementa la lógica de negocio para usuarios externos.
 * Permite a los usuarios externos consultar el catálogo de libros y realizar búsquedas.
 */
public class ControladorUsuarioExterno implements IControladorUsuarioExterno {
    private final IRepositorio<Libro> repositorioLibros;
    private final VistaUsuarioExterno vista;

    /**
     * Constructor que inicializa el repositorio y la vista.
     * @param repositorioLibros Repositorio de libros.
     * @param vista Vista específica para usuarios externos.
     */
    public ControladorUsuarioExterno(IRepositorio<Libro> repositorioLibros,
                                   VistaUsuarioExterno vista) {
        this.repositorioLibros = repositorioLibros;
        this.vista = vista;
    }

    /**
     * Método principal que ejecuta el flujo de la aplicación pública.
     */
    @Override
    public void ejecutar() {
        vista.mostrarBienvenida();
        
        int opcion;
        do {
            vista.mostrarMenuPublico();
            opcion = vista.leerOpcion();
            procesarOpcion(opcion);
        } while (opcion != 0);
        
        vista.mostrarDespedida();
    }

    /**
     * Procesa la opción seleccionada por el usuario externo.
     * @param opcion Opción seleccionada.
     */
    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> mostrarCatalogoCompleto();
            case 2 -> buscarPorTitulo();
            case 3 -> buscarPorAutor();
            case 4 -> mostrarLibrosDisponibles();
            case 5 -> mostrarEstadisticas();
            case 6 -> mostrarLibrosPopulares();
            case 7 -> mostrarAyuda();
            case 8 -> mostrarContacto();
            case 0 -> {}
            default -> vista.mostrarMensaje(" Opción inválida. Por favor intenta nuevamente.");
        }
        
        if (opcion != 0 && opcion >= 1 && opcion <= 8) {
            vista.pausa();
        }
    }

    /**
     * Muestra el catálogo completo de libros.
     */
    @Override
    public void mostrarCatalogoCompleto() {
        List<Libro> libros = repositorioLibros.obtenerTodos();
        vista.mostrarCatalogo(libros);
    }

    /**
     * Busca libros por título en el repositorio.
     */
    @Override
    public void buscarPorTitulo() {
        String titulo = vista.leerTexto(" Ingresa el título a buscar: ");
        
        if (titulo.isEmpty()) {
            vista.mostrarMensaje(" Por favor ingresa un título válido.");
            return;
        }
        
        BibliotecaRepositorio repo = (BibliotecaRepositorio) repositorioLibros;
        List<Libro> resultados = repo.buscarPorTitulo(titulo);
        vista.mostrarResultadoBusqueda(resultados, titulo);
    }

    /**
     * Busca libros por autor en el repositorio.
     */
    @Override
    public void buscarPorAutor() {
        String autor = vista.leerTexto(" Ingresa el autor a buscar: ");
        
        if (autor.isEmpty()) {
            vista.mostrarMensaje(" Por favor ingresa un autor válido.");
            return;
        }
        
        BibliotecaRepositorio repo = (BibliotecaRepositorio) repositorioLibros;
        List<Libro> resultados = repo.buscarPorAutor(autor);
        vista.mostrarResultadoBusqueda(resultados, autor);
    }

    /**
     * Muestra los libros disponibles en el repositorio.
     */
    @Override
    public void mostrarLibrosDisponibles() {
        BibliotecaRepositorio repo = (BibliotecaRepositorio) repositorioLibros;
        List<Libro> librosDisponibles = repo.obtenerLibrosDisponibles();
        
        System.out.println("\n LIBROS DISPONIBLES PARA PRÉSTAMO:");
        vista.mostrarCatalogo(librosDisponibles);
    }

    /**
     * Muestra estadísticas generales de la biblioteca.
     */
    @Override
    public void mostrarEstadisticas() {
        List<Libro> todosLosLibros = repositorioLibros.obtenerTodos();
        BibliotecaRepositorio repo = (BibliotecaRepositorio) repositorioLibros;
        
        int totalLibros = todosLosLibros.size();
        int librosDisponibles = repo.obtenerLibrosDisponibles().size();
        int totalUsuarios = 58; // Simulado - en implementación real vendría del repositorio
        int prestamosHoy = 12; // Simulado - en implementación real vendría del repositorio
        
        vista.mostrarEstadisticasPublicas(totalLibros, librosDisponibles, 
                                        totalUsuarios, prestamosHoy);
    }
    
    @Override
    public void mostrarLibrosPopulares() {
        vista.mostrarLibrosPopulares();
    }
    
    @Override
    public void mostrarAyuda() {
        vista.mostrarAyudaPrestamos();
    }
    
    @Override
    public void mostrarContacto() {
        vista.mostrarInformacionContacto();
    }
}