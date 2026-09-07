package Vista;
import Controlador.*;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.CardLayout;

public class VentanaJuego extends JFrame {


    private Controlador controlador;
    private PanelJuego panelJuego;
    private Timer timerAnimacion;
    private PanelMenu panelMenu;
    private boolean enJuego = false;

    private CardLayout cardLayout;
    private JPanel contenedorPrincipal;


    public VentanaJuego() {

        this.controlador = Controlador.getInstance();

        // Configuramos la ventana
        this.setTitle("Submarine Attack");
        this.setSize(800, 900);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        // Configuramos el CardLayout y el "contenedor de pantallas"
        this.cardLayout = new CardLayout();
        this.contenedorPrincipal = new JPanel(cardLayout);

        this.panelMenu = new PanelMenu(this);
        this.panelJuego = new PanelJuego(controlador);

        // Los metemos en el "contenedor" poniendoles nombre para identificarlos
        this.contenedorPrincipal.add(panelMenu, "MENU");
        this.contenedorPrincipal.add(panelJuego, "JUEGO");

        // Agregamos el contenedor a la ventana
        this.add(contenedorPrincipal);

        // Mostramos el menú de entrada al arrancar
        this.cardLayout.show(contenedorPrincipal, "MENU");
        Sonido.iniciarMusica("recursos/Sonido/menu.wav", -20.0f);

        // configuramos los eventos en cada tecla
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(!enJuego) return;

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    pausa();
                    return;
                }

                if (controlador.isPausa()) return;
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_W:
                            controlador.moverSubmarinoArriba();
                            break;
                        case KeyEvent.VK_S:
                            controlador.moverSubmarinoAbajo();
                            break;
                        case KeyEvent.VK_A:
                            controlador.moverSubmarinoIzquierda();
                            break;
                        case KeyEvent.VK_D:
                            controlador.moverSubmarinoDerecha();
                            break;
                    }
            }
        });

        // CONFIGURACION DEL TIMER
        this.timerAnimacion = new Timer(30, e -> {

            if (!this.enJuego)  return;


                controlador.actualizar();

                for (int i=0; i< controlador.getCantCargasActivas(); i++) {
                if((controlador.getPosYCargaActiva(i) + 8) >= controlador.getPosExpCarga(i)){
                    Sonido.reproducirEfecto("recursos/Sonido/explosion.wav", -40.0f);
                }}

            if (controlador.pasoNivel()){
                Sonido.reproducirEfecto("recursos/Sonido/nivel.wav", -25.0F);
            }

                panelJuego.repaint();


                // Verificamos si el jugador perdio
                if (controlador.isGameOver()) {
                    Sonido.detenerMusica();
                    Sonido.reproducirEfecto("recursos/Sonido/final.wav", -25.0F);
                    timerAnimacion.stop();


                    // mostramos mensaje de GAMER OVER
                    javax.swing.JOptionPane.showMessageDialog(
                            this,
                            "¡GAME OVER!\nPuntaje final: " + controlador.getPuntaje() + "\nNivel alcanzado: "
                            + controlador.getNivelActual(),
                            "Fin de la Partida",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE
                    );
                    //volvemos al menú de inicio
                    mostrarPantallaMenu();
                }

        });
    }

    /**
     * Activa la pantalla de juego y inicia el TIMER
     */
    public void mostrarPantallaJuego() {
        this.enJuego = true;
        controlador.iniciarPartida(); // iniciamos nuevamente la partida para resetear las vidas y los puntos
        cardLayout.show(contenedorPrincipal, "JUEGO"); // Cambia el panel visible
        this.requestFocusInWindow(); // Obliga a la ventana a escuchar el teclado en el panel de juego
        timerAnimacion.start();

        Sonido.iniciarMusica("recursos/Sonido/sonar.wav", -30.0f);

    }

    /**
     * Activa la pantalla del menu
     */
    public void mostrarPantallaMenu() {
        this.enJuego = false;

        cardLayout.show(contenedorPrincipal, "MENU"); // Volvemos a la carta de inicio

        Sonido.iniciarMusica("recursos/Sonido/menu.wav", -20.0f);
    }

    // main para ejecutar la ventana
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            VentanaJuego ventana = new VentanaJuego();
            ventana.setVisible(true);
        });
    }

    public void pausa(){
        controlador.pausa();
        if (timerAnimacion.isRunning()) {

            // detenemos la musica del juego, y reproducimos el efecto de pausa
            Sonido.detenerMusica();
            Sonido.reproducirEfecto("recursos/Sonido/pausa.wav", -10.0f);
            // --- SE PAUSA EL JUEGO ---
            timerAnimacion.stop(); // Congelamos el juego
            panelJuego.repaint();

            // reproducimos musica de fondo de pausa
            Sonido.iniciarMusica("recursos/Sonido/pausaMusica.wav", -20.0f);

        } else {

            // Pausamos la música de fondo
            Sonido.detenerMusica();
            // reproducimos sonido de pausa
            Sonido.reproducirEfecto("recursos/Sonido/pausa.wav", -10.0f);
            // --- SE REANUDA EL JUEGO ---
            timerAnimacion.start();

            // Reanudamos la música del juego
            Sonido.iniciarMusica("recursos/Sonido/sonar.wav", -20.0f);
        }

    }
}
