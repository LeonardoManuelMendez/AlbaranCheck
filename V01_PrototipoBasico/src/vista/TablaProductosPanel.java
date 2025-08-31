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

import controlador.Controlador;
import modelo.Producto;

/**
 * Clase que representa un panel con una tabla de productos. Muestra los datos
 * de cada producto (código, nombre, EAN unidad, EAN bulto) y agrega una columna
 * con un JComboBox que permite realizar acciones: - Borrar producto -
 * Actualizar producto
 */
public class TablaProductosPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JTable tabla; // La tabla donde se muestran los productos
	private DefaultTableModel modelo; // El modelo que gestiona los datos de la tabla
	private List<Producto> listaProductosRef; // Referencia a la lista de productos original

	/**
	 * Constructor que recibe la lista de productos y configura el panel.
	 */
	public TablaProductosPanel(List<Producto> listaProductos) {
		initializeComponents(listaProductos);
	}

	/**
	 * Inicializa los componentes del panel: la tabla, el modelo y el JComboBox de
	 * acciones.
	 */
	private void initializeComponents(List<Producto> listaProductos) {
		this.listaProductosRef = listaProductos; // Guardar referencia para usar en el action listener
		// Nombres de las columnas de la tabla
		String[] nombresColumnas = { "Código", "Nombre", "EAN unidad", "EAN bulto", "Formato", "Opciones" };
		// Convertimos la lista de productos en un arreglo bidimensional
		Object[][] datosfilas = convertirListaAArray(listaProductos);

		if (datosfilas != null) {
			// Modelo de tabla personalizado
			modelo = new DefaultTableModel(datosfilas, nombresColumnas) {
				private static final long serialVersionUID = 1L;

				// Solo la columna "Opciones" (índice 4) es editable
				@Override
				public boolean isCellEditable(int row, int column) {
					return column == 5; // Solo la columna "Opciones" es editable
				}
			};

			// Creamos la tabla a partir del modelo
			tabla = new JTable(modelo);

			// Configuramos el editor y renderer para la columna "Opciones"
			instalarEditorYRendererOpciones();

			// Agregamos la tabla dentro de un JScrollPane para scroll horizontal/vertical
			JScrollPane scrollPane = new JScrollPane(tabla);
			this.add(scrollPane); // Añadimos el scrollPane al panel
		}
	}

	/**
	 * Abre un diálogo de edición para el producto en la fila seleccionada.
	 */
	private void abrirDialogoEditar(int fila, List<Producto> listaProductos) {
		// Obtener código de producto de la fila
		String codigo = (String) modelo.getValueAt(fila, 0);
		// Buscar el producto en la lista
		Producto producto = buscarProductoPorCodigo(codigo, listaProductos);
		if (producto != null) {
			// Abrir diálogo de edición/creación de productos
			EditarCrearProductos dialog = new EditarCrearProductos(producto, listaProductos);
			dialog.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					actualizarTabla(listaProductos); // vuelve a cargar desde la lista
					tabla.clearSelection();
				}
			});
			dialog.setVisible(true);
		}
	}

	/**
	 * Busca un producto en la lista por su código.
	 */
	private Producto buscarProductoPorCodigo(String codigo, List<Producto> listaProductos) {
		for (Producto producto : listaProductos) {
			if (producto.getCodigo().equals(codigo)) {
				return producto;
			}
		}
		return null;
	}

	/**
	 * Convierte la lista de productos a una matriz de objetos para ser usada en el
	 * modelo de la tabla.
	 */
	private Object[][] convertirListaAArray(List<Producto> listaProductos) {
		Object[][] matriz = new Object[listaProductos.size()][6]; // 6 columnas
		for (int i = 0; i < listaProductos.size(); i++) {
			Producto producto = listaProductos.get(i);
			if (producto == null) {
				System.err.println("Error: Producto nulo en la posición " + i);
				continue; // Saltar productos nulos
			}
			matriz[i][0] = producto.getCodigo();
			matriz[i][1] = producto.getNombre();
			matriz[i][2] = producto.getEanProducto();
			matriz[i][3] = producto.getEanBulto();
			matriz[i][4] = producto.getFormato(); // Valor inicial para el JComboBox
			matriz[i][5] = ""; // Columna de opciones inicialmente vacía
		}
		return matriz;
	}

	/**
	 * Devuelve la JTable contenida en el panel.
	 */
	public JTable getTabla() {
		return tabla;
	}

	/**
	 * Actualiza los datos de la tabla con una nueva lista de productos.
	 */
	public void actualizarTabla(List<Producto> listaProductos) {
		this.listaProductosRef = listaProductos; // Actualizar referencia
		Object[][] datosfilas = convertirListaAArray(listaProductos);
		modelo.setDataVector(datosfilas, new String[] { "Código", "Nombre", "EAN unidad", "EAN bulto", "Formato", "Opciones" });
		instalarEditorYRendererOpciones(); // Reinstalar editor y renderer
		tabla.revalidate();
		tabla.repaint();
	}

	/**
	 * Renderer para mostrar correctamente el JComboBox en la columna "Opciones".
	 * Esta clase interna extiende JComboBox y se usa para pintar la celda.
	 */
	static class ComboBoxRenderer extends JComboBox<String> implements TableCellRenderer {
		private static final long serialVersionUID = 1L;

		public ComboBoxRenderer() {
			super(new String[] { "", "Borrar", "Actualizar" });
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			setSelectedItem(value); // Mostrar la opción seleccionada en la celda
			if (isSelected) {
				// Cambiar colores si la celda está seleccionada
				setBackground(table.getSelectionBackground());
				setForeground(table.getSelectionForeground());
			} else {
				// Colores normales
				setBackground(table.getBackground());
				setForeground(table.getForeground());
			}
			return this; // Devolver el JComboBox como componente gráfico
		}
	}

	/**
	 * Configura el editor y renderer para la columna "Opciones" de la tabla.
	 * El editor es un JComboBox con las opciones "Borrar" y "Actualizar".
	 * El action listener maneja las acciones seleccionadas.
	 */
	private void instalarEditorYRendererOpciones() {
		// índice 5 = columna "Opciones"
		TableColumn columnaOpciones = tabla.getColumnModel().getColumn(5);

		JComboBox<String> comboBox = new JComboBox<>(new String[] { "", "Borrar", "Actualizar" });
		comboBox.setSelectedIndex(-1);

		// Action listener para manejar selección de opciones
		comboBox.addActionListener(e -> {
			int fila = tabla.getSelectedRow();
			if (fila >= 0) {
				String opcion = (String) comboBox.getSelectedItem();
				if (opcion != null && !opcion.isEmpty()) {
					String codigo = (String) modelo.getValueAt(fila, 0);
					switch (opcion) {
					case "Borrar":
						Controlador.borrarLineadelDAT(listaProductosRef, codigo); // ver nota abajo
						modelo.removeRow(fila);
						break;
					case "Actualizar":
						abrirDialogoEditar(fila, listaProductosRef);
						break;
					}
					if (fila < modelo.getRowCount()) {
						modelo.setValueAt("", fila, 5);
					}
				}
			}
		});
		
		columnaOpciones.setCellEditor(new DefaultCellEditor(comboBox));
		columnaOpciones.setCellRenderer(new ComboBoxRenderer());
	}

}
