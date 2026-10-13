package alquilaya;

// Clase abstracta: no se puede instanciar (no existe un "vehículo" genérico)
// y obliga a cada subclase a definir cómo calcula su precio
public abstract class Vehiculo implements Alquilable {
    private String matricula;
    private String marca;
    private String modelo;
    private double precioBaseDia;
    private Cliente clienteActual;   // null = disponible

    public Vehiculo(String matricula, String marca, String modelo, double precioBaseDia) {
        if (precioBaseDia <= 0) {
            throw new IllegalArgumentException("El precio base debe ser mayor que 0");
        }
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.precioBaseDia = precioBaseDia;
    }

    // Método abstracto: cada subclase pone su propia regla
    public abstract double calcularPrecioAlquiler(int dias);

    public abstract String getTipo();

    // protected: utilidad para las subclases que no debe usarse desde fuera
    protected void validarDias(int dias) {
        if (dias <= 0) {
            throw new IllegalArgumentException("Los días de alquiler deben ser mayores que 0");
        }
    }

    @Override
    public void alquilar(Cliente cliente) {
        if (!estaDisponible()) {
            throw new IllegalStateException("El vehículo " + matricula + " ya está alquilado");
        }
        this.clienteActual = cliente;
    }

    @Override
    public void devolver() {
        this.clienteActual = null;
    }

    @Override
    public boolean estaDisponible() {
        return clienteActual == null;
    }

    public String getMatricula() { return matricula; }

    public String getMarca() { return marca; }

    public String getModelo() { return modelo; }

    public double getPrecioBaseDia() { return precioBaseDia; }

    public Cliente getClienteActual() { return clienteActual; }
}
