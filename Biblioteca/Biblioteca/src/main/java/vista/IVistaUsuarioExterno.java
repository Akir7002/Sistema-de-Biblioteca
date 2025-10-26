package vista;

import modelo.Libro;
import java.util.List;

public interface IVistaUsuarioExterno {
    void mostrarMenuPublico();
    void mostrarCatalogo(List<Libro> libros);
    void mostrarDetalleLibro(Libro libro);
    void mostrarResultadoBusqueda(List<Libro> libros, String termino);
    void mostrarMensaje(String mensaje);
    void mostrarBienvenida();
    void mostrarDespedida();
    int leerOpcion();
    String leerTexto(String prompt);
    void pausa();
}