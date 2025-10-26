#  Sistema de Gestión de Biblioteca

## Descripción
El proyecto **Biblioteca** es un sistema completo de gestión bibliotecaria con múltiples interfaces de usuario. Ofrece tanto aplicaciones gráficas (Swing) como de consola para administradores, bibliotecarios y usuarios externos. 

###  Características principales:
-  **Aplicación de Administrador**: Gestión completa de libros, usuarios y préstamos
-  **Catálogo Público**: Interfaz para consulta externa de disponibilidad de libros
-  **Interfaz de Consola**: Versión tradicional por línea de comandos
-  **Persistencia CSV**: Los datos se guardan automáticamente en archivos CSV
-  **Sincronización de datos**: Todas las aplicaciones comparten la misma información
-  **Sistema de búsqueda avanzada**: Filtros por título, autor, ID y disponibilidad
-  **Estadísticas en tiempo real**: Dashboards con métricas de la biblioteca
-  **Diseño elegante**: Interfaz gráfica con tema bibliotecario terroso y profesional

## Estructura del Proyecto
El proyecto está organizado siguiendo el patrón **MVC (Modelo-Vista-Controlador)**:

- ** aplicacion**: Clases principales de inicio para cada tipo de interfaz
  - `AplicacionSwingMain.java` - Aplicación principal de administrador
  - `ApUsuarioExternoSwing.java` - Catálogo público para consultas
  - `MainConsola.java` - Versión de consola tradicional

- ** modelo**: Entidades y lógica de negocio
  - `Libro.java` - Gestión de libros y disponibilidad
  - `Usuario.java` - Tipos de usuario (Estudiante, Profesor, Administrador)
  - `Prestamo.java` - Control de préstamos y fechas límite
  - `TipoUsuario.java` - Enum con límites de préstamo por tipo
  - `EstadoPrestamo.java` - Estados del préstamo (Activo, Devuelto, Vencido)

- ** repositorio**: Capa de acceso a datos con persistencia CSV
  - `BibliotecaRepositorio.java` - CRUD de libros con almacenamiento en CSV
  - `UsuarioRepositorio.java` - Gestión de usuarios con persistencia
  - `PrestamoRepositorio.java` - Historial y control de préstamos persistente

- ** util**: Utilidades del sistema
  - `DatosEjemplo.java` - Carga centralizada de datos de ejemplo

- ** data**: Archivos de persistencia CSV (generados automáticamente)
  - `libros.csv` - Catálogo de libros con disponibilidad
  - `usuarios.csv` - Base de datos de usuarios registrados
  - `prestamos.csv` - Historial completo de préstamos

- ** vista**: Interfaces de usuario
  - `BibliotecaSwingVista.java` - Interfaz gráfica de administrador
  - `VistaUsuarioExternoSwing.java` - Interfaz pública de consulta
  - `BibliotecaVista.java` - Interfaz de consola

- ** controlador**: Lógica de control y coordinación
  - `BibliotecaSwingControlador.java` - Controlador principal Swing
  - `ControladorUsuarioExternoSwing.java` - Controlador del catálogo público
  - `BibliotecaControlador.java` - Controlador de consola

##  Requisitos del Sistema
-  **Java 8 o superior**
-  **Maven** para gestión de dependencias
-  **Sistema operativo**: Windows, Linux o macOS
-  **Resolución mínima**: 1024x768 para interfaces gráficas

##  Instalación y Ejecución

### 1️ Preparación del proyecto
```bash
# Clonar el repositorio
git clone https://github.com/Akir7002/Sistema-de-Biblioteca.git
cd Biblioteca-3-semestre/Biblioteca/Biblioteca

# Compilar el proyecto
mvn clean compile
```

### 2️ Ejecutar aplicaciones

####  **Aplicación de Administrador (Recomendada)**
```bash
java -cp target/classes aplicacion.AplicacionSwingMain
```
**Características:**
- ✅ Gestión completa de 20 libros precargados
- ✅ Sistema de usuarios con diferentes tipos y límites
- ✅ Módulo de préstamos con fechas límite automáticas  
- ✅ Búsqueda avanzada con múltiples filtros
- ✅ Estadísticas en tiempo real
- ✅ Persistencia automática en archivos CSV
- ✅ Datos sincronizados entre todas las aplicaciones
- ✅ Interfaz intuitiva con pestañas organizadas

#### 👥 **Catálogo Público (Para consultas externas)**
```bash
java -cp target/classes aplicacion.ApUsuarioExternoSwing
```
**Características:**
- ✅ Consulta de disponibilidad de libros
- ✅ Búsqueda pública sin permisos de modificación
- ✅ Interfaz simplificada y amigable
- ✅ Información actualizada en tiempo real
- ✅ Datos sincronizados con el sistema principal

####  **Versión de Consola (Clásica)**
```bash
java -cp target/classes aplicacion.MainConsola
```
**Características:**
- ✅ Interfaz de texto tradicional
- ✅ Todas las funcionalidades core
- ✅ Ideal para servidores sin interfaz gráfica
- ✅ Persistencia de datos en archivos CSV

### 3️ Alternativa con Maven (Consola únicamente)
```bash
mvn exec:java -Dexec.mainClass="aplicacion.MainConsola"
```

## 🔧 Funcionalidades Detalladas

###  **Gestión de Libros**
-  **Agregar libros**: Título, autor y cantidad inicial
-  **Listar catálogo**: Vista completa con disponibilidad
-  **Búsqueda avanzada**:
  - Por título (búsqueda parcial, no case-sensitive)
  - Por autor (búsqueda parcial, no case-sensitive)  
  - Por ID específico
  - Solo libros disponibles para préstamo
-  **Eliminar libros**: Con validación de préstamos activos
-  **Estados visuales**: Disponible ✅ / Agotado ❌

###  **Gestión de Usuarios**
-  **Tipos de usuario**:
  -  **Estudiante**: Límite de 3 préstamos simultáneos
  -  **Profesor**: Límite de 5 préstamos simultáneos  
  -  **Administrador**: Límite de 10 préstamos simultáneos
  -  **Registro completo**: Nombre, email, teléfono y tipo
  -  **Búsqueda de usuarios**:
  - Por nombre (búsqueda parcial)
  - Por email (búsqueda parcial)
  - Por ID específico
  - Por tipo de usuario
-  **Control de préstamos**: Seguimiento de límites por tipo

###  **Sistema de Préstamos**
-  **Realizar préstamos**: Con validaciones automáticas
  - Verificación de disponibilidad del libro
  - Validación de límites por tipo de usuario
  - Comprobación de estado activo del usuario
-  **Control de fechas**: 
  - Fecha límite automática (14 días)
  - Detección de préstamos vencidos
  - Cálculo de días de retraso
-  **Devolución inteligente**: 
  - Liberación automática de disponibilidad
  - Registro de fecha de devolución
  - Alertas por devolución tardía
-  **Consultas especializadas**:
  - Préstamos activos
  - Préstamos vencidos con días de retraso
  - Historial completo por usuario

###  **Sistema de Búsqueda Avanzada**
-  **Búsqueda inteligente**: No distingue mayúsculas/minúsculas
-  **Coincidencias parciales**: "odis" encuentra "La Odisea" 
-  **Resultados instantáneos**: Se muestran en tablas organizadas
-  **Navegación automática**: Te lleva directamente a los resultados
-  **Feedback informativo**: Cantidad de resultados encontrados

##  **Sistema de Persistencia CSV**

###  **Almacenamiento Automático**
-  **Carpeta `data/`**: Todos los archivos CSV se generan automáticamente
-  **Guardado instantáneo**: Cada operación se guarda inmediatamente
-  **Sincronización**: Todas las aplicaciones comparten los mismos datos
-  **Inicialización inteligente**: Carga datos de ejemplo solo la primera vez

###  **Archivos CSV Generados**
- **`libros.csv`**: Catálogo completo con disponibilidad en tiempo real
  ```csv
  id;titulo;autor;cantidadDisponible;cantidadTotal
  1;Don Quijote de la Mancha;Miguel de Cervantes;2;3
  ```
  
- **`usuarios.csv`**: Base de datos de usuarios registrados
  ```csv
  id;nombre;email;telefono;tipoUsuario;fechaRegistro;activo
  1;Ana García;ana.garcia@email.com;123-456-7890;ESTUDIANTE;2025-09-30;true
  ```
  
- **`prestamos.csv`**: Historial completo de transacciones
  ```csv
  id;usuarioId;libroId;fechaPrestamo;fechaDevolucionEsperada;fechaDevolucionReal;estado;notas
  1;1;1;2025-09-30;2025-10-15;;ACTIVO;
  ```

###  **Características Técnicas**
-  **Codificación UTF-8**: Soporte completo para caracteres especiales
-  **Referencias cruzadas**: Los préstamos mantienen vínculos a usuarios y libros
-  **Validación de integridad**: Verificación automática de dependencias
-  **Manejo de errores**: Recuperación automática en caso de archivos corruptos

###  **Ventajas del Sistema**
- ✅ **Persistencia real**: Los datos se mantienen entre sesiones
- ✅ **Portabilidad**: Archivos CSV fáciles de respaldar y transferir
- ✅ **Compatibilidad**: Los CSV se pueden abrir en Excel, Google Sheets, etc.
- ✅ **Transparencia**: Datos legibles y editables manualmente si es necesario
- ✅ **Sin dependencias externas**: No requiere base de datos externa

###  **Estadísticas y Reportes**
-  **Métricas en tiempo real**:
  - Total de libros en catálogo
  - Libros disponibles vs. agotados
  - Total de usuarios registrados
  - Préstamos activos actuales
  - Préstamos vencidos pendientes
-  **Actualización automática**: Cada acción actualiza las estadísticas
-  **Dashboard visual**: Presentación clara con iconos y colores

##  **Diseño de Interfaz**

###  **Tema Visual**
-  **Paleta terrosa bibliotecaria**: Beiges, marrones y dorados
-  **Iconos temáticos**: Emojis representativos para cada sección
-  **Efectos hover**: Botones interactivos con retroalimentación visual
-  **Tablas elegantes**: Zebra-striping y selección clara

###  **Organización por Pestañas**
-  **Libros**: Gestión completa del catálogo
-  **Usuarios**: Administración de la comunidad
-  **Préstamos**: Control de transacciones
-  **Búsquedas**: Sistema de consultas avanzadas  
-  **Estadísticas**: Dashboard de métricas

###  **Sistema de Mensajes**
- ✅ **Mensajes de éxito**: Confirmaciones verdes
- ❌ **Errores**: Alertas rojas con descripción clara
- ℹ️ **Información**: Notificaciones azules informativas
- 📜 **Log persistente**: Historial de acciones en panel inferior

##  **Datos de Ejemplo Precargados**

###  **20 Libros en Catálogo**
La aplicación incluye una biblioteca diversa con clásicos de la literatura:

** Disponibles:**
- Don Quijote de la Mancha (Miguel de Cervantes) - 3 ejemplares
- Cien Años de Soledad (Gabriel García Márquez) - 2 ejemplares  
- 1984 (George Orwell) - 4 ejemplares
- Crimen y Castigo (Fiódor Dostoyevski) - 2 ejemplares
- Orgullo y Prejuicio (Jane Austen) - 3 ejemplares
- *...y 13 títulos más*

**❌ Temporalmente Agotados:**
- El Principito (Antoine de Saint-Exupéry) - 0 ejemplares
- Hamlet (William Shakespeare) - 0 ejemplares

###  **6 Usuarios de Ejemplo**
-  **Estudiantes**: Ana García, María Rodríguez, Pedro Sánchez
-  **Profesores**: Carlos López, Laura Martínez  
-  **Administrador**: Dr. Juan Pérez

###  **Préstamos Activos**
- 2 préstamos preconfigurados para demostración del sistema

## 🤝 **Contribuciones**

¿Quieres mejorar el sistema? ¡Excelente!

1.  **Fork** del repositorio
2.  **Crea una rama** para tu funcionalidad:
   ```bash
   git checkout -b feature/nueva-funcionalidad
   ```
3.  **Commit** tus cambios:
   ```bash
   git commit -m "Agregar funcionalidad increíble"
   ```
4.  **Push** a tu rama:
   ```bash
   git push origin feature/nueva-funcionalidad
   ```
5.  **Pull Request** para revisión

###  **¿Por qué elegir este Sistema de Biblioteca?**

✅ **Completo**: Tres tipos de interfaz para diferentes necesidades  
✅ **Persistente**: Datos guardados automáticamente en archivos CSV  
✅ **Intuitivo**: Diseño pensado para bibliotecarios y usuarios finales  
✅ **Robusto**: Validaciones exhaustivas y manejo de errores  
✅ **Sincronizado**: Todas las aplicaciones comparten la misma información  
✅ **Escalable**: Arquitectura MVC preparada para crecimiento  
✅ **Moderno**: Interfaz gráfica atractiva y funcional  
✅ **Educativo**: Código limpio ideal para aprendizaje de Java/Swing  

**¡Perfecto para bibliotecas académicas, públicas o proyectos educativos!** 

## 🆘 **Soporte y Resolución de Problemas**

###  **Problemas Comunes**
- **Error de compilación**: Verificar Java 8+ y Maven instalados, en caso de problemas eliminar carpeta `target/` manualmente y recompilar
- **Interfaz no se abre**: Comprobar que no hay otra instancia ejecutándose  
- **Archivos CSV corruptos**: Eliminar carpeta `data/` para regenerar datos de ejemplo
- **Referencias no resueltas**: Reiniciar la aplicación para recargar dependencias
- **Pérdida de datos**: Verificar permisos de escritura en la carpeta `data/`

### 📧 **Contacto**
- **Desarrolladores**: Akir7002 (Maria Fernanda Patiño) y David Mauricio Perez
- **Email** mariafernandap941@gmail.com 
-  **Repositorio**: [Sistema-de-Biblioteca/Biblioteca/Biblioteca](https://github.com/Akir7002/Sistema-de-Biblioteca.git)
-  **Issues**: Reportar bugs en GitHub Issues 



---


