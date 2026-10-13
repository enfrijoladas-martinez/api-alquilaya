package alquilaya;

public class Furgoneta extends Vehiculo {
    private static final double SUPLEMENTO_CARGA = 15;
    private double capacidadCargaKg;

    public Furgoneta(String matricula, String marca, String modelo, double precioBaseDia, double capacidadCargaKg) {
        super(matricula, marca, modelo, precioBaseDia);
        this.capacidadCargaKg = capacidadCargaKg;
    }

    // Base × días, +15 €/día si carga > 1000 kg
    @Override
    public double calcularPrecioAlquiler(int dias) {
        validarDias(dias);
        double precioDia = getPrecioBaseDia();
        if (capacidadCargaKg > 1000) {
            precioDia += SUPLEMENTO_CARGA;
        }
        return precioDia * dias;
    }

    @Override
    public String getTipo() { return "Furgoneta"; }

    public double getCapacidadCargaKg() { return capacidadCargaKg; }
}
