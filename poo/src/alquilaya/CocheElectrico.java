package alquilaya;

// Herencia multinivel (Vehiculo -> Coche -> CocheElectrico) + interfaz Electrico
public class CocheElectrico extends Coche implements Electrico {
    private static final double DESCUENTO_ECOLOGICO = 0.15;
    private int nivelBateria;
    private int autonomiaMaximaKm;

    public CocheElectrico(String matricula, String marca, String modelo, double precioBaseDia,
                          int numeroPuertas, int autonomiaMaximaKm) {
        super(matricula, marca, modelo, precioBaseDia, numeroPuertas);
        this.autonomiaMaximaKm = autonomiaMaximaKm;
        this.nivelBateria = NIVEL_MAXIMO_BATERIA;
    }

    @Override
    public double calcularPrecioAlquiler(int dias) {
        // Reutiliza la regla de Coche...
        double precioCoche = super.calcularPrecioAlquiler(dias);
        // ...y añade la suya
        return precioCoche * (1 - DESCUENTO_ECOLOGICO);
    }

    @Override
    public String getTipo() { return "Coche eléctrico"; }

    @Override
    public void cargarBateria() {
        nivelBateria = NIVEL_MAXIMO_BATERIA;
    }

    @Override
    public int getNivelBateria() { return nivelBateria; }

    public int getAutonomiaMaximaKm() { return autonomiaMaximaKm; }
}
