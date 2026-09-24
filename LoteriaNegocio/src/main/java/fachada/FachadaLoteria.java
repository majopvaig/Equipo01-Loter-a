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

    private Tarjeta tarjetaActual;
    
    @Override
    public boolean marcarCasilla(Casilla casilla) {
        if(!casilla.getNombre().equals(tarjetaActual.getNombre())){
            return false;
        }
        return true;
    }

    @Override
    public boolean reclamarPuntaje(Accion accion) {
        if(!accion.isRealizada()){
            return false;
        }
        
        accion.setRealizada(true);
        return true;
    }
    
}
