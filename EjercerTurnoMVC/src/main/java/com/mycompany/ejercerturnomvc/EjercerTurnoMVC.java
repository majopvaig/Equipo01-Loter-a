/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejercerturnomvc;

import controlador.ControlTablero;
import fachada.FachadaLoteria;
import interfaz.ILoteria;
import modelo.ModeloTablero;
import red.ManejadorSocket;
import vista.VistaTablero;

/**
 *
 * @author maria
 */
public class EjercerTurnoMVC {

    public static void main(String[] args) {
        ILoteria fachada = new FachadaLoteria();
        ModeloTablero modelo = new ModeloTablero(fachada, "Ash Lynx");
        ControlTablero control = new ControlTablero(modelo);
        ManejadorSocket red = new ManejadorSocket(control);
        VistaTablero vista = new VistaTablero(modelo, control, red);
        vista.iniciar();
    }
}
