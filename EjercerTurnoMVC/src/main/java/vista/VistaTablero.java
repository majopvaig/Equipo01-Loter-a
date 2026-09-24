/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

import controlador.ControlTablero;
import dominio.Accion;
import dominio.Casilla;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import modelo.IModeloTablero;
import modelo.IObserverTablero;
import objetosPresentacion.PanelCarta;
import objetosPresentacion.PanelTarjetaActual;
import objetosPresentacion.RepresentacionJugador;
import red.ManejadorSocket;

/**
 *
 * @author nafbr
 */
public class VistaTablero extends JFrame implements IObserverTablero {

    //Está aqui pq no hay un negorio real todavía, no me fusilen
    public static final int MAX_JUGADORES = 4;
    public static final int MAX_RIVALES = MAX_JUGADORES - 1;
    //SOLO conoce al control tablero
    private final ControlTablero control;
    private final ManejadorSocket manejadorSocket;
    private final PanelTarjetaActual panelTarjeta;
    private final PanelCarta panelCarta;
    private final RepresentacionJugador[] rivales = new RepresentacionJugador[MAX_RIVALES];
    private final JLabel lblPuntosLateral;
    private final JLabel lblError;
    private final Map<String, JButton> botonesAccion = new HashMap<>();

    private boolean[][] ultimoTablero = new boolean[4][4];
    private String ultimoError = "";
    private final Set<String> puntajesYaEnviados = new HashSet<>();

    public VistaTablero(IModeloTablero modelo, ControlTablero control, ManejadorSocket manejadorSocket) {
        this.control = control;
        this.manejadorSocket = manejadorSocket;

        setTitle("Lotería — ejercer turno (mock GUI)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 620));

        JPanel root = new JPanel(new BorderLayout(12, 8));
        root.setBackground(new Color(196, 160, 110));
        root.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

       // El contenedor de los rivales
        JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 4));
        top.setOpaque(false);

        //Hardcodeado x ahorita
        String[] nombresRivales = {"Luna", "Majojo", "Daya"};
        for (int i = 0; i < MAX_RIVALES; i++) {
            rivales[i] = new RepresentacionJugador("remoto-" + (i + 1), nombresRivales[i]);
            top.add(wrapPlaceholder(rivales[i]));
            manejadorSocket.registrarRemoto(rivales[i]);
        }

        // Lo mero mero jeje (La carta)
        panelTarjeta = new PanelTarjetaActual();
        panelCarta = new PanelCarta(modelo.getCasillas(), this::onMarcarCasilla);
        lblPuntosLateral = new JLabel("0 Puntos", SwingConstants.CENTER);
        lblError = new JLabel(" ", SwingConstants.CENTER);
        lblError.setForeground(new Color(140, 20, 20));
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 12));

        JPanel centro = new JPanel(new BorderLayout(16, 0));
        centro.setOpaque(false);
        centro.add(panelTarjeta, BorderLayout.WEST);
        centro.add(panelCarta, BorderLayout.CENTER);
        centro.add(crearPanelAcciones(), BorderLayout.EAST);

        root.add(top, BorderLayout.NORTH);
        root.add(centro, BorderLayout.CENTER);
        root.add(lblError, BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);

        modelo.suscribir(this);
    }

    private JPanel wrapPlaceholder(RepresentacionJugador r) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(new Color(230, 140, 55));
        wrap.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        wrap.add(r, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 8, 8, 8));

        String[] nombres = {"Llena", "Centro", "Chorro", "Cuatro Esquinas"};
        for (String n : nombres) {
            JButton b = new JButton(n);
            b.setAlignmentX(CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(160, 36));
            b.setBackground(Color.BLACK);
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.addActionListener(e -> onReclamar(n));
            botonesAccion.put(n, b);
            panel.add(b);
            panel.add(Box.createVerticalStrut(8));
        }

        lblPuntosLateral.setAlignmentX(CENTER_ALIGNMENT);
        lblPuntosLateral.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblPuntosLateral);

        JButton abandonar = new JButton("Abandonar partida");
        abandonar.setAlignmentX(CENTER_ALIGNMENT);
        abandonar.setBorderPainted(false);
        abandonar.setContentAreaFilled(false);
        abandonar.setForeground(new Color(80, 40, 20));
        abandonar.addActionListener(e -> {
            manejadorSocket.detener();
            dispose();
        });
        panel.add(Box.createVerticalStrut(8));
        panel.add(abandonar);

        return panel;
    }

    private void onMarcarCasilla(Casilla casilla) {
        control.marcarCasilla(casilla);
    }

    private void onReclamar(String nombreAccion) {
        // El modelo resuelve la Accion real de la partida por nombre.
        control.reclamarPuntaje(new Accion(nombreAccion, puntosDe(nombreAccion), false));
    }

    private static int puntosDe(String nombre) {
        return switch (nombre) {
            case "Llena" -> 1000;
            case "Centro" -> 300;
            case "Chorro" -> 400;
            case "Cuatro Esquinas" -> 500;
            default -> 0;
        };
    }

    public void iniciar() {
        setVisible(true);
        manejadorSocket.iniciarGriton();
    }

    @Override
    public void update(IModeloTablero modelo) {
        // El obsverver observando y updateando
        panelTarjeta.mostrarTarjeta(modelo.getTarjetaActual());
        panelTarjeta.mostrarSegundos(modelo.getSegundosRestantes());
        panelCarta.mostrarCartaActualizada();

        boolean[][] marcadas = modelo.getMarcadas();
        int puntos = modelo.consultarPuntaje();
        lblPuntosLateral.setText(puntos + " Puntos");

        String err = modelo.getError();
        if (err != null && !err.isBlank()) {
            lblError.setText(err);
            lblError.setForeground(new Color(160, 20, 20));
            lblError.setFont(new Font("SansSerif", Font.BOLD, 16));
            lblError.setOpaque(true);
            lblError.setBackground(new Color(255, 220, 180));
            lblError.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(160, 60, 20), 2),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
        } else {
            lblError.setText(" ");
            lblError.setOpaque(false);
            lblError.setBackground(null);
            lblError.setBorder(null);
            lblError.setFont(new Font("SansSerif", Font.PLAIN, 12));
        }

        // Detectar marcado nuevo, sale
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (marcadas[i][j] && !ultimoTablero[i][j]) {
                    for (Casilla c : modelo.getCasillas()) {
                        if (c.getFila() == i && c.getColumna() == j) {
                            manejadorSocket.enviarMarcadoLocal(c, marcadas);
                            break;
                        }
                    }
                }
            }
        }
        ultimoTablero = copy(marcadas);

        for (Accion a : modelo.getAcciones()) {
            JButton b = botonesAccion.get(a.getNombre());
            if (b != null && a.isRealizada()) {
                b.setBackground(new Color(160, 180, 200));
                b.setForeground(Color.DARK_GRAY);
                if (puntajesYaEnviados.add(a.getNombre())) {
                    manejadorSocket.enviarPuntajeLocal(a.getNombre(), puntos);
                }
            }
        }
    }

    private static boolean[][] copy(boolean[][] src) {
        boolean[][] d = new boolean[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(src[i], 0, d[i], 0, 4);
        }
        return d;
    }
}
