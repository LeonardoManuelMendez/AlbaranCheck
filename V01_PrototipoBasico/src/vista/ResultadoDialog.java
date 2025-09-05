package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.print.PrinterException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import modelo.Albaran;
import modelo.Producto;
import modelo.ProductoEnAlbaran;

public class ResultadoDialog extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();
    private JTextPane textPane;

    public ResultadoDialog(Window parent, List<ProductoEnAlbaran> listaProductosEnAlbaran) {
        super(parent, "Resultado de Verificación", ModalityType.APPLICATION_MODAL);
        setBounds(100, 100, 600, 500);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));

        textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setContentType("text/html");

        JScrollPane scroll = new JScrollPane(textPane);
        contentPanel.add(scroll, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        // Botón Imprimir
        JButton printButton = new JButton("Imprimir");
        printButton.addActionListener(e -> imprimir());
        buttonPane.add(printButton);

        JButton okButton = new JButton("Cerrar");
        okButton.addActionListener(e -> dispose());
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        // Construir y mostrar el HTML
        String html = construirHtmlIncidencias(listaProductosEnAlbaran);
        textPane.setText(html);
        textPane.setCaretPosition(0);
    }

    private void imprimir() {
        try {
            // Muestra diálogo de impresión y gestiona el job automáticamente
            boolean ok = textPane.print(null, null, true, null, null, true);
            if (!ok) {
                JOptionPane.showMessageDialog(this, "Impresión cancelada.", "Imprimir", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo imprimir: " + ex.getMessage(), "Error de impresión",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String construirHtmlIncidencias(List<ProductoEnAlbaran> lista) {
        // Cabecera del albarán (si está disponible)
        String numero = "-";
        String fecha  = "-";
        Albaran alb = obtenerAlbaran(lista);
        if (alb != null) {
            if (alb.getNumero() != null) numero = String.valueOf(alb.getNumero());
            if (alb.getFecha()  != null) {
                try {
                    fecha = alb.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception ignored) {
                    fecha = String.valueOf(alb.getFecha());
                }
            }
        }

        List<String> faltas = new ArrayList<>();
        List<String> sobras = new ArrayList<>();

        if (lista != null) {
            for (ProductoEnAlbaran pea : lista) {
                if (pea == null || pea.getProducto() == null) continue;

                Producto prod = pea.getProducto();
                String nombre = (prod.getNombre() != null && !prod.getNombre().isBlank())
                        ? prod.getNombre()
                        : (prod.getCodigo() != null ? ("Producto " + prod.getCodigo()) : "Producto sin nombre");

                int diffUnidades = safe(pea.getUnidades_recibidas()) - safe(pea.getUnidades_esperadas());
                int diffBultos   = safe(pea.getBultos_recibidos())   - safe(pea.getBultos_esperados());

                if (diffUnidades < 0) faltas.add(String.format("-%d %s (unidades)", Math.abs(diffUnidades), nombre));
                if (diffBultos   < 0) faltas.add(String.format("-%d %s (bultos)",   Math.abs(diffBultos),   nombre));
                if (diffUnidades > 0) sobras.add(String.format("+%d %s (unidades)", diffUnidades, nombre));
                if (diffBultos   > 0) sobras.add(String.format("+%d %s (bultos)",   diffBultos,   nombre));
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("""
        <html>
        <head>
          <meta charset="UTF-8">
          <style>
            body { font-family: sans-serif; font-size: 13px; }
            .hdr { margin-bottom: 10px; }
            .title { font-size: 16px; font-weight: bold; margin-bottom: 6px; }
            .albaran { color: #444; margin-bottom: 12px; }
            .section { margin-top: 8px; }
            .label { font-weight: bold; }
            ul { margin: 6px 0 10px 18px; }
            .faltas { color: #b00020; }
            .sobras { color: #0b8457; }
            .none  { color: #666; font-style: italic; }
            .pill { display:inline-block; padding:2px 8px; border-radius:12px; background:#eee; margin-left:6px; font-size:12px;}
          </style>
        </head>
        <body>
        """);

        sb.append("<div class='hdr'>")
          .append("<div class='title'>Resultado de Verificación</div>")
          .append("<div class='albaran'>")
          .append("<span class='label'>Albarán:</span> ")
          .append(htmlEscape(numero))
          .append(" &nbsp;&nbsp; <span class='label'>Fecha:</span> ")
          .append(htmlEscape(fecha))
          .append("</div>")
          .append("</div>");

        sb.append("<div class='section'><span class='label'>Incidencias Encontradas:</span></div>");

        // FALTAS
        sb.append("<div class='section faltas'>Faltas:<span class='pill'>")
          .append(faltas.size())
          .append("</span></div>");
        if (faltas.isEmpty()) {
            sb.append("<div class='none'> (ninguna) </div>");
        } else {
            sb.append("<ul>");
            for (String f : faltas) {
                sb.append("<li>").append(htmlEscape(f)).append("</li>");
            }
            sb.append("</ul>");
        }

        // SOBRAS
        sb.append("<div class='section sobras'>Sobras:<span class='pill'>")
          .append(sobras.size())
          .append("</span></div>");
        if (sobras.isEmpty()) {
            sb.append("<div class='none'> (ninguna) </div>");
        } else {
            sb.append("<ul>");
            for (String s : sobras) {
                sb.append("<li>").append(htmlEscape(s)).append("</li>");
            }
            sb.append("</ul>");
        }

        sb.append("</body></html>");
        return sb.toString();
    }

    private Albaran obtenerAlbaran(List<ProductoEnAlbaran> lista) {
        if (lista == null) return null;
        for (ProductoEnAlbaran pea : lista) {
            if (pea != null && pea.getAlbaran() != null) return pea.getAlbaran();
        }
        return null;
    }

    private int safe(Integer v) {
        return (v == null) ? 0 : v;
    }

    private String htmlEscape(String s) {
        if (s == null) return "";
        StringBuilder out = new StringBuilder(Math.max(16, s.length()));
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '<' -> out.append("&lt;");
                case '>' -> out.append("&gt;");
                case '&' -> out.append("&amp;");
                case '"' -> out.append("&quot;");
                case '\'' -> out.append("&#39;");
                default -> out.append(c);
            }
        }
        return out.toString();
    }
}
