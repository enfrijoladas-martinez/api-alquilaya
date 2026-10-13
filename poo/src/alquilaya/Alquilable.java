package alquilaya;

// Interfaz: dice QUÉ se sabe hacer, no CÓMO
public interface Alquilable {
    void alquilar(Cliente cliente);
    void devolver();
    boolean estaDisponible();
}
