package Modelo;


import java.util.Random;

public class BarcoEnemigo {
    private int id;
    private double posicionX;
    private Direccion direccion;
    private double velocidad;
    private int destino;

    // Ancho de pantalla de referencia para determinar los bordes
    private final double ANCHO_PANTALLA = 800.0;

    /**
     * Constructor del Barco.
     * @param id Identificador del barco.
     * @param velocidadNivel Velocidad base del barco.
     */
    public BarcoEnemigo(int id, double velocidadNivel) {
        this.id = id;
        this.velocidad = velocidadNivel;

        // RF07: Determinar de manera aleatoria si inicia desde la izquierda o la derecha
        Random random = new Random();
        if (random.nextBoolean()) {
            // Inicia en la izquierda
            this.posicionX = 0.0;
            this.direccion = Direccion.DERECHA; // Se mueve hacia la derecha
            this.destino = (int) ANCHO_PANTALLA;
        } else {
            // Inicia en la derecha
            this.posicionX = ANCHO_PANTALLA;
            this.direccion = Direccion.IZQUIERDA; // Se mueve hacia la izquierda
            this.destino = 0;
        }
    }

    /**
     * Actualiza la posición horizontal del barco según su dirección y velocidad.
     */
    public void desplazarse() {
        if (this.direccion == Direccion.DERECHA) {
            this.posicionX += this.velocidad;
        } else {
            this.posicionX -= this.velocidad;
        }
    }

    /**
     * El barco lanza una carga de profundidad.
     * @return la posicion en la que lanza la carga.
     */
    public double lanzarCarga() {
        return this.posicionX;
    }

    /**
     * Verifica si el barco ya completó su recorrido cruzando el destino asignado.
     * @return true si llegó o pasó su destino, false de lo contrario.
     */
    public boolean llegoDestino() {
        if (this.direccion == Direccion.DERECHA) {
            return this.posicionX >= this.destino;
        } else {
            return this.posicionX <= this.destino;
        }
    }


    public int getId() { return id; }
    public double getPosicionX() { return posicionX; }
    public Direccion getDireccion() { return direccion; }
    public double getVelocidad() { return velocidad; }
    public int getDestino() { return destino; }


}
