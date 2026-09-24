/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author maria
 */
public class Carta {
    
    private Integer numeroTablero;
    private String imgTablero;

    public Carta() {
    }

    public Carta(Integer numeroTablero, String imgTablero) {
        this.numeroTablero = numeroTablero;
        this.imgTablero = imgTablero;
    }

    public Integer getNumeroTablero() {
        return numeroTablero;
    }

    public void setNumeroTablero(Integer numeroTablero) {
        this.numeroTablero = numeroTablero;
    }

    public String getImgTablero() {
        return imgTablero;
    }

    public void setImgTablero(String imgTablero) {
        this.imgTablero = imgTablero;
    }
    
}
