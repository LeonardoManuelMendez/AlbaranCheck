package controlador;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import modelo.ProductoEnAlbaran;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;


class ControladorTest {

	private File getResourceFile(String path) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("No se encontró el recurso: " + path);
        }
        return new File(resource.toURI());
    }

    @Test
    @DisplayName("Debe leer un PDF de albarán real y devolver productos")
    void testLeerAlbaranDesdePdf() throws Exception {
        File pdf = getResourceFile("/V01_PrototipoBasico/resources/14128_RD484026_Entrega-segun-pedido_924322_29618832.pdf");

        List<ProductoEnAlbaran> lista = Controlador.leerAlbaran(pdf);

        assertFalse(lista.isEmpty(), "El PDF de ejemplo debería devolver productos");
        // más asserts según el contenido del PDF de prueba
    }

    @Test
    @DisplayName("Debe parsear número, fecha y una línea de ítem con unidades")
    void testLeerAlbaran_ok() throws Exception {
        
        File pdf = getResourceFile("pdfs/albaran_ejemplo.pdf");

        // Llama a tu método real
        List<ProductoEnAlbaran> lista = Controlador.leerAlbaran(pdf);

        assertNotNull(lista, "La lista no debe ser null");
        assertEquals(1, lista.size(), "Debe haber exactamente 1 ítem");

        ProductoEnAlbaran pea = lista.get(0);

        // Ajusta getters a tus nombres reales:
        assertEquals("12345", pea.getProducto().getCodigo(), "Código del producto");
        assertEquals("ProductoXYZ", pea.getProducto().getNombre(), "Nombre del producto");
        assertEquals(4, pea.getBultos_esperados(), "Bultos");
        assertEquals(15, pea.getUnidades_esperadas(), "Unidades");

        // Verifica Albarán (número y fecha):
        assertEquals("12- 345678", pea.getAlbaran().getNumero(), "Número de albarán");
        assertEquals(LocalDate.of(2025, 8, 31), pea.getAlbaran().getFecha(), "Fecha del albarán");
    }

    @Test
    @DisplayName("Si el campo de unidades va vacío, debe interpretar 0 unidades")
    void testLeerAlbaran_sinUnidades() throws Exception {
        File pdf = crearPdfTemporalConLineas(
                "NUMERO DE ALBARAN ..........: 01- 9",
                "Fecha ..:  01/01/25",
                // Sin unidades (grupo 6 vacío) → tu código pone 0
                "99 ABCdefgh   3  1      "
        );

        List<ProductoEnAlbaran> lista = Controlador.leerAlbaran(pdf);

        assertEquals(1, lista.size());
        ProductoEnAlbaran pea = lista.get(0);

        assertEquals("99", pea.getProducto().getCodigo());
        assertEquals("ABCdefgh", pea.getProducto().getNombre());
        assertEquals(3, pea.getBultos_esperados());
        assertEquals(0, pea.getUnidades_esperadas(), "Unidades debe ser 0 si el grupo está vacío");
    }

    @Test
    @DisplayName("Si no hay líneas que casen con el patrón, la lista debe ser vacía")
    void testLeerAlbaran_pdfSinCoincidenciasDevuelveVacio() throws Exception {
        File pdf = crearPdfTemporalConLineas(
                "NUMERO DE ALBARAN ..........: 02- 123",
                "Fecha ..:  15/07/25",
                "Esto no tiene formato de ítem válido",
                "Tampoco esta línea"
        );

        List<ProductoEnAlbaran> lista = Controlador.leerAlbaran(pdf);
        assertNotNull(lista);
        assertTrue(lista.isEmpty(), "Sin líneas válidas, lista vacía");
    }

    @Test
    @DisplayName("Si el PDF no existe o no se puede leer, no debe lanzar excepción y retorna lista (posiblemente vacía)")
    void testLeerAlbaran_pdfInaccesibleNoLanza() {
        File noExiste = new File("no_existe_abc_123.pdf");
        assertDoesNotThrow(() -> {
            List<ProductoEnAlbaran> lista = Controlador.leerAlbaran(noExiste);
            assertNotNull(lista, "Debe devolver lista no nula, aunque vacía");
        });
    }

	@Test
	void testBorrarLineadelDAT() {
		fail("Not yet implemented");
	}

}