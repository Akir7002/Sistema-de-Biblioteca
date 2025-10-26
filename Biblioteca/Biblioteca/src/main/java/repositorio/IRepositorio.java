package repositorio;

import java.util.List;

public interface IRepositorio<T> {
    boolean crear(T entidad);
    T obtenerPorId(int id);
    List<T> obtenerTodos();
    boolean actualizar(T entidad);
    boolean eliminar(int id);
}