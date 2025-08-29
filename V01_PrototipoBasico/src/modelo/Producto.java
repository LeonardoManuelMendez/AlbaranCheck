package modelo;

/**
 *
 * @author Leonardo Méndez
 */
public class Producto implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String codigo;
	private String nombre;
	private String eanProducto;
	private String eanBulto;
	private String formato;

	public Producto(String codigo, String nombre, String eanProducto, String eanBulto, String formato) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.eanProducto = eanProducto;
		this.eanBulto = eanBulto;
		this.formato = formato;
	}

	public Producto(String codigo, String nombre) {
		this.codigo = codigo;
		this.nombre = nombre;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEanBulto() {
		return eanBulto;
	}

	public void setEanBulto(String eanBulto) {
		this.eanBulto = eanBulto;
	}

	public String getEanProducto() {
		return eanProducto;
	}

	public void setEanProducto(String eanProducto) {
		this.eanProducto = eanProducto;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	@Override
	public String toString() {
		return "Producto [codigo=" + codigo + ", nombre=" + nombre + ", eanProducto=" + eanProducto + ", eanBulto="
				+ eanBulto + ", formato=" + formato + "]";
	}

}
