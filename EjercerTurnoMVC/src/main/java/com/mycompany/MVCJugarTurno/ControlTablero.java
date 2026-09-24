/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.MVCJugarTurno;

import dominio.Accion;
import dominio.Casilla;

/**
 * Contralor (MVC) del tablero de la lotería.
 *
 * <p>La vista se refresca por el patrón Observer (el modelo notifica a sus observadores
 * con {@code update(modelo)}), por lo que el controlador nunca invoca directamente a la vista.
 *
 * @author Andres
 */
public class ControlTablero {

    /**
     * Modelo del tablero.
     */
    private final IModeloTablero modelo;

    /**
     * Crea el controlador con el modelo del tablero.
     *
     * @param modelo modelo del tablero (implementación de {@code IModeloTablero})
     */
    public ControlTablero(IModeloTablero modelo) {
        this.modelo = modelo;
    }

    /**
     * Anuncia la tarjeta que gritó el gritón.
     */
    public void gritarTarjeta() {
        modelo.gritarTarjeta();
        modelo.getTarjetaActual();
    }

    /**
     * Marca la casilla que el jugador seleccionó en la vista.
     *
     * @param casilla casilla seleccionada por el jugador
     */
    public void marcarCasilla(Casilla casilla) {
        modelo.marcarCasilla(casilla);
    }

    /**
     * Reclama el puntaje presionado por el jugador.
     */
    public void reclamarPuntaje(Accion accion) {
        modelo.reclamarPuntaje(accion);
    }
    
    public void tickTemporizador(int segundos) {
        modelo.setSegundosRestantes(segundos);
    }
}