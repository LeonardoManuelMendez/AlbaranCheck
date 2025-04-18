package vista;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Leonardo Méndez
 */
public class InicioDeSesion extends JFrame {

    private static final long serialVersionUID = 1L;
	private final JLabel labelIncioSesion;
    private final JLabel labelUsuario;
    private JTextField textFieldUsuario;
    private final JLabel labelClave;
    private JPasswordField textFieldClave;
    private final JButton botonIniciarSesion;
    private final JPanel panelUnico;

    public InicioDeSesion() {
        // Cambia el tipo de marco por uno con mejor apariencia
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
        }

        // Titulo del marco. Será el mismo en todos para indicar el nombre del progrma en ejecución
        setTitle("AlbaranCheck");
        // Carga la imagen
        ImageIcon imagenIcono = new ImageIcon("./src/ficheros/logoPequeno2.png");
        // Escala la imagen si es necesario (opcional)
        Image imagen = imagenIcono.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
        setIconImage(imagen);
        // Establece el tamaño y la posición del marco
        setBounds(800, 400, 280, 175);
        // indica la ación a realizar al cerrar el marco
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // establece que al marco no puede cambiarsele el tamaño
        setResizable(false);
        // etiqueta que sirve de titulo para su panel interno. Se la cambian los valores por defecto
        labelIncioSesion = new JLabel("INICIO DE SESIÓN");
        Font font = new Font("Arial", Font.BOLD, 12); // Arial, negrita, tamaño 24
        labelIncioSesion.setFont(font);
        // establece etiqueta y campo para colocar el usuario
        labelUsuario = new JLabel("Usuario: ");
        textFieldUsuario = new JTextField();
        // estable etiqueta y campo para colocar la clave
        labelClave = new JLabel("Clave: ");
        textFieldClave = new JPasswordField();
        // crea el boton para iniciar la cesión
        botonIniciarSesion = new JButton("Iniciar Sesión");
        // estable el tamaño que tendrán los campos. Los dos a la vez para que sean iguales
        Dimension textFieldSize = new Dimension(150, 24);
        textFieldUsuario.setPreferredSize(textFieldSize);
        textFieldUsuario.setMinimumSize(textFieldSize);
        textFieldUsuario.setMaximumSize(textFieldSize);
        textFieldClave.setPreferredSize(textFieldSize);
        textFieldClave.setMinimumSize(textFieldSize);
        textFieldClave.setMaximumSize(textFieldSize);
        //crea un panel para colocar todos los objetos antes creados y poder organizarlos
        panelUnico = new JPanel();
        // crea una instacia para la disposición de los objetos dentro del panel
        GroupLayout layout = new GroupLayout(panelUnico);
        panelUnico.setLayout(layout);
        // Ajustes para el GroupLayout
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        //estable el orden de izquierda a derecha de como se van a poresentar los los componentes y los agraga al panel
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createSequentialGroup() // Grupo para etiquetas y campos
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(labelUsuario)
                                        .addComponent(labelClave))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(labelIncioSesion)
                                        .addComponent(textFieldUsuario)
                                        .addComponent(textFieldClave)
                                        .addComponent(botonIniciarSesion))
                        )
        );
        // estable el orden de arriba a abajo de aparición de los componentes. 
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(labelIncioSesion))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(labelUsuario)
                                .addComponent(textFieldUsuario))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(labelClave)
                                .addComponent(textFieldClave))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(botonIniciarSesion)
                        .addContainerGap()
        );
        // se añade el panel al marco
        add(panelUnico);
        
        setVisible(true);

        botonIniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuario = textFieldUsuario.getText();
                String clave = new String(textFieldClave.getPassword());
                if ("leonardo".equals(usuario) && "1234".equals(clave)) {
                    InicioDeSesion.this.dispose(); // Cierra la ventana actual
                    new JFrameMenu().setVisible(true); // Abre la ventana del menú
                } else {

                }

            }

        });
    }
}
