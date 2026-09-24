/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.MVCJugarTurno;

import dominio.Accion;
import dominio.Casilla;
import dominio.Tarjeta;

import java.util.List;

/**
 *
 * @author maria
 */
public interface IModeloTablero {

    public Tarjeta getTarjetaActual();

    public Integer consultarPuntaje();

    public List<Casilla> getCasillas();

    public String getError();

    public String getNombreJugadorLocal();

    public boolean[][] getMarcadas();

    public List<Accion> getAcciones();

    public int getSegundosRestantes();

}
