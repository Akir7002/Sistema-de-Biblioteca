package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Usuario extends RecursoBiblioteca {
    private String nombre;
    private String email;
    private String telefono;
    private TipoUsuario tipoUsuario;
    private LocalDate fechaRegistro;
    private List<Prestamo> historialPrestamos;
    private boolean activo;
    
    // Constructores
    public Usuario() {
        this.historialPrestamos = new ArrayList<>();
        this.fechaRegistro = LocalDate.now();
        this.activo = true;
    }
    
    public Usuario(String nombre, String email, String telefono, TipoUsuario tipoUsuario) {
        this();
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.tipoUsuario = tipoUsuario;
    }
    
    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }
    
    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }
    
    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public List<Prestamo> getHistorialPrestamos() {
        return historialPrestamos;
    }
    
    public void setHistorialPrestamos(List<Prestamo> historialPrestamos) {
        this.historialPrestamos = historialPrestamos;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    // Métodos de negocio
    public int getPrestamosActivos() {
        return (int) historialPrestamos.stream()
                .filter(prestamo -> prestamo.getEstado() == EstadoPrestamo.ACTIVO)
                .count();
    }
    
    public boolean puedePrestar() {
        return activo && getPrestamosActivos() < tipoUsuario.getLimitePrestamos();
    }
    
    public void agregarPrestamo(Prestamo prestamo) {
        historialPrestamos.add(prestamo);
    }
    
    // Métodos para manejo de CSV
    private static String escape(String valor) {
        if (valor == null) return "";
        // Evita romper el CSV si el texto trae ';' o saltos de línea
        return valor.replace(";", ",").replace("\n", " ").replace("\r", " ");
    }
    
    public String toCsv() {
        return getId() + ";" +
               escape(nombre) + ";" +
               escape(email) + ";" +
               escape(telefono) + ";" +
               tipoUsuario.name() + ";" +
               fechaRegistro.toString() + ";" +
               activo;
    }
    
    public static Usuario fromCsv(String linea) {
        // Espera 7 columnas: id;nombre;email;telefono;tipoUsuario;fechaRegistro;activo
        String[] p = linea.split(";", -1);
        if (p.length < 7) {
            throw new IllegalArgumentException("Línea CSV inválida: " + linea);
        }
        
        int id = Integer.parseInt(p[0].trim());
        String nombre = p[1].trim();
        String email = p[2].trim();
        String telefono = p[3].trim();
        TipoUsuario tipoUsuario = TipoUsuario.valueOf(p[4].trim());
        LocalDate fechaRegistro = LocalDate.parse(p[5].trim());
        boolean activo = Boolean.parseBoolean(p[6].trim());
        
        Usuario usuario = new Usuario(nombre, email, telefono, tipoUsuario);
        usuario.setId(id);
        usuario.setFechaRegistro(fechaRegistro);
        usuario.setActivo(activo);
        // El historial de préstamos se manejará por separado
        
        return usuario;
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d | Nombre: %s | Email: %s | Tipo: %s | Préstamos Activos: %d/%d", 
                           getId(), nombre, email, tipoUsuario.getDescripcion(), 
                           getPrestamosActivos(), tipoUsuario.getLimitePrestamos());
    }
}
