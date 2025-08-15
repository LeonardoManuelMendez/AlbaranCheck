package vista;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

/**
 * Panel central/derecho que muestra el contenido activo.
 * Inicialmente muestra el logo de inicio; permite cambiar a "Nuevo" o "Productos".
 */
public class MenuContenidoPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public MenuContenidoPanel() {
        setLayout(new BorderLayout());
        showInicio();
    }

    public void showInicio() {
        removeAll();
        add(new InicialConLogo(), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void showNuevo() {
        setContent(new NuevoGUI());
    }

    public void showProductos() {
        setContent(new ProductosGUI());
    }

    private void setContent(Component comp) {
        removeAll();
        add(comp, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}