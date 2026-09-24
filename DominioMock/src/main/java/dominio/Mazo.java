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
public class Mazo {
    
    private Integer cantidadTarjetas;
    private List<Tarjeta> listaTarjetas;

    public Mazo() {
    }

    public Mazo(Integer cantidadTarjetas, List<Tarjeta> listaTarjetas) {
        this.cantidadTarjetas = cantidadTarjetas;
        this.listaTarjetas = listaTarjetas;
    }

    public Integer getCantidadTarjetas() {
        return cantidadTarjetas;
    }

    public void setCantidadTarjetas(Integer cantidadTarjetas) {
        this.cantidadTarjetas = cantidadTarjetas;
    }

    public List<Tarjeta> getListaTarjetas() {
        return listaTarjetas;
    }

    public void setListaTarjetas(List<Tarjeta> listaTarjetas) {
        this.listaTarjetas = listaTarjetas;
    }
    
}
