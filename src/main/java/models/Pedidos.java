 package models;
/**/
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
/**/
/**
 ** @author Ridouan Abdellah Tieb
 **/
@Entity
@Table(name="pedidos")
public class Pedidos implements Serializable {
    /*
    Creo la clase Pedidos que contendrá todos los atributos de esa entidad y que
    serán las columnas de la tabla. Creo el constructor vacío, constructor 
    con parámetros, getters, setters y toString.
    */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    
    @Column(name="usuario")
    private String usuario;
    
    @Column(name="ciclo")
    private String ciclo;
    
    @Column(name="fecha")
    private Date fecha;
    
    @Column(name="estado")
    private String estado; 
    
    @ManyToOne
    @JoinColumn (name= "productos_id")
    private Productos productos;
    
    public Productos getProductos(){
        return productos;
    }
    
    public void setProductos (Productos productos) {
        this.productos=productos;
    }
    
    public Pedidos() {}

    public Pedidos(int id, String usuario, int productos_id, Date fecha, 
                   String ciclo, String estado) {
        this.id = id;
        this.usuario = usuario;
        this.ciclo = ciclo;
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCiclo() {
        return ciclo;
    }

    public void setCiclo(String ciclo) {
        this.ciclo = ciclo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "PEDIDOS [" + "ID: " + id + "; USUARIO: " + usuario + "; CICLO: " + 
                ciclo + "; FECHA: " + fecha + "; ESTADO: " + estado + ']';
    }
    
    
}
