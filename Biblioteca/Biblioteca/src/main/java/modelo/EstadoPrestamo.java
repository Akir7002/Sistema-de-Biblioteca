package modelo;
/**
 * Enum que representa los posibles estados de un préstamo en la biblioteca.
 */
public enum EstadoPrestamo {
    ACTIVO("Activo"),
    DEVUELTO("Devuelto"),
    VENCIDO("Vencido");
    
    private final String descripcion;
    
    EstadoPrestamo(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
}