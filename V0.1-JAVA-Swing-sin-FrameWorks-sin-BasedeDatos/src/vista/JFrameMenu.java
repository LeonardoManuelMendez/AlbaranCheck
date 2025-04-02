package vista;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Leonardo Méndez
 */
public class JFrameMenu extends JFrame {

    private static final long serialVersionUID = 1L;
	public InicialConLogo nuevoInicialConLogo;
    public PanelIzquierdo panelIzquierdo;
    public PanelCentral panelCentral;
    public NuevoGUI panelNuevo;
    public ProductosGUI panelMostrarProductos;

    public JFrameMenu() {
        setTitle("AlbaranCheck");
        setBounds(200, 200, 1000, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null); // Centrar la ventana
        setLayout(new BorderLayout());

        // Carga la imagen
        ImageIcon imagenIcono = new ImageIcon("logo5.png");
        // Escala la imagen si es necesario (opcional)
        Image imagen = imagenIcono.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        setIconImage(imagen);

        panelIzquierdo = new PanelIzquierdo();
        add(panelIzquierdo, BorderLayout.WEST);
        panelIzquierdo.setBorder(javax.swing.BorderFactory.createTitledBorder(" Menú "));

        panelCentral = new PanelCentral();
        add(panelCentral, BorderLayout.CENTER);
        JPanel panelPiedePagina = new JPanel();
        JLabel textoPiePagina = new JLabel("Realizado por: Leonardo Méndez. AlbaranCheck™.");
        panelPiedePagina.add(textoPiePagina);
        add(panelPiedePagina, BorderLayout.SOUTH);

        setVisible(true);

    }

    public class PanelIzquierdo extends JPanel {

        private static final long serialVersionUID = 1L;
		JButton botonNuevo;
        JButton botonProductos;

        public PanelIzquierdo() {
            botonNuevo = new JButton("Nuevo");
            botonProductos=new JButton("Productos");

            JToolBar barraHerramientas = new JToolBar(JToolBar.VERTICAL); // Orientación vertical
            barraHerramientas.setFloatable(false); // Evitar que se pueda desacoplar

            // Agregar botones a la barra de herramientas
            barraHerramientas.add(botonNuevo);
            barraHerramientas.add(botonProductos);
            add(barraHerramientas);

            botonNuevo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    panelCentral.remove(panelCentral.getComponent(0));
                    panelNuevo = new NuevoGUI();
                    panelCentral.add(panelNuevo);
                    revalidate();                             // Actualiza el diseño
                    repaint();                                // Repinta el JFrame
                }
            });

            
            botonProductos.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    panelCentral.remove(panelCentral.getComponent(0));
                    panelMostrarProductos=new ProductosGUI();
                    panelCentral.add(panelMostrarProductos);
                    revalidate();                             // Actualiza el diseño
                    repaint();                                // Repinta el JFrame
                }
            
            });

        }

    }

    public class PanelCentral extends JPanel {

        private static final long serialVersionUID = 1L;

		public PanelCentral() {
            nuevoInicialConLogo = new InicialConLogo();
            setLayout(new BorderLayout());
            add(nuevoInicialConLogo, BorderLayout.CENTER);
        }
    }

}
