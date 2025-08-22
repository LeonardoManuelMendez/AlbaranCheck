package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import dao.UsuariosRepo;

import javax.swing.JScrollPane;

public class UsuariosGUI extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UsuariosGUI dialog = new UsuariosGUI();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public UsuariosGUI() {
		setTitle("Gestion de Usuarios");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JScrollPane scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				table = new JTable();
				table.setModel(new DefaultTableModel(
						new Object[][] { { null, null, null }, { null, null, null }, { null, null, null }, },
						new String[] { "Nombre", "Perfil", "Clave" }));
				scrollPane.setViewportView(table);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton agregarButton = new JButton("Agregar");
				getRootPane().setDefaultButton(agregarButton);
				buttonPane.add(agregarButton);
				agregarButton.addActionListener(e -> {
					UsuariosRepo repo = new dao.UsuariosRepo();
					DefaultTableModel model = (DefaultTableModel) table.getModel();
					JOptionPane.showInputDialog(this, "Ingrese los datos del nuevo usuario (Nombre, Perfil, Clave):", "Agregar Usuario", JOptionPane.PLAIN_MESSAGE);
					
				});
			}
			{
				JButton modificarButton = new JButton("Modificar");
				buttonPane.add(modificarButton);

			}
			{
				JButton eliminarButton = new JButton("Eliminar");
				buttonPane.add(eliminarButton);
			}
		}
		
	}

}
