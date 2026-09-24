/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package modelo;

import dominio.Accion;
import dominio.Casilla;
import dominio.Tarjeta;

/**
 *
 * @author maria
 */
public interface IModeloTablero {
    
    public void notificarSubs();
    
    public Tarjeta getTarjetaActual();
    
    public Integer consultarPuntaje(Accion accion);
    
    public void gritarTarjeta();
    
    public void reclamarPuntaje(Accion accion);
    
    public void marcarCasilla(Casilla casilla);
}
