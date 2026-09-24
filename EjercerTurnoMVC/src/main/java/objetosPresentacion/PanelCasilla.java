/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package objetosPresentacion;

import dominio.Casilla;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * La celda de la CARTA
 * @author nafbr
 */
public class PanelCasilla extends JPanel{
    private final Casilla casilla;
    private final JLabel lblNombre;
    private final JLabel lblFicha;
    private Consumer<Casilla> onClick;

    public PanelCasilla(Casilla casilla) {
        this.casilla = casilla;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(90, 110));
        setBackground(new Color(245, 235, 210));
        setBorder(BorderFactory.createLineBorder(new Color(120, 80, 40), 2));

        JLabel num = new JLabel(String.valueOf(casilla.getNumero()), SwingConstants.CENTER);
        num.setFont(new Font("SansSerif", Font.BOLD, 10));
        num.setForeground(new Color(100, 60, 30));

        lblNombre = new JLabel("<html><center>" + casilla.getNombre() + "</center></html>", SwingConstants.CENTER);
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 11));

        lblFicha = new JLabel("", SwingConstants.CENTER);
        lblFicha.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblFicha.setForeground(new Color(180, 30, 30));

        add(num, BorderLayout.NORTH);
        add(lblNombre, BorderLayout.CENTER);
        add(lblFicha, BorderLayout.SOUTH);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (onClick != null) {
                    onClick.accept(casilla);
                }
            }
        });
    }

    public void setOnClick(Consumer<Casilla> onClick) {
        this.onClick = onClick;
    }

    public void refrescar() {
        if (casilla.isMarcada()) {
            lblFicha.setText("MARCADA");
            setBackground(new Color(220, 200, 160));
        } else {
            lblFicha.setText("");
            setBackground(new Color(245, 235, 210));
        }
    }

    public Casilla getCasilla() {
        return casilla;
    }
}
