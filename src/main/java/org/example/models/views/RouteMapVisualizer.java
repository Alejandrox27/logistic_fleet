package org.example.models.views;

import org.example.models.graphs.City;
import org.example.models.graphs.CityGraph;
import org.example.models.graphs.Road;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class RouteMapVisualizer {

    // ── Posición y velocidad de cada nodo para el layout de fuerza ──────────
    private static class NodeState {
        double x, y, vx, vy;
        NodeState(double x, double y) { this.x = x; this.y = y; }
    }

    // ── Panel que dibuja y gestiona interacción ──────────────────────────────
    private static class GraphPanel extends JPanel {

        private final Map<Integer, NodeState> positions = new HashMap<>();
        private final Map<Integer, String>    labels    = new HashMap<>();
        private final List<int[]>             edges     = new ArrayList<>();   // [id1, id2, distKm]
        private final Map<Integer, List<int[]>> adjEdges = new HashMap<>();

        // Zoom & Pan
        private double scale      = 1.0;
        private double offsetX    = 0;
        private double offsetY    = 0;
        private Point  lastDrag;

        // Arrastre de nodo
        private Integer draggingNode = null;

        // Simulación de fuerzas
        private volatile boolean running = true;

        // Constantes de fuerza
        private static final double REPULSION  = 8_000;
        private static final double SPRING_LEN = 120;
        private static final double SPRING_K   = 0.05;
        private static final double DAMPING    = 0.85;
        private static final int    NODE_R     = 14;   // radio del nodo en px

        GraphPanel() {
            setBackground(new Color(18, 22, 34));

            // ── Zoom con rueda del ratón ─────────────────────────────────────
            addMouseWheelListener(e -> {
                double factor = (e.getWheelRotation() < 0) ? 1.1 : 1 / 1.1;
                // Zoom centrado en la posición del cursor
                double mx = e.getX(), my = e.getY();
                offsetX = mx - factor * (mx - offsetX);
                offsetY = my - factor * (my - offsetY);
                scale  *= factor;
                repaint();
            });

            // ── Pan y arrastre de nodos ──────────────────────────────────────
            MouseAdapter ma = new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) {
                    // ¿Hay un nodo bajo el cursor?
                    draggingNode = nodeAt(e.getX(), e.getY());
                    lastDrag = e.getPoint();
                }
                @Override public void mouseDragged(MouseEvent e) {
                    if (draggingNode != null) {
                        // Mover nodo en coordenadas del grafo
                        NodeState ns = positions.get(draggingNode);
                        ns.x += (e.getX() - lastDrag.x) / scale;
                        ns.y += (e.getY() - lastDrag.y) / scale;
                        ns.vx = ns.vy = 0;
                    } else {
                        // Pan del canvas
                        offsetX += e.getX() - lastDrag.x;
                        offsetY += e.getY() - lastDrag.y;
                    }
                    lastDrag = e.getPoint();
                    repaint();
                }
                @Override public void mouseReleased(MouseEvent e) {
                    draggingNode = null;
                }
            };
            addMouseListener(ma);
            addMouseMotionListener(ma);
        }

        // ── Carga de datos ───────────────────────────────────────────────────
        void loadGraph(CityGraph cityGraph, int panelW, int panelH) {
            Map<City, List<Road>> adj = cityGraph.getAdjacencyList();
            Random rng = new Random(42);

            for (City c : adj.keySet()) {
                int id = c.getIdCity();
                // Posición inicial aleatoria centrada en el panel
                positions.put(id, new NodeState(
                        panelW * 0.1 + rng.nextDouble() * panelW * 0.8,
                        panelH * 0.1 + rng.nextDouble() * panelH * 0.8));
                labels.put(id, c.getName());
                adjEdges.put(id, new ArrayList<>());
            }

            Set<String> seen = new HashSet<>();
            for (Map.Entry<City, List<Road>> entry : adj.entrySet()) {
                int from = entry.getKey().getIdCity();
                for (Road road : entry.getValue()) {
                    int to   = road.getDestination().getIdCity();
                    int dist = (int) road.getDistance();
                    String key = Math.min(from, to) + "-" + Math.max(from, to);
                    if (seen.add(key)) {
                        edges.add(new int[]{from, to, dist});
                        adjEdges.get(from).add(new int[]{to,   dist});
                        if (adjEdges.containsKey(to))
                            adjEdges.get(to).add(new int[]{from, dist});
                    }
                }
            }
        }

        // ── Devuelve el id del nodo bajo (sx, sy) o null ─────────────────────
        private Integer nodeAt(int sx, int sy) {
            for (Map.Entry<Integer, NodeState> e : positions.entrySet()) {
                double wx = e.getValue().x * scale + offsetX;
                double wy = e.getValue().y * scale + offsetY;
                double r  = NODE_R * scale;
                if (Math.hypot(sx - wx, sy - wy) <= r)
                    return e.getKey();
            }
            return null;
        }

        // ── Un paso del layout de fuerzas ────────────────────────────────────
        void stepForces() {
            Integer[] ids = positions.keySet().toArray(new Integer[0]);
            Map<Integer, double[]> forces = new HashMap<>();
            for (int id : ids) forces.put(id, new double[]{0, 0});

            // Repulsión entre todos los nodos
            for (int i = 0; i < ids.length; i++) {
                for (int j = i + 1; j < ids.length; j++) {
                    NodeState a = positions.get(ids[i]);
                    NodeState b = positions.get(ids[j]);
                    double dx = a.x - b.x, dy = a.y - b.y;
                    double dist = Math.max(1, Math.hypot(dx, dy));
                    double f = REPULSION / (dist * dist);
                    forces.get(ids[i])[0] += f * dx / dist;
                    forces.get(ids[i])[1] += f * dy / dist;
                    forces.get(ids[j])[0] -= f * dx / dist;
                    forces.get(ids[j])[1] -= f * dy / dist;
                }
            }

            // Atracción por aristas (muelle)
            for (int[] edge : edges) {
                NodeState a = positions.get(edge[0]);
                NodeState b = positions.get(edge[1]);
                if (a == null || b == null) continue;
                double dx = b.x - a.x, dy = b.y - a.y;
                double dist = Math.max(1, Math.hypot(dx, dy));
                double f = SPRING_K * (dist - SPRING_LEN);
                double fx = f * dx / dist, fy = f * dy / dist;
                forces.get(edge[0])[0] += fx;
                forces.get(edge[0])[1] += fy;
                forces.get(edge[1])[0] -= fx;
                forces.get(edge[1])[1] -= fy;
            }

            // Aplicar fuerzas + amortiguación
            for (int id : ids) {
                if (draggingNode != null && draggingNode == id) continue;
                NodeState ns = positions.get(id);
                double[] f = forces.get(id);
                ns.vx = (ns.vx + f[0]) * DAMPING;
                ns.vy = (ns.vy + f[1]) * DAMPING;
                ns.x += ns.vx;
                ns.y += ns.vy;
            }
        }

        // ── Pintura ──────────────────────────────────────────────────────────
        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Aplicar transformación de vista
            g.translate(offsetX, offsetY);
            g.scale(scale, scale);

            // ── Aristas ──────────────────────────────────────────────────────
            g.setStroke(new BasicStroke(1.5f));
            Font edgeFont = new Font("SansSerif", Font.PLAIN, 9);
            g.setFont(edgeFont);

            for (int[] edge : edges) {
                NodeState a = positions.get(edge[0]);
                NodeState b = positions.get(edge[1]);
                if (a == null || b == null) continue;

                // Línea
                g.setColor(new Color(100, 140, 200, 120));
                g.drawLine((int) a.x, (int) a.y, (int) b.x, (int) b.y);

                // Etiqueta de distancia (solo si el zoom es suficiente)
                if (scale > 0.6) {
                    double mx = (a.x + b.x) / 2, my = (a.y + b.y) / 2;
                    g.setColor(new Color(230, 100, 80));
                    String lbl = edge[2] + " km";
                    FontMetrics fm = g.getFontMetrics();
                    g.drawString(lbl,
                            (int) mx - fm.stringWidth(lbl) / 2,
                            (int) my - 3);
                }
            }

            // ── Nodos ─────────────────────────────────────────────────────────
            int r = NODE_R;
            Font nodeFont = new Font("SansSerif", Font.BOLD, 11);
            g.setFont(nodeFont);

            for (Map.Entry<Integer, NodeState> entry : positions.entrySet()) {
                int id = entry.getKey();
                NodeState ns = entry.getValue();
                int cx = (int) ns.x, cy = (int) ns.y;

                // Sombra
                g.setColor(new Color(0, 0, 0, 60));
                g.fillOval(cx - r + 2, cy - r + 2, r * 2, r * 2);

                // Relleno
                boolean isDragging = Objects.equals(draggingNode, id);
                g.setColor(isDragging ? new Color(80, 200, 120)
                        : new Color(52, 152, 219));
                g.fillOval(cx - r, cy - r, r * 2, r * 2);

                // Borde
                g.setColor(new Color(200, 230, 255));
                g.setStroke(new BasicStroke(1.5f));
                g.drawOval(cx - r, cy - r, r * 2, r * 2);

                // Etiqueta del nodo
                if (scale > 0.4) {
                    String lbl = labels.getOrDefault(id, String.valueOf(id));
                    FontMetrics fm = g.getFontMetrics();
                    int tx = cx - fm.stringWidth(lbl) / 2;
                    int ty = cy + r + fm.getAscent() + 2;
                    g.setColor(new Color(30, 30, 30, 160));
                    g.fillRoundRect(tx - 2, ty - fm.getAscent(),
                            fm.stringWidth(lbl) + 4, fm.getHeight(), 4, 4);
                    g.setColor(Color.WHITE);
                    g.drawString(lbl, tx, ty);
                }
            }
        }
    }

    // ── Punto de entrada público ─────────────────────────────────────────────
    public void drawGraph(CityGraph cityGraph) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Logistic Route Map");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);

            GraphPanel panel = new GraphPanel();
            frame.add(panel);
            frame.setVisible(true);

            // Cargar datos después de que el panel tenga tamaño real
            SwingUtilities.invokeLater(() -> {
                panel.loadGraph(cityGraph, panel.getWidth(), panel.getHeight());

                // ── Loop de simulación en hilo separado ───────────────────
                Thread sim = new Thread(() -> {
                    while (panel.running) {
                        panel.stepForces();
                        panel.repaint();
                        try { Thread.sleep(16); } // ~60 fps
                        catch (InterruptedException ex) { break; }
                    }
                });
                sim.setDaemon(true);   // muere con la JVM
                sim.start();
            });
        });
    }
}