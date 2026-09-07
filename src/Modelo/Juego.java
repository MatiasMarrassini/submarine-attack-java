package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Juego {
    private int puntaje;
    private int nivelActual;
    private int barcosRestantes;
    private List<BarcoEnemigo> listaBarcosActivos;
    private List<CargaDeProfundidad> listaCargasActivas;
    private Submarino submarino;
    private boolean pasoNivel = false;
    private boolean pausa = false;

    private int idBarcoContador;

    private final double VELOCIDAD_BARCO_BASE = 3.0;
    private final double VELOCIDAD_CARGA_BASE = 4.0;


    public Juego() {
        this.listaBarcosActivos = new ArrayList<>();
        this.listaCargasActivas = new ArrayList<>();
        // Submarino en una posición inicial por defecto (X=350, Y=400, Ancho=100, Alto=40, Vidas=3)
        this.submarino = new Submarino(350.0, 400.0, 100.0, 40.0, 3);
        iniciarPartida();
    }

    /**
     * Inicializa o reinicia los valores para una nueva partida.
     */
    public void iniciarPartida() {
        this.puntaje = 0;
        this.nivelActual = 1;
        this.barcosRestantes = 12;
        this.idBarcoContador = 1;
        this.listaBarcosActivos.clear();
        this.listaCargasActivas.clear();

        if (this.submarino != null){
            this.submarino.setVidas(3);
            this.submarino.setSalud(100);
            this.submarino.setPosicionX(350.0);
            this.submarino.setProfundidad(400.0);
        }

        actualizarBarcosEnemigos();
    }

    public void pausa(){
        if (this.pausa){this.pausa = false;}else{
            this.pausa = true;
        }
    }

    /**
     * Controla que haya hasta 3 barcos simultáneos en pantalla
     * siempre y cuando queden barcos disponibles de los 12 por nivel.
     */
    public void actualizarBarcosEnemigos() {
        while (listaBarcosActivos.size() < 3 && this.barcosRestantes > 0) {
            // Si la pantalla está vacía, metemos el primero sin problemas
            if (listaBarcosActivos.isEmpty()) {
                agregarBarco();
            } else {
                // Si ya hay barcos, miramos el ÚLTIMO que se agregó (el que está más cerca de la zona de salida)
                BarcoEnemigo ultimoBarco = listaBarcosActivos.get(listaBarcosActivos.size() - 1);

                // Si el último barco avanza por la izquierda (X aumenta), esperamos a que se aleje al menos 180 píxeles del borde izquierdo.
                if (ultimoBarco.getPosicionX() > 180.0 & ultimoBarco.getDireccion()== Direccion.DERECHA) {
                    agregarBarco();
                }
                // Si el último barco avanza por la Derecha (X disminuye), esperamos a que se aleje al menos 180 píxeles del borde derecho.
                if (ultimoBarco.getPosicionX() < 620.0 & ultimoBarco.getDireccion()== Direccion.IZQUIERDA) {
                    agregarBarco();
                }else {
                    // Si el último barco todavía no dejó suficiente espacio, frenamos el bucle while
                    break;
                }
            }
        }
        if (this.barcosRestantes == 0){this.idBarcoContador = 1;}
    }

    /**
     * Agrega un barco calculando su velocidad según el nivel.
     * La velocidad aumenta un 20% por cada nivel arriba de 1.
     */
    public void agregarBarco() {
        double multiplicadorDificultad = Math.pow(1.2, this.nivelActual - 1);
        double velocidadActualBarco = VELOCIDAD_BARCO_BASE * multiplicadorDificultad;

        BarcoEnemigo nuevoBarco = new BarcoEnemigo(idBarcoContador++, velocidadActualBarco);
        this.listaBarcosActivos.add(nuevoBarco);
        this.barcosRestantes--;
    }

    /**
     * Agrega una carga a la lista.
     * La velocidad de caida aumenta un 20% por cada nivel arriba de 1.
     * @param posX posicion en X del barco que lanzo la carga.
     */
    public void agregarCarga(double posX) {
        double multiplicadorDificultad = Math.pow(1.2, this.nivelActual - 1);
        double velocidadActualCarga = VELOCIDAD_CARGA_BASE * multiplicadorDificultad;

        CargaDeProfundidad nuevaCarga = new CargaDeProfundidad(posX, velocidadActualCarga);
        this.listaCargasActivas.add(nuevaCarga);
    }


    /**
     * Recorre la lista de cargas y si alguna exploto, el Submarino
     * calcula la distancia para luego actualizar el puntaje global.
     */
    public void verificarExplosion() {
        for (CargaDeProfundidad carga : listaCargasActivas) {
            if (carga.isExploto() && !carga.isColision()) {
                int puntosObtenidos = submarino.verificarDistExplosion(carga.getPosicionX(), carga.getPosicionY());

                sumarPuntaje(puntosObtenidos);
            }
        }
    }

    /**
     * Aumenta los puntos y evalúa la condición de vida extra cada 500 puntos.
     * @param puntos los puntos a aplicar
     */
    public void sumarPuntaje(int puntos) {
        if (puntos <= 0) return;

        int puntajeAnterior = this.puntaje;
        this.puntaje += puntos;

        // Comparamos la división entera antes y después de sumar para ver si cruzamos la barrera de los 500
        if ((this.puntaje / 500) > (puntajeAnterior / 500)) {
            this.submarino.sumarVida();
        }
    }

    /**
     * Pasa de nivel si ya no quedan barcos ni cargas activas.
     */
    public void pasarNivel() {
        if (this.barcosRestantes == 0 && this.listaBarcosActivos.isEmpty() && this.listaCargasActivas.isEmpty()) {
            this.nivelActual++;
            sumarPuntaje(200); // RF20: Bonificación de 200 puntos por nivel completado
            this.barcosRestantes = 12; // Se resetea la serie para el nuevo nivel
            this.pasoNivel = true;
            actualizarBarcosEnemigos();
        }
    }

    /**
     * "Tick" central del juego.
     */
    public void actualizar() {
        if (this.pasoNivel){this.pasoNivel = false;}
        // Desplazar barcos y eliminar los que llegaron a su destino.
        for (int i = listaBarcosActivos.size() - 1; i >= 0; i--) {
            BarcoEnemigo b = listaBarcosActivos.get(i);
            b.desplazarse(); // RF04

            // Disparo aleatorio (1% de probabilidad por frame por barco multiplicado por el nivel)
            if (Math.random() < (0.01 * this.nivelActual)) {
                agregarCarga(b.lanzarCarga());
            }

            if (b.llegoDestino()) {
                listaBarcosActivos.remove(i);
            }
        }

        // Rellena la pantalla con los barcos restantes
        actualizarBarcosEnemigos();

        // Desciende cada carga
        for (CargaDeProfundidad carga : listaCargasActivas) {
            carga.descenderYExplotar();

            // Si la carga colisiona contra el cuerpo del submarino:
            if (!carga.isExploto() && this.submarino.hayColision(carga.getPosicionX(), carga.getPosicionY())) {
                carga.forzarExplosion();
            }

        }

        verificarExplosion();

        // Elimina de la lista las cargas que ya explotaron
        listaCargasActivas.removeIf(CargaDeProfundidad::isExploto);

        // Evaluar si se cumplen las condiciones para subir de nivel
        pasarNivel();
    }

    /**
     * Evalúa si el juego ha terminado.
     * @return true si el submarino ya no tiene vidas, false si sigue jugando.
     */
    public boolean isGameOver() {
        return this.submarino.getVidas() <= 0;
    }

    public void moverSubmarinoArriba(){this.submarino.moverArriba();}

    public void moverSubmarinoAbajo(){this.submarino.moverAbajo();}

    public void moverSubmarinoDerecha(){this.submarino.moverDerecha();}

    public void moverSubmarinoIzquierda(){this.submarino.moverIzquierda();}



    public int getPuntaje() {
        return puntaje;
    }

    public int getNivelActual() {
        return nivelActual;
    }

    public int getBarcosRestantes() {
        return barcosRestantes;
    }

    public List<BarcoEnemigo> getListaBarcosActivos() {
        return listaBarcosActivos;
    }

    public List<CargaDeProfundidad> getListaCargasActivas() {
        return listaCargasActivas;
    }

    public Submarino getSubmarino() {
        return submarino;
    }

    public boolean isPasoNivel() {return pasoNivel;}

    public boolean isPausa(){return this.pausa;}
}