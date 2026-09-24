/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fachada;

import dominio.Accion;
import dominio.Casilla;
import dominio.Tarjeta;
import interfaz.ILoteria;

/**
 *
 * @author maria
 */
public class FachadaLoteria implements ILoteria {

    @Override
    public boolean marcarCasilla(Casilla casilla, Tarjeta tarjetaActual) {
        if (casilla == null || tarjetaActual == null) {
            return false;
        }
        if (casilla.isMarcada()) {
            return false;
        }
        return casilla.getNombre().equalsIgnoreCase(tarjetaActual.getNombre());
    }

    @Override
    public boolean reclamarPuntaje(Accion accion, boolean[][] marcadas) {
        if (accion == null || accion.isRealizada() || marcadas == null) {
            return false;
        }
        String nombre = accion.getNombre();
        boolean ok = switch (nombre) {
            case "Cuatro Esquinas" ->
                esquinas(marcadas);
            case "Centro" ->
                centro(marcadas);
            case "Chorro" ->
                chorro(marcadas);
            case "Llena" ->
                llena(marcadas);
            default ->
                false;
        };
        if (ok) {
            accion.setRealizada(true);
        }
        return ok;
    }

    private static boolean esquinas(boolean[][] m) {
        return m[0][0] && m[0][3] && m[3][0] && m[3][3];
    }

    private static boolean centro(boolean[][] m) {
        return m[1][1] && m[1][2] && m[2][1] && m[2][2];
    }

    private static boolean chorro(boolean[][] m) {
        for (int i = 0; i < 4; i++) {
            if (m[i][0] && m[i][1] && m[i][2] && m[i][3]) {
                return true;
            }
            if (m[0][i] && m[1][i] && m[2][i] && m[3][i]) {
                return true;
            }
        }
        return false;
    }

    private static boolean llena(boolean[][] m) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!m[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

}
