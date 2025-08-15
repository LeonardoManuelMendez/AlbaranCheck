package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controlador.Controlador;
import modelo.Producto;

public class EditarProductoDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private JTextField txtCodigo, txtNombre, txtEanUnidad, txtEanBulto;
    private Producto producto;
    private List<Producto> listaProductos;
    private DefaultTableModel modelo;
    private int fila;
    
    public EditarProductoDialog(JFrame parent, Producto producto, List<Producto> listaProductos, 
                               DefaultTableModel modelo, int fila) {
        super(parent, "Editar Producto", true);
        this.producto = producto;
        this.listaProductos = listaProductos;
        this.modelo = modelo;
        this.fila = fila;
        
        initializeComponents();
        cargarDatos();
        pack();
        setLocationRelativeTo(parent);
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        // Panel principal con GridBagLayout
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Código (solo lectura)
        gbc.gridx = 0; gbc.gridy = 0;
        panelPrincipal.add(new JLabel("Código:"), gbc);
        gbc.gridx = 1;
        txtCodigo = new JTextField(15);
        txtCodigo.setEditable(false);
        panelPrincipal.add(txtCodigo, gbc);
        
        // Nombre
        gbc.gridx = 0; gbc.gridy = 1;
        panelPrincipal.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(15);
        panelPrincipal.add(txtNombre, gbc);
        
        // EAN Unidad
        gbc.gridx = 0; gbc.gridy = 2;
        panelPrincipal.add(new JLabel("EAN Unidad:"), gbc);
        gbc.gridx = 1;
        txtEanUnidad = new JTextField(15);
        panelPrincipal.add(txtEanUnidad, gbc);
        
        // EAN Bulto
        gbc.gridx = 0; gbc.gridy = 3;
        panelPrincipal.add(new JLabel("EAN Bulto:"), gbc);
        gbc.gridx = 1;
        txtEanBulto = new JTextField(15);
        panelPrincipal.add(txtEanBulto, gbc);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCambios();
                dispose();
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
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
        Controlador.actualizarListadoProductos(listaProductos);
    }
}
