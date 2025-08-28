package vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Ventana principal del menú de AlbaranCheck.
 */
public class JFrameMenu extends JFrame {

    private static final long serialVersionUID = 1L;

    private final MenuLateralPanel panelLateral;
    private final JPanel panelContenido;
    private final JScrollPane scrollPane;

    public JFrameMenu() {
    	// Cambia el tipo de marco por uno con mejor apariencia
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        	System.err.println("Error al establecer el Look and Feel: " + e.getMessage());
        }

        setTitle("AlbaranCheck - Menú");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1000, 700);
        setLocationRelativeTo(null);

        // Crear el panel de contenido
        panelContenido = new JPanel(new BorderLayout());
        scrollPane = new JScrollPane(panelContenido);
        
        // Crear el panel lateral pasando referencia a este JFrame para que pueda llamar a los métodos
        panelLateral = new MenuLateralPanel(this);
        
        // Si quieres ajustar más el ancho fijo:
        panelLateral.setPreferredSize(new Dimension(120, getHeight()));

        // Sin JSplitPane: lateral fijo y contenido central con scroll
        add(panelLateral, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        
        // Mostrar la pantalla inicial
        setContent(new JPanelInicialConLogo());
    }
    
    /**
     * Muestra la pantalla inicial con el logo
     */
    public void showInicio() {
        setContent(new JPanelInicialConLogo());
    }

    /**
     * Muestra la pantalla de Nuevo Albarán
     */
    public void showNuevo() {
        setContent(new NuevoAlbaranGUI());
    }

    /**
     * Muestra la pantalla de Listado de Productos
     */
    public void showProductos() {
        setContent(new ProductosGUI());
    }
    /**
     * Cambia el contenido del panel central
     */
    private void setContent(Component comp) {
        panelContenido.removeAll();
        panelContenido.add(comp, BorderLayout.CENTER);
        panelContenido.revalidate();
        panelContenido.repaint();
    }
}

