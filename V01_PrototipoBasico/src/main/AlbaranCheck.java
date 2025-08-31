package main;

import vista.*;

import java.util.List;

import javax.swing.SwingUtilities;

import modelo.Albaran;
import modelo.Producto;
import modelo.ProductoEnAlbaran;
import static dao.DAO.leerListaDeProductos;


/**
 *
 * @author Leonardo Méndez
 */
public class AlbaranCheck {
	


    public static void main(String[] args) {
    	// Cargar la lista de productos desde el DAO
		List<Producto> listaProductos = new java.util.ArrayList<>(leerListaDeProductos());
		List<ProductoEnAlbaran> listaProductosEnAlbaran = new java.util.ArrayList<>();
		Albaran albaranActual = new Albaran(); // Albarán vacío inicialmente
       
        SwingUtilities.invokeLater(() -> {
            JFrameMenu frame = new JFrameMenu(listaProductos, listaProductosEnAlbaran, albaranActual);
            frame.setVisible(true);
        });
    }
}
