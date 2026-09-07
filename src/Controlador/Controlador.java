package Controlador;
import Modelo.Juego;

public class Controlador {
    private static Controlador instance;

    private Juego juego;

    private Controlador() {
        this.juego = new Juego();
    }

    public static Controlador getInstance() {
        if (instance == null) {
            instance = new Controlador();
        }
        return instance;
    }


    public void moverSubmarinoArriba() {
        this.juego.getSubmarino().moverArriba();
    }

    public void moverSubmarinoAbajo() {
        this.juego.getSubmarino().moverAbajo();
    }

    public void moverSubmarinoDerecha() {
        this.juego.getSubmarino().moverDerecha();
    }

    public void moverSubmarinoIzquierda() {
        this.juego.getSubmarino().moverIzquierda();
    }

    public void iniciarPartida(){this.juego.iniciarPartida();}

    public int getNivelActual(){return this.juego.getNivelActual();}

    public int getBarcosRestantes(){return this.juego.getBarcosRestantes();}

    public int getVidasSub(){return this.juego.getSubmarino().getVidas();}

    public double getSaludSub(){return this.juego.getSubmarino().getSalud();}

    public double getPosXBarco(int pos){return this.juego.getListaBarcosActivos().get(pos).getPosicionX();}

    public double getIdBarco(int pos){return this.juego.getListaBarcosActivos().get(pos).getId();}

    public void actualizar(){this.juego.actualizar();}

    public double getProfSub(){return this.juego.getSubmarino().getProfundidad();}

    public double getPosXSub(){return this.juego.getSubmarino().getPosicionX();}

    public double getAltoSub(){return this.juego.getSubmarino().getAlto();}

    public double getAnchoSub(){return this.juego.getSubmarino().getAncho();}

    public int getPuntaje(){return this.juego.getPuntaje();}

    public double getPosYCargaActiva(int pos){return this.juego.getListaCargasActivas().get(pos).getPosicionY();}

    public double getPosXCargaActiva(int pos){return this.juego.getListaCargasActivas().get(pos).getPosicionX();}

    public boolean isGameOver(){return this.juego.isGameOver();}

    public double getPosExpCarga(int pos){return this.juego.getListaCargasActivas().get(pos).getProfundidadDetonacion();}

    public int getCantBarcosActivos(){return this.juego.getListaBarcosActivos().size();}

    public int getCantCargasActivas(){return this.juego.getListaCargasActivas().size();}

    public boolean pasoNivel(){return this.juego.isPasoNivel();}

    public boolean isPausa(){return this.juego.isPausa();}

    public void pausa (){this.juego.pausa();}
}