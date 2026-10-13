package alquilaya;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Polimorfismo: la Flota solo conoce el tipo común Vehiculo
public class Flota {
    private List<Vehiculo> vehiculos = new ArrayList<>();

    public void agregar(Vehiculo v) {
        vehiculos.add(v);
    }

    public void mostrarPresupuestos(int dias) {
        for (Vehiculo v : vehiculos) {
            double p = v.calcularPrecioAlquiler(dias);
            System.out.println(String.format(Locale.US, "%-16s %s  ->  %.2f €", v.getTipo(), v.getMatricula(), p));
        }
    }

    public List<Vehiculo> getDisponibles() {
        List<Vehiculo> disponibles = new ArrayList<>();
        for (Vehiculo v : vehiculos) {
            if (v.estaDisponible()) {
                disponibles.add(v);
            }
        }
        return disponibles;
    }
}
