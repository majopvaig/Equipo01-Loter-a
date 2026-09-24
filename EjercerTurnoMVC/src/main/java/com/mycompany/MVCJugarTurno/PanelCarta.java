/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.MVCJugarTurno;

import dominio.Casilla;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author nafbr
 */
public class PanelCarta extends JPanel{

    private final Map<Casilla, PanelCasilla> paneles = new HashMap<>();

    public PanelCarta(List<Casilla> casillas, Consumer<Casilla> alMarcar) {
        setLayout(new BorderLayout(0, 6));
        setOpaque(false);

        JLabel titulo = new JLabel("TABLA 10", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.BOLD, 18));
        titulo.setForeground(new Color(80, 40, 20));

        JPanel grid = new JPanel(new GridLayout(4, 4, 4, 4));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        for (Casilla c : casillas) {
            PanelCasilla pc = new PanelCasilla(c);
            pc.setOnClick(alMarcar);
            paneles.put(c, pc);
            grid.add(pc);
        }

        add(titulo, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
    }

    public void mostrarCartaActualizada() {
        for (PanelCasilla pc : paneles.values()) {
            pc.refrescar();
        }
    }

    public void mostrarTableroJugadorMarcado() {
        mostrarCartaActualizada();
    }
}
