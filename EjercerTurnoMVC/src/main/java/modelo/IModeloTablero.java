/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package modelo;

import dominio.Accion;
import dominio.Casilla;
import dominio.Tarjeta;
import java.util.List;

/**
 *
 * @author maria
 */
public interface IModeloTablero {

    public void notificarSubs();

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

    public void suscribir(IObserverTablero observador);
}
