/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.MVCJugarTurno;

import dominio.Tarjeta;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author nafbr
 */
public class PanelTarjetaActual extends JPanel{
    private final JLabel lblCarta;
    private final JLabel lblTimer;
    private final JLabel lblNumero;

    public PanelTarjetaActual() {
        setLayout(new BorderLayout(0, 8));
        setOpaque(false);
        setPreferredSize(new Dimension(160, 260));

        JLabel titulo = new JLabel("carta", SwingConstants.CENTER);
        titulo.setFont(new Font("Serif", Font.ITALIC, 22));
        titulo.setForeground(new Color(60, 30, 10));

        JPanel marco = new JPanel(new BorderLayout());
        marco.setBackground(new Color(250, 245, 230));
        marco.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 60, 20), 3),
                BorderFactory.createEmptyBorder(12, 8, 12, 8)
        ));
        marco.setPreferredSize(new Dimension(140, 180));

        lblNumero = new JLabel("#", SwingConstants.CENTER);
        lblNumero.setFont(new Font("SansSerif", Font.BOLD, 12));

        lblCarta = new JLabel("<html><center>—</center></html>", SwingConstants.CENTER);
        lblCarta.setFont(new Font("SansSerif", Font.BOLD, 16));

        marco.add(lblNumero, BorderLayout.NORTH);
        marco.add(lblCarta, BorderLayout.CENTER);

        lblTimer = new JLabel("00:05", SwingConstants.CENTER);
        lblTimer.setFont(new Font("Monospaced", Font.BOLD, 20));
        lblTimer.setForeground(new Color(40, 40, 40));

        add(titulo, BorderLayout.NORTH);
        add(marco, BorderLayout.CENTER);
        add(lblTimer, BorderLayout.SOUTH);
    }

    public void mostrarTarjeta(Tarjeta tarjeta) {
        if (tarjeta == null) {
            lblNumero.setText("#");
            //No se me ocurrió como más ponerlo """bonito"""
            lblCarta.setText("<html><center>—</center></html>");
            return;
        }
        lblNumero.setText("#" + tarjeta.getNumero());
        lblCarta.setText("<html><center>" + tarjeta.getNombre() + "</center></html>");
    }

    public void mostrarSegundos(int segundos) {
        lblTimer.setText(String.format("00:%02d", Math.max(0, segundos)));
    }
}
