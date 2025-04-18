package modelo;

/**
 *
 * @author Leonardo Méndez
 */
public class Usuarios {
    private int codigoUsuario;
    private String nombreUsuario;
    private int tipoUsuario;

    public Usuarios(int codigoUsuario, String nombreUsuario, int tipoUsuario) {
        this.codigoUsuario = codigoUsuario;
        this.nombreUsuario = nombreUsuario;
        this.tipoUsuario = tipoUsuario;
    }

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    @Override
    public String toString() {
        return "Usuarios{" + "codigoUsuario=" + codigoUsuario + ", nombreUsuario=" + nombreUsuario + ", tipoUsuario=" + tipoUsuario + '}';
    }
    
}
