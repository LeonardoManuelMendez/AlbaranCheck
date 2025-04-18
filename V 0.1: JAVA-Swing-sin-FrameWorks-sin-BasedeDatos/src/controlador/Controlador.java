package controlador;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.table.*;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import modelo.Albaran;
import modelo.Producto;
import modelo.ProductoEnAlbaran;

public class Controlador {
	
	public static List<ProductoEnAlbaran> leerAlbaran(File pdfFile) {
		List<ProductoEnAlbaran> listaProductos = new ArrayList<>();
		ProductoEnAlbaran nuevoProductoEnAlbaran;
		Producto nuevoProducto = null;
		Albaran nuevoAlbaran = null;
		String codigo, nombre, numero = null;
		LocalDate fecha = null;
		int cantidadBultos;
		int cantidadUnidades;

		try (PDDocument document = Loader.loadPDF(pdfFile)) {
			PDFTextStripper stripper = new PDFTextStripper();
			String text = stripper.getText(document);
			Pattern patronItems = Pattern.compile(
					"(\\d{2,6})\\s{1,1}(.{3,32}?)\\s{1,1}(\\.{0,1})\\s{1,8}(\\d{1,6})\\s{1,3}(\\d{1,3})\\s{1,5}(\\d{0,4})");

			Matcher matcherItems = patronItems.matcher(text);

			Pattern patronNumero = Pattern.compile("NUMERO DE ALBARAN ..........: (\\d{1,2})- (\\d{1,9})");
			Matcher matcherNumero = patronNumero.matcher(text);
			if (matcherNumero.find()) {
				numero = matcherNumero.group(1) + "- " + matcherNumero.group(2);
			}

			Pattern patronFecha = Pattern.compile("Fecha ..:  (\\d{2}/\\d{2}/\\d{2})"); // Patrón para dd/mm/aa
			Matcher matcherFecha = patronFecha.matcher(text);

			if (matcherFecha.find()) {
				try {
					fecha = LocalDate.parse(matcherFecha.group(1), DateTimeFormatter.ofPattern("dd/MM/yy"));
				} catch (DateTimeParseException e) {
					System.err.println("Error al analizar la fecha: " + e.getMessage());
				}
			}

			nuevoAlbaran = new Albaran(numero, fecha);

			while (matcherItems.find()) {
				codigo = matcherItems.group(1);

				nombre = matcherItems.group(2);

				cantidadBultos = Integer.parseInt(matcherItems.group(4));
				cantidadUnidades = 0;
				String unidadesPRE = matcherItems.group(6);
				if (!unidadesPRE.equals("")) {
					cantidadUnidades = Integer.parseInt(unidadesPRE);
				}
				nuevoProducto = new Producto(codigo, nombre);

				nuevoProductoEnAlbaran = new ProductoEnAlbaran(nuevoProducto, nuevoAlbaran, cantidadBultos,
						cantidadUnidades);
				listaProductos.add(nuevoProductoEnAlbaran);

			}

		} catch (IOException e) {
			System.out.println("Error al leer el archivo PDF: " + e.getMessage()); // Mensaje más detallado
		}
		System.out.println(listaProductos.toString());

		return listaProductos;
	}

	public static void borrarLineadelTXT(List<Producto> listaProductos, String id) {

		for (int i = 0; i < listaProductos.size(); i++) {
			if (listaProductos.get(i).getCodigo().equals(id)) {
				listaProductos.remove(i);

				break;
			}
		}

		actualizarListadoProductos(listaProductos);
	}
	
	public static void actualizarListadoProductos(List<Producto> listaProductos) {

		// escribir en el .dat la nueva lista de productos con el cambio
		try {
			FileOutputStream fos = new FileOutputStream("./src/ficheros/productos.dat", false);
			OutputStreamWriter os = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(os);
			String linea;
			for (int j = 0; j < listaProductos.size(); j++) {

				linea = listaProductos.get(j).getCodigo() + "    " + listaProductos.get(j).getNombre() + "    "
						+ listaProductos.get(j).getEanProducto() + "    " + listaProductos.get(j).getEanBulto();
				bw.write(linea, 0, linea.length());

				bw.newLine();
			}

			bw.close();
			os.close();
			fos.close();
		} catch (FileNotFoundException e) {
			System.out.println("Archivo no encontrado: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Error de entrada/salida: " + e.getMessage());
		}
	}

    public static List<Producto> obtenerListaProductos() {
        File archivotxt = new File("./src/ficheros/productos.dat");
        List<Producto> listaProductos = new ArrayList<>();
        String codigo_t;
        String nombre_t;
        String eanBulto_t;
        String eanProducto_t;

        String regex1 = "^(\\d{1,6})\\s{3,}(.{3,32})\\s{3,}(\\d*)\\s{3,}(\\d*)\\s*$";
        Pattern pattern = Pattern.compile(regex1);

        try (BufferedReader br = new BufferedReader(new FileReader(archivotxt))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(linea);
                if (matcher.find()) {
                    codigo_t = matcher.group(1);
                    nombre_t = matcher.group(2).trim();
                    eanProducto_t = matcher.group(3);
                    eanBulto_t = matcher.group(4);
                    listaProductos.add(new Producto(codigo_t, nombre_t, eanProducto_t, eanBulto_t));
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return listaProductos;
    }

    public static JTable tablaProductos() {
        String[] nombresColumnas = {"Código", "Nombre", "EAN unidad", "EAN bulto", "Opciones"};
        List<Producto> listaProductos = obtenerListaProductos();
        Object[][] datosfilas = convertirListaAArray(listaProductos);

        if (datosfilas != null) {
            DefaultTableModel modelo = new DefaultTableModel(datosfilas, nombresColumnas) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column == 4; // Solo la columna "Opciones" es editable
                }
            };
            JTable tabla = new JTable(modelo);

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
                                borrarLineadelTXT(listaProductos, codigo);
                                modelo.removeRow(fila);
                                break;
                            case "Actualizar":
                                Producto producto = listaProductos.get(fila);
                                EditarProductoDialog dialog = new EditarProductoDialog(null, producto, listaProductos, modelo, fila);
                                dialog.setVisible(true);
                                break;
                        }
                    }
                }
            });

            TableColumn columnaOpciones = tabla.getColumnModel().getColumn(4);
            columnaOpciones.setCellEditor(new DefaultCellEditor(comboBox));
            columnaOpciones.setCellRenderer(new ComboBoxRenderer());

            return tabla;
        }
        return null;
    }

    public static Object[][] convertirListaAArray(List<Producto> listaProductos) {
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

    static class ComboBoxRenderer extends JComboBox<String> implements TableCellRenderer {
        private static final long serialVersionUID = 1L;

        public ComboBoxRenderer() {
            super(new String[]{"", "Borrar", "Actualizar"});
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
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



	public static void actualizarArchivoProductos() {
		List<Producto> listaProductos = obtenerListaProductos();
		try (BufferedWriter bw = new BufferedWriter(new FileWriter("./src/ficheros/productos.txt"))) {
			for (Producto producto : listaProductos) {
				bw.write(producto.getCodigo() + "   " + producto.getNombre() + "   " + producto.getEanProducto() + "   " + producto.getEanBulto());
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static class EditarProductoDialog extends JDialog {
        private JTextField txtCodigo, txtNombre, txtEanUnidad, txtEanBulto;
        private Producto producto;
        private List<Producto> listaProductos;
        private DefaultTableModel modelo;
        private int fila;

        public EditarProductoDialog(JFrame parent, Producto producto, List<Producto> listaProductos, DefaultTableModel modelo, int fila) {
            super(parent, "Editar Producto", true); // Modal: bloquea la ventana padre
            this.producto = producto;
            this.listaProductos = listaProductos;
            this.modelo = modelo;
            this.fila = fila;

            initComponents();
            cargarDatos();

            setResizable(false); // No redimensionable
            pack(); // Ajusta el tamaño al contenido
            setLocationRelativeTo(null); // Centrar en la pantalla
        }

        private void initComponents() {
            // Usar GridBagLayout para control preciso
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes

            // JLabel y JTextField para Código
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.EAST;
            add(new JLabel("Código:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            txtCodigo = new JTextField(10);
            txtCodigo.setEnabled(false); // No editable
            add(txtCodigo, gbc);

            // JLabel y JTextField para Nombre
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.EAST;
            add(new JLabel("Nombre:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            gbc.anchor = GridBagConstraints.WEST;
            txtNombre = new JTextField(30);
            add(txtNombre, gbc);

            // JLabel y JTextField para EAN Unidad
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.EAST;
            add(new JLabel("EAN Unidad:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.anchor = GridBagConstraints.WEST;
            txtEanUnidad = new JTextField(15);
            add(txtEanUnidad, gbc);

            // JLabel y JTextField para EAN Bulto
            gbc.gridx = 0;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.EAST;
            add(new JLabel("EAN Bulto:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.WEST;
            txtEanBulto = new JTextField(15);
            add(txtEanBulto, gbc);

            // Botones
            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.gridwidth = 1;
            JButton btnGuardar = new JButton("Guardar");
            btnGuardar.addActionListener(e -> guardarCambios());
            add(btnGuardar, gbc);

            gbc.gridx = 1;
            gbc.gridy = 4;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.addActionListener(e -> dispose());
            add(btnCancelar, gbc);
        }

        private void cargarDatos() {
            txtCodigo.setText(producto.getCodigo());
            txtNombre.setText(producto.getNombre());
            txtEanUnidad.setText(producto.getEanProducto());
            txtEanBulto.setText(producto.getEanBulto());
        }

        private void guardarCambios() {
            producto.setNombre(txtNombre.getText());
            producto.setEanProducto(txtEanUnidad.getText());
            producto.setEanBulto(txtEanBulto.getText());

            // Actualizar la lista y la tabla
            listaProductos.set(fila, producto);
            modelo.setValueAt(producto.getCodigo(), fila, 0);
            modelo.setValueAt(producto.getNombre(), fila, 1);
            modelo.setValueAt(producto.getEanProducto(), fila, 2);
            modelo.setValueAt(producto.getEanBulto(), fila, 3);

            // Guardar en el archivo
            actualizarListadoProductos(listaProductos);
            dispose();
        }
    }

}
