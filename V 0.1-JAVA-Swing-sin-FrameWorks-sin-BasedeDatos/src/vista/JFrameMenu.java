package vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * Ventana principal del menú de AlbaranCheck.
 */
public class JFrameMenu extends JFrame {

    private static final long serialVersionUID = 1L;

    private MenuLateralPanel panelLateral;
    private MenuContenidoPanel panelContenido;

    public JFrameMenu() {
        setTitle("AlbaranCheck - Menú");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(1000, 700);
        setLocationRelativeTo(null);

        panelLateral = new MenuLateralPanel();
        // Si quieres ajustar más el ancho fijo:
        panelLateral.setPreferredSize(new Dimension(120, getHeight()));

        panelContenido = new MenuContenidoPanel();

        // Cableado de acciones
        panelLateral.onNuevo(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                panelContenido.showNuevo();
            }
        });
        panelLateral.onProductos(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                panelContenido.showProductos();
            }
        });

        // Sin JSplitPane: lateral fijo y contenido central con scroll
        add(panelLateral, BorderLayout.WEST);
        add(new JScrollPane(panelContenido), BorderLayout.CENTER);
    }
}

