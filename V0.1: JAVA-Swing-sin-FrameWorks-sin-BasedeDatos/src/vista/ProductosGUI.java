package vista;

import controlador.Controlador;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;

public class ProductosGUI extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;
    private JLabel jLabel1;
    private JScrollPane jScrollPane1;
    private JTable table;

    public ProductosGUI() {
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel1.setText("Listado de Productos");

        try {
            table = Controlador.tablaProductos();
            if (table != null) {
                jScrollPane1.setViewportView(table);
            } else {
                System.err.println("Error: La tabla no se ha inicializado correctamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al inicializar la tabla: " + e.getMessage());
        }

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(22)
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                    .addContainerGap())
        );
    }
}
