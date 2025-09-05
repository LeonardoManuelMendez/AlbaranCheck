package vista;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import modelo.ProductoEnAlbaran;
import modelo.Producto;

public class TablaProductosEnAlbaranPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTable tabla;
    private DefaultTableModel modelo;
    private List<ProductoEnAlbaran> listaProductosEnAlbaranRef;
    private List<Producto> listaProductosRef;

    // ====== RENDERER PARA OPCIONES ======
    static class ComboBoxRenderer extends JComboBox<String> implements TableCellRenderer {
        private static final long serialVersionUID = 1L;
        public ComboBoxRenderer() {
            super(new String[] { "", "Borrar Producto", "Agre/Modif Producto" });
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

    public TablaProductosEnAlbaranPanel(List<ProductoEnAlbaran> listaProductosEnAlbaran, List<Producto> listaProductos) {
        initializeComponents(listaProductosEnAlbaran, listaProductos);
    }

    private void initializeComponents(List<ProductoEnAlbaran> listaProductosEnAlbaran, List<Producto> listaProductos) {
        this.listaProductosEnAlbaranRef = listaProductosEnAlbaran;
        this.listaProductosRef = listaProductos;

        final String[] nombresColumnas = {
            "Código", "Nombre",
            "Unidades esperadas", "Bultos esperados",
            "Opciones"
        };

        Object[][] datosfilas = convertirListaAArray(listaProductosEnAlbaran, listaProductos);

        modelo = new DefaultTableModel(datosfilas, nombresColumnas) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int column) {
                return column == 4; // Solo la columna Opciones
            }
            @Override public Class<?> getColumnClass(int columnIndex) {
                return switch (columnIndex) {
                    case 2, 3 -> Integer.class;
                    default -> String.class;
                };
            }
        };

        tabla = new JTable(modelo);
        instalarEditorYRendererOpciones();

        JScrollPane scrollPane = new JScrollPane(tabla);
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    private void abrirDialogoEditarProducto(int fila, List<ProductoEnAlbaran> listaProductosEnAlbaran, List<Producto> listaPr) {
        Object valorCodigo = modelo.getValueAt(fila, 0);
        String codigo = (valorCodigo == null) ? "" : valorCodigo.toString().trim();

        Producto producto = buscarProductoPorCodigo(codigo, listaPr);

        try {
            EditarCrearProductos dialog;
            if (producto != null) {
                // EDITAR
                dialog = new EditarCrearProductos(producto, listaPr);
            } else {
                // CREAR (producto null) → pre-rellenar código
                dialog = new EditarCrearProductos(null, listaPr);
                dialog.setCodigoInicial(codigo);
                JOptionPane.showMessageDialog(
                    this,
                    "El producto con código \"" + codigo + "\" no existe en el catálogo.\nSe abrirá en modo CREAR.",
                    "Producto no encontrado",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    actualizarTabla(listaProductosEnAlbaran);
                    tabla.clearSelection();
                }
            });

            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "No se pudo abrir el editor: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Producto buscarProductoPorCodigo(String codigo, List<Producto> listaProductos) {
        if (codigo == null) return null;
        String buscado = codigo.trim();
        if (listaProductos == null) return null;

        for (Producto producto : listaProductos) {
            if (producto == null) continue;
            String cod = producto.getCodigo();
            if (cod != null && buscado.equals(cod.trim())) {
                return producto;
            }
        }
        return null;
    }

    private Object[][] convertirListaAArray(List<ProductoEnAlbaran> listaProductosEnAlbaran, List<Producto> listaProductos) {
        if (listaProductosEnAlbaran == null) return new Object[0][5];
        Object[][] matriz = new Object[listaProductosEnAlbaran.size()][5];

        for (int i = 0; i < listaProductosEnAlbaran.size(); i++) {
            ProductoEnAlbaran pea = listaProductosEnAlbaran.get(i);
            if (pea == null || pea.getProducto() == null) {
                matriz[i] = new Object[] { "", "", 0, 0, "" };
                continue;
            }
            Producto producto = pea.getProducto();
            Producto productoInfo = buscarProductoPorCodigo(producto.getCodigo(), listaProductos);

            matriz[i][0] = producto.getCodigo();
            matriz[i][1] = (productoInfo != null && productoInfo.getNombre() != null) ? productoInfo.getNombre() : "No existe";
            matriz[i][2] = pea.getUnidades_esperadas();
            matriz[i][3] = pea.getBultos_esperados();
            matriz[i][4] = ""; // Opciones
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
            "Unidades esperadas", "Bultos esperados",
            "Opciones"
        });
        instalarEditorYRendererOpciones();
        tabla.revalidate();
        tabla.repaint();
    }

    private void instalarEditorYRendererOpciones() {
        // Editor/renderer para la columna Opciones (índice 4)
        TableColumn columnaOpciones = tabla.getColumnModel().getColumn(4);

        String[] opciones = { "", "Borrar Producto", "Agre/Modif Producto" };
        JComboBox<String> comboBox = new JComboBox<>(opciones);
        comboBox.setSelectedIndex(-1);

        comboBox.addActionListener(e -> {
            // Fila robusta: primero la que se está editando, si no, la seleccionada
            int fila = tabla.getEditingRow();
            if (fila < 0) fila = tabla.getSelectedRow();
            if (fila < 0 && tabla.getSelectedRows().length > 0) fila = tabla.getSelectedRows()[0];
            if (fila < 0) return;

            String opcion = (String) comboBox.getSelectedItem();
            if (opcion == null || opcion.isEmpty()) return;

            // Cerrar edición antes de actuar
            if (tabla.isEditing()) {
                try { tabla.getCellEditor().stopCellEditing(); } catch (Exception ignore) {}
            }

            switch (opcion) {
                case "Borrar Producto" -> {
                    modelo.removeRow(fila);
                }
                case "Agre/Modif Producto" -> {
                    abrirDialogoEditarProducto(fila, listaProductosEnAlbaranRef, listaProductosRef);
                }
            }

            // Limpiar la celda “Opciones” y selección
            if (fila < modelo.getRowCount()) {
                modelo.setValueAt("", fila, 4);
            }
            tabla.clearSelection();
        });

        columnaOpciones.setCellEditor(new DefaultCellEditor(comboBox));
        columnaOpciones.setCellRenderer(new ComboBoxRenderer());
    }
}



