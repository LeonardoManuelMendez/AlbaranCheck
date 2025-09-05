package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import modelo.ProductoEnAlbaran;

public class EditarProductosEnAlbaran extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextField textField;          // Nombre (solo lectura)
    private JTextField textField_1;        // Código (solo lectura)
    private JTextField textFieldCantEsp;   // Cantidad esperada (editable)
    private JTextField textFieldCantRecib; // Cantidad recibida (editable)

    public EditarProductosEnAlbaran(ProductoEnAlbaran productoEnAlbaran,
                                    List<ProductoEnAlbaran> listaProductosEnAlbaran) {
        setTitle("Editar producto en albarán");
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setBounds(100, 100, 343, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JLabel lblTitulo = new JLabel("Editar Producto En Albarán");
        lblTitulo.setFont(new Font("Dialog", Font.BOLD, 14));
        lblTitulo.setBounds(65, 12, 220, 20);
        contentPanel.add(lblTitulo);

        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblCodigo.setBounds(12, 54, 60, 17);
        contentPanel.add(lblCodigo);

        textField_1 = new JTextField();
        textField_1.setEditable(false);
        textField_1.setBounds(99, 47, 160, 21);
        contentPanel.add(textField_1);
        textField_1.setColumns(10);

        JLabel lblNombre = new JLabel("Nombre: ");
        lblNombre.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblNombre.setBounds(12, 82, 60, 17);
        contentPanel.add(lblNombre);

        textField = new JTextField();
        textField.setEditable(false);
        textField.setBounds(99, 80, 220, 21);
        contentPanel.add(textField);
        textField.setColumns(10);

        JLabel lblFormato = new JLabel("Formato:");
        lblFormato.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblFormato.setBounds(12, 111, 60, 17);
        contentPanel.add(lblFormato);

        // Radios solamente informativos (deshabilitados)
        JRadioButton rdbtnUnidades = new JRadioButton("Unidades");
        rdbtnUnidades.setEnabled(false);
        rdbtnUnidades.setBounds(99, 103, 100, 25);
        contentPanel.add(rdbtnUnidades);

        JRadioButton rdbtnBultos = new JRadioButton("Bultos");
        rdbtnBultos.setEnabled(false);
        rdbtnBultos.setBounds(201, 103, 80, 25);
        contentPanel.add(rdbtnBultos);

        JLabel lblCantidadEsperada = new JLabel("Cantidad Esperada:");
        lblCantidadEsperada.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblCantidadEsperada.setBounds(12, 141, 114, 17);
        contentPanel.add(lblCantidadEsperada);

        textFieldCantEsp = new JTextField();
        textFieldCantEsp.setEditable(false);
        textFieldCantEsp.setBounds(144, 137, 114, 21);
        contentPanel.add(textFieldCantEsp);
        textFieldCantEsp.setColumns(10);

        JLabel lblCantidadRecibida = new JLabel("Cantidad Recibida:");
        lblCantidadRecibida.setFont(new Font("Dialog", Font.PLAIN, 12));
        lblCantidadRecibida.setBounds(12, 170, 114, 17);
        contentPanel.add(lblCantidadRecibida);

        textFieldCantRecib = new JTextField();
        textFieldCantRecib.setBounds(144, 166, 114, 21);
        contentPanel.add(textFieldCantRecib);
        textFieldCantRecib.setColumns(10);

        // Filtro: solo enteros no negativos
        DocumentFilter soloEnteros = new DocumentFilter() {
            @Override public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                if (string == null) return;
                if (string.matches("\\d+")) super.insertString(fb, offset, string, attr);
            }
            @Override public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                if (text == null) return;
                String nuevo = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()))
                        .replace(offset, offset + length, text).toString();
                if (nuevo.matches("\\d*")) super.replace(fb, offset, length, text, attrs);
            }
        };
        ((AbstractDocument) textFieldCantEsp.getDocument()).setDocumentFilter(soloEnteros);
        ((AbstractDocument) textFieldCantRecib.getDocument()).setDocumentFilter(soloEnteros);

        // Botonera
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton botonGuardar = new JButton("Guardar");
        botonGuardar.addActionListener((ActionEvent e) -> {
            // Validar números (vacío = 0)
            int cantEsp = parseEnteroSeguro(textFieldCantEsp.getText());
            int cantRecib = parseEnteroSeguro(textFieldCantRecib.getText());

            if (cantEsp < 0 || cantRecib < 0) {
                javax.swing.JOptionPane.showMessageDialog(
                        EditarProductosEnAlbaran.this,
                        "Las cantidades no pueden ser negativas.",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // === ÚNICO EFECTO: actualizar cantidades ===
            if (productoEnAlbaran != null) {
                productoEnAlbaran.setUnidades_esperadas(cantEsp);
                productoEnAlbaran.setUnidades_recibidas(cantRecib);
            }

            dispose();
        });
        buttonPane.add(botonGuardar);
        getRootPane().setDefaultButton(botonGuardar);

        JButton botonCancelar = new JButton("Cancelar");
        botonCancelar.addActionListener(e -> dispose());
        buttonPane.add(botonCancelar);

        // Cargar datos (solo lectura para código/nombre/formato)
        if (productoEnAlbaran != null && productoEnAlbaran.getProducto() != null) {
            var p = productoEnAlbaran.getProducto();
            textField_1.setText(p.getCodigo() != null ? p.getCodigo() : "");
            textField.setText(p.getNombre() != null ? p.getNombre() : "");

            textFieldCantEsp.setText(String.valueOf(productoEnAlbaran.getUnidades_esperadas()));
            textFieldCantRecib.setText(String.valueOf(productoEnAlbaran.getUnidades_recibidas()));

            // Marcar radios informativos según datos del albarán
            boolean esUnidades = (productoEnAlbaran.getUnidades_esperadas() > 0 && productoEnAlbaran.getBultos_esperados() == 0)
                               || (productoEnAlbaran.getUnidades_recibidas() > 0 && productoEnAlbaran.getBultos_recibidos() == 0);
            rdbtnUnidades.setSelected(esUnidades);
            rdbtnBultos.setSelected(!esUnidades);
        } else {
            // Defaults
            textField_1.setText("");
            textField.setText("");
            textFieldCantEsp.setText("0");
            textFieldCantRecib.setText("0");
        }
    }

    private static int parseEnteroSeguro(String s) {
        if (s == null || s.isBlank()) return 0;
        try { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return -1; } // Para disparar validación de negativo
    }
}
