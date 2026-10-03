/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.MVCJugarTurno;

import dominio.Accion;
import dominio.Carta;
import dominio.Casilla;
import dominio.Tarjeta;

import java.util.List;

/**
 *
 * @author maria
 */
public interface IModeloTablero {
//    esta interfaz no se si está mal nombrada o se fucionó con la IModeloCarta
//    o de plano estos metodos no iban pero el caso es que le terminé de agregar 
//    los metodos que faltaban y el de suscribirse y setSegundosRestantes
    public void suscribir(IObserverTablero observador);
    
    public void notificarSubs();
    
    public void obtenerCarta();

    public Tarjeta getTarjetaActual();

    public Integer consultarPuntaje();
    
    public void gritarTarjeta();
    
    public void reclamarPuntaje(Accion accion);
    
    public void marcarCasilla(Casilla casilla);

    public List<Casilla> getCasillas();

    public String getError();

    public String getNombreJugadorLocal();

    public boolean[][] getMarcadas();

    public List<Accion> getAcciones();

    public int getSegundosRestantes();
    
    public void setSegundosRestantes(int segundos);

}
