package vista;
import javax.swing.*;
import java.awt.*;


/**
 *
 * @author Leonardo Méndez
 */
public class InicialConLogo extends JPanel {

    private static final long serialVersionUID = 1L;

	public InicialConLogo() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        JLabel saludo = new JLabel("Bienvenidos!!!");

        // Carga la imagen
        ImageIcon imagenIcono = new ImageIcon("./src/ficheros/logo.png");       
      

        // Escala la imagen si es necesario (opcional)
        Image imagen = imagenIcono.getImage().getScaledInstance(270, 200, Image.SCALE_SMOOTH);
        imagenIcono = new ImageIcon(imagen);

        // Crea un JLabel y establece la imagen
        JLabel etiquetaImagen = new JLabel(imagenIcono);

        // crea una instacia para la disposición de los objetos dentro del panel
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);
        
        add(saludo, BorderLayout.NORTH);
        add(etiquetaImagen, BorderLayout.CENTER);
        
      
        
    }

}
