package fr.skyforce77.towerminer.game;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Popup;
import fr.skyforce77.towerminer.blocks.renders.BlockRender;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.protocol.packets.Packet12Popup;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

public class Game extends JFrame implements MouseListener, MouseWheelListener, MouseMotionListener, WindowListener, KeyListener {

    private static final long serialVersionUID = 1L;

    public static String version = "Beta 0.1b";
    public boolean fpsdisplay = TowerMiner.dev;
    public static boolean offline = false;

    public int CanvasX = 0;
    public int CanvasY = 40;
    public int CANVAS_WIDTH;
    public int CANVAS_HEIGHT;

    public boolean terminated = false;

    public int Xblocks;
    public int Yblocks;

    public GameCanvas canvas;

    public int frames = 0;
    public int fps = 0;
    public int freeze = 10;

    public Popup popup;

    public Game() {
        onInit();

        canvas = new GameCanvas();
        canvas.addKeyListener(this);
        canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        this.setContentPane(canvas);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setTitle(LanguageManager.getText("towerminer") + " | " + version);
        this.setIconImage(RessourcesManager.getIcon());
        this.setVisible(false);
        this.setResizable(false);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.addKeyListener(this);

        onStart();

        new Thread() {
            public void run() {
                try {
                    while (!terminated) {
                        sleep(1000l);
                        fps = frames;
                        frames = 0;

                        if (fps < 70) {
                            freeze--;
                        } else if (fps > 70) {
                            freeze++;
                        }
                    }
                } catch (Exception e) {
                }
            }

            ;
        }.start();
    }

    public void onInit() {
        Xblocks = Maps.getActualMap().getXMax();
        Yblocks = Maps.getActualMap().getYMax();
        CANVAS_WIDTH = Xblocks * 48 + CanvasX;
        CANVAS_HEIGHT = Yblocks * 48 + CanvasY;
    }

    public void onStart() {
        new Thread() {
            public void run() {
                while (!terminated) {
                    if (TowerMiner.menu != null) {
                        TowerMiner.menu.onTick();
                        for (BlockRender r : BlockRender.renders) {
                            if (r != null) {
                                r.onGameTick();
                            }
                        }
                    }

                    try {
                        if (TowerMiner.menu instanceof SinglePlayer && ((SinglePlayer) TowerMiner.menu).speed.isSelected()) {
                            Thread.sleep(10l);
                        } else {
                            Thread.sleep(20l);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            ;
        }.start();

        new Thread() {
            public void run() {
                while (!terminated) {
                    repaint();
                    frames++;

                    try {
                        Thread.sleep(freeze);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            ;
        }.start();
    }

    public class GameCanvas extends JPanel {

        private static final long serialVersionUID = 1L;

        public GameCanvas() {
            setFocusable(true);
            requestFocus();
        }

        @Override
        public void paintComponent(Graphics g) {
            final Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            super.paintComponent(g2d);
            setBackground(Color.WHITE);
            TowerMiner.menu.drawMenu(g2d);
            g2d.setColor(new Color(0, 0, 0, 180));
            if (fpsdisplay) {
                g2d.fillRect(0, 0, 30, 25);
                g2d.setColor(Color.YELLOW);
                g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 20));
                g2d.drawString(fps + "", 0, 20);
            }
        }
    }

    public void displayPopup(Popup popup) {
        this.popup = popup;
        popup.time = new Date().getTime();
    }

    public void displayMultiPlayerPopup(Popup popup) {
        if (TowerMiner.menu instanceof MultiPlayer) {
            MultiPlayer mp = (MultiPlayer) TowerMiner.menu;
            if (mp.server) {
                new Packet12Popup(popup.text, null, popup.icon).sendClientTCP();
                this.popup = popup;
            }
        } else {
            this.popup = popup;
        }
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        TowerMiner.menu.onKeyPressed(arg0.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        TowerMiner.menu.onKeyReleased(arg0.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        TowerMiner.menu.onKeyTyped(arg0.getKeyChar());
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
        TowerMiner.menu.onWindowActivated(arg0);
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
        TowerMiner.menu.onWindowClosed(arg0);
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        TowerMiner.menu.onWindowClosing(arg0);
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
        TowerMiner.menu.onWindowDeactivated(arg0);
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
        TowerMiner.menu.onWindowDeiconified(arg0);
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
        TowerMiner.menu.onWindowIconified(arg0);
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
        TowerMiner.menu.onWindowOpened(arg0);
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        TowerMiner.menu.onMouseDragged(arg0);
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        TowerMiner.menu.onMouseMoved(arg0);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent arg0) {
        TowerMiner.menu.onMouseWheelMoved(arg0);
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        TowerMiner.menu.onMouseClicked(arg0);
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        TowerMiner.menu.onMouseEntered(arg0);
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        TowerMiner.menu.onMouseExited(arg0);
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        TowerMiner.menu.onMousePressed(arg0);
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        TowerMiner.menu.onMouseReleased(arg0);
    }
}
