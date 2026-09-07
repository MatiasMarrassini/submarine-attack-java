package Modelo;

public class Submarino {
    private double posicionX;
    private double profundidad;
    private double ancho;
    private double alto;
    private int salud;
    private int vidas;


    private final double VELOCIDAD_MOVIMIENTO = 10.0;

    public Submarino(double posicionX, double profundidad, double ancho, double alto, int vidas) {
        this.posicionX = posicionX;
        this.profundidad = profundidad;
        this.ancho = ancho;
        this.alto = alto;
        this.salud = 100;
        this.vidas = vidas;
    }

    /***
     * Mueve el submarino hacia la izquierda.
     * Disminuye la posicion en X.
     */
    public void moverIzquierda() {
        double aux = (this.posicionX -= VELOCIDAD_MOVIMIENTO);

        this.posicionX = Math.max(0.0, aux);
    }


    /***
     * Mueve el submarino hacia la derecha.
     * Aumenta la posicion en X.
     */
    public void moverDerecha() {
        double aux = (this.posicionX += VELOCIDAD_MOVIMIENTO);
        this.posicionX = Math.min((800.00 - this.ancho), aux);
    }

    /***
     * Mueve el submarino hacia abajo.
     * Incrementa la profuncidad hasta maximo 800 metros.
     */
    public void moverAbajo() {
        double nuevaProfundidad = this.profundidad + VELOCIDAD_MOVIMIENTO;
        if (nuevaProfundidad <= 800) {
            this.profundidad = nuevaProfundidad;
        }
    }

    // RF03: Mover arriba disminuye la profundidad (mínimo 300 metros)
    /***
     * Mueve el submarino hacia arriba.
     * Disminuye la profundidad hasta minimo 300 metros.
     */
    public void moverArriba() {
        double nuevaProfundidad = this.profundidad - VELOCIDAD_MOVIMIENTO;
        if (nuevaProfundidad >= 300) {
            this.profundidad = nuevaProfundidad;
        }
    }

    /**
     * Calcula la distancia desde la explosión al punto más cercano
     * de su cuerpo y aplica el daño.
     * @param xExp posicion en X de la explosion.
     * @param yExp posicion en Y de la explosion.
     * @return los puntos a aplicar.
     */
    public int verificarDistExplosion(double xExp, double yExp) {
        double cercanoX = Math.max(this.posicionX, Math.min(xExp, this.posicionX + this.ancho));
        double cercanoY = Math.max(this.profundidad, Math.min(yExp, this.profundidad + this.alto));

        double difX = xExp - cercanoX;
        double difY = yExp - cercanoY;
        double distancia = Math.sqrt((difX * difX) + (difY * difY));

        if (distancia < 10) {
            this.vidas--;
            this.salud = 100;
            return 0;
        } else if (distancia >= 10 && distancia <= 50) {
            this.recibirDanio(50);
            return 0;
        } else if (distancia > 50 && distancia <= 100) {
            this.recibirDanio(30);
            return 10;
        } else {
            return 30;
        }
    }

    /***
     * Aplica el daño recibido.
     * En caso de que la salud llegue a 0, resta una vida.
     * @param cantidad cantidad de daño a recibir.
     */
    private void recibirDanio(int cantidad) {
        this.salud -= cantidad;
        if (this.salud <= 0) {
            this.vidas--;
            if (this.vidas > 0) {
                this.salud = 100;
            } else {
                this.salud = 0;
            }
        }
    }

    public void sumarVida() {
        this.vidas++;
    }

    /**
     * Verifica si una carga colisiona con el submarino.
     * @param xCarga posicion X de la carga.
     * @param yCarga posicion Y de la carga.
     */
    public boolean hayColision(double xCarga, double yCarga) {
        // Encontramos el punto más cercano del cuerpo del submarino a la carga
        double cercanoX = Math.max(this.posicionX, Math.min(xCarga, this.posicionX + this.ancho));
        double cercanoY = Math.max(this.profundidad, Math.min(yCarga, this.profundidad + this.alto));


        double difX = xCarga - cercanoX;
        double difY = yCarga - cercanoY;
        double distancia = Math.sqrt((difX * difX) + (difY * difY));

        // Evaluamos si hay colision con una distancia menor al radio de la bomba
        if (distancia < 6.0) {
            this.vidas--;

            // Validamos si el jugador todavía sigue en juego
            if (this.vidas > 0) {
                this.salud = 100;
            } else {
                this.salud = 0;
            }
            return true;
        }

        return false;
    }


    public double getPosicionX() { return posicionX; }
    public void setPosicionX(double posicionX) { this.posicionX = posicionX; }

    public double getProfundidad() { return profundidad; }
    public void setProfundidad(double profundidad) { this.profundidad = profundidad; }

    public double getAncho() { return ancho; }
    public double getAlto() { return alto; }
    public int getSalud() { return salud; }
    public int getVidas() { return vidas; }
    public void setSalud(int salud) {this.salud = salud;}
    public void setVidas(int vidas) {this.vidas = vidas;}
}