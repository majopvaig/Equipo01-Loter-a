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
public class MazoTirado {
    
    private Integer cantTarjetas;
    private List<Tarjeta> listaTarjetas;
    private Tarjeta ultimaTarjeta;

    public MazoTirado() {
    }

    public MazoTirado(Integer cantTarjetas, List<Tarjeta> listaTarjetas, Tarjeta ultimaTarjeta) {
        this.cantTarjetas = cantTarjetas;
        this.listaTarjetas = listaTarjetas;
        this.ultimaTarjeta = ultimaTarjeta;
    }

    public Integer getCantTarjetas() {
        return cantTarjetas;
    }

    public void setCantTarjetas(Integer cantTarjetas) {
        this.cantTarjetas = cantTarjetas;
    }

    public List<Tarjeta> getListaTarjetas() {
        return listaTarjetas;
    }

    public void setListaTarjetas(List<Tarjeta> listaTarjetas) {
        this.listaTarjetas = listaTarjetas;
    }

    public Tarjeta getUltimaTarjeta() {
        return ultimaTarjeta;
    }

    public void setUltimaTarjeta(Tarjeta ultimaTarjeta) {
        this.ultimaTarjeta = ultimaTarjeta;
    }
    
}
