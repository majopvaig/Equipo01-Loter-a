/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaz;

import dominio.Accion;
import dominio.Casilla;

/**
 *
 * @author maria
 */
public interface ILoteria {
    
    boolean marcarCasilla(Casilla casilla);
    
    boolean reclamarPuntaje(Accion accion);
    
}
