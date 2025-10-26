package controlador;

/**
 * Interfaz que define los métodos principales para el controlador de la biblioteca.
 * Proporciona las operaciones necesarias para gestionar libros, usuarios y préstamos.
 */
public interface IControlador {

    void ejecutar();
    void agregarLibro();
    void listarLibros();
    void buscarLibro();
    void eliminarLibro();
    void agregarUsuario();
    void listarUsuarios();
    void buscarUsuario();
    void eliminarUsuario();
    void realizarPrestamo();
    void devolverLibro();
    void listarPrestamosActivos();
    void mostrarPrestamosVencidos();
    void mostrarEstadisticas();
}