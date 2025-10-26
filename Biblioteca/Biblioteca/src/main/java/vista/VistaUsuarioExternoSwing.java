package vista;

import modelo.Libro;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class VistaUsuarioExternoSwing extends JFrame implements IVistaUsuarioExterno {
    // Paleta de colores terrosos mejorada para biblioteca (igual que la vista principal)
    private static final Color COLOR_FONDO_PRINCIPAL = new Color(250, 245, 235); // Beige más suave
    private static final Color COLOR_FONDO_SECUNDARIO = new Color(235, 205, 175); // Marrón claro más suave
    private static final Color COLOR_ACENTO = new Color(139, 69, 19); // Marrón chocolate para acentos
    private static final Color COLOR_TEXTO_PRINCIPAL = new Color(62, 39, 35); // Marrón más oscuro para mejor contraste
    private static final Color COLOR_BOTONES = new Color(205, 170, 125); // Marrón dorado más suave
    private static final Color COLOR_BOTONES_HOVER = new Color(180, 140, 90); // Color hover más oscuro
    private static final Color COLOR_BORDES = new Color(160, 120, 90); // Bordes más suaves
    private static final Color COLOR_TABLA_ALTERNANTE = new Color(245, 240, 230); // Para zebra-striping
    
    // Fuentes personalizadas para un aspecto más elegante
    private static final Font FUENTE_TITULO = new Font("Georgia", Font.BOLD, 18);
    private static final Font FUENTE_SUBTITULO = new Font("Georgia", Font.BOLD, 14);
    private static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 11);
    
    // Componentes principales
    private JTable tablaCatalogo;
    private DefaultTableModel modeloCatalogo;
    private JTextField txtBusqueda;
    private JComboBox<String> cmbTipoBusqueda;
    private JTextArea txtInformacion;
    private JLabel lblEstadisticas;
    
    // Botones
    private JButton btnBuscar, btnMostrarTodos, btnSoloDisponibles;
    private JButton btnAyuda, btnContacto, btnPopulares;
    
    public VistaUsuarioExternoSwing() {
        inicializarComponentes();
        configurarVentana();
        mostrarBienvenida();
    }
    
    private void inicializarComponentes() {
        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        
        // Panel superior con título
        JPanel panelTitulo = crearPanelTitulo();
        
        // Panel de búsqueda
        JPanel panelBusqueda = crearPanelBusqueda();
        
        // Panel central con catálogo
        JPanel panelCatalogo = crearPanelCatalogo();
        
        // Panel lateral con información
        JPanel panelLateral = crearPanelLateral();
        
        // Panel inferior con estadísticas
        JPanel panelInferior = crearPanelInferior();
        
        // Ensamblar layout
        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(panelCatalogo, BorderLayout.CENTER);
        panelPrincipal.add(panelLateral, BorderLayout.EAST);
        panelPrincipal.add(panelInferior, BorderLayout.SOUTH);
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(41, 128, 185));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titulo = new JLabel("📖 CATÁLOGO PÚBLICO - BIBLIOTECA", SwingConstants.CENTER);
        titulo.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titulo.setForeground(Color.WHITE);
        
        JLabel subtitulo = new JLabel("Explora nuestro catálogo de libros disponibles", SwingConstants.CENTER);
        subtitulo.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 14));
        subtitulo.setForeground(Color.WHITE);
        
        panel.add(titulo, BorderLayout.CENTER);
        panel.add(subtitulo, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createTitledBorder("🔍 Buscar en el Catálogo"));
        
        panel.add(new JLabel("Buscar:"));
        txtBusqueda = new JTextField(20);
        panel.add(txtBusqueda);
        
        String[] tiposBusqueda = {"Por Título", "Por Autor"};
        cmbTipoBusqueda = new JComboBox<>(tiposBusqueda);
        panel.add(cmbTipoBusqueda);
        
        btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.setActionCommand("BUSCAR");
        panel.add(btnBuscar);
        
        btnMostrarTodos = new JButton("📚 Ver Todos");
        btnMostrarTodos.setActionCommand("MOSTRAR_TODOS");
        panel.add(btnMostrarTodos);
        
        btnSoloDisponibles = new JButton("✅ Solo Disponibles");
        btnSoloDisponibles.setActionCommand("SOLO_DISPONIBLES");
        panel.add(btnSoloDisponibles);
        
        return panel;
    }
    
    private JPanel crearPanelCatalogo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("📋 Catálogo de Libros"));
        
        // Crear tabla
        String[] columnas = {"ID", "Título", "Autor", "Disponibles", "Total", "Estado"};
        modeloCatalogo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaCatalogo = new JTable(modeloCatalogo);
        tablaCatalogo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCatalogo.getTableHeader().setReorderingAllowed(false);
        tablaCatalogo.setRowHeight(25);
        tablaCatalogo.setGridColor(Color.LIGHT_GRAY);
        tablaCatalogo.setShowGrid(true);
        
        // Configurar anchos de columnas
        tablaCatalogo.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaCatalogo.getColumnModel().getColumn(1).setPreferredWidth(250);
        tablaCatalogo.getColumnModel().getColumn(2).setPreferredWidth(200);
        tablaCatalogo.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaCatalogo.getColumnModel().getColumn(4).setPreferredWidth(60);
        tablaCatalogo.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        JScrollPane scrollPane = new JScrollPane(tablaCatalogo);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelLateral() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 0));
        panel.setBorder(BorderFactory.createTitledBorder("ℹ️ Información"));
        
        // Panel de botones de información
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 5, 5));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        btnPopulares = new JButton("🔥 Libros Populares");
        btnPopulares.setActionCommand("POPULARES");
        btnAyuda = new JButton("❓ ¿Cómo pedir prestado?");
        btnAyuda.setActionCommand("AYUDA");
        btnContacto = new JButton("📞 Información de Contacto");
        btnContacto.setActionCommand("CONTACTO");
        
        panelBotones.add(btnPopulares);
        panelBotones.add(btnAyuda);
        panelBotones.add(btnContacto);
        
        // Área de texto para mostrar información
        txtInformacion = new JTextArea();
        txtInformacion.setEditable(false);
        txtInformacion.setWrapStyleWord(true);
        txtInformacion.setLineWrap(true);
        txtInformacion.setBackground(new Color(248, 248, 248));
        txtInformacion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        txtInformacion.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        
        JScrollPane scrollInfo = new JScrollPane(txtInformacion);
        scrollInfo.setPreferredSize(new Dimension(0, 300));
        
        panel.add(panelBotones, BorderLayout.NORTH);
        panel.add(scrollInfo, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("📊 Estadísticas"));
        panel.setPreferredSize(new Dimension(0, 80));
        
        lblEstadisticas = new JLabel("Cargando estadísticas...", SwingConstants.CENTER);
        lblEstadisticas.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        panel.add(lblEstadisticas, BorderLayout.CENTER);
        
        // Panel de instrucciones
        JPanel panelInstrucciones = new JPanel();
        panelInstrucciones.setBackground(new Color(230, 248, 255));
        JLabel instrucciones = new JLabel("<html><center>💡 <b>Para solicitar un préstamo:</b><br>" +
                                        "Anota el ID del libro y acércate al mostrador de la biblioteca</center></html>");
        instrucciones.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        panelInstrucciones.add(instrucciones);
        panel.add(panelInstrucciones, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void configurarVentana() {
        setTitle("📖 Catálogo Público - Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
        
        // Aplicar tema terroso de biblioteca
        getContentPane().setBackground(COLOR_FONDO_PRINCIPAL);
        
        // Configurar look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            // Continuar con el look and feel por defecto
        }
        
        // Aplicar colores personalizados después del look and feel
        aplicarColoresPersonalizados();
    }
    
    private void aplicarColoresPersonalizados() {
        // Aplicar colores a todos los componentes
        aplicarColoresRecursivo(this);
    }
    
    private void aplicarColoresRecursivo(Container container) {
        container.setBackground(COLOR_FONDO_PRINCIPAL);
        
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                configurarBotonElegante((JButton) component);
            } else if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setForeground(COLOR_TEXTO_PRINCIPAL);
                // Aplicar fuentes según el contexto
                if (label.getText().contains("📖") || label.getText().contains("BIBLIOTECA") ||
                    label.getText().contains("CATÁLOGO")) {
                    label.setFont(FUENTE_TITULO);
                } else if (label.getText().contains(":") && label.getText().length() < 30) {
                    label.setFont(FUENTE_SUBTITULO);
                } else {
                    label.setFont(FUENTE_NORMAL);
                }
            } else if (component instanceof JTextField) {
                JTextField campo = (JTextField) component;
                campo.setBackground(Color.WHITE);
                campo.setForeground(COLOR_TEXTO_PRINCIPAL);
                campo.setFont(FUENTE_NORMAL);
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_BORDES, 1),
                    BorderFactory.createEmptyBorder(5, 8, 5, 8)
                ));
            } else if (component instanceof JComboBox) {
                JComboBox<?> combo = (JComboBox<?>) component;
                combo.setBackground(Color.WHITE);
                combo.setForeground(COLOR_TEXTO_PRINCIPAL);
                combo.setFont(FUENTE_NORMAL);
            } else if (component instanceof JTable) {
                configurarTablaElegante((JTable) component);
            } else if (component instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) component;
                scroll.setBackground(COLOR_FONDO_PRINCIPAL);
                scroll.getViewport().setBackground(COLOR_FONDO_PRINCIPAL);
                scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDES, 1));
            } else if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                if (panel.getBorder() != null) {
                    panel.setBackground(COLOR_FONDO_SECUNDARIO);
                    panel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(COLOR_BORDES, 1),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    ));
                } else {
                    panel.setBackground(COLOR_FONDO_PRINCIPAL);
                }
                aplicarColoresRecursivo(panel);
            } else if (component instanceof Container) {
                aplicarColoresRecursivo((Container) component);
            }
        }
    }
    
    private void configurarBotonElegante(JButton boton) {
        boton.setBackground(COLOR_BOTONES);
        boton.setForeground(COLOR_TEXTO_PRINCIPAL);
        boton.setFont(FUENTE_BOTON);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDES, 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Efectos de hover elegantes
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_BOTONES_HOVER);
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_ACENTO, 2),
                    BorderFactory.createEmptyBorder(7, 15, 7, 15)
                ));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_BOTONES);
                boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_BORDES, 1),
                    BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }
        });
    }
    
    private void configurarTablaElegante(JTable tabla) {
        tabla.setBackground(Color.WHITE);
        tabla.setForeground(COLOR_TEXTO_PRINCIPAL);
        tabla.setFont(FUENTE_NORMAL);
        tabla.setSelectionBackground(COLOR_FONDO_SECUNDARIO);
        tabla.setSelectionForeground(COLOR_TEXTO_PRINCIPAL);
        tabla.setGridColor(COLOR_BORDES);
        tabla.setRowHeight(28); // Un poco más alto para el catálogo público
        tabla.setShowVerticalLines(true);
        tabla.setShowHorizontalLines(true);
        
        // Configurar el header de la tabla
        if (tabla.getTableHeader() != null) {
            tabla.getTableHeader().setBackground(COLOR_ACENTO);
            tabla.getTableHeader().setForeground(Color.WHITE);
            tabla.getTableHeader().setFont(FUENTE_SUBTITULO);
            tabla.getTableHeader().setBorder(BorderFactory.createLineBorder(COLOR_BORDES, 1));
        }
        
        // Implementar zebra-striping (filas alternantes)
        tabla.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (isSelected) {
                    setBackground(COLOR_FONDO_SECUNDARIO);
                    setForeground(COLOR_TEXTO_PRINCIPAL);
                } else if (row % 2 == 0) {
                    setBackground(Color.WHITE);
                    setForeground(COLOR_TEXTO_PRINCIPAL);
                } else {
                    setBackground(COLOR_TABLA_ALTERNANTE);
                    setForeground(COLOR_TEXTO_PRINCIPAL);
                }
                
                setFont(FUENTE_NORMAL);
                setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
                return this;
            }
        });
    }
    
    // Métodos para conectar con el controlador
    public void setControladorListener(ActionListener listener) {
        btnBuscar.addActionListener(listener);
        btnMostrarTodos.addActionListener(listener);
        btnSoloDisponibles.addActionListener(listener);
        btnPopulares.addActionListener(listener);
        btnAyuda.addActionListener(listener);
        btnContacto.addActionListener(listener);
        
        // Listener para Enter en el campo de búsqueda
        @SuppressWarnings("unused")
        ActionListener enterListener = e -> {
            ActionEvent evento = new ActionEvent(btnBuscar, ActionEvent.ACTION_PERFORMED, "BUSCAR");
            listener.actionPerformed(evento);
        };
        txtBusqueda.addActionListener(enterListener);
    }
    
    // Implementación de IVistaUsuarioExterno
    @Override
    public void mostrarMenuPublico() {
        setVisible(true);
    }
    
    @Override
    public void mostrarCatalogo(List<Libro> libros) {
        modeloCatalogo.setRowCount(0);
        
        for (Libro libro : libros) {
            String estado = libro.estaDisponible() ? "✅ Disponible" : "❌ Agotado";
            Object[] fila = {
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getCantidadDisponible(),
                libro.getCantidadTotal(),
                estado
            };
            modeloCatalogo.addRow(fila);
        }
        
        // Actualizar estadísticas en tiempo real
        long disponibles = libros.stream().filter(Libro::estaDisponible).count();
        lblEstadisticas.setText(String.format("📚 Total: %d libros | ✅ Disponibles: %d | ❌ Agotados: %d", 
                                            libros.size(), disponibles, libros.size() - disponibles));
    }
    
    @Override
    public void mostrarDetalleLibro(Libro libro) {
        String detalle = String.format("""
            📖 DETALLE DEL LIBRO
            ────────────────────
            🆔 ID: %d
            📚 Título: %s
            👤 Autor: %s
            📊 Total: %d ejemplares
            ✅ Disponibles: %d
            
            %s
            
            💡 Para solicitar este libro:
            1. Anota el ID: %d
            2. Acércate al mostrador
            3. Presenta tu documento
            """, 
            libro.getId(), libro.getTitulo(), libro.getAutor(),
            libro.getCantidadTotal(), libro.getCantidadDisponible(),
            libro.estaDisponible() ? "🟢 DISPONIBLE PARA PRÉSTAMO" : "🔴 TEMPORALMENTE AGOTADO",
            libro.getId());
            
        txtInformacion.setText(detalle);
        txtInformacion.setCaretPosition(0);
    }
    
    @Override
    public void mostrarResultadoBusqueda(List<Libro> libros, String termino) {
        mostrarCatalogo(libros);
        if (libros.isEmpty()) {
            txtInformacion.setText(String.format("""
                🔍 RESULTADO DE BÚSQUEDA
                ────────────────────────
                
                ❌ No se encontraron libros con: "%s"
                
                💡 Sugerencias:
                • Verifica la ortografía
                • Intenta con palabras más generales
                • Busca por autor en lugar de título
                • Consulta el catálogo completo
                """, termino));
        } else {
            txtInformacion.setText(String.format("""
                🔍 RESULTADO DE BÚSQUEDA
                ────────────────────────
                
                ✅ Se encontraron %d resultado(s)
                para: "%s"
                
                💡 Tip: Haz clic en una fila de la tabla
                para ver más detalles del libro.
                """, libros.size(), termino));
        }
        txtInformacion.setCaretPosition(0);
    }
    
    @Override
    public void mostrarMensaje(String mensaje) {
        // Mostrar mensaje en un diálogo
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void mostrarBienvenida() {
        txtInformacion.setText("""
            🌟 ¡BIENVENIDO AL CATÁLOGO PÚBLICO! 🌟
            ═══════════════════════════════════
            
            📖 Explora nuestro catálogo de libros
            🔍 Busca por título o autor
            ✅ Consulta disponibilidad en tiempo real
            🏛️ Para préstamos, acércate al mostrador
            
            📚 CARACTERÍSTICAS:
            • Más de 1000 títulos disponibles
            • Actualización en tiempo real
            • Búsqueda avanzada
            • Información detallada de cada libro
            
            🕐 HORARIOS DE ATENCIÓN:
            • Lunes a Viernes: 8:00 AM - 8:00 PM
            • Sábados: 9:00 AM - 5:00 PM
            • Domingos: 10:00 AM - 4:00 PM
            
            💡 Usa los botones de la derecha para:
            • Ver libros populares
            • Obtener ayuda sobre préstamos
            • Ver información de contacto
            """);
        txtInformacion.setCaretPosition(0);
    }
    
    @Override
    public void mostrarDespedida() {
        String mensaje = """
            🌟 ¡GRACIAS POR VISITAR NUESTRA BIBLIOTECA! 🌟
            
            📚 Esperamos haberte ayudado a encontrar tu próxima lectura
            🏛️ Te esperamos pronto para realizar tus préstamos
            
            📞 ¿Dudas? Llama al: (01) 123-4567
            🌐 Visita: www.biblioteca.gov.co
            📧 Escríbenos: info@biblioteca.gov.co
            """;
        JOptionPane.showMessageDialog(this, mensaje, "¡Hasta la próxima!", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }
    
    @Override
    public int leerOpcion() {
        return 0; // No se usa en GUI
    }
    
    @Override
    public String leerTexto(String prompt) {
        return JOptionPane.showInputDialog(this, prompt);
    }
    
    @Override
    public void pausa() {
        // No aplicable en GUI
    }
    
    // Métodos específicos para obtener datos de la interfaz
    public String getTextoBusqueda() {
        return txtBusqueda.getText().trim();
    }
    
    public String getTipoBusqueda() {
        return (String) cmbTipoBusqueda.getSelectedItem();
    }
    
    public Libro getLibroSeleccionado() {
        int filaSeleccionada = tablaCatalogo.getSelectedRow();
        if (filaSeleccionada >= 0) {
            int id = (Integer) tablaCatalogo.getValueAt(filaSeleccionada, 0);
            String titulo = (String) tablaCatalogo.getValueAt(filaSeleccionada, 1);
            String autor = (String) tablaCatalogo.getValueAt(filaSeleccionada, 2);
            int disponibles = (Integer) tablaCatalogo.getValueAt(filaSeleccionada, 3);
            int total = (Integer) tablaCatalogo.getValueAt(filaSeleccionada, 4);
            
            Libro libro = new Libro(titulo, autor, total);
            libro.setId(id);
            libro.setCantidadDisponible(disponibles);
            return libro;
        }
        return null;
    }
    
    public void mostrarLibrosPopulares() {
        txtInformacion.setText("""
            🔥 LOS MÁS POPULARES DE ESTE MES
            ═══════════════════════════════
            
            🥇 1. Don Quijote de la Mancha
               📖 Miguel de Cervantes
               ⭐ 45 préstamos este mes
            
            🥈 2. Cien Años de Soledad
               📖 Gabriel García Márquez
               ⭐ 38 préstamos este mes
            
            🥉 3. 1984
               📖 George Orwell
               ⭐ 32 préstamos este mes
            
            4️⃣ El Principito
               📖 Antoine de Saint-Exupéry
               ⭐ 28 préstamos este mes
            
            5️⃣ Orgullo y Prejuicio
               📖 Jane Austen
               ⭐ 21 préstamos este mes
            
            💡 ¿Te interesa alguno? 
            ¡Consúltalo en nuestro catálogo!
            """);
        txtInformacion.setCaretPosition(0);
    }
    
    public void mostrarAyudaPrestamos() {
        txtInformacion.setText("""
            ❓ ¿CÓMO SOLICITAR UN PRÉSTAMO?
            ══════════════════════════════
            
            📝 PASOS PARA REALIZAR UN PRÉSTAMO:
            
            1️⃣ CONSULTA EL CATÁLOGO
               • Usa este sistema para encontrar libros
               • Anota el ID del libro que te interesa
            
            2️⃣ ACÉRCATE AL MOSTRADOR
               • Ve al área de préstamos
               • Presenta tu documento de identidad
            
            3️⃣ REGÍSTRATE (si es la primera vez)
               • Completa el formulario de registro
               • Proporciona tus datos de contacto
            
            4️⃣ SOLICITA EL PRÉSTAMO
               • Menciona el ID del libro
               • El bibliotecario procesará tu solicitud
            
            ⏰ PLAZOS DE DEVOLUCIÓN:
            • 📚 Estudiantes: 15 días (máx. 3 libros)
            • 👨‍🏫 Profesores: 30 días (máx. 5 libros)
            • 🎓 Personal: 60 días (máx. 10 libros)
            
            📞 ¿Más dudas? Llama al (01) 123-4567
            """);
        txtInformacion.setCaretPosition(0);
    }
    
    public void mostrarInformacionContacto() {
        txtInformacion.setText("""
            📞 INFORMACIÓN DE CONTACTO
            ═════════════════════════
            
            🏛️ BIBLIOTECA PÚBLICA CENTRAL
            
            📍 DIRECCIÓN:
               Calle 123 #45-67
               Bogotá D.C., Colombia
               Código Postal: 110111
            
            📞 TELÉFONOS:
               • Principal: (01) 123-4567
               • Préstamos: (01) 123-4568
               • Renovaciones: (01) 123-4569
            
            🌐 ONLINE:
               • Web: www.biblioteca.gov.co
               • Email: info@biblioteca.gov.co
               • Redes: @BibliotecaCentral
            
            🚌 TRANSPORTE:
               • TransMilenio: Estación Biblioteca
               • Buses: Rutas 15, 23, 45, 67
               • Parqueadero gratuito disponible
            
            🕐 HORARIOS:
               • Lun-Vie: 8:00 AM - 8:00 PM
               • Sábados: 9:00 AM - 5:00 PM
               • Domingos: 10:00 AM - 4:00 PM
            """);
        txtInformacion.setCaretPosition(0);
    }
    
    public void mostrarEstadisticasPublicas(int totalLibros, int librosDisponibles, 
                                          int totalUsuarios, int prestamosHoy) {
        lblEstadisticas.setText(String.format(
            "📊 Total: %,d libros | ✅ Disponibles: %,d | 👥 Usuarios: %,d | 📤 Préstamos hoy: %d", 
            totalLibros, librosDisponibles, totalUsuarios, prestamosHoy));
            
        // También mostrar información detallada
        txtInformacion.setText(String.format("""
            📈 ESTADÍSTICAS DE LA BIBLIOTECA
            ════════════════════════════════
            
            📚 COLECCIÓN:
               • Total de libros: %,d
               • Disponibles ahora: %,d
               • En préstamo: %,d
            
            👥 COMUNIDAD:
               • Usuarios registrados: %,d
               • Préstamos hoy: %d
            
            📊 PORCENTAJE DE DISPONIBILIDAD:
            %s %.1f%%
            
            📈 TENDENCIAS:
            • Libros más solicitados: Ficción clásica
            • Hora pico: 2:00 PM - 4:00 PM
            • Día más activo: Martes
            
            💡 Datos actualizados en tiempo real
            """, 
            totalLibros, librosDisponibles, (totalLibros - librosDisponibles),
            totalUsuarios, prestamosHoy,
            generarBarraProgreso(totalLibros > 0 ? ((double) librosDisponibles / totalLibros) * 100 : 0),
            totalLibros > 0 ? ((double) librosDisponibles / totalLibros) * 100 : 0));
        txtInformacion.setCaretPosition(0);
    }
    
    private String generarBarraProgreso(double porcentaje) {
        int barrasCompletas = (int) (porcentaje / 10);
        StringBuilder barra = new StringBuilder("   [");
        
        for (int i = 0; i < 10; i++) {
            if (i < barrasCompletas) {
                barra.append("█");
            } else {
                barra.append("░");
            }
        }
        barra.append("]");
        return barra.toString();
    }
}