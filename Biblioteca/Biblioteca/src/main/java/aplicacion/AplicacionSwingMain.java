package aplicacion;

// Imports específicos para evitar conflictos
import modelo.Libro;
import modelo.Usuario;
import modelo.Prestamo;
import modelo.TipoUsuario;
import repositorio.BibliotecaRepositorio;
import repositorio.UsuarioRepositorio;
import repositorio.PrestamoRepositorio;
import repositorio.IRepositorio;
import vista.BibliotecaSwingVista;
import controlador.BibliotecaSwingControlador;
import util.DatosEjemplo;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class AplicacionSwingMain {
    
    public static void main(String[] args) {
        // Configurar codificación UTF-8
        System.setProperty("file.encoding", "UTF-8");
        
        // Configurar el Look and Feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("No se pudo configurar el Look and Feel: " + e.getMessage());
            // Continuar con el Look and Feel por defecto
        }
        
        // Ejecutar la aplicación en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                iniciarAplicacion();
            } catch (Exception e) {
                System.err.println("Error al iniciar la aplicación: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    private static void iniciarAplicacion() {
        System.out.println("Iniciando Sistema de Biblioteca con interfaz gráfica completa...");
        
        // Crear instancias siguiendo el patrón MVC
        BibliotecaRepositorio repositorioLibros = new BibliotecaRepositorio();
        UsuarioRepositorio repositorioUsuarios = new UsuarioRepositorio();
        PrestamoRepositorio repositorioPrestamos = new PrestamoRepositorio();
        
        // Configurar las dependencias entre repositorios
        repositorioPrestamos.setUsuarioRepositorio(repositorioUsuarios);
        repositorioPrestamos.setBibliotecaRepositorio(repositorioLibros);
        
        BibliotecaSwingVista vista = new BibliotecaSwingVista();
        BibliotecaSwingControlador controlador = new BibliotecaSwingControlador(
                repositorioLibros, repositorioUsuarios, repositorioPrestamos, vista);
        
        // Cargar datos de ejemplo solo si no existen archivos CSV
        DatosEjemplo.cargarSiNoExisten(repositorioLibros, repositorioUsuarios, repositorioPrestamos);
        
        // Ejecutar aplicación
        controlador.ejecutar();
        
        System.out.println("Sistema de Biblioteca iniciado correctamente.");
    }
}