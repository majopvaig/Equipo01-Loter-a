/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import java.util.List;

/**
 *
 * @author maria
 */
public class Partida {
    
    private List<Jugador> jugadores;
    private List<Accion> acciones;

    public Partida() {
    }

    public Partida(List<Jugador> jugadores, List<Accion> acciones) {
        this.jugadores = jugadores;
        this.acciones = acciones;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public List<Accion> getAcciones() {
        return acciones;
    }

    public void setAcciones(List<Accion> acciones) {
        this.acciones = acciones;
    }
    
}
