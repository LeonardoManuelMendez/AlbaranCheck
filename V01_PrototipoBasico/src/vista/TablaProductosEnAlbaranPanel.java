package vista;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
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

    public TablaProductosEnAlbaranPanel(List<ProductoEnAlbaran> listaProductosEnAlbaran, List<Producto> listaProductos) {
        initializeComponents(listaProductosEnAlbaran, listaProductos);
    }

    private void initializeComponents(List<ProductoEnAlbaran> listaProductosEnAlbaran, List<Producto> listaProductos) {
        this.listaProductosEnAlbaranRef = listaProductosEnAlbaran;
        this.listaProductosRef = listaProductos;

        final String[] nombresColumnas = {
            "Código", "Nombre",
            "Unidades esperadas", "Bultos esperados",
            "Unidades recibidas", "Bultos recibidos",
            "Opciones"
        };

        Object[][] datosfilas = convertirListaAArray(listaProductosEnAlbaran, listaProductos);

        modelo = new DefaultTableModel(datosfilas, nombresColumnas) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Solo la columna Opciones
            }
        };

        tabla = new JTable(modelo);
        instalarEditorYRendererOpciones();

        JScrollPane scrollPane = new JScrollPane(tabla);
        this.setLayout(new java.awt.BorderLayout());
        this.add(scrollPane, java.awt.BorderLayout.CENTER);
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
		ProductoEnAlbaran pea = buscarProductoEnAlbaranPorCodigo(codigo, listaProductosEnAlbaran);
		// buscar el producto en la lista de productos
		Producto producto = buscarProductoPorCodigo(codigo, listaPr);
		if (pea != null) {
			EditarCrearProductos dialog = new EditarCrearProductos(pea.getProducto(), listaPr);
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
        if (listaProductosEnAlbaran == null) return new Object[0][7];
        Object[][] matriz = new Object[listaProductosEnAlbaran.size()][7];
        for (int i = 0; i < listaProductosEnAlbaran.size(); i++) {
            ProductoEnAlbaran pea = listaProductosEnAlbaran.get(i);
            if (pea == null || pea.getProducto() == null) {
                matriz[i] = new Object[] { "", "", 0, 0, 0, 0, "" };
                continue;
            }
            Producto producto = pea.getProducto();
            Producto productoInfo = buscarProductoPorCodigo(producto.getCodigo(), listaProductos);
            matriz[i][0] = producto.getCodigo();
            // el nombre del producto se obtiene del objeto Producto pero no del objeto Producto de la lista ProductoEnAlbaran. Si no del objeto Producto de la lista Productos
            if (productoInfo != null && productoInfo.getNombre() != null) {
                matriz[i][1] = productoInfo.getNombre();
            } else {
                matriz[i][1] = "No existe";
            }
            matriz[i][2] = pea.getUnidades_esperadas();
            matriz[i][3] = pea.getBultos_esperados();
            matriz[i][4] = pea.getUnidades_recibidas();
            matriz[i][5] = pea.getBultos_recibidos();
            matriz[i][6] = ""; // Opciones
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
            "Unidades recibidas", "Bultos recibidos",
            "Opciones"
        });
        instalarEditorYRendererOpciones();
        tabla.revalidate();
        tabla.repaint();
    }

    static class ComboBoxRenderer extends JComboBox<String> implements TableCellRenderer {
        private static final long serialVersionUID = 1L;
        public ComboBoxRenderer() {
            super(new String[] { "", "Borrar", "Actualizar" });
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
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

    private void instalarEditorYRendererOpciones() {
        // Columna Opciones = índice 6
        TableColumn columnaOpciones = tabla.getColumnModel().getColumn(6);

        JComboBox<String> comboBox = new JComboBox<>(new String[] { "", "Borrar Producto", "Actualizar Cantidades", "Agre/Modif Producto"});
        comboBox.setSelectedIndex(-1);

        comboBox.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String opcion = (String) comboBox.getSelectedItem();
                if (opcion != null && !opcion.isEmpty()) {
                    switch (opcion) {
                        case "Borrar Producto":
                            modelo.removeRow(fila);
                            break;
                        case "Actualizar Cantidades":
                            abrirDialogoEditarPEA(fila, listaProductosEnAlbaranRef, listaProductosRef);
                            break;
                        case "Agre/Modif Producto":
                        	abrirDialogoEditarProducto(fila, listaProductosEnAlbaranRef, listaProductosRef);
							break;
                    }
                    if (fila < modelo.getRowCount()) {
                        modelo.setValueAt("", fila, 6);
                    }
                    tabla.clearSelection();
                }
            }
        });

        columnaOpciones.setCellEditor(new DefaultCellEditor(comboBox));
        columnaOpciones.setCellRenderer(new ComboBoxRenderer());
    }
}

