package controlador;

/**
 * Interfaz que define las operaciones del controlador para usuarios externos.
 */
public interface IControladorUsuarioExterno {
    void ejecutar();
    void mostrarCatalogoCompleto();
    void buscarPorTitulo();
    void buscarPorAutor();
    void mostrarLibrosDisponibles();
    void mostrarEstadisticas();
    void mostrarLibrosPopulares();
    void mostrarAyuda();
    void mostrarContacto();
}

