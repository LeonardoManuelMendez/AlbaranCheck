package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import dao.DAO;
import modelo.Producto;
import controlador.Controlador;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

public class EditarCrearProductos extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField textField;   // nombre
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JTextField textField_1; // código
    private JTextField textField_2; // ean unidad
    private JTextField textField_3; // ean bulto

    /**
     * Create the dialog.
     */
    public EditarCrearProductos(Producto producto, List<Producto> listaProductos) {
        setTitle(producto == null ? "Crear producto" : "Editar producto");
        setModal(true); // Bloquea hasta cerrar (clave)
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBounds(100, 100, 464, 287);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblCreareditarProducto = new JLabel("Crear/Editar Producto");
        lblCreareditarProducto.setFont(new Font("Dialog", Font.BOLD, 14));
        lblCreareditarProducto.setBounds(12, 12, 171, 30);
        contentPanel.add(lblCreareditarProducto);

        JLabel lblNombre = new JLabel("Nombre: ");
        lblNombre.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblNombre.setBounds(12, 82, 60, 17);
        contentPanel.add(lblNombre);

        textField = new JTextField();
        textField.setBounds(99, 80, 328, 25);
        contentPanel.add(textField);
        textField.setColumns(10);

        JLabel lblFormato = new JLabel("Formato:");
        lblFormato.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblFormato.setBounds(12, 111, 60, 17);
        contentPanel.add(lblFormato);

        JRadioButton rdbtnUnidades = new JRadioButton("Unidades");
        buttonGroup.add(rdbtnUnidades);
        rdbtnUnidades.setBounds(99, 107, 89, 25);
        contentPanel.add(rdbtnUnidades);

        JRadioButton rdbtnBultos = new JRadioButton("Bultos");
        buttonGroup.add(rdbtnBultos);
        rdbtnBultos.setBounds(186, 107, 130, 25);
        contentPanel.add(rdbtnBultos);

        JLabel lblEanUnidad = new JLabel("EAN unidad:");
        lblEanUnidad.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblEanUnidad.setBounds(12, 141, 82, 17);
        contentPanel.add(lblEanUnidad);

        JLabel lblEanBulto = new JLabel("EAN Bulto:");
        lblEanBulto.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblEanBulto.setBounds(12, 170, 82, 17);
        contentPanel.add(lblEanBulto);

        JLabel lblCdigo = new JLabel("Código:");
        lblCdigo.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblCdigo.setBounds(12, 54, 60, 17);
        contentPanel.add(lblCdigo);

        textField_1 = new JTextField();
        textField_1.setBounds(99, 47, 114, 25);
        contentPanel.add(textField_1);
        textField_1.setColumns(10);

        textField_2 = new JTextField();
        textField_2.setBounds(99, 139, 114, 25);
        contentPanel.add(textField_2);
        textField_2.setColumns(10);

        textField_3 = new JTextField();
        textField_3.setBounds(99, 168, 114, 25);
        contentPanel.add(textField_3);
        textField_3.setColumns(10);

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton botonGuardar = new JButton("Guardar");
        botonGuardar.addActionListener((ActionListener) e -> {
            String codigo = textField_1.getText().trim();
            String nombre = textField.getText().trim();
            String eanUnidad = textField_2.getText().trim();
            String eanBulto = textField_3.getText().trim();
            String formato = rdbtnUnidades.isSelected() ? "Unidades" :
                             rdbtnBultos.isSelected()   ? "Bultos"    : "";

            // ¿Existe ya un producto con ese código?
            Producto existente = Controlador.buscarProductoPorCodigo(listaProductos, codigo);

            if (producto == null) {
                // CREAR (si ya existe un producto con ese código, se actualiza ese)
                Producto destino = (existente != null) ? existente : new Producto(codigo, nombre);
                destino.setCodigo(codigo);
                destino.setNombre(nombre);
                destino.setEanProducto(eanUnidad);
                destino.setEanBulto(eanBulto);
                destino.setFormato(formato);
                if (existente == null) {
                    listaProductos.add(destino);
                }
            } else {
                // EDITAR
                producto.setCodigo(codigo);
                producto.setNombre(nombre);
                producto.setEanProducto(eanUnidad);
                producto.setEanBulto(eanBulto);
                producto.setFormato(formato);
            }

            // Guardar
            DAO.guardarListaProductos(listaProductos);
            dispose(); // cerrar
        });
        botonGuardar.setActionCommand("Guardar");
        buttonPane.add(botonGuardar);
        getRootPane().setDefaultButton(botonGuardar);

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(e -> dispose());
        buttonPane.add(botonCancelar);

        // Si viene producto, rellenar campos
        if (producto != null) {
            if (producto.getCodigo() != null) textField_1.setText(producto.getCodigo());
            if (producto.getNombre() != null) textField.setText(producto.getNombre());
            if (producto.getEanProducto() != null) textField_2.setText(producto.getEanProducto());
            if (producto.getEanBulto() != null) textField_3.setText(producto.getEanBulto());
            if ("Unidades".equals(producto.getFormato())) {
                rdbtnUnidades.setSelected(true);
            } else if ("Bultos".equals(producto.getFormato())) {
                rdbtnBultos.setSelected(true);
            }
        }
    }

    /** Permite pre-rellenar el código cuando se abre en modo CREAR */
    public void setCodigoInicial(String codigo) {
        if (codigo != null) {
            textField_1.setText(codigo.trim());
        }
    }
}



