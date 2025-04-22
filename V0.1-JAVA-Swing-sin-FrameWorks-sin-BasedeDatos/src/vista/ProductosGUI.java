package vista;

import controlador.Controlador;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ProductosGUI extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel jLabel1;
    private JScrollPane jScrollPanelListado;
    private JTable table;

    public ProductosGUI() {
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jScrollPanelListado = new javax.swing.JScrollPane();
        jLabel1.setText("Listado de Productos");

        try {
            table = Controlador.tablaProductos();
            if (table != null) {
                jScrollPanelListado.setViewportView(table);
            } else {
                System.err.println("Error: La tabla no se ha inicializado correctamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al inicializar la tabla: " + e.getMessage());
        }
        
        JButton botonIngresarNuevoP = new JButton("Ingresar Nuevo Producto");

        GroupLayout layout = new GroupLayout(this);
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
