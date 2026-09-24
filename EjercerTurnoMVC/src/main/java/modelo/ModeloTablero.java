/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import dominio.Accion;
import dominio.Casilla;
import dominio.CatalogoLoteria;
import dominio.Jugador;
import dominio.Partida;
import dominio.Tarjeta;
import interfaz.ILoteria;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maria
 */
public class ModeloTablero implements IModeloTablero {
    
    private final List<IObserverTablero> observadores = new ArrayList<>();
    private final ILoteria fachada;
    private final Partida partida;
    private final List<Casilla> casillas;
    private final List<Tarjeta> mazoGriton;
    private final String nombreJugadorLocal;

    private Tarjeta tarjetaActual;
    private String error = "";
    private Integer puntajeAcumulado = 0;
    private int indiceMazo = 0;
    private int segundosRestantes = 5;
    
    public ModeloTablero(ILoteria loteria, String nombreJugadorLocal) {
        this.fachada = loteria;
        this.nombreJugadorLocal = nombreJugadorLocal;
        this.casillas = CatalogoLoteria.tablaAleatoria();
        this.mazoGriton = CatalogoLoteria.mazoBarajado();

        //Set up hardcodeado
        List<Accion> acciones = List.of(
                new Accion("Llena", 1000, false),
                new Accion("Centro", 300, false),
                new Accion("Chorro", 400, false),
                new Accion("Cuatro Esquinas", 500, false)
        );
        this.partida = new Partida(
                List.of(new Jugador(nombreJugadorLocal, "avatar_local")),
                new ArrayList<>(acciones)
        );
    }

    @Override
    public void suscribir(IObserverTablero observador) {
        observadores.add(observador);
    }

    @Override
    public void gritarTarjeta() {
        if (indiceMazo >= mazoGriton.size()) {
            indiceMazo = 0;
        }
        tarjetaActual = mazoGriton.get(indiceMazo++);
        error = "";
        segundosRestantes = 5;
        notificarSubs();
    }

    @Override
    public Tarjeta getTarjetaActual() {
        return tarjetaActual;
    }

    @Override
    public void marcarCasilla(Casilla casilla) {
        if (fachada.marcarCasilla(casilla, tarjetaActual)) {
            casilla.setMarcada(true);
            error = "";
        } else {
            error = "CasillaEquivocada: no coincide con la tarjeta gritón";
        }
        notificarSubs();
    }

    @Override
    public void reclamarPuntaje(Accion accion) {
        Accion real = encontrarAccion(accion.getNombre());
        if (real == null) {
            error = "PuntajeNoValido: acción desconocida";
            notificarSubs();
            return;
        }
        if (fachada.reclamarPuntaje(real, getMarcadas())) {
            error = "";
            puntajeAcumulado += real.getPuntaje();
        } else {
            error = "PuntajeNoValido: patrón no completado o ya reclamado";
        }
        notificarSubs();
    }

    private Accion encontrarAccion(String nombre) {
        for (Accion a : partida.getAcciones()) {
            if (a.getNombre().equalsIgnoreCase(nombre)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public Integer consultarPuntaje() {
        return puntajeAcumulado;
    }

    @Override
    public List<Casilla> getCasillas() {
        return casillas;
    }

    @Override
    public String getError() {
        return error;
    }

    @Override
    public String getNombreJugadorLocal() {
        return nombreJugadorLocal;
    }

    @Override
    public boolean[][] getMarcadas() {
        boolean[][] m = new boolean[4][4];
        for (Casilla c : casillas) {
            m[c.getFila()][c.getColumna()] = c.isMarcada();
        }
        return m;
    }

    @Override
    public List<Accion> getAcciones() {
        return partida.getAcciones();
    }

    @Override
    public int getSegundosRestantes() {
        return segundosRestantes;
    }

    @Override
    public void setSegundosRestantes(int segundos) {
        this.segundosRestantes = segundos;
        notificarSubs();
    }

    @Override
    public void notificarSubs() {
        for (IObserverTablero obs : observadores) {
            obs.update(this);
        }
    }
}
