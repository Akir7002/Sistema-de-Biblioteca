package modelo;


public class Libro extends RecursoBiblioteca {
    private String titulo;
    private String autor;
    private int cantidadDisponible;
    private int cantidadTotal;
    
    // Constructores
    public Libro() {}
    
    public Libro(String titulo, String autor, int cantidadDisponible) {
        this.titulo = titulo;
        this.autor = autor;
        this.cantidadDisponible = cantidadDisponible;
        this.cantidadTotal = cantidadDisponible;
    }
    
    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getAutor() {
        return autor;
    }
    
    public void setAutor(String autor) {
        this.autor = autor;
    }
    
    public int getCantidadDisponible() {
        return cantidadDisponible;
    }
    
    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
    
    public int getCantidadTotal() {
        return cantidadTotal;
    }
    
    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
    }
    
    // Métodos de negocio
    public boolean prestar() {
        if (cantidadDisponible > 0) {
            cantidadDisponible--;
            return true;
        }
        return false;
    }
    
    public void devolver() {
        if (cantidadDisponible < cantidadTotal) {
            cantidadDisponible++;
        }
    }
    
    public boolean estaDisponible() {
        return cantidadDisponible > 0;
    }
    
    // Métodos para manejo de CSV
    private static String escape(String valor) {
        if (valor == null) return "";
        // Evita romper el CSV si el texto trae ';' o saltos de línea
        return valor.replace(";", ",").replace("\n", " ").replace("\r", " ");
    }
    
    public String toCsv() {
        return getId() + ";" +
               escape(titulo) + ";" +
               escape(autor) + ";" +
               cantidadDisponible + ";" +
               cantidadTotal;
    }
    
    public static Libro fromCsv(String linea) {
        // Espera 5 columnas separadas por ';': id;titulo;autor;cantidadDisponible;cantidadTotal
        String[] p = linea.split(";", -1);
        if (p.length < 5) {
            throw new IllegalArgumentException("Línea CSV inválida: " + linea);
        }
        
        int id = Integer.parseInt(p[0].trim());
        String titulo = p[1].trim();
        String autor = p[2].trim();
        int cantidadDisponible = Integer.parseInt(p[3].trim());
        int cantidadTotal = Integer.parseInt(p[4].trim());
        
        Libro libro = new Libro(titulo, autor, cantidadDisponible);
        libro.setId(id);
        libro.setCantidadTotal(cantidadTotal);
        
        return libro;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Título: %s | Autor: %s | Disponibles: %d/%d", 
                           getId(), titulo, autor, cantidadDisponible, cantidadTotal);
    }
}