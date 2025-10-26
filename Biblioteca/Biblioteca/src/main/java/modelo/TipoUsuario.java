package modelo;

public enum TipoUsuario {
    ESTUDIANTE("Estudiante", 3, 15),
    PROFESOR("Profesor", 5, 30),
    ADMINISTRADOR("Administrador", 10, 60);
    
    private final String descripcion;
    private final int limitePrestamos;
    private final int diasMaximoPrestamo;
    
    TipoUsuario(String descripcion, int limitePrestamos, int diasMaximoPrestamo) {
        this.descripcion = descripcion;
        this.limitePrestamos = limitePrestamos;
        this.diasMaximoPrestamo = diasMaximoPrestamo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public int getLimitePrestamos() {
        return limitePrestamos;
    }
    
    public int getDiasMaximoPrestamo() {
        return diasMaximoPrestamo;
    }
}