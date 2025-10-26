package controlador;

import modelo.*;
import repositorio.*;
import vista.BibliotecaSwingVista;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class BibliotecaSwingControlador implements IControlador, ActionListener {
    private final IRepositorio<Libro> repositorioLibros;
    private final IRepositorio<Usuario> repositorioUsuarios;
    private final IRepositorio<Prestamo> repositorioPrestamos;
    private final BibliotecaSwingVista vista;
    
    public BibliotecaSwingControlador(IRepositorio<Libro> repositorioLibros,
                                     IRepositorio<Usuario> repositorioUsuarios,
                                     IRepositorio<Prestamo> repositorioPrestamos,
                                     BibliotecaSwingVista vista) {
        this.repositorioLibros = repositorioLibros;
        this.repositorioUsuarios = repositorioUsuarios;
        this.repositorioPrestamos = repositorioPrestamos;
        this.vista = vista;
        
        // Conectar la vista con este controlador
        vista.setControladorListener(this);
    }
    
    @Override
    public void ejecutar() {
        vista.mostrarMensaje("Sistema de Biblioteca iniciado correctamente.");
        cargarDatosIniciales();
        vista.mostrarMenu();
    }
    
    private void cargarDatosIniciales() {
        listarLibros();
        listarUsuarios();
        listarPrestamosActivos();
        mostrarEstadisticas();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        
        try {
            switch (comando) {
                case "AGREGAR_LIBRO":
                    agregarLibro();
                    break;
                case "ELIMINAR_LIBRO":
                    eliminarLibroSeleccionado();
                    break;
                case "LISTAR_LIBROS":
                    listarLibros();
                    break;
                case "AGREGAR_USUARIO":
                    agregarUsuario();
                    break;
                case "ELIMINAR_USUARIO":
                    eliminarUsuarioSeleccionado();
                    break;
                case "LISTAR_USUARIOS":
                    listarUsuarios();
                    break;
                case "REALIZAR_PRESTAMO":
                    realizarPrestamo();
                    break;
                case "DEVOLVER_LIBRO":
                    devolverLibro();
                    break;
                case "LISTAR_PRESTAMOS_ACTIVOS":
                    listarPrestamosActivos();
                    break;
                case "PRESTAMOS_VENCIDOS":
                    mostrarPrestamosVencidos();
                    break;
                case "BUSCAR_LIBRO":
                    buscarLibro();
                    break;
                case "BUSCAR_USUARIO":
                    buscarUsuario();
                    break;
                case "MOSTRAR_ESTADISTICAS":
                    mostrarEstadisticas();
                    break;
                case "LIMPIAR_CAMPOS":
                    limpiarTodosLosCampos();
                    break;
                default:
                    vista.mostrarMensaje("Comando no reconocido: " + comando);
            }
        } catch (Exception ex) {
            vista.mostrarError("Error inesperado: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    @Override
    public void agregarLibro() {
        Libro nuevoLibro = vista.solicitarDatosLibro();
        
        if (nuevoLibro != null && repositorioLibros.crear(nuevoLibro)) {
            vista.mostrarExito("Libro agregado exitosamente con ID: " + nuevoLibro.getId());
            vista.limpiarCamposLibro();
            listarLibros();
            mostrarEstadisticas();
        } else if (nuevoLibro != null) {
            vista.mostrarError("Error al agregar el libro.");
        }
    }
    
    private void eliminarLibroSeleccionado() {
        int id = vista.getLibroSeleccionadoId();
        
        if (id == -1) {
            vista.mostrarError("Debe seleccionar un libro de la tabla.");
            return;
        }
        
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
        
        String mensaje = "¿Está seguro de que desea eliminar el libro?\n\n" +
                        "ID: " + libro.getId() + "\n" +
                        "Título: " + libro.getTitulo() + "\n" +
                        "Autor: " + libro.getAutor();
        
        if (vista.confirmarAccion(mensaje)) {
            if (repositorioLibros.eliminar(id)) {
                vista.mostrarExito("Libro eliminado exitosamente.");
                listarLibros();
                mostrarEstadisticas();
            } else {
                vista.mostrarError("Error al eliminar el libro.");
            }
        } else {
            vista.mostrarMensaje("Operación cancelada.");
        }
    }
    
    @Override
    public void listarLibros() {
        List<Libro> libros = repositorioLibros.obtenerTodos();
        vista.mostrarLibros(libros);
    }
    
    @Override
    public void buscarLibro() {
        String textoBusqueda = vista.getTextoBusquedaLibro();
        String tipoBusqueda = vista.getTipoBusquedaLibro();
        
        if (textoBusqueda.isEmpty() && !tipoBusqueda.equals("Solo Disponibles")) {
            vista.mostrarError("Debe ingresar un término de búsqueda.");
            return;
        }
        
        List<Libro> todosLosLibros = repositorioLibros.obtenerTodos();
        List<Libro> resultados = new java.util.ArrayList<>();
        
        switch (tipoBusqueda) {
            case "Por Título":
                for (Libro libro : todosLosLibros) {
                    if (libro.getTitulo().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                        resultados.add(libro);
                    }
                }
                break;
                
            case "Por Autor":
                for (Libro libro : todosLosLibros) {
                    if (libro.getAutor().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                        resultados.add(libro);
                    }
                }
                break;
                
            case "Por ID":
                try {
                    int id = Integer.parseInt(textoBusqueda);
                    Libro libro = repositorioLibros.obtenerPorId(id);
                    if (libro != null) {
                        resultados.add(libro);
                    }
                } catch (NumberFormatException e) {
                    vista.mostrarError("Para búsqueda por ID debe ingresar un número válido.");
                    return;
                }
                break;
                
            case "Solo Disponibles":
                for (Libro libro : todosLosLibros) {
                    if (libro.estaDisponible()) {
                        // Si hay texto de búsqueda, filtrar también por título o autor
                        if (textoBusqueda.isEmpty() || 
                            libro.getTitulo().toLowerCase().contains(textoBusqueda.toLowerCase()) ||
                            libro.getAutor().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                            resultados.add(libro);
                        }
                    }
                }
                break;
                
            default:
                vista.mostrarError("Tipo de búsqueda no reconocido.");
                return;
        }
        
        vista.mostrarResultadosBusquedaLibros(resultados);
    }
    
    @Override
    public void eliminarLibro() {
        // Implementado como eliminarLibroSeleccionado()
        eliminarLibroSeleccionado();
    }
    
    @Override
    public void agregarUsuario() {
        Usuario nuevoUsuario = vista.solicitarDatosUsuario();
        
        if (nuevoUsuario != null && repositorioUsuarios.crear(nuevoUsuario)) {
            vista.mostrarExito("Usuario agregado exitosamente con ID: " + nuevoUsuario.getId());
            vista.limpiarCamposUsuario();
            listarUsuarios();
            mostrarEstadisticas();
        } else if (nuevoUsuario != null) {
            vista.mostrarError("Error al agregar el usuario. Verifique que el email no esté en uso.");
        }
    }
    
    private void eliminarUsuarioSeleccionado() {
        int id = vista.getUsuarioSeleccionadoId();
        
        if (id == -1) {
            vista.mostrarError("Debe seleccionar un usuario de la tabla.");
            return;
        }
        
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
        
        String mensaje = "¿Está seguro de que desea eliminar el usuario?\n\n" +
                        "ID: " + usuario.getId() + "\n" +
                        "Nombre: " + usuario.getNombre() + "\n" +
                        "Email: " + usuario.getEmail();
        
        if (vista.confirmarAccion(mensaje)) {
            if (repositorioUsuarios.eliminar(id)) {
                vista.mostrarExito("Usuario eliminado exitosamente.");
                listarUsuarios();
                mostrarEstadisticas();
            } else {
                vista.mostrarError("Error al eliminar el usuario.");
            }
        } else {
            vista.mostrarMensaje("Operación cancelada.");
        }
    }
    
    @Override
    public void listarUsuarios() {
        List<Usuario> usuarios = repositorioUsuarios.obtenerTodos();
        vista.mostrarUsuarios(usuarios);
    }
    
    @Override
    public void buscarUsuario() {
        String textoBusqueda = vista.getTextoBusquedaUsuario();
        String tipoBusqueda = vista.getTipoBusquedaUsuario();
        
        if (textoBusqueda.isEmpty()) {
            vista.mostrarError("Debe ingresar un término de búsqueda.");
            return;
        }
        
        List<Usuario> todosLosUsuarios = repositorioUsuarios.obtenerTodos();
        List<Usuario> resultados = new java.util.ArrayList<>();
        
        switch (tipoBusqueda) {
            case "Por Nombre":
                for (Usuario usuario : todosLosUsuarios) {
                    if (usuario.getNombre().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                        resultados.add(usuario);
                    }
                }
                break;
                
            case "Por Email":
                for (Usuario usuario : todosLosUsuarios) {
                    if (usuario.getEmail().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                        resultados.add(usuario);
                    }
                }
                break;
                
            case "Por ID":
                try {
                    int id = Integer.parseInt(textoBusqueda);
                    Usuario usuario = repositorioUsuarios.obtenerPorId(id);
                    if (usuario != null) {
                        resultados.add(usuario);
                    }
                } catch (NumberFormatException e) {
                    vista.mostrarError("Para búsqueda por ID debe ingresar un número válido.");
                    return;
                }
                break;
                
            case "Por Tipo":
                for (Usuario usuario : todosLosUsuarios) {
                    if (usuario.getTipoUsuario().toString().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                        resultados.add(usuario);
                    }
                }
                break;
                
            default:
                vista.mostrarError("Tipo de búsqueda no reconocido.");
                return;
        }
        
        vista.mostrarResultadosBusquedaUsuarios(resultados);
    }
    
    @Override
    public void eliminarUsuario() {
        // Implementado como eliminarUsuarioSeleccionado()
        eliminarUsuarioSeleccionado();
    }
    
    @Override
    public void realizarPrestamo() {
        int idLibro = vista.getIdLibroPrestamo();
        int idUsuario = vista.getIdUsuarioPrestamo();
        
        if (idLibro == -1 || idUsuario == -1) {
            vista.mostrarError("Debe ingresar ID válidos para libro y usuario.");
            return;
        }
        
        Libro libro = repositorioLibros.obtenerPorId(idLibro);
        if (libro == null) {
            vista.mostrarError("Libro no encontrado con ID: " + idLibro);
            return;
        }
        
        Usuario usuario = repositorioUsuarios.obtenerPorId(idUsuario);
        if (usuario == null) {
            vista.mostrarError("Usuario no encontrado con ID: " + idUsuario);
            return;
        }
        
        if (!libro.estaDisponible()) {
            vista.mostrarError("El libro no está disponible para préstamo.");
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
        
        // Mostrar resumen y confirmar
        String mensaje = "¿Confirmar préstamo?\n\n" +
                        "Libro: " + libro.getTitulo() + "\n" +
                        "Usuario: " + usuario.getNombre() + "\n" +
                        "Fecha límite: " + prestamo.getFechaDevolucionEsperada();
        
        if (vista.confirmarAccion(mensaje)) {
            if (libro.prestar() && repositorioPrestamos.crear(prestamo) &&
                repositorioLibros.actualizar(libro)) {
                usuario.agregarPrestamo(prestamo);
                repositorioUsuarios.actualizar(usuario);
                vista.mostrarExito("Préstamo realizado exitosamente. ID: " + prestamo.getId());
                vista.limpiarCamposPrestamo();
                listarLibros();
                listarUsuarios();
                listarPrestamosActivos();
                mostrarEstadisticas();
            } else {
                vista.mostrarError("Error al realizar el préstamo.");
            }
        } else {
            vista.mostrarMensaje("Préstamo cancelado.");
        }
    }
    
    @Override
    public void devolverLibro() {
        int idPrestamo = vista.getIdPrestamo();
        
        if (idPrestamo == -1) {
            vista.mostrarError("Debe ingresar un ID de préstamo válido.");
            return;
        }
        
        Prestamo prestamo = repositorioPrestamos.obtenerPorId(idPrestamo);
        if (prestamo == null) {
            vista.mostrarError("Préstamo no encontrado con ID: " + idPrestamo);
            return;
        }
        
        if (prestamo.getEstado() != EstadoPrestamo.ACTIVO) {
            vista.mostrarError("El préstamo ya fue devuelto.");
            return;
        }
        
        // Mostrar información del préstamo
        String mensaje = "¿Confirmar devolución?\n\n" +
                        "Libro: " + prestamo.getLibro().getTitulo() + "\n" +
                        "Usuario: " + prestamo.getUsuario().getNombre() + "\n" +
                        "Fecha límite: " + prestamo.getFechaDevolucionEsperada();
        
        if (prestamo.estaVencido()) {
            mensaje += "\n\n⚠️ PRÉSTAMO VENCIDO\nDías de retraso: " + prestamo.getDiasRetraso();
        }
        
        if (vista.confirmarAccion(mensaje)) {
            prestamo.devolver();
            prestamo.getLibro().devolver();
            
            if (repositorioPrestamos.actualizar(prestamo) &&
                repositorioLibros.actualizar(prestamo.getLibro())) {
                vista.mostrarExito("Libro devuelto exitosamente.");
                if (prestamo.getDiasRetraso() > 0) {
                    vista.mostrarMensaje("Nota: El libro fue devuelto con " +
                                       prestamo.getDiasRetraso() + " día(s) de retraso.");
                }
                vista.limpiarCamposPrestamo();
                listarLibros();
                listarPrestamosActivos();
                mostrarEstadisticas();
            } else {
                vista.mostrarError("Error al procesar la devolución.");
            }
        } else {
            vista.mostrarMensaje("Devolución cancelada.");
        }
    }
    
    @Override
    public void listarPrestamosActivos() {
        PrestamoRepositorio repo = (PrestamoRepositorio) repositorioPrestamos;
        List<Prestamo> prestamosActivos = repo.obtenerPrestamosActivos();
        vista.mostrarPrestamos(prestamosActivos);
    }
    
    @Override
    public void mostrarPrestamosVencidos() {
        PrestamoRepositorio repo = (PrestamoRepositorio) repositorioPrestamos;
        List<Prestamo> prestamosVencidos = repo.obtenerPrestamosVencidos();
        
        if (prestamosVencidos.isEmpty()) {
            vista.mostrarExito("¡No hay préstamos vencidos!");
            vista.mostrarPrestamos(prestamosVencidos); // Mostrar tabla vacía
        } else {
            vista.mostrarMensaje("Se encontraron " + prestamosVencidos.size() + " préstamo(s) vencido(s):");
            vista.mostrarPrestamos(prestamosVencidos);
        }
    }
    
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
        
        vista.mostrarEstadisticas(totalLibros, librosDisponibles,
                                totalUsuarios, prestamosActivos, prestamosVencidos);
    }
    
    private void limpiarTodosLosCampos() {
        vista.limpiarTodosLosCampos();
        vista.mostrarMensaje("Todos los campos han sido limpiados.");
    }
    
    // Métodos adicionales para búsquedas específicas (pueden ser llamados desde la vista)
    public void buscarLibroPorTitulo(String titulo) {
        if (titulo.isEmpty()) {
            vista.mostrarError("Ingrese un título para buscar.");
            return;
        }
        
        BibliotecaRepositorio repo = (BibliotecaRepositorio) repositorioLibros;
        List<Libro> libros = repo.buscarPorTitulo(titulo);
        vista.mostrarLibros(libros);
        vista.mostrarMensaje("Búsqueda por título '" + titulo + "': " + libros.size() + " resultado(s).");
    }
    
    public void buscarLibroPorAutor(String autor) {
        if (autor.isEmpty()) {
            vista.mostrarError("Ingrese un autor para buscar.");
            return;
        }
        
        BibliotecaRepositorio repo = (BibliotecaRepositorio) repositorioLibros;
        List<Libro> libros = repo.buscarPorAutor(autor);
        vista.mostrarLibros(libros);
        vista.mostrarMensaje("Búsqueda por autor '" + autor + "': " + libros.size() + " resultado(s).");
    }
    
    public void buscarLibroPorId(int id) {
        Libro libro = repositorioLibros.obtenerPorId(id);
        if (libro != null) {
            List<Libro> libros = List.of(libro);
            vista.mostrarLibros(libros);
            vista.mostrarMensaje("Libro encontrado: " + libro.getTitulo());
        } else {
            vista.mostrarError("No se encontró ningún libro con ID: " + id);
        }
    }
    
    public void mostrarSoloLibrosDisponibles() {
        BibliotecaRepositorio repo = (BibliotecaRepositorio) repositorioLibros;
        List<Libro> librosDisponibles = repo.obtenerLibrosDisponibles();
        vista.mostrarLibros(librosDisponibles);
        vista.mostrarMensaje("Libros disponibles: " + librosDisponibles.size() + " resultado(s).");
    }
    
    public void buscarUsuarioPorNombre(String nombre) {
        if (nombre.isEmpty()) {
            vista.mostrarError("Ingrese un nombre para buscar.");
            return;
        }
        
        UsuarioRepositorio repo = (UsuarioRepositorio) repositorioUsuarios;
        List<Usuario> usuarios = repo.buscarPorNombre(nombre);
        vista.mostrarUsuarios(usuarios);
        vista.mostrarMensaje("Búsqueda por nombre '" + nombre + "': " + usuarios.size() + " resultado(s).");
    }
    
    public void buscarUsuarioPorEmail(String email) {
        if (email.isEmpty()) {
            vista.mostrarError("Ingrese un email para buscar.");
            return;
        }
        
        UsuarioRepositorio repo = (UsuarioRepositorio) repositorioUsuarios;
        Usuario usuario = repo.buscarPorEmail(email);
        if (usuario != null) {
            List<Usuario> usuarios = List.of(usuario);
            vista.mostrarUsuarios(usuarios);
            vista.mostrarMensaje("Usuario encontrado: " + usuario.getNombre());
        } else {
            vista.mostrarError("No se encontró ningún usuario con email: " + email);
        }
    }
    
    public void buscarUsuarioPorId(int id) {
        Usuario usuario = repositorioUsuarios.obtenerPorId(id);
        if (usuario != null) {
            List<Usuario> usuarios = List.of(usuario);
            vista.mostrarUsuarios(usuarios);
            vista.mostrarMensaje("Usuario encontrado: " + usuario.getNombre());
        } else {
            vista.mostrarError("No se encontró ningún usuario con ID: " + id);
        }
    }
    
    public void buscarUsuariosPorTipo(TipoUsuario tipo) {
        UsuarioRepositorio repo = (UsuarioRepositorio) repositorioUsuarios;
        List<Usuario> usuarios = repo.obtenerPorTipo(tipo);
        vista.mostrarUsuarios(usuarios);
        vista.mostrarMensaje("Usuarios tipo '" + tipo.getDescripcion() + "': " + usuarios.size() + " resultado(s).");
    }
}