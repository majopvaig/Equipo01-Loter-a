/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import dominio.Accion;
import dominio.Casilla;
import dominio.Partida;
import dominio.Tarjeta;
import interfaz.ILoteria;
import java.util.List;

/**
 *
 * @author maria
 */
public class ModeloTablero implements IModeloTablero {
    
    private List<IObserverTablero> observadores;
    private Partida partida;
    private Tarjeta tarjetaActual;
    private ILoteria fachada;
    private String error;
    private Integer puntajeAcumulado;
    
    public ModeloTablero(ILoteria loteria){
        this.fachada = loteria;
    }
    
    @Override
    public void gritarTarjeta(){}
    
    @Override
    public Tarjeta getTarjetaActual(){
        return tarjetaActual;
    }
    
    @Override
    public void marcarCasilla(Casilla casilla){
        if(fachada.marcarCasilla(casilla)){
            error = "";
            /*
            q se supone q hará si se marca la casilla? cambiar la imagen de
            la carta? dudas para mañana
            */
        } else {
            error = "No se pudo marcar la casilla seleccionada";
        }
        notificarSubs();
    }
    
    @Override
    public void reclamarPuntaje(Accion accion){
        if(fachada.reclamarPuntaje(accion)){
            error = "";
            puntajeAcumulado += accion.getPuntaje();
        } else {
            error = "Puntaje no reclamado";
        }
        notificarSubs();
    }
    
    @Override
    public Integer consultarPuntaje(Accion accion){
        return puntajeAcumulado;
    }
    
    @Override
    public void notificarSubs(){}
}
