package alquilaya;

// Interfaz: contrato que solo cumplen los vehículos con batería
public interface Electrico {
    int NIVEL_MAXIMO_BATERIA = 100;   // constante (public static final implícito)

    void cargarBateria();
    int getNivelBateria();
}
