package repositorio;

import modelo.Usuario;
import modelo.TipoUsuario;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class UsuarioRepositorio implements IRepositorio<Usuario> {
    private final File archivoCsv;
    private int contadorId;
    private static final String RUTA = "data/usuarios.csv";

    public UsuarioRepositorio() {
        this.archivoCsv = new File(RUTA);
        this.contadorId = obtenerMaximoId() + 1;
    }
    
    private void asegurarDirectorio() {
        File parent = archivoCsv.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }
    
    private void guardarTodos(List<Usuario> usuarios) throws IOException {
        asegurarDirectorio();
        
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(archivoCsv, false), StandardCharsets.UTF_8))) {
            
            // Cabecera
            bw.write("id;nombre;email;telefono;tipoUsuario;fechaRegistro;activo");
            bw.newLine();
            
            for (Usuario usuario : usuarios) {
                bw.write(usuario.toCsv());
                bw.newLine();
            }
        }
    }
    
    private List<Usuario> cargarTodos() throws IOException {
        List<Usuario> lista = new ArrayList<>();
        if (!archivoCsv.exists()) {
            return lista; // Si no existe, devuelve lista vacía
        }
        
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivoCsv), StandardCharsets.UTF_8))) {
            
            String linea;
            boolean primera = true;
            
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) continue;
                
                // Saltar cabecera si es la primera línea
                if (primera) {
                    primera = false;
                    if (linea.toLowerCase().startsWith("id;")) {
                        continue;
                    }
                }
                
                try {
                    lista.add(Usuario.fromCsv(linea));
                } catch (Exception e) {
                    System.err.println("Error al procesar línea CSV: " + linea + " - " + e.getMessage());
                }
            }
        }
        return lista;
    }
    
    private int obtenerMaximoId() {
        try {
            List<Usuario> usuarios = cargarTodos();
            return usuarios.stream()
                          .mapToInt(Usuario::getId)
                          .max()
                          .orElse(0);
        } catch (IOException e) {
            return 0; // Si hay error al cargar, empezamos desde 0
        }
    }

    @Override
    public boolean crear(Usuario usuario) {
        try {
            // Verificar email único
            if (existeEmail(usuario.getEmail())) {
                return false;
            }
            usuario.setId(contadorId++);
            List<Usuario> usuarios = cargarTodos();
            usuarios.add(usuario);
            guardarTodos(usuarios);
            return true;
        } catch (IOException e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
            return false;
        }
    }

    private boolean existeEmail(String email) {
        try {
            List<Usuario> usuarios = cargarTodos();
            return usuarios.stream().anyMatch(usuario -> usuario.getEmail().equals(email));
        } catch (IOException e) {
            System.err.println("Error al verificar email: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Usuario obtenerPorId(int id) {
        try {
            List<Usuario> usuarios = cargarTodos();
            return usuarios.stream()
                          .filter(usuario -> usuario.getId() == id)
                          .findFirst()
                          .orElse(null);
        } catch (IOException e) {
            System.err.println("Error al obtener usuario por ID: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Usuario> obtenerTodos() {
        try {
            return new ArrayList<>(cargarTodos());
        } catch (IOException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        try {
            List<Usuario> usuarios = cargarTodos();
            for (int i = 0; i < usuarios.size(); i++) {
                if (usuarios.get(i).getId() == usuario.getId()) {
                    usuarios.set(i, usuario);
                    guardarTodos(usuarios);
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        try {
            List<Usuario> usuarios = cargarTodos();
            boolean eliminado = usuarios.removeIf(usuario -> usuario.getId() == id);
            if (eliminado) {
                guardarTodos(usuarios);
            }
            return eliminado;
        } catch (IOException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    public List<Usuario> buscarPorNombre(String nombre) {
        try {
            List<Usuario> usuarios = cargarTodos();
            return usuarios.stream()
                          .filter(usuario -> usuario.getNombre().equalsIgnoreCase(nombre))
                          .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error al buscar por nombre: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Usuario buscarPorEmail(String email) {
        try {
            List<Usuario> usuarios = cargarTodos();
            return usuarios.stream()
                          .filter(usuario -> usuario.getEmail().equalsIgnoreCase(email))
                          .findFirst()
                          .orElse(null);
        } catch (IOException e) {
            System.err.println("Error al buscar por email: " + e.getMessage());
            return null;
        }
    }

    public List<Usuario> obtenerPorTipo(TipoUsuario tipo) {
        try {
            List<Usuario> usuarios = cargarTodos();
            return usuarios.stream()
                          .filter(usuario -> usuario.getTipoUsuario() == tipo)
                          .collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Error al obtener por tipo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public String rutaAbsoluta() {
        try {
            return archivoCsv.getCanonicalPath();
        } catch (IOException e) {
            return archivoCsv.getAbsolutePath();
        }
    }
}