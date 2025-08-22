package modelo;

/**
 *
 * @author Leonardo Méndez
 */
public class ProductoEnAlbaran {
    private Producto producto;
    private Albaran albaran;    
    private int unidades_esperadas;
    private int bultos_esperados;
    private int unidades_recibidas;
    private int bultos_recibidos;

    public ProductoEnAlbaran(Producto producto, Albaran albaran, int unidades_esperadas, int bultos_esperados) {
        this.producto = producto;
        this.albaran = albaran;
        this.unidades_esperadas = unidades_esperadas;
        this.bultos_esperados = bultos_esperados;
        this.unidades_recibidas = 0;
        this.bultos_recibidos = 0;
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
    public int getUnidades_esperadas() {
        return unidades_esperadas;
    }
    public void setUnidades_esperadas(int unidades_esperadas) {
        this.unidades_esperadas = unidades_esperadas;
    }
    public int getBultos_esperados() {
        return bultos_esperados;
    }
    public void setBultos_esperados(int bultos_esperados) {
        this.bultos_esperados = bultos_esperados;
    }
    public int getUnidades_recibidas() {
        return unidades_recibidas;
    }
    public void setUnidades_recibidas(int unidades_recibidas) {
        this.unidades_recibidas = unidades_recibidas;
    }
    public int getBultos_recibidos() {
        return bultos_recibidos;
    }
    public void setBultos_recibidos(int bultos_recibidos) {
        this.bultos_recibidos = bultos_recibidos;
    }
    @Override
    public String toString() {
        return "ProductoEnAlbaran{" + "producto=" + producto + ", albaran=" + albaran + ", unidades_esperadas=" + unidades_esperadas + ", bultos_esperados=" + bultos_esperados + ", unidades_recibidas=" + unidades_recibidas + ", bultos_recibidos=" + bultos_recibidos + '}';
    }
}
