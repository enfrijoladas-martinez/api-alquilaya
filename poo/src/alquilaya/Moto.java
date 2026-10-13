package alquilaya;

public class Moto extends Vehiculo {
    private static final double SEGURO_DIA = 3;
    private static final double SUPLEMENTO_CILINDRADA = 5;
    private int cilindrada;

    public Moto(String matricula, String marca, String modelo, double precioBaseDia, int cilindrada) {
        super(matricula, marca, modelo, precioBaseDia);
        this.cilindrada = cilindrada;
    }

    // (Base + 3 € seguro) × días, +5 €/día si > 500 cc
    @Override
    public double calcularPrecioAlquiler(int dias) {
        validarDias(dias);
        double precioDia = getPrecioBaseDia() + SEGURO_DIA;
        if (cilindrada > 500) {
            precioDia += SUPLEMENTO_CILINDRADA;
        }
        return precioDia * dias;
    }

    @Override
    public String getTipo() { return "Moto"; }

    public int getCilindrada() { return cilindrada; }
}
