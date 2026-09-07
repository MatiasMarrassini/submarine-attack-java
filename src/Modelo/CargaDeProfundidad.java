package Modelo;

import java.util.Random;

public class CargaDeProfundidad {
    private double posicionX;
    private double posicionY;
    private double profundidadDetonacion;
    private double velocidadCaida;
    private boolean exploto;
    private boolean colision = false;



    /**
     * Constructor de la carga.
     * @param posX posicion X del barco.
     * @param velocidadNivel Velocidad base de la carga.
     */
    public CargaDeProfundidad(double posX, double velocidadNivel) {
        this.posicionX = posX;
        this.posicionY = 100.0; // Los barcos transitan en superficie (profundidad cero)
        this.velocidadCaida = velocidadNivel;
        this.exploto = false;

        // profundidad de detonación aleatoria entre 300 y 700 metros
        Random random = new Random();
        this.profundidadDetonacion = 300 + random.nextInt(401);
    }

    /**
     * La carga desciende verticalmente.
     * Si toca o supera la profundidad objetivo, detona.
     */
    public void descenderYExplotar() {
        if (!exploto) {
            this.posicionY += this.velocidadCaida;


            if (this.posicionY >= this.profundidadDetonacion) {
                this.exploto = true;
            }
        }
    }

    public void forzarExplosion (){
        this.exploto = true;
        this.colision = true;
    }


    public double getPosicionX() { return posicionX; }
    public double getPosicionY() { return posicionY; }
    public double getProfundidadDetonacion() { return profundidadDetonacion; }
    public boolean isExploto() { return exploto; }
    public boolean isColision() {return colision; }
}
