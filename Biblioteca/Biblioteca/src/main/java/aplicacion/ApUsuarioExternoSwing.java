package aplicacion;

import modelo.Libro;
import repositorio.BibliotecaRepositorio;
import repositorio.IRepositorio;
import vista.VistaUsuarioExternoSwing;
import controlador.ControladorUsuarioExternoSwing;
import util.DatosEjemplo;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ApUsuarioExternoSwing {
    
    public static void main(String[] args) {
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
                iniciarCatalogoPublico();
            } catch (Exception e) {
                System.err.println("Error al iniciar el catálogo público: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
    
    private static void iniciarCatalogoPublico() {
        System.out.println("Iniciando Catálogo Público de la Biblioteca...");
        
        // Crear repositorio (solo lectura)
        IRepositorio<Libro> repositorioLibros = new BibliotecaRepositorio();
        
        // Crear vista específica para usuarios externos
        VistaUsuarioExternoSwing vista = new VistaUsuarioExternoSwing();
        
        // Crear controlador específico
        ControladorUsuarioExternoSwing controlador = new ControladorUsuarioExternoSwing(
                repositorioLibros, vista);
        
        // Cargar datos de ejemplo solo si no existen archivos CSV
        DatosEjemplo.cargarSiNoExisten(repositorioLibros, null, null);
        
        // Ejecutar aplicación pública
        controlador.ejecutar();
        
        System.out.println("Catálogo Público iniciado correctamente.");
    }
}