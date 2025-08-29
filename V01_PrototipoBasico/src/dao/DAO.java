package dao;

import java.io.*;
import java.util.List;

import modelo.Producto;

/**
 *
 * @author Leonardo Méndez
 */

public class DAO {
	private static String rutaFicheroProductos = "./src/ficheros/productos.dat";
	public static void guardarListaProductos(List<Producto> listaProductos) {
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaFicheroProductos, false))){  // con false sobreescribe el archivo. Borrando todo su contenido
			for(Producto producto : listaProductos) {
				oos.writeObject(producto);
			}
			oos.flush(); // Asegura que todos los datos se escriban en el archivo
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static List<Producto> leerListaDeProductosProducto() {
		List<Producto> listaProductos = new java.util.ArrayList<>();
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaFicheroProductos))) {
			while (true) {
				try {
					Producto producto = (Producto) ois.readObject();
					listaProductos.add(producto);
				} catch (EOFException e) {
					break; // Fin del archivo
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listaProductos;
	}

}