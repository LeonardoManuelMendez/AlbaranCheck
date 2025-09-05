package vista;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import modelo.ProductoEnAlbaran;
import modelo.Producto;

public class TablaVerificacionPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTable tabla;
    private DefaultTableModel modelo;
    private List<ProductoEnAlbaran> listaProductosEnAlbaranRef;
    private List<Producto> listaProductosRef;

    // ====== ESTADO ======
    enum EstadoLinea { PENDIENTE, COMPLETO, EXCESO }

    /** Icono ligero para dibujar un punto de color */
    static class DotIcon implements Icon {
        private final int size;
        private final Color color;
        public DotIcon(Color color, int size) {
            this.color = color;
            this.size = size;
        }
        @Override public int getIconWidth() { return size; }
        @Override public int getIconHeight() { return size; }
        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillOval(x, y, size, size);
            g2.setColor(new Color(0,0,0,80));
            g2.drawOval(x, y, size, size);
            g2.dispose();
        }
    }

    static class EstadoRenderer extends JLabel implements TableCellRenderer {
        private static final long serialVersionUID = 1L;
        private static final DotIcon OK_ICON     = new DotIcon(new Color(0,170,0), 12);
        private static final DotIcon EXCESO_ICON = new DotIcon(new Color(0,90,200), 12);
        private static final DotIcon PEND_ICON   = new DotIcon(new Color(200,0,0), 12);

        public EstadoRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setFont(getFont().deriveFont(Font.BOLD));
        }
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            EstadoLinea estado = (value instanceof EstadoLinea) ? (EstadoLinea) value : EstadoLinea.PENDIENTE;

            String texto;
            Icon icono;
            switch (estado) {
                case COMPLETO -> { texto = " OK";     icono = OK_ICON; }
                case EXCESO   -> { texto = " EXCESO"; icono = EXCESO_ICON; }
                default       -> { texto = " PEND.";  icono = PEND_ICON; }
            }

            setText(texto);
            setIcon(icono);

            // Tooltip con más detalle (lee columnas vecinas)
            try {
                int bEsp = (Integer) table.getValueAt(row, 2);
                int uEsp = (Integer) table.getValueAt(row, 3);
                int bRec = (Integer) table.getValueAt(row, 4);
                int uRec = (Integer) table.getValueAt(row, 5);
                setToolTipText("Bultos: " + bRec + "/" + bEsp + " • Unidades: " + uRec + "/" + uEsp);
            } catch (Exception ignored) {
                setToolTipText(null);
            }

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
    }

    // ====== RENDERER PARA OPCIONES ======
    static class ComboBoxRenderer extends JComboBox<String> implements TableCellRenderer {
        private static final long serialVersionUID = 1L;
        public ComboBoxRenderer() {
            super(new String[] { "", "Borrar Producto", "Actualizar Cantidades", "Agre/Modif Producto" });
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            setSelectedItem(value);
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            return this;
        }
    }

    public TablaVerificacionPanel(List<ProductoEnAlbaran> listaProductosEnAlbaran, List<Producto> listaProductos) {
        initializeComponents(listaProductosEnAlbaran, listaProductos);
    }

    private void initializeComponents(List<ProductoEnAlbaran> listaProductosEnAlbaran, List<Producto> listaProductos) {
        this.listaProductosEnAlbaranRef = listaProductosEnAlbaran;
        this.listaProductosRef = listaProductos;

        final String[] nombresColumnas = {
            "Código", "Nombre",
            "Bultos esperados", "Unidades esperadas",
            "Bultos recibidos", "Unidades recibidas",
            "Estado", "Opciones"
        };

        Object[][] datosfilas = convertirListaAArray(listaProductosEnAlbaran, listaProductos);

        modelo = new DefaultTableModel(datosfilas, nombresColumnas) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int column) {
                return column == 7; // Solo la columna Opciones
            }
            @Override public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 2, 3, 4, 5 -> Integer.class;
                    case 6           -> EstadoLinea.class;
                    default          -> String.class;
                };
            }
        };

        tabla = new JTable(modelo);
        instalarEditoresYRenderers();

        JScrollPane scrollPane = new JScrollPane(tabla);
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void abrirDialogoEditarPEA(int fila, List<ProductoEnAlbaran> listaProductosEnAlbaran, List<Producto> listaPr) {
        String codigo = (String) modelo.getValueAt(fila, 0);
        ProductoEnAlbaran pea = buscarProductoEnAlbaranPorCodigo(codigo, listaProductosEnAlbaran);
        if (pea != null) {
            EditarProductosEnAlbaran dialog = new EditarProductosEnAlbaran(pea, listaProductosEnAlbaran);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    actualizarTabla(listaProductosEnAlbaran);
                    tabla.clearSelection();
                }
            });
            dialog.setVisible(true);
        }
    }

    private void abrirDialogoEditarProducto(int fila, List<ProductoEnAlbaran> listaProductosEnAlbaran, List<Producto> listaPr) {
        String codigo = (String) modelo.getValueAt(fila, 0);
        Producto producto = buscarProductoPorCodigo(codigo, listaPr);
        if (producto != null) {
            EditarCrearProductos dialog = new EditarCrearProductos(producto, listaPr);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    actualizarTabla(listaProductosEnAlbaran);
                    tabla.clearSelection();
                }
            });
            dialog.setVisible(true);
        }
    }

    private ProductoEnAlbaran buscarProductoEnAlbaranPorCodigo(String codigo, List<ProductoEnAlbaran> listaProductosEnAlbaran) {
        for (ProductoEnAlbaran pea : listaProductosEnAlbaran) {
            Producto producto = pea.getProducto();
            if (producto != null && codigo.equals(producto.getCodigo())) {
                return pea;
            }
        }
        return null;
    }

    private Producto buscarProductoPorCodigo(String codigo, List<Producto> listaProductos) {
        for (Producto producto : listaProductos) {
            if (codigo.equals(producto.getCodigo())) {
                return producto;
            }
        }
        return null;
    }

    private Object[][] convertirListaAArray(List<ProductoEnAlbaran> listaProductosEnAlbaran, List<Producto> listaProductos) {
        if (listaProductosEnAlbaran == null) return new Object[0][8];
        Object[][] matriz = new Object[listaProductosEnAlbaran.size()][8];

        for (int i = 0; i < listaProductosEnAlbaran.size(); i++) {
            ProductoEnAlbaran pea = listaProductosEnAlbaran.get(i);
            if (pea == null || pea.getProducto() == null) {
                matriz[i] = new Object[] { "", "", 0, 0, 0, 0, EstadoLinea.PENDIENTE, "" };
                continue;
            }
            Producto producto = pea.getProducto();
            Producto productoInfo = buscarProductoPorCodigo(producto.getCodigo(), listaProductos);

            String nombre = (productoInfo != null && productoInfo.getNombre() != null)
                    ? productoInfo.getNombre()
                    : (producto.getNombre() != null ? producto.getNombre() : "No existe");

            boolean completo = pea.getUnidades_esperadas() == pea.getUnidades_recibidas()
                            &&  pea.getBultos_esperados()   == pea.getBultos_recibidos();
            boolean exceso   = pea.getUnidades_recibidas() > pea.getUnidades_esperadas()
                            ||  pea.getBultos_recibidos()   > pea.getBultos_esperados();
            EstadoLinea estado = completo ? EstadoLinea.COMPLETO : (exceso ? EstadoLinea.EXCESO : EstadoLinea.PENDIENTE);

            matriz[i][0] = producto.getCodigo();
            matriz[i][1] = nombre;
            matriz[i][2] = pea.getBultos_esperados();
            matriz[i][3] = pea.getUnidades_esperadas();
            matriz[i][4] = pea.getBultos_recibidos();
            matriz[i][5] = pea.getUnidades_recibidas();
            matriz[i][6] = estado;
            matriz[i][7] = ""; // Opciones
        }
        return matriz;
    }

    public JTable getTabla() {
        return tabla;
    }

    public void actualizarTabla(List<ProductoEnAlbaran> listaProductosEnAlbaran) {
        this.listaProductosEnAlbaranRef = listaProductosEnAlbaran;
        Object[][] datosfilas = convertirListaAArray(listaProductosEnAlbaran, listaProductosRef);
        modelo.setDataVector(datosfilas, new String[] {
            "Código", "Nombre",
            "Bultos esperados", "Unidades esperadas",
            "Bultos recibidos", "Unidades recibidas",
            "Estado", "Opciones"
        });
        instalarEditoresYRenderers();
        tabla.revalidate();
        tabla.repaint();
    }

    private void instalarEditoresYRenderers() {
        // Renderer para la columna Estado (índice 6)
        tabla.getColumnModel().getColumn(6).setCellRenderer(new EstadoRenderer());

        // Editor/renderer para la columna Opciones (índice 7)
        TableColumn columnaOpciones = tabla.getColumnModel().getColumn(7);

        String[] opciones = { "", "Borrar Producto", "Actualizar Cantidades", "Agre/Modif Producto" };
        JComboBox<String> comboBox = new JComboBox<>(opciones);
        comboBox.setSelectedIndex(-1);

        comboBox.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String opcion = (String) comboBox.getSelectedItem();
                if (opcion != null && !opcion.isEmpty()) {
                    switch (opcion) {
                    	// Al borrar, se quita de la tabla y de la lista de referencia
                        case "Borrar Producto" -> {
							String codigo = (String) modelo.getValueAt(fila, 0);
							ProductoEnAlbaran pea = buscarProductoEnAlbaranPorCodigo(codigo, listaProductosEnAlbaranRef);
							if (pea != null) {
								listaProductosEnAlbaranRef.remove(pea);
								modelo.removeRow(fila);
							}
						}
                        case "Actualizar Cantidades" -> abrirDialogoEditarPEA(fila, listaProductosEnAlbaranRef, listaProductosRef);
                        case "Agre/Modif Producto" -> abrirDialogoEditarProducto(fila, listaProductosEnAlbaranRef, listaProductosRef);
                    }
                    if (fila < modelo.getRowCount()) {
                        modelo.setValueAt("", fila, 7); // limpiar selección
                    }
                    tabla.clearSelection();
                }
            }
        });

        columnaOpciones.setCellEditor(new DefaultCellEditor(comboBox));
        columnaOpciones.setCellRenderer(new ComboBoxRenderer());
    }
}
