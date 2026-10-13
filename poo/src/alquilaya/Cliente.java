package alquilaya;

// Clase: objetos, atributos privados y getters/setters con validación (encapsulación)
public class Cliente {
    private String dni;       // nadie accede directamente
    private String nombre;
    private String email;

    // constructor
    public Cliente(String dni, String nombre, String email) {
        // Ejercicio 2: el DNI debe tener exactamente 9 caracteres
        if (dni == null || dni.length() != 9) {
            throw new IllegalArgumentException("DNI no válido: debe tener exactamente 9 caracteres");
        }
        this.dni = dni;
        this.nombre = nombre;
        setEmail(email);
    }

    public String getDni() { return dni; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email no válido");
        }
        this.email = email;
    }

    @Override
    public String toString() {
        return nombre + " (" + dni + ")";
    }
}
