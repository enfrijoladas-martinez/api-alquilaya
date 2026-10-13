package alquilaya;

// Ejercicio 1: nueva subclase. Flota no se modifica para poder usarla.
public class Bicicleta extends Vehiculo {
    private static final double SUPLEMENTO_ELECTRICA = 2;
    private boolean electrica;

    public Bicicleta(String matricula, String marca, String modelo, double precioBaseDia, boolean electrica) {
        super(matricula, marca, modelo, precioBaseDia);
        this.electrica = electrica;
    }

    // Base × días, +2 €/día si es eléctrica
    @Override
    public double calcularPrecioAlquiler(int dias) {
        validarDias(dias);
        double precioDia = getPrecioBaseDia();
        if (electrica) {
            precioDia += SUPLEMENTO_ELECTRICA;
        }
        return precioDia * dias;
    }

    @Override
    public String getTipo() { return "Bicicleta"; }

    public boolean isElectrica() { return electrica; }
}
