/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosPresentacion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author nafbr
 */
public class RepresentacionJugador extends JPanel{
    private final String idJugador;
    private final JLabel lblPuntos;
    private final JLabel lblNombre;
    private final JPanel miniGrid;
    private final JPanel[][] celdas = new JPanel[4][4];
    private final boolean[][] marcadas = new boolean[4][4];

    public RepresentacionJugador(String idJugador, String nombre) {
        this.idJugador = idJugador;
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        lblPuntos = new JLabel("0 Puntos", SwingConstants.CENTER);
        lblPuntos.setAlignmentX(CENTER_ALIGNMENT);
        lblPuntos.setFont(new Font("SansSerif", Font.BOLD, 12));

        JPanel fila = new JPanel();
        fila.setOpaque(false);
        fila.setLayout(new BoxLayout(fila, BoxLayout.X_AXIS));

        JPanel avatar = new JPanel();
        avatar.setPreferredSize(new Dimension(56, 56));
        avatar.setMaximumSize(new Dimension(56, 56));
        avatar.setBackground(new Color(220, 130, 60));
        avatar.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        miniGrid = new JPanel(new GridLayout(4, 4, 1, 1));
        miniGrid.setPreferredSize(new Dimension(48, 48));
        miniGrid.setMaximumSize(new Dimension(48, 48));
        miniGrid.setBackground(Color.DARK_GRAY);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                JPanel c = new JPanel();
                c.setBackground(Color.WHITE);
                celdas[i][j] = c;
                miniGrid.add(c);
            }
        }

        fila.add(avatar);
        fila.add(Box.createHorizontalStrut(6));
        fila.add(miniGrid);

        lblNombre = new JLabel(nombre, SwingConstants.CENTER);
        lblNombre.setAlignmentX(CENTER_ALIGNMENT);
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 11));

        add(lblPuntos);
        add(Box.createVerticalStrut(4));
        add(fila);
        add(Box.createVerticalStrut(2));
        add(lblNombre);
    }

    public String getIdJugador() {
        return idJugador;
    }

    public boolean[][] getMarcadas() {
        return marcadas;
    }

    public void marcarCasillaRemota(int fila, int columna) {
        if (fila < 0 || fila > 3 || columna < 0 || columna > 3) {
            return;
        }
        marcadas[fila][columna] = true;
        celdas[fila][columna].setBackground(new Color(70, 130, 200));
        revalidate();
        repaint();
    }

    public void setPlaceholderRemoto(boolean vacio) {
        setVisible(!vacio);
    }
}
