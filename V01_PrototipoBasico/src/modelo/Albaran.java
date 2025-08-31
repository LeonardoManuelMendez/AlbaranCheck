package modelo;

import java.time.LocalDate;


/**
 *
 * @author Leonardo Méndez
 */
public class Albaran {
    private String numero;
    private LocalDate fecha;
    private String tipo;

    public Albaran(String numero, LocalDate fecha, String tipo) {
        this.numero = numero;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public Albaran(String numero, LocalDate fecha) {
		this.numero = numero;
		this.fecha = fecha;
		this.tipo = "ENTRADA"; // Valor por defecto
	}
    
    public Albaran() {
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Albaran{" + "numero=" + numero + ", fecha=" + fecha + ", tipo=" + tipo + '}';
    }
    
    

}

