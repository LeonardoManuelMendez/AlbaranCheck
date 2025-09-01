package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dao.DAO;
import modelo.Producto;
import modelo.ProductoEnAlbaran;

import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

public class EditarProductosEnAlbaran extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField textField_1;
	private JTextField textFieldCantEsp;
	private JTextField textFieldCantRecib;

	/**
	 * Create the dialog.
	 */
	public EditarProductosEnAlbaran(ProductoEnAlbaran productoEnAlbaran,
			List<ProductoEnAlbaran> listaProductosEnAlbaran) {
		setTitle(productoEnAlbaran == null ? "Crear producto" : "Editar producto");
		setModal(true); // Bloquea hasta cerrar (clave)
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setBounds(100, 100, 343, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblCreareditarProducto = new JLabel("Editar Producto En Albaran");
		lblCreareditarProducto.setFont(new Font("Dialog", Font.BOLD, 14));
		lblCreareditarProducto.setBounds(77, 12, 188, 20);
		contentPanel.add(lblCreareditarProducto);

		JLabel lblNombre = new JLabel("Nombre: ");
		lblNombre.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNombre.setBounds(12, 82, 60, 17);
		contentPanel.add(lblNombre);

		textField = new JTextField();
		textField.setBounds(99, 80, 114, 21);
		contentPanel.add(textField);
		textField.setColumns(10);

		JLabel lblFormato = new JLabel("Formato:");
		lblFormato.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblFormato.setBounds(12, 111, 60, 17);
		contentPanel.add(lblFormato);

		JLabel lblCantidadEsperada = new JLabel("Cantidad Esperada:");
		lblCantidadEsperada.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblCantidadEsperada.setBounds(12, 141, 114, 17);
		contentPanel.add(lblCantidadEsperada);

		JLabel lblCantidadRecibida = new JLabel("Cantidad Recibida:");
		lblCantidadRecibida.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblCantidadRecibida.setBounds(12, 170, 114, 17);
		contentPanel.add(lblCantidadRecibida);

		JLabel lblCdigo = new JLabel("Código:");
		lblCdigo.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblCdigo.setBounds(12, 54, 60, 17);
		contentPanel.add(lblCdigo);

		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(99, 47, 114, 21);
		contentPanel.add(textField_1);
		textField_1.setColumns(10);

		textFieldCantEsp = new JTextField();
		textFieldCantEsp.setBounds(144, 137, 114, 21);
		contentPanel.add(textFieldCantEsp);
		textFieldCantEsp.setColumns(10);

		textFieldCantRecib = new JTextField();
		textFieldCantRecib.setBounds(144, 166, 114, 21);
		contentPanel.add(textFieldCantRecib);
		textFieldCantRecib.setColumns(10);

		JRadioButton rdbtnBultos = new JRadioButton("Bultos");
		rdbtnBultos.setBounds(186, 103, 130, 25);
		contentPanel.add(rdbtnBultos);

		JRadioButton rdbtnUnidades = new JRadioButton("Unidades");
		rdbtnUnidades.setBounds(99, 103, 89, 25);
		contentPanel.add(rdbtnUnidades);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton botonGuardar = new JButton("Guardar");
				botonGuardar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String codigo = textField_1.getText().trim();
						String nombre = textField.getText().trim();
						String cantidadEsp = textFieldCantEsp.getText().trim();
						String cantidadRecib = textFieldCantRecib.getText().trim();
						String formato = "";
						if (rdbtnUnidades.isSelected()) {
							formato = "Unidades";
						} else if (rdbtnBultos.isSelected()) {
							formato = "Bultos";
						}
						// Validaciones básicas
						if (codigo.isEmpty() || nombre.isEmpty() || formato.isEmpty()) {
							javax.swing.JOptionPane.showMessageDialog(EditarProductosEnAlbaran.this,
									"Por favor, complete todos los campos obligatorios.", "Error",
									javax.swing.JOptionPane.ERROR_MESSAGE);
							return;
						}
						// Validar que las cantidades sean números enteros
						int cantEsp = 0;
						int cantRecib = 0;
						try {
							cantEsp = Integer.parseInt(cantidadEsp);
							cantRecib = Integer.parseInt(cantidadRecib);
						} catch (NumberFormatException ex) {
							javax.swing.JOptionPane.showMessageDialog(EditarProductosEnAlbaran.this,
									"Las cantidades deben ser números enteros.", "Error",
									javax.swing.JOptionPane.ERROR_MESSAGE);
							return;
						}
						if (cantEsp < 0 || cantRecib < 0) {
							javax.swing.JOptionPane.showMessageDialog(EditarProductosEnAlbaran.this,
									"Las cantidades no pueden ser negativas.", "Error",
									javax.swing.JOptionPane.ERROR_MESSAGE);
							return;
						}
						// Actualizar el producto en albarán
						productoEnAlbaran.getProducto().setNombre(nombre);
						productoEnAlbaran.setUnidades_esperadas(cantEsp);
						productoEnAlbaran.setUnidades_recibidas(cantRecib);
						productoEnAlbaran.getProducto().setFormato(formato);
						// Si se cambia el código, verificar que no exista otro producto con el mismo
						
						

						dispose(); // Cerrar el diálogo después de guardar
					}
				});
				botonGuardar.setActionCommand("Guardar");
				buttonPane.add(botonGuardar);
				getRootPane().setDefaultButton(botonGuardar);
			}
			{
				JButton botonCancelar = new JButton("Cancelar");
				botonCancelar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				buttonPane.add(botonCancelar);
			}
		}
		if (productoEnAlbaran != null) {
			textField_1.setText(productoEnAlbaran.getProducto().getCodigo());
			textField.setText(productoEnAlbaran.getProducto().getNombre());
			textFieldCantEsp.setText(String.valueOf(productoEnAlbaran.getUnidades_esperadas()));
			textFieldCantRecib.setText(String.valueOf(productoEnAlbaran.getUnidades_recibidas()));
			if (productoEnAlbaran.getProducto().getFormato().equals("Unidades")) {
				rdbtnUnidades.setSelected(true);
			} else if (productoEnAlbaran.getProducto().getFormato().equals("Bultos")) {
				rdbtnBultos.setSelected(true);
			}
		}
	}
}