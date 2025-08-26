package vista;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import modelo.Producto;

public class ProductosGUI extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel jLabel1;
    private JScrollPane jScrollPanelListado;
    private TablaProductosPanel tablaProductosPanel;

    public ProductosGUI() {
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jScrollPanelListado = new javax.swing.JScrollPane();
        jLabel1.setText("Listado de Productos");

        try {
            tablaProductosPanel = new TablaProductosPanel();
            if (tablaProductosPanel.getTabla() != null) {
                jScrollPanelListado.setViewportView(tablaProductosPanel.getTabla());
            } else {
                System.err.println("Error: La tabla no se ha inicializado correctamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al inicializar la tabla: " + e.getMessage());
        }
        
        
        JButton botonIngresarNuevoP = new JButton("Ingresar Nuevo Producto");
        botonIngresarNuevoP.addActionListener(e -> {
        	Producto producto = null; // Pasar null para crear un nuevo producto
        	EditarCrearProductos editarCrearProductos = new EditarCrearProductos(producto);
        	editarCrearProductos.setVisible(true);
        	// Actualizar la tabla después de cerrar el diálogo
        	editarCrearProductos.addWindowListener(new java.awt.event.WindowAdapter() {
        		@Override
        		public void windowClosed(java.awt.event.WindowEvent windowEvent) {
        			if (tablaProductosPanel != null) {
        				tablaProductosPanel.actualizarTabla();
        			}
        		}
        	});
        });        GroupLayout layout = new GroupLayout(this);
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(layout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jScrollPanelListado, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
        				.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
        				.addComponent(botonIngresarNuevoP, Alignment.TRAILING))
        			.addContainerGap())
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addGap(22)
        			.addComponent(jLabel1)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jScrollPanelListado, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addComponent(botonIngresarNuevoP)
        			.addContainerGap())
        );
        this.setLayout(layout);
    }
}
