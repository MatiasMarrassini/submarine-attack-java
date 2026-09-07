package Vista;
import javax.swing.*;
import java.awt.*;

import Controlador.Controlador;


public class PanelJuego extends JPanel {
    private Controlador controlador;
    private Image imgSubmarino;
    private Image imgBarco;
    private Image imgCarga;
    private Image imgFondo;
    private Image imgCielo;
    private Image imgExp;
    private int framesMensajeNivel;


    public PanelJuego(Controlador controlador) {
        this.controlador = controlador;

        this.imgSubmarino = new ImageIcon("recursos/Submarino.png").getImage();
        this.imgBarco = new ImageIcon("recursos/Barco.png").getImage();
        this.imgCarga = new ImageIcon("recursos/Carga.png").getImage();
        this.imgFondo = new ImageIcon("recursos/Fondo.jpg").getImage();
        this.imgCielo = new ImageIcon("recursos/Cielo.jpg").getImage();
        this.imgExp = new ImageIcon("recursos/explosion.png").getImage();
    }


    /**
     * A este metodo lo llama Swing automáticamente cada vez que la pantalla necesita redibujarse.
     * El objeto Graphics (g) es con lo que "dibujamos".
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Limpia la pantalla antes de volver a dibujar

        if (imgFondo != null) {
            g.drawImage(imgFondo, 0, 0, this.getWidth(), this.getHeight(), this);
        }

        // --- DIBUJO DEL CIELO Y EL MAR ---
        // CIELO
        g.drawImage(imgCielo, 0, 0, this.getWidth(), 100, this);

        // Linea azul que representa el "mar", por donde se desplazan los barcos (el agua empieza en Y=100)
        g.setColor(Color.BLUE);
        g.drawLine(0, 100, getWidth(), 100);

        // --- DIBUJO DE LOS BARCOS  ---

        for (int i=0; i< controlador.getCantBarcosActivos(); i++) {
            g.drawImage(imgBarco,(int) controlador.getPosXBarco(i), 70, 60, 30, this);

            //g.setColor(Color.BLACK);
            //g.drawString("ID: " + (int)controlador.getIdBarco(i), (int) controlador.getPosXBarco(i), 65);

        }

        // --- DIBUJAMOS LAS CARGAS DE PROFUNDIDAD ---
        // si la carga esta por explotar ponemos imagen de explosion

        for (int i=0; i< controlador.getCantCargasActivas(); i++) {
            if((controlador.getPosYCargaActiva(i) + 8) >= controlador.getPosExpCarga(i)){
                g.drawImage(imgExp,(int) controlador.getPosXCargaActiva(i), (int) controlador.getPosYCargaActiva(i), 30, 30, this);
            }else {

                g.drawImage(imgCarga, (int) controlador.getPosXCargaActiva(i), (int) controlador.getPosYCargaActiva(i), 12, 12, this);
            }

        }

        // --- DIBUJAMOS EL SUBMARINO ---
        g.drawImage(imgSubmarino,(int) controlador.getPosXSub(), (int) controlador.getProfSub(),
                (int) controlador.getAnchoSub(), (int) controlador.getAltoSub(), this);


        // --- DIBUJAMOS MENSAJE SIGUIENTE NIVEL ---
        if (controlador.pasoNivel()) {
            this.framesMensajeNivel = 60;
        }

        if (this.framesMensajeNivel > 0) {

            String texto = "¡ SIGUIENTE NIVEL !";
            g.setFont(new Font("Impact", Font.BOLD, 40));

            // Obtenemos el centro de la pantalla
            int centroX = this.getWidth() / 2;
            int centroY = this.getHeight() / 2;

            int x = centroX - 150;
            int y = centroY;

            g.setColor(new Color(0, 0, 0, 160));
            g.fillRect(0, y - 40, this.getWidth(), 65);


            g.setColor(Color.YELLOW);
            g.drawString(texto, x, y);

            // Restamos un frame en cada iteracion
            this.framesMensajeNivel--;
        }
        // CARTEL PAUSA
        if (controlador.isPausa()){
            String texto = "PAUSA";
            g.setFont(new Font("Impact", Font.BOLD, 40));

            // Obtenemos el centro de la pantalla
            int centroX = this.getWidth() / 2;
            int centroY = this.getHeight() / 2;

            int x = centroX - 40;
            int y = centroY;

            g.setColor(new Color(0, 0, 0, 160));
            g.fillRect(0, y - 40, this.getWidth(), 65);


            g.setColor(Color.YELLOW);
            g.drawString(texto, x, y);


        }

        // --- DIBUJAR LA INTERFAZ DE TEXTO (HUD) ---
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Puntaje: " + controlador.getPuntaje(), 20, 30);
        g.drawString("Nivel: " + controlador.getNivelActual(), 150, 30);
        g.drawString("Barcos Restantes: " + controlador.getBarcosRestantes(), 250, 30);
        g.drawString("Vidas: " + controlador.getVidasSub(), 450, 30);
        g.drawString("Salud: " + (int) controlador.getSaludSub() + "%", 550, 30);
    }


}
