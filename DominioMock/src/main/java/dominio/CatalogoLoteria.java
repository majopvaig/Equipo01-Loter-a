/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase asi de muletilla para hechar a andar lo del las tablas y el mazo para
 * no preocuparnos por el negocio
 *
 * @author nafbr
 */
public class CatalogoLoteria {

    private CatalogoLoteria() {
    }

    private static final String[] NOMBRES = {
        "El Gallo", "El Diablito", "La Dama", "El Catrín", "El Paraguas",
        "La Sirena", "La Escalera", "La Botella", "El Barril", "El Árbol",
        "El Melón", "El Valiente", "El Gorrito", "La Muerte", "La Pera",
        "La Bandera", "El Bandolón", "El Violoncello", "La Garza", "El Pájaro",
        "La Mano", "El Bota", "La Luna", "El Cotorro", "El Borracho",
        "El Negrito", "El Corazón", "La Sandía", "El Tambor", "El Camarón",
        "Las Jaras", "El Músico", "La Araña", "El Soldado", "La Estrella",
        "El Cazo", "El Mundo", "El Apache", "El Nopal", "El Alacrán",
        "La Rosa", "La Calavera", "La Campana", "El Cantarito", "El Venado",
        "El Sol", "La Corona", "La Chalupa", "El Pino", "El Pescado",
        "La Palma", "La Maceta", "El Arpa", "La Rana"
    };

    public static List<Tarjeta> todas() {
        List<Tarjeta> lista = new ArrayList<>();
        for (int i = 0; i < NOMBRES.length; i++) {
            int numero = i + 1;
            lista.add(new Tarjeta(numero, NOMBRES[i], "tarjeta_" + numero));
        }
        return lista;
    }

    public static List<Tarjeta> mazoBarajado() {
        List<Tarjeta> mazo = todas();
        Collections.shuffle(mazo);
        return mazo;
    }

    /**
     * Arma una tabla 4x4 tomando 16 cartas distintas del catálogo.
     */
    public static List<Casilla> tablaAleatoria() {
        List<Tarjeta> pool = todas();
        Collections.shuffle(pool);
        List<Casilla> casillas = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            Tarjeta t = pool.get(i);
            casillas.add(new Casilla(t.getNumero(), t.getNombre(), t.getImagen(), i / 4, i % 4));
        }
        return casillas;
    }
}
