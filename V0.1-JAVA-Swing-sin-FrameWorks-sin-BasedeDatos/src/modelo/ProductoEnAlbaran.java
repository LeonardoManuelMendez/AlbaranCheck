package modelo;

/**
 *
 * @author Leonardo Méndez
 */
public class ProductoEnAlbaran {
    private Producto producto;
    private Albaran albaran;    
    private int cantidadUnidades;
    private int cantidadBultos;

    public ProductoEnAlbaran(Producto producto, Albaran albaran, int cantidadUnidades, int cantidadBultos) {
        this.producto = producto;
        this.albaran = albaran;
        this.cantidadUnidades = cantidadUnidades;
        this.cantidadBultos = cantidadBultos;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Albaran getAlbaran() {
        return albaran;
    }

    public void setAlbaran(Albaran albaran) {
        this.albaran = albaran;
    }

    public int getCantidadUnidades() {
        return cantidadUnidades;
    }

    public void setCantidadUnidades(int cantidadUnidades) {
        this.cantidadUnidades = cantidadUnidades;
    }

    public int getCantidadBultos() {
        return cantidadBultos;
    }

    public void setCantidadBultos(int cantidadBultos) {
        this.cantidadBultos = cantidadBultos;
    }

    @Override
    public String toString() {
        return "ProductoEnAlbaran{" + "producto=" + producto + ", albaran=" + albaran + ", cantidadUnidades=" + cantidadUnidades + ", cantidadBultos=" + cantidadBultos + '}';
    }
    
}
