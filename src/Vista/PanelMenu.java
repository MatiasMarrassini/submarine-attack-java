package Vista;
import javax.swing.*;
import java.awt.*;

public class PanelMenu extends JPanel{
    private Image imgFondo;
    // recibe la ventana en la que va a "estar"
    public PanelMenu(VentanaJuego ventana) {
        this.imgFondo = new ImageIcon("recursos/Menu.jpg").getImage();
        this.setOpaque(true);

        // Usamos GridBagLayout para que quede centrado
        this.setLayout(new GridBagLayout());


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20); // Margen entre elementos
        gbc.gridx = 0;

        // TÍTULO Submarine Attack
        JLabel titulo = new JLabel("SUBMARINE ATTACK", SwingConstants.CENTER);
        titulo.setForeground(Color.RED);
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        gbc.gridy = 1;
        this.add(titulo, gbc);

        // TITULO Grupo 1
        JLabel subTitulo = new JLabel("Grupo 1", SwingConstants.CENTER);
        subTitulo.setForeground(Color.RED);
        subTitulo.setFont(new Font("Arial", Font.BOLD, 36));
        gbc.gridy = 0;
        this.add(subTitulo, gbc);


        // BOTÓN DE INICIAR PARTIDA
        JButton botonIniciar = new JButton("EMPEZAR PARTIDA");
        botonIniciar.setFont(new Font("Arial", Font.BOLD, 18));
        botonIniciar.setBackground(Color.white);
        botonIniciar.setForeground(Color.BLACK);
        botonIniciar.setFocusable(false); // saca el "marco negro"

        // Acción del botón: Le dice a la ventana que arranque el juego
        botonIniciar.addActionListener(e -> {
            ventana.mostrarPantallaJuego();
        });

        gbc.gridy = 2;
        this.add(botonIniciar, gbc);
    }
    @Override
    protected void paintComponent(Graphics g) {
        // Llamamos al super para limpiar la pantalla
        super.paintComponent(g);

        // aplicamos la imagen estirada en todo el alto y ancho de la ventana
        if (imgFondo != null) {
            g.drawImage(imgFondo, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}
