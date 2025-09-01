package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

/**
 * Panel lateral izquierdo del menú principal (solo con JToolBar vertical).
 */
public class MenuLateralPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private final JButton btnNuevo = new JButton("Nuevo Albarán");
    private final JButton btnProductos = new JButton("Lista de Productos");
    private final JToolBar toolbar = new JToolBar(JToolBar.VERTICAL);
    
    public MenuLateralPanel(JFrameMenu frameMenu) {
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
        
        // Configuración de los botones
        btnNuevo.addActionListener(e -> {
            // Acción para el botón Nuevo
            // Modificar lo mostrado en el panel de contenido con el GUI de NuevoGUI
            frameMenu.showNuevo();
        });
        
        btnProductos.addActionListener(e -> {
            // Acción para el botón Productos
            frameMenu.showProductos();
        });

	} 
}
