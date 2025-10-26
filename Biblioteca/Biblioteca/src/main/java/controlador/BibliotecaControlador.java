package controlador;

import modelo.*;
import repositorio.*;
import vista.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que implementa la lógica de negocio de la biblioteca.
 * Conecta los repositorios con la vista para gestionar libros, usuarios y préstamos.
 */
public class BibliotecaControlador implements IControlador {
    private final IRepositorio<Libro> repositorioLibros;
    private final IRepositorio<Usuario> repositorioUsuarios;
    private final IRepositorio<Prestamo> repositorioPrestamos;
    private final IVista vista;

    /**
     * Constructor que inicializa los repositorios y la vista.
     * @param repositorioLibros Repositorio de libros.
     * @param repositorioUsuarios Repositorio de usuarios.
     * @param repositorioPrestamos Repositorio de préstamos.
     * @param vista Vista para interactuar con el usuario.
     */
    public BibliotecaControlador(IRepositorio<Libro> repositorioLibros,
                               IRepositorio<Usuario> repositorioUsuarios,
                               IRepositorio<Prestamo> repositorioPrestamos,
                               IVista vista) {
        this.repositorioLibros = repositorioLibros;
        this.repositorioUsuarios = repositorioUsuarios;
        this.repositorioPrestamos = repositorioPrestamos;
        this.vista = vista;
    }

    /**
     * Método principal que ejecuta el flujo de la aplicación.
     */
    @Override
    public void ejecutar() {
        int opcion;
        do {
            vista.mostrarMenu();
            opcion = vista.leerOpcion();
            procesarOpcion(opcion);
        } while (opcion != 0);

        vista.mostrarExito("¡Gracias por usar el Sistema de Gestión de Biblioteca!");
    }

    /**
     * Procesa la opción seleccionada por el usuario.
     * @param opcion Opción seleccionada.
     */
    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> agregarLibro();
            case 2 -> listarLibros();
            case 3 -> buscarLibro();
            case 4 -> eliminarLibro();
            case 5 -> agregarUsuario();
            case 6 -> listarUsuarios();
            case 7 -> buscarUsuario();
            case 8 -> eliminarUsuario();
            case 9 -> realizarPrestamo();
            case 10 -> devolverLibro();
            case 11 -> listarPrestamosActivos();
            case 12 -> mostrarPrestamosVencidos();
            case 13 -> mostrarEstadisticas();
            case 0 -> {}
            default -> vista.mostrarError("Opción inválida. Intente nuevamente.");
        }

        if (opcion != 0) {
            vista.pausa();
        }
    }

    /**
     * Agrega un nuevo libro al repositorio.
     */
    @Override
    public void agregarLibro() {
        BibliotecaVista bibliotecaVista = (BibliotecaVista) vista;
        Libro nuevoLibro = bibliotecaVista.solicitarDatosLibro();

        if (repositorioLibros.crear(nuevoLibro)) {
            vista.mostrarExito("Libro agregado exitosamente.");
            vista.mostrarMensaje(" ID asignado automáticamente: " + nuevoLibro.getId());
            vista.mostrarMensaje(" Título: " + nuevoLibro.getTitulo());
            vista.mostrarMensaje(" Puede buscar este libro por ID, título o autor usando la opción 3.");
        } else {
            vista.mostrarError("Error al agregar el libro.");
        }
    }

    /**
     * Lista todos los libros disponibles en el repositorio.
     */
    @Override
    public void listarLibros() {
        List<Libro> libros = repositorioLibros.obtenerTodos();
        vista.mostrarLibros(libros);
    }

    /**
     * Busca un libro en el repositorio por título o autor.
     */
    @Override
    public void buscarLibro() {
        vista.mostrarMensaje("\n OPCIONES DE BÚSQUEDA:");
        vista.mostrarMensaje("1. Buscar por ID (el ID se asigna automáticamente al crear libro)");
        vista.mostrarMensaje("2. Buscar por título");
        vista.mostrarMensaje("3. Buscar por autor");
        vista.mostrarMensaje("4. Mostrar solo libros disponibles");
        
        BibliotecaVista bibliotecaVista = (BibliotecaVista) vista;
        int opcionBusqueda = bibliotecaVista.leerOpcionConValidacion(1, 4, "Seleccione una opción (1-4): ");

        BibliotecaRepositorio repo = (BibliotecaRepositorio) repositorioLibros;

        switch (opcionBusqueda) {
            case 1 -> {
                // Mostrar lista de libros para referencia
                List<Libro> todosLibros = repositorioLibros.obtenerTodos();
                if (!todosLibros.isEmpty()) {
                    vista.mostrarMensaje("\n Lista de libros para referencia:");
                    vista.mostrarLibros(todosLibros);
                }
                
                int id = vista.leerNumero(" Ingrese el ID del libro a buscar: ");
                Libro libro = repositorioLibros.obtenerPorId(id);
                if (libro != null) {
                    vista.mostrarMensaje("\n Libro encontrado:");
                    vista.mostrarMensaje(libro.toString());
                } else {
                    vista.mostrarError("Libro no encontrado con ID: " + id);
                }
            }
            case 2 -> {
                String titulo = vista.leerTexto(" Ingrese el título: ");
                List<Libro> librosPorTitulo = repo.buscarPorTitulo(titulo);
                if (librosPorTitulo.isEmpty()) {
                    vista.mostrarError("No se encontraron libros con el título: " + titulo);
                } else {
                    vista.mostrarLibros(librosPorTitulo);
                }
            }
            case 3 -> {
                String autor = vista.leerTexto(" Ingrese el autor: ");
                List<Libro> librosPorAutor = repo.buscarPorAutor(autor);
                if (librosPorAutor.isEmpty()) {
                    vista.mostrarError("No se encontraron libros del autor: " + autor);
                } else {
                    vista.mostrarLibros(librosPorAutor);
                }
            }
            case 4 -> {
                List<Libro> librosDisponibles = repo.obtenerLibrosDisponibles();
                if (librosDisponibles.isEmpty()) {
                    vista.mostrarError("No hay libros disponibles en este momento.");
                } else {
                    vista.mostrarLibros(librosDisponibles);
                }
            }
        }
    }

    /**
     * Elimina un libro del repositorio.
     */
    @Override
    public void eliminarLibro() {
        int id = vista.leerNumero(" Ingrese el ID del libro a eliminar: ");
        Libro libro = repositorioLibros.obtenerPorId(id);

        if (libro == null) {
            vista.mostrarError("Libro no encontrado.");
            return;
        }

        // Verificar si tiene préstamos activos
        PrestamoRepositorio prestamosRepo = (PrestamoRepositorio) repositorioPrestamos;
        List<Prestamo> prestamosActivos = prestamosRepo.obtenerPorLibro(libro)
                .stream()
                .filter(p -> p.getEstado() == EstadoPrestamo.ACTIVO)
                .collect(Collectors.toList());

        if (!prestamosActivos.isEmpty()) {
            vista.mostrarError("No se puede eliminar el libro. Tiene " +
                             prestamosActivos.size() + " préstamo(s) activo(s).");
            return;
        }

        vista.mostrarMensaje(" Libro a eliminar: " + libro.getTitulo());
        String confirmacion = vista.leerTexto(" ¿Está seguro? (s/N): ");

        if (confirmacion.toLowerCase().equals("s")) {
            if (repositorioLibros.eliminar(id)) {
                vista.mostrarExito("Libro eliminado exitosamente.");
            } else {
                vista.mostrarError("Error al eliminar el libro.");
            }
        } else {
            vista.mostrarMensaje("Operación cancelada.");
        }
    }

    /**
     * Agrega un nuevo usuario al repositorio.
     */
    @Override
    public void agregarUsuario() {
        BibliotecaVista bibliotecaVista = (BibliotecaVista) vista;
        Usuario nuevoUsuario = bibliotecaVista.solicitarDatosUsuario();

        if (nuevoUsuario != null && repositorioUsuarios.crear(nuevoUsuario)) {
            vista.mostrarExito("Usuario agregado exitosamente.");
            vista.mostrarMensaje(" ID asignado automáticamente: " + nuevoUsuario.getId());
            vista.mostrarMensaje(" Email: " + nuevoUsuario.getEmail());
            vista.mostrarMensaje(" Puede buscar este usuario por ID, nombre o email usando la opción 7.");
        } else {
            vista.mostrarError("Error al agregar el usuario. Verifique que el email no esté en uso.");
        }
    }

    /**
     * Lista todos los usuarios disponibles en el repositorio.
     */
    @Override
    public void listarUsuarios() {
        List<Usuario> usuarios = repositorioUsuarios.obtenerTodos();
        vista.mostrarUsuarios(usuarios);
    }

    /**
     * Busca un usuario en el repositorio por nombre o email.
     */
    @Override
    public void buscarUsuario() {
        vista.mostrarMensaje("\n OPCIONES DE BÚSQUEDA:");
        vista.mostrarMensaje("1. Buscar por ID (el ID se asigna automáticamente al crear usuario)");
        vista.mostrarMensaje("2. Buscar por nombre");
        vista.mostrarMensaje("3. Buscar por email");
        vista.mostrarMensaje("4. Filtrar por tipo");
        
        BibliotecaVista bibliotecaVista = (BibliotecaVista) vista;
        int opcionBusqueda = bibliotecaVista.leerOpcionConValidacion(1, 4, "Seleccione una opción (1-4): ");

        UsuarioRepositorio repo = (UsuarioRepositorio) repositorioUsuarios;

        switch (opcionBusqueda) {
            case 1 -> {
                // Mostrar lista de usuarios para referencia
                List<Usuario> todosUsuarios = repositorioUsuarios.obtenerTodos();
                if (!todosUsuarios.isEmpty()) {
                    vista.mostrarMensaje("\n Lista de usuarios para referencia:");
                    vista.mostrarUsuarios(todosUsuarios);
                }
                
                int id = vista.leerNumero(" Ingrese el ID del usuario a buscar: ");
                Usuario usuario = repositorioUsuarios.obtenerPorId(id);
                if (usuario != null) {
                    vista.mostrarMensaje("\n Usuario encontrado:");
                    vista.mostrarUsuarios(List.of(usuario));
                } else {
                    vista.mostrarError("Usuario no encontrado con ID: " + id);
                }
            }
            case 2 -> {
                String nombre = vista.leerTexto(" Ingrese el nombre: ");
                List<Usuario> usuariosPorNombre = repo.buscarPorNombre(nombre);
                if (usuariosPorNombre.isEmpty()) {
                    vista.mostrarError("No se encontraron usuarios con el nombre: " + nombre);
                } else {
                    vista.mostrarUsuarios(usuariosPorNombre);
                }
            }
            case 3 -> {
                String email = vista.leerTexto(" Ingrese el email: ");
                Usuario usuarioPorEmail = repo.buscarPorEmail(email);
                if (usuarioPorEmail != null) {
                    vista.mostrarUsuarios(List.of(usuarioPorEmail));
                } else {
                    vista.mostrarError("No se encontró usuario con el email: " + email);
                }
            }
            case 4 -> {
                vista.mostrarMensaje("Tipos de usuario:");
                vista.mostrarMensaje("1. Estudiante");
                vista.mostrarMensaje("2. Profesor");
                vista.mostrarMensaje("3. Administrador");
                int tipoOpcion = bibliotecaVista.leerOpcionConValidacion(1, 3, "Seleccione tipo (1-3): ");

                TipoUsuario tipo = switch (tipoOpcion) {
                    case 1 -> TipoUsuario.ESTUDIANTE;
                    case 2 -> TipoUsuario.PROFESOR;
                    case 3 -> TipoUsuario.ADMINISTRADOR;
                    default -> null; // No debería llegar aquí debido a la validación
                };

                if (tipo != null) {
                    List<Usuario> usuariosPorTipo = repo.obtenerPorTipo(tipo);
                    if (usuariosPorTipo.isEmpty()) {
                        vista.mostrarError("No se encontraron usuarios del tipo: " + tipo.getDescripcion());
                    } else {
                        vista.mostrarUsuarios(usuariosPorTipo);
                    }
                }
            }
        }
    }

    /**
     * Elimina un usuario del repositorio.
     */
    @Override
    public void eliminarUsuario() {
        int id = vista.leerNumero(" Ingrese el ID del usuario a eliminar: ");
        Usuario usuario = repositorioUsuarios.obtenerPorId(id);

        if (usuario == null) {
            vista.mostrarError("Usuario no encontrado.");
            return;
        }

        // Verificar si tiene préstamos activos
        if (usuario.getPrestamosActivos() > 0) {
            vista.mostrarError("No se puede eliminar el usuario. Tiene " +
                             usuario.getPrestamosActivos() + " préstamo(s) activo(s).");
            return;
        }

        vista.mostrarMensaje(" Usuario a eliminar: " + usuario.getNombre());
        String confirmacion = vista.leerTexto(" ¿Está seguro? (s/N): ");

        if (confirmacion.toLowerCase().equals("s")) {
            if (repositorioUsuarios.eliminar(id)) {
                vista.mostrarExito("Usuario eliminado exitosamente.");
            } else {
                vista.mostrarError("Error al eliminar el usuario.");
            }
        } else {
            vista.mostrarMensaje("Operación cancelada.");
        }
    }

    /**
     * Realiza un préstamo de un libro a un usuario.
     */
    @Override
    public void realizarPrestamo() {
        // Seleccionar libro
        int idLibro = vista.leerNumero(" Ingrese el ID del libro a prestar: ");
        Libro libro = repositorioLibros.obtenerPorId(idLibro);

        if (libro == null) {
            vista.mostrarError("Libro no encontrado.");
            return;
        }

        if (!libro.estaDisponible()) {
            vista.mostrarError("El libro no está disponible para préstamo.");
            return;
        }

        // Seleccionar usuario
        int idUsuario = vista.leerNumero("👤 Ingrese el ID del usuario: ");
        Usuario usuario = repositorioUsuarios.obtenerPorId(idUsuario);

        if (usuario == null) {
            vista.mostrarError("Usuario no encontrado.");
            return;
        }

        if (!usuario.isActivo()) {
            vista.mostrarError("El usuario no está activo.");
            return;
        }

        if (!usuario.puedePrestar()) {
            vista.mostrarError("El usuario ha alcanzado su límite de préstamos (" +
                             usuario.getTipoUsuario().getLimitePrestamos() + ").");
            return;
        }

        // Crear préstamo
        Prestamo prestamo = new Prestamo(usuario, libro);

        // Mostrar resumen
        vista.mostrarMensaje("\n RESUMEN DEL PRÉSTAMO:");
        vista.mostrarMensaje(" Libro: " + libro.getTitulo());
        vista.mostrarMensaje(" Usuario: " + usuario.getNombre());
        vista.mostrarMensaje(" Fecha límite: " + prestamo.getFechaDevolucionEsperada());

        String confirmacion = vista.leerTexto(" ¿Confirmar préstamo? (s/N): ");

        if (confirmacion.toLowerCase().equals("s")) {
            if (libro.prestar() && repositorioPrestamos.crear(prestamo) &&
                repositorioLibros.actualizar(libro)) {
                usuario.agregarPrestamo(prestamo);
                repositorioUsuarios.actualizar(usuario);
                vista.mostrarExito("Préstamo realizado exitosamente. ID: " + prestamo.getId());
            } else {
                vista.mostrarError("Error al realizar el préstamo.");
            }
        } else {
            vista.mostrarMensaje("Préstamo cancelado.");
        }
    }

    /**
     * Procesa la devolución de un libro prestado.
     */
    @Override
    public void devolverLibro() {
        int idPrestamo = vista.leerNumero(" Ingrese el ID del préstamo a devolver: ");
        Prestamo prestamo = repositorioPrestamos.obtenerPorId(idPrestamo);

        if (prestamo == null) {
            vista.mostrarError("Préstamo no encontrado.");
            return;
        }

        if (prestamo.getEstado() != EstadoPrestamo.ACTIVO) {
            vista.mostrarError("El préstamo ya fue devuelto.");
            return;
        }

        // Mostrar información del préstamo
        vista.mostrarMensaje("\n INFORMACIÓN DEL PRÉSTAMO:");
        vista.mostrarMensaje(" Libro: " + prestamo.getLibro().getTitulo());
        vista.mostrarMensaje(" Usuario: " + prestamo.getUsuario().getNombre());
        vista.mostrarMensaje(" Fecha límite: " + prestamo.getFechaDevolucionEsperada());

        if (prestamo.estaVencido()) {
            vista.mostrarMensaje(" PRÉSTAMO VENCIDO - Días de retraso: " + prestamo.getDiasRetraso());
        }

        String confirmacion = vista.leerTexto(" ¿Confirmar devolución? (s/N): ");

        if (confirmacion.toLowerCase().equals("s")) {
            prestamo.devolver();
            prestamo.getLibro().devolver();

            if (repositorioPrestamos.actualizar(prestamo) &&
                repositorioLibros.actualizar(prestamo.getLibro())) {
                vista.mostrarExito("Libro devuelto exitosamente.");
                if (prestamo.getDiasRetraso() > 0) {
                    vista.mostrarMensaje("📝 Nota: El libro fue devuelto con " +
                                       prestamo.getDiasRetraso() + " día(s) de retraso.");
                }
            } else {
                vista.mostrarError("Error al procesar la devolución.");
            }
        } else {
            vista.mostrarMensaje("Devolución cancelada.");
        }
    }

    /**
     * Lista todos los préstamos activos en el repositorio.
     */
    @Override
    public void listarPrestamosActivos() {
        PrestamoRepositorio repo = (PrestamoRepositorio) repositorioPrestamos;
        List<Prestamo> prestamosActivos = repo.obtenerPrestamosActivos();
        vista.mostrarPrestamos(prestamosActivos);
    }

    /**
     * Muestra los préstamos vencidos en el repositorio.
     */
    @Override
    public void mostrarPrestamosVencidos() {
        PrestamoRepositorio repo = (PrestamoRepositorio) repositorioPrestamos;
        List<Prestamo> prestamosVencidos = repo.obtenerPrestamosVencidos();

        if (prestamosVencidos.isEmpty()) {
            vista.mostrarExito("¡No hay préstamos vencidos!");
            return;
        }

        vista.mostrarMensaje(" PRÉSTAMOS VENCIDOS:");
        vista.mostrarPrestamos(prestamosVencidos);
    }

    /**
     * Muestra estadísticas generales de la biblioteca.
     */
    @Override
    public void mostrarEstadisticas() {
        // Obtener datos
        List<Libro> todosLosLibros = repositorioLibros.obtenerTodos();
        List<Usuario> todosLosUsuarios = repositorioUsuarios.obtenerTodos();
        PrestamoRepositorio prestamoRepo = (PrestamoRepositorio) repositorioPrestamos;

        int totalLibros = todosLosLibros.size();
        int librosDisponibles = (int) todosLosLibros.stream()
                                      .filter(Libro::estaDisponible)
                                      .count();
        int totalUsuarios = todosLosUsuarios.size();
        int prestamosActivos = prestamoRepo.obtenerPrestamosActivos().size();
        int prestamosVencidos = prestamoRepo.obtenerPrestamosVencidos().size();

        BibliotecaVista bibliotecaVista = (BibliotecaVista) vista;
        bibliotecaVista.mostrarEstadisticas(totalLibros, librosDisponibles,
                                          totalUsuarios, prestamosActivos, prestamosVencidos);
    }
}