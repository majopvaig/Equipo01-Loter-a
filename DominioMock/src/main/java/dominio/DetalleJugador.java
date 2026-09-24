/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author maria
 */
public class DetalleJugador {
    
    private Integer puntaje;
    private Carta carta;

    public DetalleJugador() {
    }

    public DetalleJugador(Integer puntaje, Carta carta) {
        this.puntaje = puntaje;
        this.carta = carta;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }

    public Carta getCarta() {
        return carta;
    }

    public void setCarta(Carta carta) {
        this.carta = carta;
    }
    
}
