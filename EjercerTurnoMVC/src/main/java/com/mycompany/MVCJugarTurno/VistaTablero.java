/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.MVCJugarTurno;

import dominio.Accion;
import dominio.Casilla;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author nafbr
 */
public class VistaTablero extends JFrame implements IObserverTablero {

    //Está aqui pq no hay un negorio real todavía, no me fusilen
    public static final int MAX_JUGADORES = 4;
    public static final int MAX_RIVALES = MAX_JUGADORES - 1;

    private static final Color COLOR_FONDO = new Color(196, 160, 110);
    private static final Color COLOR_RIVAL = new Color(218, 132, 49);
    private static final Color COLOR_BOTON = new Color(31, 31, 31);
    private static final Color COLOR_TEXTO = new Color(24, 24, 24);
    private static final Color COLOR_CARTA = new Color(248, 244, 228);

    //SOLO conoce al control tablero
    private final ControlTablero control;
    //Va al modelo, no aqui
    private final ManejadorSocket manejadorSocket;
    private final PanelTarjetaActual panelTarjeta;
    private final PanelCarta panelCarta;
    private final RepresentacionJugador[] rivales = new RepresentacionJugador[MAX_RIVALES];
    private final JLabel lblPuntosLateral;
    private final JLabel lblPuntosSuperior;
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
        setMinimumSize(new Dimension(820, 520));
        setPreferredSize(new Dimension(880, 560));

        // Los componentes existentes conservan su lógica; aquí sólo se ajusta
        // su tamaño y su jerarquía visual.
        panelTarjeta = new PanelTarjetaActual();
        panelCarta = new PanelCarta(modelo.getCasillas(), this::onMarcarCasilla);
        panelTarjeta.setPreferredSize(new Dimension(120, 244));
        panelTarjeta.setMinimumSize(new Dimension(120, 244));
        panelTarjeta.setMaximumSize(new Dimension(120, 244));
        panelCarta.setPreferredSize(new Dimension(220, 346));
        panelCarta.setMinimumSize(new Dimension(220, 300));
        panelCarta.setMaximumSize(new Dimension(220, 346));
        panelCarta.setBorder(BorderFactory.createLineBorder(COLOR_CARTA, 3));
        ajustarTituloPanelCarta();

        lblPuntosLateral = new JLabel("0 Puntos", SwingConstants.CENTER);
        lblPuntosLateral.setFont(new Font("SansSerif", Font.PLAIN, 21));
        lblPuntosLateral.setForeground(COLOR_TEXTO);

        lblPuntosSuperior = new JLabel("0 Puntos", SwingConstants.CENTER);
        lblPuntosSuperior.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblPuntosSuperior.setForeground(COLOR_TEXTO);

        lblError = new JLabel(" ", SwingConstants.CENTER);
        lblError.setForeground(new Color(140, 20, 20));
        lblError.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblError.setOpaque(false);
        lblError.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel root = new FondoLoteria();
        root.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel top = crearPanelSuperior(modelo);
        JPanel centro = crearPanelCentral();
        JPanel areaCentral = crearAreaCentral(centro);

        JPanel zonaError = new JPanel(new BorderLayout());
        zonaError.setOpaque(false);
        zonaError.setBorder(BorderFactory.createEmptyBorder(0, 28, 8, 28));
        zonaError.add(lblError, BorderLayout.CENTER);

        root.add(top, BorderLayout.NORTH);
        root.add(areaCentral, BorderLayout.CENTER);
        root.add(zonaError, BorderLayout.SOUTH);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);

        modelo.suscribir(this);
    }

    /**
     * Encabezado del tablero. Los dos recuadros laterales representan los
     * lugares de los rivales; las instancias de los tres rivales se mantienen
     * registradas en el socket aunque una de ellas no sea visible en el mock.
     */
    private JPanel crearPanelSuperior(IModeloTablero modelo) {
        JPanel top = new JPanel(new GridBagLayout());
        top.setOpaque(false);
        top.setBorder(BorderFactory.createEmptyBorder(8, 30, 8, 30));

        //Hardcodeado x ahorita
        String[] nombresRivales = {"Luna", "Majojo", "Daya"};
        for (int i = 0; i < MAX_RIVALES; i++) {
            rivales[i] = new RepresentacionJugador("remoto-" + (i + 1), nombresRivales[i]);
            manejadorSocket.registrarRemoto(rivales[i]);
        }

        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.gridy = 0;
        restricciones.insets = new Insets(6, 0, 0, 24);
        restricciones.anchor = GridBagConstraints.CENTER;
        top.add(wrapPlaceholder(rivales[0]), restricciones);

        restricciones.gridx = 1;
        restricciones.insets = new Insets(0, 8, 0, 8);
        top.add(crearPanelJugadorLocal(modelo.getNombreJugadorLocal()), restricciones);

        restricciones.gridx = 2;
        restricciones.insets = new Insets(6, 24, 0, 0);
        top.add(wrapPlaceholder(rivales[MAX_RIVALES - 1]), restricciones);

        return top;
    }

    private JPanel wrapPlaceholder(RepresentacionJugador r) {
        JPanel wrap = new RoundedPanel(COLOR_RIVAL, 12);
        wrap.setPreferredSize(new Dimension(176, 88));
        wrap.setMinimumSize(new Dimension(130, 74));
        wrap.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Se conserva la instancia para el socket, pero el hueco se muestra
        // vacío como en el storyboard. Si se activa más adelante, reaparece.
        r.setVisible(false);
        wrap.add(r, BorderLayout.CENTER);
        return wrap;
    }

    private JPanel crearPanelJugadorLocal(String nombre) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(190, 108));
        panel.setMinimumSize(new Dimension(190, 108));
        panel.setMaximumSize(new Dimension(190, 108));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.anchor = GridBagConstraints.CENTER;
        panel.add(lblPuntosSuperior, restricciones);

        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        fila.setOpaque(false);
        fila.setPreferredSize(new Dimension(190, 58));
        fila.setMinimumSize(new Dimension(190, 58));
        fila.setMaximumSize(new Dimension(190, 58));
        fila.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));

        AvatarPlaceholder avatar = new AvatarPlaceholder(nombre);
        avatar.setMaximumSize(new Dimension(52, 52));
        fila.add(avatar);
        fila.add(Box.createHorizontalStrut(22));
        JPanel miniGrid = crearMiniTablero();
        miniGrid.setMaximumSize(new Dimension(50, 50));
        fila.add(miniGrid);

        restricciones.gridy = 1;
        restricciones.weightx = 1;
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        restricciones.anchor = GridBagConstraints.WEST;
        panel.add(fila, restricciones);

        JLabel lblNombre = new JLabel(
                nombre == null || nombre.isBlank() ? "Jugador" : nombre,
                SwingConstants.LEFT
        );
        lblNombre.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblNombre.setForeground(COLOR_TEXTO);
        lblNombre.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 0));
        restricciones.gridy = 2;
        restricciones.anchor = GridBagConstraints.WEST;
        restricciones.insets = new Insets(0, 0, 0, 0);
        panel.add(lblNombre, restricciones);

        return panel;
    }

    private JPanel crearMiniTablero() {
        JPanel miniGrid = new JPanel(new GridLayout(4, 4, 1, 1));
        miniGrid.setPreferredSize(new Dimension(50, 50));
        miniGrid.setMinimumSize(new Dimension(50, 50));
        miniGrid.setBackground(new Color(105, 105, 105));
        miniGrid.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                JPanel celda = new JPanel();
                celda.setBackground(Color.WHITE);
                miniGrid.add(celda);
            }
        }
        return miniGrid;
    }

    private void ajustarTituloPanelCarta() {
        if (panelCarta.getComponentCount() > 0
                && panelCarta.getComponent(0) instanceof JLabel titulo) {
            titulo.setFont(new Font("Serif", Font.BOLD, 9));
            titulo.setForeground(Color.BLACK);
        }
    }

    private JPanel crearPanelCentral() {
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.setPreferredSize(new Dimension(706, 360));
        centro.setMinimumSize(new Dimension(706, 360));
        centro.setMaximumSize(new Dimension(706, 360));
        centro.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel columnaCarta = new JPanel(new BorderLayout());
        columnaCarta.setOpaque(false);
        columnaCarta.setPreferredSize(new Dimension(120, 360));
        columnaCarta.setMinimumSize(new Dimension(120, 360));
        columnaCarta.setMaximumSize(new Dimension(120, 360));
        columnaCarta.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));
        columnaCarta.add(panelTarjeta, BorderLayout.NORTH);

        JPanel columnaTabla = new JPanel(new BorderLayout());
        columnaTabla.setOpaque(false);
        columnaTabla.setPreferredSize(new Dimension(220, 360));
        columnaTabla.setMinimumSize(new Dimension(220, 360));
        columnaTabla.setMaximumSize(new Dimension(220, 360));
        columnaTabla.add(panelCarta, BorderLayout.NORTH);

        JPanel columnaAcciones = new JPanel(new BorderLayout());
        columnaAcciones.setOpaque(false);
        columnaAcciones.setPreferredSize(new Dimension(230, 360));
        columnaAcciones.setMinimumSize(new Dimension(230, 360));
        columnaAcciones.setMaximumSize(new Dimension(230, 360));
        columnaAcciones.add(crearPanelAcciones(), BorderLayout.NORTH);

        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.anchor = GridBagConstraints.NORTH;
        restricciones.fill = GridBagConstraints.NONE;
        restricciones.insets = new Insets(0, 0, 0, 92);
        centro.add(columnaCarta, restricciones);

        restricciones.gridx = 1;
        restricciones.insets = new Insets(0, 0, 0, 44);
        centro.add(columnaTabla, restricciones);

        restricciones.gridx = 2;
        restricciones.anchor = GridBagConstraints.CENTER;
        restricciones.insets = new Insets(0, 0, 0, 0);
        centro.add(columnaAcciones, restricciones);

        return centro;
    }

    private JPanel crearAreaCentral(JPanel centro) {
        JPanel area = new JPanel(new GridBagLayout());
        area.setOpaque(false);
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.anchor = GridBagConstraints.CENTER;
        restricciones.fill = GridBagConstraints.NONE;
        restricciones.insets = new Insets(0, 58, 0, 0);
        area.add(centro, restricciones);
        return area;
    }

    private JPanel crearPanelAcciones() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(230, 340));
        panel.setMinimumSize(new Dimension(230, 340));
        panel.setMaximumSize(new Dimension(230, 340));
        panel.setBorder(BorderFactory.createEmptyBorder(80, 8, 8, 8));

        String[] nombres = {"Llena", "Centro", "Chorro", "Cuatro Esquinas"};
        for (String n : nombres) {
            JButton b = new RoundedButton(n);
            boolean botonCorto = n.equals("Centro") || n.equals("Chorro");
            int ancho = botonCorto ? 100 : 150;
            b.setFont(new Font("SansSerif", Font.BOLD, 16));
            b.setForeground(Color.WHITE);
            b.setBackground(COLOR_BOTON);
            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setContentAreaFilled(false);
            b.setHorizontalAlignment(SwingConstants.CENTER);
            b.setPreferredSize(new Dimension(ancho, 30));
            b.setMinimumSize(new Dimension(ancho, 30));
            b.setMaximumSize(new Dimension(ancho, 30));
            b.addActionListener(e -> onReclamar(n));
            botonesAccion.put(n, b);
        }

        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.gridx = 0;
        restricciones.gridy = 0;
        restricciones.gridwidth = 2;
        restricciones.weightx = 0;
        restricciones.fill = GridBagConstraints.NONE;
        restricciones.anchor = GridBagConstraints.NORTH;
        restricciones.insets = new Insets(0, 0, 13, 0);
        panel.add(botonesAccion.get("Llena"), restricciones);

        restricciones.gridy = 1;
        restricciones.gridwidth = 1;
        restricciones.insets = new Insets(0, 0, 11, 6);
        panel.add(botonesAccion.get("Centro"), restricciones);

        restricciones.gridx = 1;
        restricciones.insets = new Insets(0, 6, 11, 0);
        panel.add(botonesAccion.get("Chorro"), restricciones);

        restricciones.gridx = 0;
        restricciones.gridy = 2;
        restricciones.gridwidth = 2;
        restricciones.insets = new Insets(0, 0, 0, 0);
        panel.add(botonesAccion.get("Cuatro Esquinas"), restricciones);

        lblPuntosLateral.setAlignmentX(CENTER_ALIGNMENT);
        lblPuntosLateral.setPreferredSize(new Dimension(190, 30));
        restricciones.gridy = 3;
        restricciones.insets = new Insets(40, 0, 0, 0);
        panel.add(lblPuntosLateral, restricciones);

        JButton abandonar = new JButton("Abandonar partida");
        abandonar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        abandonar.setAlignmentX(CENTER_ALIGNMENT);
        abandonar.setBorderPainted(false);
        abandonar.setContentAreaFilled(false);
        abandonar.setOpaque(false);
        abandonar.setFocusPainted(false);
        abandonar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        abandonar.setForeground(new Color(0, 45, 190));
        abandonar.setPreferredSize(new Dimension(190, 24));
        abandonar.setMaximumSize(new Dimension(190, 24));
        abandonar.addActionListener(e -> {
            manejadorSocket.detener();
            dispose();
        });
        restricciones.gridy = 4;
        restricciones.insets = new Insets(41, 0, 0, 0);
        panel.add(abandonar, restricciones);

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
        lblPuntosSuperior.setText(puntos + " Puntos");

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
            lblError.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
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

    /** Panel redondeado usado para los huecos de los jugadores remotos. */
    private static final class RoundedPanel extends JPanel {

        private final int arc;

        private RoundedPanel(Color color, int arc) {
            this.arc = arc;
            setOpaque(false);
            setBackground(color);
            setLayout(new BorderLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.dispose();
        }
    }

    /** Botón plano y redondeado, sin depender de una biblioteca externa. */
    private static final class RoundedButton extends JButton {

        private RoundedButton(String text) {
            super(text);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color colorBase = getBackground() == null ? COLOR_BOTON : getBackground();
            Color colorFondo = colorBase;
            if (!isEnabled()) {
                colorFondo = colorBase.darker();
            } else if (getModel().isPressed()) {
                colorFondo = colorBase.darker();
            } else if (getModel().isRollover()) {
                colorFondo = colorBase.brighter();
            }

            g2.setColor(colorFondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);

            String texto = getText();
            if (texto != null) {
                FontMetrics metricas = g2.getFontMetrics(getFont());
                int x = (getWidth() - metricas.stringWidth(texto)) / 2;
                int y = (getHeight() - metricas.getHeight()) / 2 + metricas.getAscent();
                g2.setColor(isEnabled() ? getForeground() : Color.DARK_GRAY);
                g2.drawString(texto, x, y);
            }

            if (hasFocus() && isFocusPainted()) {
                g2.setColor(new Color(255, 255, 255, 130));
                g2.drawRoundRect(2, 2, getWidth() - 5, getHeight() - 5, 15, 15);
            }
            g2.dispose();
        }
    }

    /** Avatar mínimo para el jugador local, dibujado sin recursos externos. */
    private static final class AvatarPlaceholder extends JPanel {

        private AvatarPlaceholder(String nombre) {
            setOpaque(false);
            setPreferredSize(new Dimension(52, 52));
            setMinimumSize(new Dimension(52, 52));
            String texto = nombre == null || nombre.isBlank() ? "?" : nombre.substring(0, 1).toUpperCase();
            setToolTipText(texto);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(60, 82, 70));
            g2.fillRoundRect(0, 0, 52, 52, 4, 4);
            g2.setColor(new Color(224, 199, 137));
            g2.fillOval(16, 7, 21, 23);
            g2.fillOval(11, 29, 31, 25);
            g2.setColor(new Color(118, 77, 54));
            g2.fillOval(20, 16, 5, 5);
            g2.fillOval(28, 16, 5, 5);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawLine(21, 26, 31, 26);
            g2.dispose();
        }
    }

    /**
     * Fondo decorativo de cartas, similar al marco del storyboard. Es sólo
     * pintura: los componentes interactivos siguen usando layout managers.
     */
    private static final class FondoLoteria extends JPanel {

        private FondoLoteria() {
            setLayout(new BorderLayout());
            setOpaque(true);
            setBackground(COLOR_FONDO);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            Color azul = new Color(93, 177, 191);
            Color verde = new Color(124, 161, 74);
            Color amarillo = new Color(235, 195, 54);
            Color rojo = new Color(190, 59, 47);

            dibujarCarta(g2, -38, -48, 105, 148, -17, 7, azul);
            dibujarCarta(g2, 78, -66, 94, 136, 9, 3, verde);
            dibujarCarta(g2, ancho - 86, -47, 112, 159, 16, 12, rojo);
            dibujarCarta(g2, ancho - 38, 76, 98, 145, -19, 8, azul);
            dibujarCarta(g2, -45, alto / 2 - 74, 102, 148, 15, 6, amarillo);
            dibujarCarta(g2, ancho - 42, alto / 2 - 48, 94, 137, -14, 9, rojo);
            dibujarCarta(g2, 76, alto - 55, 108, 152, -11, 11, azul);
            dibujarCarta(g2, ancho - 122, alto - 45, 108, 150, 18, 4, verde);

            g2.dispose();
        }

        private void dibujarCarta(Graphics2D g2, int x, int y, int ancho, int alto,
                                   double angulo, int numero, Color acento) {
            AffineTransform transformacion = g2.getTransform();
            g2.translate(x, y);
            g2.rotate(Math.toRadians(angulo), ancho / 2.0, alto / 2.0);

            g2.setColor(COLOR_CARTA);
            g2.fillRoundRect(0, 0, ancho, alto, 6, 6);
            g2.setColor(acento);
            g2.fillRect(6, 6, ancho - 12, Math.max(16, alto / 6));
            g2.setColor(new Color(100, 94, 77));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRect(6, 6, ancho - 13, alto - 13);
            g2.setFont(new Font("SansSerif", Font.BOLD, 11));
            g2.drawString(String.valueOf(numero), 8, alto - 10);
            g2.drawLine(10, alto - 35, ancho - 12, alto - 35);

            g2.setTransform(transformacion);
        }
    }
}
