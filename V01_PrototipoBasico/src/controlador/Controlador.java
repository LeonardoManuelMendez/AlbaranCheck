package controlador;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import dao.DAO;
import modelo.Albaran;
import modelo.Producto;
import modelo.ProductoEnAlbaran;

public class Controlador {
	
	public static List<ProductoEnAlbaran> leerAlbaran(File pdfFile) {
		List<ProductoEnAlbaran> listaProductos = new ArrayList<>();
		ProductoEnAlbaran nuevoProductoEnAlbaran;
		Producto nuevoProducto = null;
		Albaran nuevoAlbaran = null;
		String codigo, nombre, numero = null;
		LocalDate fecha = null;
		int cantidadBultos;
		int cantidadUnidades;

		try (PDDocument document = Loader.loadPDF(pdfFile)) {
			PDFTextStripper stripper = new PDFTextStripper();
			String text = stripper.getText(document);
			Pattern patronItems = Pattern.compile("""
				    (?x)                             # modo extendido: ignora espacios y permite comentarios con #
				    (\\d{2,6})                       # 1: Código del producto (2 a 6 dígitos)
				    \\s{1}                           #    un espacio obligatorio
				    (.{3,32}?)                       # 2: Nombre del producto (3 a 32 chars, lazy)
				    \\s{1}                           #    un espacio obligatorio
				    (\\.{0,1})                       # 3: Punto opcional
				    \\s{1,8}                         #    1 a 8 espacios
				    (\\d{1,6})                       # 4: Cantidad de bultos (1 a 6 dígitos)
				    \\s{1,3}                         #    1 a 3 espacios
				    (\\d{1,3})                       # 5: Campo intermedio (1 a 3 dígitos)
				    \\s{1,5}                         #    1 a 5 espacios
				    (\\d{0,4})                       # 6: Cantidad de unidades (0 a 4 dígitos; puede estar vacío)
				    """);


			Matcher matcherItems = patronItems.matcher(text);

			Pattern patronNumero = Pattern.compile("NUMERO DE ALBARAN ..........: (\\d{1,2})- (\\d{1,9})");
			Matcher matcherNumero = patronNumero.matcher(text);
			if (matcherNumero.find()) {
				numero = matcherNumero.group(1) + "- " + matcherNumero.group(2);
			}

			Pattern patronFecha = Pattern.compile("Fecha ..:  (\\d{2}/\\d{2}/\\d{2})"); // Patrón para dd/mm/aa
			Matcher matcherFecha = patronFecha.matcher(text);

			if (matcherFecha.find()) {
				try {
					fecha = LocalDate.parse(matcherFecha.group(1), DateTimeFormatter.ofPattern("dd/MM/yy"));
				} catch (DateTimeParseException e) {
					System.err.println("Error al analizar la fecha: " + e.getMessage());
				}
			}

			nuevoAlbaran = new Albaran(numero, fecha);

			while (matcherItems.find()) {
				codigo = matcherItems.group(1);

				nombre = matcherItems.group(2);

				cantidadBultos = Integer.parseInt(matcherItems.group(4));
				cantidadUnidades = 0;
				String unidadesPRE = matcherItems.group(6);
				if (!unidadesPRE.equals("")) {
					cantidadUnidades = Integer.parseInt(unidadesPRE);
				}
				nuevoProducto = new Producto(codigo, nombre);

				nuevoProductoEnAlbaran = new ProductoEnAlbaran(nuevoProducto, nuevoAlbaran, cantidadUnidades,
						cantidadBultos);
				listaProductos.add(nuevoProductoEnAlbaran);

			}

		} catch (IOException e) {
			System.out.println("Error al leer el archivo PDF: " + e.getMessage()); // Mensaje más detallado
		} catch (Exception e) {
			System.out.println("Error inesperado: " + e.getMessage());
		}

		return listaProductos;
	}

	public static void borrarLineadelDAT(List<Producto> listaProductos, String id) {

		for (int i = 0; i < listaProductos.size(); i++) {
			if (listaProductos.get(i).getCodigo().equals(id)) {
				listaProductos.remove(i);

				break;
			}
		}

		DAO.guardarListaProductos(listaProductos);
	}
	
	public static void borrarProductoEnAlbaran(List<ProductoEnAlbaran> listaProductosEnAlbaran, String codigo) {
		for (int i = 0; i < listaProductosEnAlbaran.size(); i++) {
			ProductoEnAlbaran pea = listaProductosEnAlbaran.get(i);
			if (pea != null && pea.getProducto() != null && pea.getProducto().getCodigo().equals(codigo)) {
				listaProductosEnAlbaran.remove(i);
				break;
			}
		}
		// Si tienes persistencia, aquí deberías guardar la lista actualizada
		// Por ejemplo: DAO.guardarListaProductosEnAlbaran(listaProductosEnAlbaran);
	}
	
	public static String resultadoValidacion(ProductoEnAlbaran pea, Producto productoLeido, String eanLeido) {
		if (eanLeido.equals(productoLeido.getEanProducto())) {
			pea.setUnidades_recibidas(pea.getUnidades_recibidas() + 1);
			return "Unidad del producto " + productoLeido.getNombre() + " recibida correctamente.";
		} else if (eanLeido.equals(productoLeido.getEanBulto())) {
			pea.setBultos_recibidos(pea.getBultos_recibidos() + 1);
			return "Bulto del producto " + productoLeido.getNombre() + " recibido correctamente.";
		} else {
			return "El EAN leído no coincide con el producto esperado.";
		}
	}
	
	public static boolean esFormatoEANValido(String eanLeido) {
		// Validar que el EAN tiene 13 dígitos numéricos
		Pattern patronEAN = Pattern.compile("^\\d{13}$");
		Matcher matcherEAN = patronEAN.matcher(eanLeido);
		return matcherEAN.matches();
	}
	
	public static Producto buscarProductoPorEan(List<Producto> listaProductos, String ean) {
		for (Producto p : listaProductos) {
			if (ean.equals(p.getEanProducto()) || ean.equals(p.getEanBulto())) {
				return p;
			}
		}
		return null;
	}
	
}