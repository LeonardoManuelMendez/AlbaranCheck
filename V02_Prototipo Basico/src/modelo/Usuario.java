package modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private String perfil;
    private String clave;

    public Usuario(String nombre, String perfil, String clave) {
        this.perfil = perfil; this.nombre = nombre; this.clave = clave;
    }
    public String getPerfil() { return perfil; }
    public String getNombre() { return nombre; }
    public String getClave() { return clave; }
}

