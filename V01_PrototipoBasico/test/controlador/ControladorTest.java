package controlador;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import modelo.ProductoEnAlbaran;


class ControladorTest {

    @Test
    @DisplayName("Debe leer un PDF de albarán real y devolver productos")
    void testLeerAlbaranDesdePdf() throws Exception {
        File pdf = new File("resources/albaran_fixture_minimo.pdf");
        System.out.println("[DEBUG] Archivo encontrado: " + pdf.getAbsolutePath() + ", existe: " + pdf.exists());
        assertTrue(pdf.exists(), "El archivo PDF de ejemplo debe existir para el test");
        List<ProductoEnAlbaran> lista = Controlador.leerAlbaran(pdf);
        assertFalse(lista.isEmpty(), "El PDF de ejemplo debería devolver productos");
        // más asserts según el contenido del PDF de prueba
    }

    @Test
    @DisplayName("Debe parsear número, fecha y una línea de ítem con unidades")
    void testLeerAlbaran_ok() throws Exception {
        File pdf = new File("resources/albaran_fixture_minimo.pdf");

        List<ProductoEnAlbaran> lista = Controlador.leerAlbaran(pdf);

        assertNotNull(lista, "La lista no debe ser null");
        assertEquals(1, lista.size(), "Debe haber exactamente 1 ítem");

        ProductoEnAlbaran pea = lista.get(0);

        assertEquals("3097", pea.getProducto().getCodigo());
        assertEquals("CREMA TARRO SOFT NIVEA", pea.getProducto().getNombre());
        assertEquals(2, pea.getBultos_esperados());
        assertEquals(4, pea.getUnidades_esperadas(), "Unidades debe ser 4");

        // Verifica Albarán (número y fecha):
        assertEquals("7- 924322", pea.getAlbaran().getNumero(), "Número de albarán");
        assertEquals(LocalDate.of(2019, 10, 28), pea.getAlbaran().getFecha(), "Fecha del albarán");
    }

    @Test
    @DisplayName("Si el campo de unidades va vacío, debe interpretar 0 unidades")
    void testLeerAlbaran_sinUnidades() throws Exception {
        File pdf = new File("resources/albaran_fixture_varios.pdf");

        List<ProductoEnAlbaran> lista = Controlador.leerAlbaran(pdf);

        assertEquals(4, lista.size());
        ProductoEnAlbaran pea = lista.get(1); // El segundo ítem, que no tiene unidades

        assertEquals("19830", pea.getProducto().getCodigo());
        assertEquals("DESODORANTE SPRAY PIES", pea.getProducto().getNombre());
        assertEquals(0, pea.getUnidades_esperadas(), "Unidades debe ser 0 si el grupo está vacío");
    }

    @Test
    @DisplayName("Si no hay líneas que casen con el patrón, la lista debe ser vacía")
    void testLeerAlbaran_pdfSinCoincidenciasDevuelveVacio() throws Exception {
        File pdf = new File("resources/sinInf.pdf");

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
	@DisplayName("Test de método no implementado aún")
	void testBorrarLineadelDAT() {
		fail("Not yet implemented");
	}
	
	@Test
	@DisplayName("Test de método no implementado aún")
	void testResultadoValidacion() {
		fail("Not yet implemented");
	}

	@Test
	@DisplayName("Test de método no implementado aún")
	void testEsFormatoEANValido() {
		fail("Not yet implemented");
	}

	@Test
	@DisplayName("Test de método no implementado aún")
	void testBuscarProductoPorEan() {
		fail("Not yet implemented");
	}

}