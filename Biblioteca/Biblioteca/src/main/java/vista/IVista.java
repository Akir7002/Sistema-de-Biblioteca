package vista;

import modelo.Libro;
import modelo.Usuario;
import modelo.Prestamo;
import java.util.List;

public interface IVista {
    void mostrarMenu();
    void mostrarLibros(List<Libro> libros);
    void mostrarUsuarios(List<Usuario> usuarios);
    void mostrarPrestamos(List<Prestamo> prestamos);
    void mostrarMensaje(String mensaje);
    void mostrarError(String error);
    void mostrarExito(String mensaje);
    int leerOpcion();
    String leerTexto(String prompt);
    int leerNumero(String prompt);
    void limpiarPantalla();
    void pausa();
}