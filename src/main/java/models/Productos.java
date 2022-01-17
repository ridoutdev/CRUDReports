package models;
/**/
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
/**
 ** @author Ridouan Abdellah Tieb
 **/
@Entity
@Table (name="productos")
public class Productos implements Serializable {
    /*
    Creo la clase Productos que contendrá todos los atributos de esa entidad y 
    que serán las columnas de la tabla. Creo el constructor vacío, constructor
    con parámetros, getters, setters y toString.
    */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @Column
    private float precio;
    @Column
    private String nombre;
    @Column
    private String tipo;
    @Column
    private int disponible;
    
    @OneToMany (mappedBy = "productos", fetch=FetchType.EAGER)
    private List<Pedidos> pedidos;
    
    public List<Pedidos> getPedidos() {
        return pedidos;
    }
    
    public void setPedidos (List<Pedidos> pedidos) {
        this.pedidos = pedidos;
    }
    
    public Productos() {}

    public Productos(int id, float precio, String nombre, String tipo, 
                 int disponible) {
        this.id = id;
        this.precio = precio;
        this.nombre = nombre;
        this.tipo = tipo;
        this.disponible = disponible;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getDisponible() {
        return disponible;
    }

    public void setDisponible(int disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "PRODUCTOS [" + "ID: " + id + "; NOMBRE: " + nombre + "; PRECIO: " 
              + precio + "€; TIPO: " + tipo + "; DISPONIBLE: " + disponible + ']';
    }
    
}
