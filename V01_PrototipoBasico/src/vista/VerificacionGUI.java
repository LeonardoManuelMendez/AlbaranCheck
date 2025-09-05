package vista;

import modelo.*;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;

import controlador.SoundManager;

import javax.swing.JButton;

public class VerificacionGUI extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private List<ProductoEnAlbaran> listaProductosEnAlbaran;
    private List<Producto> listaProductos;

    // Componentes UI
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField ean_leido_textField;
    private javax.swing.JScrollPane jScrollPaneTabla;
    private JButton btnResultado;

    // Nuevo panel reusable para la tabla
    private TablaVerificacionPanel tablaPanel;

    public VerificacionGUI(List<ProductoEnAlbaran> listaProductosEnAlbaran, List<Producto> listaProductos) {
        this.listaProductosEnAlbaran = listaProductosEnAlbaran;
        this.listaProductos = listaProductos;
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        ean_leido_textField = new javax.swing.JTextField();
        jScrollPaneTabla = new JScrollPane();
        btnResultado = new JButton("Resultado");

        jLabel1.setText("Escanear producto/bulto:");
        ean_leido_textField.addActionListener(evt -> jTextFieldEANActionPerformed(evt));
        ean_leido_textField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldEANKeyPressed(evt);
            }
        });

        jLabel2.setText("Listado de artículos verificados:");

        // Crear el panel de la tabla de verificación (con Estado)
        tablaPanel = new TablaVerificacionPanel(listaProductosEnAlbaran, listaProductos);
        jScrollPaneTabla.setViewportView(tablaPanel);

        btnResultado.addActionListener(e -> {
            java.awt.Window parent = javax.swing.SwingUtilities.getWindowAncestor(this);
            ResultadoDialog dialog = new ResultadoDialog(parent, listaProductosEnAlbaran);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        });


        GroupLayout layout = new GroupLayout(this);
        // Grupo horizontal
        layout.setHorizontalGroup(layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.LEADING)
                    .addComponent(jScrollPaneTabla, GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.UNRELATED)
                        .addComponent(ean_leido_textField, GroupLayout.PREFERRED_SIZE, 235, GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)
                    .addGroup(Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnResultado)))
                .addContainerGap())
        );

        // Grupo vertical
        layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(ean_leido_textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(jScrollPaneTabla, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addComponent(btnResultado)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        this.setLayout(layout);

        // Carga inicial
        tablaPanel.actualizarTabla(listaProductosEnAlbaran);
        
        // Foco inicial en el campo de EAN
        javax.swing.SwingUtilities.invokeLater(() -> {
            ean_leido_textField.requestFocusInWindow();
        });

    }

    private void jTextFieldEANActionPerformed(java.awt.event.ActionEvent evt) {
        // No usado; mantenido por compatibilidad si tu IDE genera acciones por defecto
    }

    private void jTextFieldEANKeyPressed(java.awt.event.KeyEvent evt) {
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            String eanLeido = ean_leido_textField.getText().trim();
            if (eanLeido.isEmpty()) return;

            // 1) Buscar el producto por EAN en la lista completa de productos
            Producto productoLeido = buscarProductoPorEan(eanLeido);
            if (productoLeido == null) {
                JOptionPane.showMessageDialog(this,
                    "EAN no existe en catálogo de productos.",
                    "No encontrado",
                    JOptionPane.WARNING_MESSAGE);
                ean_leido_textField.setText("");
                return;
            }

            // 2) Buscar ese producto dentro del albarán (por código) y si no está, agregarlo
            ProductoEnAlbaran pea = buscarEnAlbaranPorCodigo(productoLeido.getCodigo());
            Albaran albaran = listaProductosEnAlbaran.isEmpty() ? null : listaProductosEnAlbaran.get(0).getAlbaran();
            if (pea == null) {
            	// agregar sonido de error
                SoundManager.warning();
                JOptionPane.showMessageDialog(this,
                    "El producto no está en este albarán.",
                    "No encontrado en albarán",
                    JOptionPane.WARNING_MESSAGE);
                
                // agregar producto al albarán
                listaProductosEnAlbaran.add(new ProductoEnAlbaran(productoLeido, albaran, 0, 0, 1, 0));
                ean_leido_textField.setText("");
                // Actualizar tabla inmediatamente
                tablaPanel.actualizarTabla(listaProductosEnAlbaran);
                return;
            }

            // 3) Incrementar cantidades recibidas en función del EAN leído
            if (eanLeido.equals(productoLeido.getEanProducto())) {
                pea.setUnidades_recibidas(pea.getUnidades_recibidas() + 1);
            } else if (eanLeido.equals(productoLeido.getEanBulto())) {
                pea.setBultos_recibidos(pea.getBultos_recibidos() + 1);
            }

            // 4) Refrescar tabla de verificación
            tablaPanel.actualizarTabla(listaProductosEnAlbaran);

            // 5) Preparar para siguiente escaneo
            ean_leido_textField.setText("");
            ean_leido_textField.requestFocusInWindow();
        }
    }

    private Producto buscarProductoPorEan(String ean) {
        for (Producto p : listaProductos) {
            if (ean.equals(p.getEanProducto()) || ean.equals(p.getEanBulto())) {
                return p;
            }
        }
        return null;
    }

    private ProductoEnAlbaran buscarEnAlbaranPorCodigo(String codigo) {
        for (ProductoEnAlbaran pea : listaProductosEnAlbaran) {
            if (pea.getProducto() != null && codigo.equals(pea.getProducto().getCodigo())) {
                return pea;
            }
        }
        return null;
    }
}

