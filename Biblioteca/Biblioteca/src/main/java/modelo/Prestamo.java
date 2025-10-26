package modelo;

import java.time.LocalDate;

public class Prestamo {
    private int id;
    private Usuario usuario;
    private Libro libro;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEsperada;
    private LocalDate fechaDevolucionReal;
    private EstadoPrestamo estado;
    private String notas;
    
    // Constructores
    public Prestamo() {
        this.fechaPrestamo = LocalDate.now();
        this.estado = EstadoPrestamo.ACTIVO;
    }
    
    public Prestamo(Usuario usuario, Libro libro) {
        this();
        this.usuario = usuario;
        this.libro = libro;
        this.fechaDevolucionEsperada = LocalDate.now()
                .plusDays(usuario.getTipoUsuario().getDiasMaximoPrestamo());
    }
    
    // Getters y Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Libro getLibro() {
        return libro;
    }
    
    public void setLibro(Libro libro) {
        this.libro = libro;
    }
    
    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }
    
    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }
    
    public LocalDate getFechaDevolucionEsperada() {
        return fechaDevolucionEsperada;
    }
    
    public void setFechaDevolucionEsperada(LocalDate fechaDevolucionEsperada) {
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
    }
    
    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }
    
    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }
    
    public EstadoPrestamo getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }
    
    public String getNotas() {
        return notas;
    }
    
    public void setNotas(String notas) {
        this.notas = notas;
    }
    
    // Métodos de negocio
    public boolean estaVencido() {
        return LocalDate.now().isAfter(fechaDevolucionEsperada) && 
               estado == EstadoPrestamo.ACTIVO;
    }
    
    public long getDiasRetraso() {
        if (!estaVencido()) {
            return 0;
        }
        return LocalDate.now().toEpochDay() - fechaDevolucionEsperada.toEpochDay();
    }
    
    public void devolver() {
        this.fechaDevolucionReal = LocalDate.now();
        this.estado = EstadoPrestamo.DEVUELTO;
    }
    
    // Métodos para manejo de CSV
    private static String escape(String valor) {
        if (valor == null) return "";
        // Evita romper el CSV si el texto trae ';' o saltos de línea
        return valor.replace(";", ",").replace("\n", " ").replace("\r", " ");
    }
    
    public String toCsv() {
        String fechaDevolucionRealStr = (fechaDevolucionReal != null) ? fechaDevolucionReal.toString() : "";
        return id + ";" +
               usuario.getId() + ";" +
               libro.getId() + ";" +
               fechaPrestamo.toString() + ";" +
               fechaDevolucionEsperada.toString() + ";" +
               fechaDevolucionRealStr + ";" +
               estado.name() + ";" +
               escape(notas);
    }
    
    // Nota: Este método necesitará acceso a los repositorios para reconstruir Usuario y Libro
    // Se manejará en el repositorio donde se tienen las referencias
    public static Prestamo fromCsv(String linea, Usuario usuario, Libro libro) {
        // Espera 8 columnas: id;usuarioId;libroId;fechaPrestamo;fechaDevolucionEsperada;fechaDevolucionReal;estado;notas
        String[] p = linea.split(";", -1);
        if (p.length < 8) {
            throw new IllegalArgumentException("Línea CSV inválida: " + linea);
        }
        
        int id = Integer.parseInt(p[0].trim());
        // p[1] = usuarioId (ya tenemos la referencia)
        // p[2] = libroId (ya tenemos la referencia)
        LocalDate fechaPrestamo = LocalDate.parse(p[3].trim());
        LocalDate fechaDevolucionEsperada = LocalDate.parse(p[4].trim());
        LocalDate fechaDevolucionReal = null;
        if (!p[5].trim().isEmpty()) {
            fechaDevolucionReal = LocalDate.parse(p[5].trim());
        }
        EstadoPrestamo estado = EstadoPrestamo.valueOf(p[6].trim());
        String notas = p[7].trim();
        
        Prestamo prestamo = new Prestamo(usuario, libro);
        prestamo.setId(id);
        prestamo.setFechaPrestamo(fechaPrestamo);
        prestamo.setFechaDevolucionEsperada(fechaDevolucionEsperada);
        prestamo.setFechaDevolucionReal(fechaDevolucionReal);
        prestamo.setEstado(estado);
        prestamo.setNotas(notas);
        
        return prestamo;
    }
    
    @Override
    public String toString() {
        return String.format("Préstamo ID: %d | Usuario: %s | Libro: %s | Estado: %s | Fecha Límite: %s", 
                           id, usuario.getNombre(), libro.getTitulo(), 
                           estado.getDescripcion(), fechaDevolucionEsperada);
    }
}