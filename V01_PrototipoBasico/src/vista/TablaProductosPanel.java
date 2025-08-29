package vista;

import java.awt.Component;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import controlador.Controlador;
import modelo.Producto;

public class TablaProductosPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTable tabla;
    private DefaultTableModel modelo;

    
    public TablaProductosPanel(List<Producto> listaProductos) {
        initializeComponents(listaProductos);
    }
    
    private void initializeComponents(List<Producto> listaProductos) {
        String[] nombresColumnas = {"Código", "Nombre", "EAN unidad", "EAN bulto", "Opciones"};
        Object[][] datosfilas = convertirListaAArray(listaProductos);

        if (datosfilas != null) {
            modelo = new DefaultTableModel(datosfilas, nombresColumnas) {
                private static final long serialVersionUID = 1L;
                
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 4; // Solo la columna "Opciones" es editable
                }
            };
            tabla = new JTable(modelo);

            // Configurar el JComboBox como editor
            JComboBox<String> comboBox = new JComboBox<>(new String[]{"", "Borrar", "Actualizar"});
            comboBox.setSelectedIndex(-1); // Asegura que no haya selección inicial
            comboBox.addActionListener(e -> {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    String opcion = (String) comboBox.getSelectedItem();
                    if (opcion != null && !opcion.isEmpty()) { // Ignorar si es la opción vacía
                        String codigo = (String) modelo.getValueAt(fila, 0);
                        System.out.println("Fila: " + fila + ", Código: " + codigo + ", Opción: " + opcion);
                        switch (opcion) {
                            case "Borrar":
                                Controlador.borrarLineadelDAT(listaProductos, codigo);
                                modelo.removeRow(fila);
                                break;
                            case "Actualizar":
                                abrirDialogoEditar(fila, listaProductos);
                                break;
                        }
                    }
                }
            });

            TableColumn columnaOpciones = tabla.getColumnModel().getColumn(4);
            columnaOpciones.setCellEditor(new DefaultCellEditor(comboBox));
            columnaOpciones.setCellRenderer(new ComboBoxRenderer());

            // Agregar la tabla a un JScrollPane y al panel
            JScrollPane scrollPane = new JScrollPane(tabla);
            this.add(scrollPane);
        }
    }
    
    private void abrirDialogoEditar(int fila, List<Producto> listaProductos) {
        String codigo = (String) modelo.getValueAt(fila, 0);
        Producto producto = buscarProductoPorCodigo(codigo, listaProductos);
        if (producto != null) {
            EditarCrearProductos dialog = new EditarCrearProductos(producto, listaProductos);
            dialog.setVisible(true);
        }
    }
    
    private Producto buscarProductoPorCodigo(String codigo, List<Producto> listaProductos) {
        for (Producto producto : listaProductos) {
            if (producto.getCodigo().equals(codigo)) {
                return producto;
            }
        }
        return null;
    }
    
    private Object[][] convertirListaAArray(List<Producto> listaProductos) {
        Object[][] matriz = new Object[listaProductos.size()][5];
        for (int i = 0; i < listaProductos.size(); i++) {
            Producto producto = listaProductos.get(i);
            matriz[i][0] = producto.getCodigo();
            matriz[i][1] = producto.getNombre();
            matriz[i][2] = producto.getEanProducto();
            matriz[i][3] = producto.getEanBulto();
            matriz[i][4] = ""; // Valor inicial del JComboBox
        }
        return matriz;
    }
    
    public JTable getTabla() {
        return tabla;
    }
    
    public void actualizarTabla(List<Producto> listaProductos) {
        Object[][] datosfilas = convertirListaAArray(listaProductos);
        modelo.setDataVector(datosfilas, new String[]{"Código", "Nombre", "EAN unidad", "EAN bulto", "Opciones"});
    }
    
    // Clase interna para el renderer del ComboBox
    static class ComboBoxRenderer extends JComboBox<String> implements TableCellRenderer {
        private static final long serialVersionUID = 1L;

        public ComboBoxRenderer() {
            super(new String[]{"", "Borrar", "Actualizar"});
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
}
