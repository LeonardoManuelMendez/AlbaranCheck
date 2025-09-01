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
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import controlador.Controlador;
import modelo.ProductoEnAlbaran;
import modelo.Albaran;
import modelo.Producto;


class ControladorTest {

	 // Helper: crea un PDF de texto simple con las líneas dadas
    private File crearPdfTemporalConLineas(String... lineas) throws IOException {
        File tmp = Files.createTempFile("albaran_test_", ".pdf").toFile();
        tmp.deleteOnExit();

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage();
            doc.addPage(page);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.beginText();
                cs.setFont(new PDFont(Standard14Fonts.FontName.HELVETICA), 11);
                // Coordenadas base (y va decreciendo por línea)
                float x = 50;
                float y = 750;
                cs.newLineAtOffset(x, y);

                for (int i = 0; i < lineas.length; i++) {
                    cs.showText(lineas[i]);
                    cs.newLineAtOffset(0, -16); // salto línea aprox.
                }

                cs.endText();
            }

            doc.save(tmp);
        }

        return tmp;
    }

    @Test
    @DisplayName("Debe parsear número, fecha y una línea de ítem con unidades")
    void testLeerAlbaran_ok() throws Exception {
        // Ojo con los espacios, deben respetar tu regex.
        // NUMERO: "NUMERO DE ALBARAN ..........: (\\d{1,2})- (\\d{1,9})"
        // FECHA : "Fecha ..:  (\\d{2}/\\d{2}/\\d{2})"
        // Ítem  : "(\\d{2,6})\\s{1,1}(.{3,32}?)\\s{1,1}(\\.{0,1})\\s{1,8}(\\d{1,6})\\s{1,3}(\\d{1,3})\\s{1,5}(\\d{0,4})"
        File pdf = crearPdfTemporalConLineas(
                "NUMERO DE ALBARAN ..........: 12- 345678",
                "Fecha ..:  31/08/25",
                // código(5) + espacio + nombre(>=3 chars) + espacio + "." opcional + espacios + bultos + espacios + ??? + espacios + unidades
                // Aquí ejemplo con "." presente, bultos=4, (campo intermedio)=2, unidades=15
                "12345 ProductoXYZ .    4  2    0015"
        );

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
