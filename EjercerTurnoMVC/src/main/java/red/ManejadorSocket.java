/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package red;

import controlador.ControlTablero;
import dominio.Casilla;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import objetosPresentacion.RepresentacionJugador;


/**
 * Clase toda cherry que nos va a simular la red por ahorita, realmente no hay
 * sockets reales porque todavía quería entrar en locura. Solo son timers y
 * datos mock que se van jalando
 *
 * @author nafbr
 */
public class ManejadorSocket {

    public static final int INTERVALO_GRITO_MS = 5000;

    private final ControlTablero control;
    //Esa lista nueva es una que se usa cuando las lecturas son mucho más que las escrituras, en este caso
    //como queremos estar en cuanto cambien las cartas de los otros pues ajá
    private final List<RepresentacionJugador> remotos = new CopyOnWriteArrayList<>();
    private final Random random = new Random();

    private Timer timerGriton;
    private Timer timerCuentaRegresiva;
    private Timer timerRemotos;
    private int segundosRestantes = INTERVALO_GRITO_MS / 1000;

    public ManejadorSocket(ControlTablero control) {
        this.control = control;
    }

    public void registrarRemoto(RepresentacionJugador representacion) {
        remotos.add(representacion);
    }

    //Todo lo q hace el gritón
    public void iniciarGriton() {
        detener();

        // Primera carta inmediata (como si ya hubiera llegado un mensaje)
        SwingUtilities.invokeLater(control::gritarTarjeta);

        segundosRestantes = INTERVALO_GRITO_MS / 1000;
        timerCuentaRegresiva = new Timer(1000, e -> {
            segundosRestantes--;
            if (segundosRestantes < 0) {
                segundosRestantes = INTERVALO_GRITO_MS / 1000;
            }
            control.tickTemporizador(segundosRestantes);
        });
        timerCuentaRegresiva.start();

        timerGriton = new Timer(INTERVALO_GRITO_MS, e -> {
            segundosRestantes = INTERVALO_GRITO_MS / 1000;
            control.gritarTarjeta();
        });
        timerGriton.start();

        // Simula que otros jugadores van marcando casillas por la red, keyword SIMULA TT
        timerRemotos = new Timer(3500, e -> simularProgresoRemoto());
        timerRemotos.start();
    }

    public void detener() {
        if (timerGriton != null) {
            timerGriton.stop();
        }
        if (timerCuentaRegresiva != null) {
            timerCuentaRegresiva.stop();
        }
        if (timerRemotos != null) {
            timerRemotos.stop();
        }
    }

    
    //Todo lo que hacen los otros
    /**
     * Mensaje SIMULADO saliente de que se marcó una casilla
     */
    public void enviarMarcadoLocal(Casilla casilla, boolean[][] tableroLocal) {
        System.out.println("OUT marcarCasilla -> "
                + casilla.getNombre() + " @" + casilla.getFila() + "," + casilla.getColumna());
    }

    /**
     * Mensaje SIMULADO saliente mock tras reclamar puntaje 
     */
    public void enviarPuntajeLocal(String accion, int puntajeTotal) {
        System.out.println("OUT reclamarPuntaje -> "
                + accion + " total=" + puntajeTotal);
    }

    /**
     * Mensaje entrante mock: otro jugador marcó algo entoces refresca su mini-tablero.
     */
    public void recibirMarcadoRemoto(String idJugador, int fila, int columna) {
        for (RepresentacionJugador r : remotos) {
            if (r.getIdJugador().equals(idJugador)) {
                r.marcarCasillaRemota(fila, columna);
                break;
            }
        }
    }

    private void simularProgresoRemoto() {
        if (remotos.isEmpty()) {
            return;
        }
        RepresentacionJugador r = remotos.get(random.nextInt(remotos.size()));
        List<int[]> libres = new ArrayList<>();
        boolean[][] m = r.getMarcadas();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (!m[i][j]) {
                    libres.add(new int[]{i, j});
                }
            }
        }
        if (libres.isEmpty()) {
            return;
        }
        int[] pick = libres.get(random.nextInt(libres.size()));
        recibirMarcadoRemoto(r.getIdJugador(), pick[0], pick[1]);
    }
}
