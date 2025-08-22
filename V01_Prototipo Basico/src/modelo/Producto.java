package modelo;

/**
 *
 * @author Leonardo Méndez
 */
public class Producto {
    private String codigo;
    private String nombre;
    private String eanProducto;
    private String eanBulto;

    public Producto(String codigo, String nombre, String eanProducto, String eanBulto) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.eanProducto = eanProducto;
        this.eanBulto = eanBulto;
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

    @Override
    public String toString() {
        return "Producto{" + "codigo=" + codigo + ", nombre=" + nombre + ", eanProducto=" + eanProducto + ", eanBulto=" + eanBulto + '}';
    }

    
    
}
