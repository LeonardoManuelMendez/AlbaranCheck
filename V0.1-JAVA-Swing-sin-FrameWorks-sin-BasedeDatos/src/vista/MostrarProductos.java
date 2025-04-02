package vista;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Leonardo Méndez
 */
public class MostrarProductos extends JPanel{
    
    private static final long serialVersionUID = 1L;

	public MostrarProductos(){
        JLabel nuevoMostrarProductos = new JLabel("Lista de Productos");
        add(nuevoMostrarProductos);
    }

}
