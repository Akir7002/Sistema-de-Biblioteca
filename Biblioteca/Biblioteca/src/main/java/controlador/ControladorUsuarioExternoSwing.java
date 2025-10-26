package controlador;

import vista.VistaUsuarioExternoSwing;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import modelo.Libro;
import repositorio.IRepositorio;
import repositorio.BibliotecaRepositorio;

public class ControladorUsuarioExternoSwing implements IControladorUsuarioExterno, ActionListener {
    private final IRepositorio<Libro> repositorioLibros;
    private final VistaUsuarioExternoSwing vista;
    
    public ControladorUsuarioExternoSwing(IRepositorio<Libro> repositorioLibros,
                                         VistaUsuarioExternoSwing vista) {
        this.repositorioLibros = repositorioLibros;
        this.vista = vista;
        
        // Conectar la vista con este controlador
        vista.setControladorListener(this);
    }
    
    @Override
    public void ejecutar() {
        mostrarCatalogoCompleto();
        mostrarEstadisticas();
        vista.mostrarMenuPublico();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        
        try {
            switch (comando) {
                case "BUSCAR" -> realizarBusqueda();
                case "MOSTRAR_TODOS" -> mostrarCatalogoCompleto();
                case "SOLO_DISPONIBLES" -> mostrarLibrosDisponibles();
                case "POPULARES" -> mostrarLibrosPopulares();
                case "AYUDA" -> mostrarAyuda();
                case "CONTACTO" -> mostrarContacto();
                default -> vista.mostrarMensaje("Comando no reconocido: " + comando);
            }
        } catch (Exception ex) {
            vista.mostrarMensaje("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void realizarBusqueda() {
        String textoBusqueda = vista.getTextoBusqueda();
        String tipoBusqueda = vista.getTipoBusqueda();
        
        if (textoBusqueda.isEmpty()) {
            vista.mostrarMensaje("Por favor ingrese un término de búsqueda.");
            return;
        }
        
        if ("Por Título".equals(tipoBusqueda)) {
            buscarPorTitulo();
        } else if ("Por Autor".equals(tipoBusqueda)) {
            buscarPorAutor();
        }
    }
    
    @Override
    public void mostrarCatalogoCompleto() {
        List<Libro> libros = repositorioLibros.obtenerTodos();
        vista.mostrarCatalogo(libros);
    }
    
    @Override
    public void buscarPorTitulo() {
        String titulo = vista.getTextoBusqueda();
        
        if (titulo.isEmpty()) {
            vista.mostrarMensaje("Por favor ingresa un título válido.");
            return;
        }
        
        BibliotecaRepositorio repo = (BibliotecaRepositorio) repositorioLibros;
        List<Libro> resultados = repo.buscarPorTitulo(titulo);
        vista.mostrarResultadoBusqueda(resultados, titulo);
    }
    
    @Override
    public void buscarPorAutor() {
        String autor = vista.getTextoBusqueda();
        
        if (autor.isEmpty()) {
            vista.mostrarMensaje("Por favor ingresa un autor válido.");
            return;
        }
        
        BibliotecaRepositorio repo = (BibliotecaRepositorio) repositorioLibros;
        List<Libro> resultados = repo.buscarPorAutor(autor);
        vista.mostrarResultadoBusqueda(resultados, autor);
    }
    
    @Override
    public void mostrarLibrosDisponibles() {
        BibliotecaRepositorio repo = (BibliotecaRepositorio) repositorioLibros;
        List<Libro> librosDisponibles = repo.obtenerLibrosDisponibles();
        
        vista.mostrarCatalogo(librosDisponibles);
        vista.mostrarMensaje("Se encontraron " + librosDisponibles.size() + " libro(s) disponible(s).");
    }
    
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