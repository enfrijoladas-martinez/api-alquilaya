package alquilaya;

// Herencia: Coche ES UN Vehiculo
public class Coche extends Vehiculo {
    private static final double DESCUENTO_SEMANAL = 0.10;
    private int numeroPuertas;

    public Coche(String matricula, String marca, String modelo, double precioBaseDia, int numeroPuertas) {
        super(matricula, marca, modelo, precioBaseDia);
        this.numeroPuertas = numeroPuertas;
    }

    // Base × días, −10 % a partir de 7 días
    @Override
    public double calcularPrecioAlquiler(int dias) {
        validarDias(dias);
        double precio = getPrecioBaseDia() * dias;
        if (dias >= 7) {
            precio = precio * (1 - DESCUENTO_SEMANAL);
        }
        return precio;
    }

    @Override
    public String getTipo() { return "Coche"; }

    public int getNumeroPuertas() { return numeroPuertas; }
}
