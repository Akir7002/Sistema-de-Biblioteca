package vista;

import modelo.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class BibliotecaSwingVista extends JFrame implements IVista {
    // Paleta de colores terrosos mejorada para biblioteca
    private static final Color COLOR_FONDO_PRINCIPAL = new Color(250, 245, 235); // Beige más suave
    private static final Color COLOR_FONDO_SECUNDARIO = new Color(235, 205, 175); // Marrón claro más suave
    private static final Color COLOR_ACENTO = new Color(139, 69, 19); // Marrón chocolate para acentos
    private static final Color COLOR_TEXTO_PRINCIPAL = new Color(62, 39, 35); // Marrón más oscuro para mejor contraste
    private static final Color COLOR_BOTONES = new Color(205, 170, 125); // Marrón dorado más suave
    private static final Color COLOR_BOTONES_HOVER = new Color(180, 140, 90); // Color hover más oscuro
    private static final Color COLOR_BORDES = new Color(160, 120, 90); // Bordes más suaves
    private static final Color COLOR_TABLA_ALTERNANTE = new Color(245, 240, 230); // Para zebra-striping
    
    // Fuentes personalizadas para un aspecto más elegante
    private static final Font FUENTE_TITULO = new Font("Georgia", Font.BOLD, 16);
    private static final Font FUENTE_SUBTITULO = new Font("Georgia", Font.BOLD, 14);
    private static final Font FUENTE_NORMAL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FUENTE_BOTON = new Font("Segoe UI", Font.BOLD, 11);
    
    // Componentes principales
    private JTabbedPane tabbedPane;
    private JTable tablaLibros, tablaUsuarios, tablaPrestamos;
    private DefaultTableModel modeloLibros, modeloUsuarios, modeloPrestamos;
    private JTextArea txtMensajes;
    
    // Campos de entrada para libros
    private JTextField txtTitulo, txtAutor, txtCantidad;
    
    // Campos de entrada para usuarios
    private JTextField txtNombre, txtEmail, txtTelefono;
    private JComboBox<TipoUsuario> cmbTipoUsuario;
    
    // Campos de entrada para préstamos
    private JTextField txtIdLibro, txtIdUsuario, txtIdPrestamo;
    
    // Campos de búsqueda
    private JTextField txtBusquedaLibro, txtBusquedaUsuario;
    
    // Tablas y modelos de búsqueda
    private JTable tablaBusquedaLibros, tablaBusquedaUsuarios;
    private DefaultTableModel modeloBusquedaLibros, modeloBusquedaUsuarios;
    private JComboBox<String> cmbTipoBusquedaLibro, cmbTipoBusquedaUsuario;
    
    // Botones
    private JButton btnAgregarLibro, btnEliminarLibro;
    private JButton btnAgregarUsuario, btnEliminarUsuario;
    private JButton btnRealizarPrestamo, btnDevolverLibro;
    private JButton btnBuscarLibro, btnBuscarUsuario;
    private JButton btnListarLibros, btnListarUsuarios, btnListarPrestamos;
    private JButton btnPrestamosVencidos, btnEstadisticas, btnLimpiarCampos;
    
    // Panel de estadísticas
    private JLabel lblTotalLibros, lblLibrosDisponibles, lblTotalUsuarios;
    private JLabel lblPrestamosActivos, lblPrestamosVencidos;
    
    public BibliotecaSwingVista() {
        inicializarComponentes();
        configurarVentana();
    }
    
    private void inicializarComponentes() {
        tabbedPane = new JTabbedPane();
        
        // Crear todas las pestañas
        tabbedPane.addTab("📚 Libros", crearPanelLibros());
        tabbedPane.addTab("👥 Usuarios", crearPanelUsuarios());
        tabbedPane.addTab("📤 Préstamos", crearPanelPrestamos());
        tabbedPane.addTab("🔍 Búsquedas", crearPanelBusquedas());
        tabbedPane.addTab("📊 Estadísticas", crearPanelEstadisticas());
        
        // Panel de mensajes en la parte inferior
        JPanel panelMensajes = crearPanelMensajes();
        
        // Layout principal
        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
        add(panelMensajes, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelLibros() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel superior con formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Gestión de Libros"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campos del formulario
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTitulo = new JTextField(20);
        panelFormulario.add(txtTitulo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtAutor = new JTextField(20);
        panelFormulario.add(txtAutor, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtCantidad = new JTextField(10);
        panelFormulario.add(txtCantidad, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnAgregarLibro = new JButton("Agregar Libro");
        btnAgregarLibro.setActionCommand("AGREGAR_LIBRO");
        btnEliminarLibro = new JButton("Eliminar Libro Seleccionado");
        btnEliminarLibro.setActionCommand("ELIMINAR_LIBRO");
        btnListarLibros = new JButton("Actualizar Lista");
        btnListarLibros.setActionCommand("LISTAR_LIBROS");
        
        panelBotones.add(btnAgregarLibro);
        panelBotones.add(btnEliminarLibro);
        panelBotones.add(btnListarLibros);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelFormulario.add(panelBotones, gbc);
        
        // Tabla de libros
        String[] columnasLibros = {"ID", "Título", "Autor", "Disponibles", "Total", "Estado"};
        modeloLibros = new DefaultTableModel(columnasLibros, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaLibros = new JTable(modeloLibros);
        tablaLibros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        configurarTabla(tablaLibros);
        
        JScrollPane scrollLibros = new JScrollPane(tablaLibros);
        scrollLibros.setPreferredSize(new Dimension(0, 300));
        
        panel.add(panelFormulario, BorderLayout.NORTH);
        panel.add(scrollLibros, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel superior con formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Gestión de Usuarios"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campos del formulario
        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtNombre = new JTextField(20);
        panelFormulario.add(txtNombre, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtEmail = new JTextField(20);
        panelFormulario.add(txtEmail, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTelefono = new JTextField(15);
        panelFormulario.add(txtTelefono, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panelFormulario.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        cmbTipoUsuario = new JComboBox<>(TipoUsuario.values());
        panelFormulario.add(cmbTipoUsuario, gbc);
        
        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnAgregarUsuario = new JButton("Agregar Usuario");
        btnAgregarUsuario.setActionCommand("AGREGAR_USUARIO");
        btnEliminarUsuario = new JButton("Eliminar Usuario Seleccionado");
        btnEliminarUsuario.setActionCommand("ELIMINAR_USUARIO");
        btnListarUsuarios = new JButton("Actualizar Lista");
        btnListarUsuarios.setActionCommand("LISTAR_USUARIOS");
        
        panelBotones.add(btnAgregarUsuario);
        panelBotones.add(btnEliminarUsuario);
        panelBotones.add(btnListarUsuarios);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelFormulario.add(panelBotones, gbc);
        
        // Tabla de usuarios
        String[] columnasUsuarios = {"ID", "Nombre", "Email", "Tipo", "Préstamos", "Estado"};
        modeloUsuarios = new DefaultTableModel(columnasUsuarios, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaUsuarios = new JTable(modeloUsuarios);
        tablaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        configurarTabla(tablaUsuarios);
        
        JScrollPane scrollUsuarios = new JScrollPane(tablaUsuarios);
        scrollUsuarios.setPreferredSize(new Dimension(0, 300));
        
        panel.add(panelFormulario, BorderLayout.NORTH);
        panel.add(scrollUsuarios, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelPrestamos() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Panel superior con controles
        JPanel panelControles = new JPanel(new GridBagLayout());
        panelControles.setBorder(BorderFactory.createTitledBorder("Gestión de Préstamos"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Realizar préstamo
        JPanel panelPrestamo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPrestamo.setBorder(BorderFactory.createTitledBorder("Realizar Préstamo"));
        panelPrestamo.add(new JLabel("ID Libro:"));
        txtIdLibro = new JTextField(5);
        panelPrestamo.add(txtIdLibro);
        panelPrestamo.add(new JLabel("ID Usuario:"));
        txtIdUsuario = new JTextField(5);
        panelPrestamo.add(txtIdUsuario);
        btnRealizarPrestamo = new JButton("Realizar Préstamo");
        btnRealizarPrestamo.setActionCommand("REALIZAR_PRESTAMO");
        panelPrestamo.add(btnRealizarPrestamo);
        
        // Devolver libro
        JPanel panelDevolucion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelDevolucion.setBorder(BorderFactory.createTitledBorder("Devolver Libro"));
        panelDevolucion.add(new JLabel("ID Préstamo:"));
        txtIdPrestamo = new JTextField(5);
        panelDevolucion.add(txtIdPrestamo);
        btnDevolverLibro = new JButton("Devolver Libro");
        btnDevolverLibro.setActionCommand("DEVOLVER_LIBRO");
        panelDevolucion.add(btnDevolverLibro);
        
        // Botones de listado
        JPanel panelListados = new JPanel(new FlowLayout());
        btnListarPrestamos = new JButton("Préstamos Activos");
        btnListarPrestamos.setActionCommand("LISTAR_PRESTAMOS_ACTIVOS");
        btnPrestamosVencidos = new JButton("Préstamos Vencidos");
        btnPrestamosVencidos.setActionCommand("PRESTAMOS_VENCIDOS");
        panelListados.add(btnListarPrestamos);
        panelListados.add(btnPrestamosVencidos);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        panelControles.add(panelPrestamo, gbc);
        gbc.gridy = 1;
        panelControles.add(panelDevolucion, gbc);
        gbc.gridy = 2;
        panelControles.add(panelListados, gbc);
        
        // Tabla de préstamos
        String[] columnasPrestamos = {"ID", "Usuario", "Libro", "Fecha Préstamo", "Fecha Límite", "Estado", "Días Retraso"};
        modeloPrestamos = new DefaultTableModel(columnasPrestamos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaPrestamos = new JTable(modeloPrestamos);
        tablaPrestamos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        configurarTabla(tablaPrestamos);
        
        JScrollPane scrollPrestamos = new JScrollPane(tablaPrestamos);
        scrollPrestamos.setPreferredSize(new Dimension(0, 300));
        
        panel.add(panelControles, BorderLayout.NORTH);
        panel.add(scrollPrestamos, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelBusquedas() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JTabbedPane tabsBusqueda = new JTabbedPane();
        
        // Búsqueda de libros
        JPanel panelBusquedaLibros = new JPanel(new BorderLayout());
        JPanel controlBusquedaLibros = new JPanel(new FlowLayout());
        controlBusquedaLibros.setBorder(BorderFactory.createTitledBorder("Buscar Libros"));
        
        controlBusquedaLibros.add(new JLabel("Buscar:"));
        txtBusquedaLibro = new JTextField(20);
        controlBusquedaLibros.add(txtBusquedaLibro);
        
        String[] tiposBusquedaLibro = {"Por Título", "Por Autor", "Por ID", "Solo Disponibles"};
        cmbTipoBusquedaLibro = new JComboBox<>(tiposBusquedaLibro);
        controlBusquedaLibros.add(cmbTipoBusquedaLibro);
        
        btnBuscarLibro = new JButton("Buscar");
        btnBuscarLibro.setActionCommand("BUSCAR_LIBRO");
        controlBusquedaLibros.add(btnBuscarLibro);
        
        // Tabla de resultados de libros
        modeloBusquedaLibros = new DefaultTableModel(
            new String[]{"ID", "Título", "Autor", "Disponibles", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaBusquedaLibros = new JTable(modeloBusquedaLibros);
        configurarTabla(tablaBusquedaLibros);
        
        panelBusquedaLibros.add(controlBusquedaLibros, BorderLayout.NORTH);
        panelBusquedaLibros.add(new JScrollPane(tablaBusquedaLibros), BorderLayout.CENTER);
        
        // Búsqueda de usuarios
        JPanel panelBusquedaUsuarios = new JPanel(new BorderLayout());
        JPanel controlBusquedaUsuarios = new JPanel(new FlowLayout());
        controlBusquedaUsuarios.setBorder(BorderFactory.createTitledBorder("Buscar Usuarios"));
        
        controlBusquedaUsuarios.add(new JLabel("Buscar:"));
        txtBusquedaUsuario = new JTextField(20);
        controlBusquedaUsuarios.add(txtBusquedaUsuario);
        
        String[] tiposBusquedaUsuario = {"Por Nombre", "Por Email", "Por ID", "Por Tipo"};
        cmbTipoBusquedaUsuario = new JComboBox<>(tiposBusquedaUsuario);
        controlBusquedaUsuarios.add(cmbTipoBusquedaUsuario);
        
        btnBuscarUsuario = new JButton("Buscar");
        btnBuscarUsuario.setActionCommand("BUSCAR_USUARIO");
        controlBusquedaUsuarios.add(btnBuscarUsuario);
        
        // Tabla de resultados de usuarios
        modeloBusquedaUsuarios = new DefaultTableModel(
            new String[]{"ID", "Nombre", "Email", "Tipo", "Préstamos"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaBusquedaUsuarios = new JTable(modeloBusquedaUsuarios);
        configurarTabla(tablaBusquedaUsuarios);
        
        panelBusquedaUsuarios.add(controlBusquedaUsuarios, BorderLayout.NORTH);
        panelBusquedaUsuarios.add(new JScrollPane(tablaBusquedaUsuarios), BorderLayout.CENTER);
        
        tabsBusqueda.addTab("📚 Libros", panelBusquedaLibros);
        tabsBusqueda.addTab("👥 Usuarios", panelBusquedaUsuarios);
        
        panel.add(tabsBusqueda, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Panel de estadísticas generales
        JPanel panelStats = new JPanel(new GridLayout(3, 2, 10, 10));
        panelStats.setBorder(BorderFactory.createTitledBorder("📊 Estadísticas del Sistema"));
        
        lblTotalLibros = crearLabelEstadistica("Total de Libros:", "0");
        lblLibrosDisponibles = crearLabelEstadistica("Libros Disponibles:", "0");
        lblTotalUsuarios = crearLabelEstadistica("Total de Usuarios:", "0");
        lblPrestamosActivos = crearLabelEstadistica("Préstamos Activos:", "0");
        lblPrestamosVencidos = crearLabelEstadistica("Préstamos Vencidos:", "0");
        
        panelStats.add(lblTotalLibros);
        panelStats.add(lblLibrosDisponibles);
        panelStats.add(lblTotalUsuarios);
        panelStats.add(lblPrestamosActivos);
        panelStats.add(lblPrestamosVencidos);
        
        // Botón para actualizar estadísticas
        btnEstadisticas = new JButton("🔄 Actualizar Estadísticas");
        btnEstadisticas.setActionCommand("MOSTRAR_ESTADISTICAS");
        btnEstadisticas.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        
        // Botón para limpiar todos los campos
        btnLimpiarCampos = new JButton("🧹 Limpiar Todos los Campos");
        btnLimpiarCampos.setActionCommand("LIMPIAR_CAMPOS");
        
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.add(btnEstadisticas);
        panelBotones.add(btnLimpiarCampos);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(panelStats, gbc);
        gbc.gridy = 1; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        panel.add(Box.createVerticalGlue(), gbc);
        gbc.gridy = 2; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(panelBotones, gbc);
        
        return panel;
    }
    
    private JLabel crearLabelEstadistica(String titulo, String valor) {
        JLabel label = new JLabel("<html><b>" + titulo + "</b><br><font size='5' color='blue'>" + valor + "</font></html>");
        label.setBorder(BorderFactory.createEtchedBorder());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    private JPanel crearPanelMensajes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("📝 Mensajes del Sistema"));
        
        txtMensajes = new JTextArea(6, 0);
        txtMensajes.setEditable(false);
        txtMensajes.setBackground(Color.WHITE);
        txtMensajes.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        txtMensajes.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollMensajes = new JScrollPane(txtMensajes);
        scrollMensajes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollMensajes, BorderLayout.CENTER);
        
        JButton btnLimpiarMensajes = new JButton("Limpiar Mensajes");
        btnLimpiarMensajes.addActionListener(e -> limpiarMensajes());
        panel.add(btnLimpiarMensajes, BorderLayout.EAST);
        
        return panel;
    }
    
    private void configurarTabla(JTable tabla) {
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setRowHeight(25);
        tabla.setGridColor(Color.LIGHT_GRAY);
        tabla.setShowGrid(true);
        
        // Agregar ordenamiento
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>((DefaultTableModel) tabla.getModel());
        tabla.setRowSorter(sorter);
    }
    
    private void configurarVentana() {
        setTitle("🏛️ Sistema de Gestión de Biblioteca - Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));
        
        // Aplicar tema terroso de biblioteca
        aplicarTemaTerrosoLibreria();
        
        // Configurar look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            // Continuar con el look and feel por defecto
        }
        
        // Aplicar colores después del look and feel
        aplicarColoresPersonalizados();
    }
    
    private void aplicarTemaTerrosoLibreria() {
        // Configurar colores globales de la interfaz
        getContentPane().setBackground(COLOR_FONDO_PRINCIPAL);
    }
    
    private void aplicarColoresPersonalizados() {
        // Aplicar colores al panel principal
        if (tabbedPane != null) {
            tabbedPane.setBackground(COLOR_FONDO_PRINCIPAL);
            tabbedPane.setForeground(COLOR_TEXTO_PRINCIPAL);
            
            // Aplicar colores a cada pestaña
            for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                Component component = tabbedPane.getComponentAt(i);
                if (component instanceof JPanel) {
                    aplicarColoresRecursivo((JPanel) component);
                }
            }
        }
    }
    
    private void aplicarColoresRecursivo(Container container) {
        container.setBackground(COLOR_FONDO_PRINCIPAL);
        
        for (Component component : container.getComponents()) {
            if (component instanceof JButton) {
                JButton boton = (JButton) component;
                configurarBotonElegante(boton);
            } else if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                label.setForeground(COLOR_TEXTO_PRINCIPAL);
                // Aplicar fuente según el contexto
                if (label.getText().contains("📚") || label.getText().contains("👥") || 
                    label.getText().contains("📤") || label.getText().contains("🔍")) {
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
            } else if (component instanceof JTextArea) {
                JTextArea area = (JTextArea) component;
                area.setBackground(Color.WHITE);
                area.setForeground(COLOR_TEXTO_PRINCIPAL);
                area.setFont(FUENTE_NORMAL);
                area.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(COLOR_BORDES, 1),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            } else if (component instanceof JTable) {
                configurarTablaElegante((JTable) component);
            } else if (component instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) component;
                scroll.setBackground(COLOR_FONDO_PRINCIPAL);
                scroll.getViewport().setBackground(COLOR_FONDO_PRINCIPAL);
                scroll.setBorder(BorderFactory.createLineBorder(COLOR_BORDES, 1));
                if (scroll.getViewport().getView() instanceof JTextArea) {
                    scroll.getViewport().getView().setBackground(Color.WHITE);
                    scroll.getViewport().getView().setForeground(COLOR_TEXTO_PRINCIPAL);
                }
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
        tabla.setRowHeight(25);
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
                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return this;
            }
        });
    }
    
    // Métodos para conectar con el controlador
    public void setControladorListener(ActionListener listener) {
        // Asignar listener a todos los botones
        btnAgregarLibro.addActionListener(listener);
        btnEliminarLibro.addActionListener(listener);
        btnListarLibros.addActionListener(listener);
        btnAgregarUsuario.addActionListener(listener);
        btnEliminarUsuario.addActionListener(listener);
        btnListarUsuarios.addActionListener(listener);
        btnRealizarPrestamo.addActionListener(listener);
        btnDevolverLibro.addActionListener(listener);
        btnListarPrestamos.addActionListener(listener);
        btnPrestamosVencidos.addActionListener(listener);
        btnBuscarLibro.addActionListener(listener);
        btnBuscarUsuario.addActionListener(listener);
        btnEstadisticas.addActionListener(listener);
        btnLimpiarCampos.addActionListener(listener);
    }
    
    // Implementación de IVista
    @Override
    public void mostrarMenu() {
        setVisible(true);
    }
    
    @Override
    public void mostrarLibros(List<Libro> libros) {
        modeloLibros.setRowCount(0);
        for (Libro libro : libros) {
            Object[] fila = {
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getCantidadDisponible(),
                libro.getCantidadTotal(),
                libro.estaDisponible() ? "✅ Disponible" : "❌ Agotado"
            };
            modeloLibros.addRow(fila);
        }
        tabbedPane.setSelectedIndex(0);
    }
    
    @Override
    public void mostrarUsuarios(List<Usuario> usuarios) {
        modeloUsuarios.setRowCount(0);
        for (Usuario usuario : usuarios) {
            Object[] fila = {
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTipoUsuario().getDescripcion(),
                usuario.getPrestamosActivos() + "/" + usuario.getTipoUsuario().getLimitePrestamos(),
                usuario.isActivo() ? "✅ Activo" : "❌ Inactivo"
            };
            modeloUsuarios.addRow(fila);
        }
        tabbedPane.setSelectedIndex(1);
    }
    
    @Override
    public void mostrarPrestamos(List<Prestamo> prestamos) {
        modeloPrestamos.setRowCount(0);
        for (Prestamo prestamo : prestamos) {
            String estado;
            String diasRetraso = "-";
            
            if (prestamo.getEstado() == EstadoPrestamo.ACTIVO) {
                if (prestamo.estaVencido()) {
                    estado = "🔴 Vencido";
                    diasRetraso = "+" + prestamo.getDiasRetraso();
                } else {
                    estado = "🟢 Activo";
                }
            } else {
                estado = "✅ Devuelto";
            }
            
            Object[] fila = {
                prestamo.getId(),
                prestamo.getUsuario().getNombre(),
                prestamo.getLibro().getTitulo(),
                prestamo.getFechaPrestamo(),
                prestamo.getFechaDevolucionEsperada(),
                estado,
                diasRetraso
            };
            modeloPrestamos.addRow(fila);
        }
        tabbedPane.setSelectedIndex(2);
    }
    
    @Override
    public void mostrarMensaje(String mensaje) {
        txtMensajes.append("ℹ️ " + mensaje + "\n");
        txtMensajes.setCaretPosition(txtMensajes.getDocument().getLength());
    }
    
    @Override
    public void mostrarError(String error) {
        txtMensajes.append("❌ ERROR: " + error + "\n");
        txtMensajes.setCaretPosition(txtMensajes.getDocument().getLength());
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    @Override
    public void mostrarExito(String mensaje) {
        txtMensajes.append("✅ ÉXITO: " + mensaje + "\n");
        txtMensajes.setCaretPosition(txtMensajes.getDocument().getLength());
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
    public int leerNumero(String prompt) {
        try {
            String input = JOptionPane.showInputDialog(this, prompt);
            return input != null ? Integer.parseInt(input) : -1;
        } catch (NumberFormatException e) {
            mostrarError("Debe ingresar un número válido.");
            return -1;
        }
    }
    
    @Override
    public void limpiarPantalla() {
        // No aplicable en GUI
    }
    
    @Override
    public void pausa() {
        // No aplicable en GUI
    }
    
    // Métodos específicos para obtener datos de los formularios
    public Libro solicitarDatosLibro() {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String cantidadText = txtCantidad.getText().trim();
        
        if (titulo.isEmpty() || autor.isEmpty() || cantidadText.isEmpty()) {
            mostrarError("Todos los campos son obligatorios.");
            return null;
        }
        
        try {
            int cantidad = Integer.parseInt(cantidadText);
            if (cantidad <= 0) {
                mostrarError("La cantidad debe ser mayor a 0.");
                return null;
            }
            return new Libro(titulo, autor, cantidad);
        } catch (NumberFormatException e) {
            mostrarError("La cantidad debe ser un número válido.");
            return null;
        }
    }
    
    public Usuario solicitarDatosUsuario() {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String telefono = txtTelefono.getText().trim();
        TipoUsuario tipo = (TipoUsuario) cmbTipoUsuario.getSelectedItem();
        
        if (nombre.isEmpty() || email.isEmpty()) {
            mostrarError("Nombre y email son obligatorios.");
            return null;
        }
        
        return new Usuario(nombre, email, telefono, tipo);
    }
    
    // Getters para los campos de entrada
    public String getTituloLibro() {
        return txtTitulo.getText().trim();
    }
    
    public String getAutorLibro() {
        return txtAutor.getText().trim();
    }
    
    public int getCantidadLibro() {
        try {
            return Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public String getNombreUsuario() {
        return txtNombre.getText().trim();
    }
    
    public String getEmailUsuario() {
        return txtEmail.getText().trim();
    }
    
    public String getTelefonoUsuario() {
        return txtTelefono.getText().trim();
    }
    
    public TipoUsuario getTipoUsuario() {
        return (TipoUsuario) cmbTipoUsuario.getSelectedItem();
    }
    
    public int getIdLibroPrestamo() {
        try {
            return Integer.parseInt(txtIdLibro.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public int getIdUsuarioPrestamo() {
        try {
            return Integer.parseInt(txtIdUsuario.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public int getIdPrestamo() {
        try {
            return Integer.parseInt(txtIdPrestamo.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    public String getTextoBusquedaLibro() {
        return txtBusquedaLibro.getText().trim();
    }
    
    public String getTextoBusquedaUsuario() {
        return txtBusquedaUsuario.getText().trim();
    }
    
    public String getTipoBusquedaLibro() {
        return (String) cmbTipoBusquedaLibro.getSelectedItem();
    }
    
    public String getTipoBusquedaUsuario() {
        return (String) cmbTipoBusquedaUsuario.getSelectedItem();
    }
    
    public void mostrarResultadosBusquedaLibros(List<Libro> libros) {
        modeloBusquedaLibros.setRowCount(0);
        for (Libro libro : libros) {
            Object[] fila = {
                libro.getId(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getCantidadDisponible(),
                libro.getCantidadTotal()
            };
            modeloBusquedaLibros.addRow(fila);
        }
        // Cambiar a la pestaña de búsquedas
        tabbedPane.setSelectedIndex(3); // Pestaña "Búsquedas"
        
        if (libros.isEmpty()) {
            mostrarMensaje("No se encontraron libros con los criterios de búsqueda especificados.");
        } else {
            mostrarExito("Se encontraron " + libros.size() + " libro(s).");
        }
    }
    
    public void mostrarResultadosBusquedaUsuarios(List<Usuario> usuarios) {
        modeloBusquedaUsuarios.setRowCount(0);
        for (Usuario usuario : usuarios) {
            Object[] fila = {
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTipoUsuario(),
                usuario.getPrestamosActivos()
            };
            modeloBusquedaUsuarios.addRow(fila);
        }
        // Cambiar a la pestaña de búsquedas
        tabbedPane.setSelectedIndex(3); // Pestaña "Búsquedas"
        
        if (usuarios.isEmpty()) {
            mostrarMensaje("No se encontraron usuarios con los criterios de búsqueda especificados.");
        } else {
            mostrarExito("Se encontraron " + usuarios.size() + " usuario(s).");
        }
    }
    
    public int getLibroSeleccionadoId() {
        int filaSeleccionada = tablaLibros.getSelectedRow();
        if (filaSeleccionada >= 0) {
            return (Integer) tablaLibros.getValueAt(filaSeleccionada, 0);
        }
        return -1;
    }
    
    public int getUsuarioSeleccionadoId() {
        int filaSeleccionada = tablaUsuarios.getSelectedRow();
        if (filaSeleccionada >= 0) {
            return (Integer) tablaUsuarios.getValueAt(filaSeleccionada, 0);
        }
        return -1;
    }
    
    public int getPrestamoSeleccionadoId() {
        int filaSeleccionada = tablaPrestamos.getSelectedRow();
        if (filaSeleccionada >= 0) {
            return (Integer) tablaPrestamos.getValueAt(filaSeleccionada, 0);
        }
        return -1;
    }
    
    public void limpiarCamposLibro() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtCantidad.setText("");
    }
    
    public void limpiarCamposUsuario() {
        txtNombre.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        cmbTipoUsuario.setSelectedIndex(0);
    }
    
    public void limpiarCamposPrestamo() {
        txtIdLibro.setText("");
        txtIdUsuario.setText("");
        txtIdPrestamo.setText("");
    }
    
    public void limpiarTodosLosCampos() {
        limpiarCamposLibro();
        limpiarCamposUsuario();
        limpiarCamposPrestamo();
        txtBusquedaLibro.setText("");
        txtBusquedaUsuario.setText("");
    }
    
    public void limpiarMensajes() {
        txtMensajes.setText("");
    }
    
    public boolean confirmarAccion(String mensaje) {
        int opcion = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar", 
                                                 JOptionPane.YES_NO_OPTION, 
                                                 JOptionPane.QUESTION_MESSAGE);
        return opcion == JOptionPane.YES_OPTION;
    }
    
    public void mostrarEstadisticas(int totalLibros, int librosDisponibles, 
                                  int totalUsuarios, int prestamosActivos, 
                                  int prestamosVencidos) {
        lblTotalLibros.setText("<html><b>Total de Libros:</b><br><font size='5' color='blue'>" + totalLibros + "</font></html>");
        lblLibrosDisponibles.setText("<html><b>Libros Disponibles:</b><br><font size='5' color='green'>" + librosDisponibles + "</font></html>");
        lblTotalUsuarios.setText("<html><b>Total de Usuarios:</b><br><font size='5' color='blue'>" + totalUsuarios + "</font></html>");
        lblPrestamosActivos.setText("<html><b>Préstamos Activos:</b><br><font size='5' color='orange'>" + prestamosActivos + "</font></html>");
        lblPrestamosVencidos.setText("<html><b>Préstamos Vencidos:</b><br><font size='5' color='red'>" + prestamosVencidos + "</font></html>");
        
        tabbedPane.setSelectedIndex(4); // Cambiar a la pestaña de estadísticas
        
        mostrarMensaje("Estadísticas actualizadas: " + totalLibros + " libros, " + 
                      totalUsuarios + " usuarios, " + prestamosActivos + " préstamos activos");
    }
}