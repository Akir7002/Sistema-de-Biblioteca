package aplicacion;

import modelo.*;
import repositorio.*;
import vista.*;
import controlador.*;
import util.DatosEjemplo;

/**
 * Clase principal que inicia la aplicación de la biblioteca.
 * Contiene el método main y los métodos necesarios para inicializar
 * los repositorios, la vista y el controlador.
 */
public class MainConsola {

    /**
     * Método principal que sirve como punto de entrada de la aplicación.
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        iniciarAplicacion();
    }

    /**
     * Método que inicializa los componentes principales de la aplicación,
     * como los repositorios, la vista y el controlador.
     */
    private static void iniciarAplicacion() {
        BibliotecaRepositorio repositorioLibros = new BibliotecaRepositorio();
        UsuarioRepositorio repositorioUsuarios = new UsuarioRepositorio();
        PrestamoRepositorio repositorioPrestamos = new PrestamoRepositorio();
        
        // Configurar las dependencias entre repositorios
        repositorioPrestamos.setUsuarioRepositorio(repositorioUsuarios);
        repositorioPrestamos.setBibliotecaRepositorio(repositorioLibros);
        
        IVista vista = new BibliotecaVista();
        IControlador controlador = new BibliotecaControlador(
                repositorioLibros, repositorioUsuarios, repositorioPrestamos, vista);
        
        // Cargar datos de ejemplo solo si no existen archivos CSV
        DatosEjemplo.cargarSiNoExisten(repositorioLibros, repositorioUsuarios, repositorioPrestamos);

        controlador.ejecutar();
    }
}