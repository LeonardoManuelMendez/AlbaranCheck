package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * Panel lateral izquierdo del menú principal (solo con JToolBar vertical).
 */
public class MenuLateralPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final JButton btnNuevo = new JButton("Nuevo");
    private final JButton btnProductos = new JButton("Productos");
    private final JToolBar toolbar = new JToolBar(JToolBar.VERTICAL);

    public MenuLateralPanel() {
        setLayout(new BorderLayout());
        // Ancho fijo cómodo para la barra
        setPreferredSize(new Dimension(150, 500));

        // Configuración del toolbar
        toolbar.setFloatable(false);
        toolbar.setRollover(true);

        // Opcional: que no tomen foco al tabular
        btnNuevo.setFocusable(false);
        btnProductos.setFocusable(false);

        // Añadimos los botones SOLO al toolbar
        toolbar.add(btnNuevo);
        toolbar.add(btnProductos);

        // Añadimos el toolbar al panel
        add(toolbar, BorderLayout.CENTER);
    }

    // API “simple” para enganchar listeners desde el JFrame
    public void onNuevo(ActionListener l)     { btnNuevo.addActionListener(l); }
    public void onProductos(ActionListener l) { btnProductos.addActionListener(l); }

}
