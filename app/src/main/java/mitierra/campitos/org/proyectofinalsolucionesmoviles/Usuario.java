package mitierra.campitos.org.proyectofinalsolucionesmoviles;

/**
 * Created by campitos on 4/14/15.
 */
public class Usuario {
    Integer idUsuario;
    String nombre;
    int edad;
    float Sueldo;

    public Usuario() {
    }

    public Usuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public float getSueldo() {
        return Sueldo;
    }

    public void setSueldo(float sueldo) {
        Sueldo = sueldo;
    }
}
