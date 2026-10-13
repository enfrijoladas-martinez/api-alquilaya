package alquilaya;

public class Main {
    public static void main(String[] args) {
        Flota flota = new Flota();
        flota.agregar(new Coche("1234ABC", "Seat", "León", 40, 5));
        flota.agregar(new Moto("5678DEF", "Honda", "CB650R", 30, 650));
        flota.agregar(new Furgoneta("9012GHI", "Renault", "Master", 60, 1200));
        flota.agregar(new CocheElectrico("3456JKL", "Tesla", "Model 3", 70, 5, 500));

        System.out.println("=== Presupuestos (7 días) ===");
        flota.mostrarPresupuestos(7);

        // ---------- Ejercicio 1: nueva subclase Bicicleta ----------
        System.out.println("\n=== Ejercicio 1: se añade Bicicleta sin tocar Flota ===");
        flota.agregar(new Bicicleta("BICI0001", "Orbea", "Gain", 10, true));
        flota.mostrarPresupuestos(7);

        // ---------- Ejercicio 2: validación del DNI ----------
        System.out.println("\n=== Ejercicio 2: DNI de 9 caracteres ===");
        Cliente ana = new Cliente("12345678A", "Ana", "ana@correo.es");
        System.out.println("Cliente creado: " + ana);
        try {
            new Cliente("123", "Luis", "luis@correo.es");
            System.out.println("ERROR: no se lanzó la excepción");
        } catch (IllegalArgumentException e) {
            System.out.println("Excepción esperada -> " + e.getMessage());
        }

        // ---------- Ejercicio 3: errores provocados + try-catch ----------
        System.out.println("\n=== Ejercicio 3: errores provocados ===");
        Vehiculo coche = new Coche("7777XYZ", "Kia", "Rio", 35, 5);

        // a) días inválidos -> validarDias() lanza IllegalArgumentException
        try {
            coche.calcularPrecioAlquiler(0);
        } catch (IllegalArgumentException e) {
            System.out.println("a) " + e.getMessage());
        }

        // b) alquilar dos veces el mismo vehículo -> IllegalStateException
        try {
            coche.alquilar(ana);
            coche.alquilar(ana);
        } catch (IllegalStateException e) {
            System.out.println("b) " + e.getMessage());
        } finally {
            coche.devolver();
            System.out.println("   (finally) vehículo devuelto, disponible = " + coche.estaDisponible());
        }

        // c) email inválido -> el setter protege el estado
        try {
            ana.setEmail("correo-sin-arroba");
        } catch (IllegalArgumentException e) {
            System.out.println("c) " + e.getMessage() + " (el email sigue siendo " + ana.getEmail() + ")");
        }

        // d) precio base negativo en el constructor
        try {
            new Moto("0000AAA", "Yamaha", "MT-07", -10, 689);
        } catch (IllegalArgumentException e) {
            System.out.println("d) " + e.getMessage());
        }

        System.out.println("\nEl programa sigue ejecutándose: los errores fueron controlados.");
    }
}
