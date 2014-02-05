package fr.skyforce77.towerminer.ressources;

import fr.skyforce77.towerminer.TowerMiner;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class RessourcesManager {

    private static HashMap<String, Image> imgurls = new HashMap<String, Image>();
    private static ArrayList<String> dl = new ArrayList<String>();

    public RessourcesManager() {
        super();
    }

    public static Image getDistantImage(final String url, String defaultt) {
        return getDistantImage(url, getTexture(defaultt));
    }
    
    public static Image getDistantImage(final String url, Image defaultt) {
        if (imgurls.containsKey(url)) {
            return imgurls.get(url);
        } else {
            if (!dl.contains(url)) {
                new Thread() {
                    @Override
                    public void run() {
                        dl.add(url);
                        try {
                            Image img = ImageIO.read(new URL(url));
                            imgurls.put(url, img);
                        } catch (Exception e) {
                            dl.remove(url);
                        }
                    }
                }.start();
            }
            return defaultt;
        }
    }

    public static Image getTexture(String texture) {
        Image image = new ImageIcon(TowerMiner.class.getResource("/ressources/textures/" + texture + ".png")).getImage();
        return image;
    }

    public static ImageIcon getIconTexture(String texture) {
        return new ImageIcon(TowerMiner.class.getResource("/ressources/textures/" + texture + ".png"));
    }

    public static Image getIcon() {
        Image image = new ImageIcon(TowerMiner.class.getResource("/ressources/icon.png")).getImage();
        return image;
    }

    public static Image getBackground() {
        Image image = new ImageIcon(TowerMiner.class.getResource("/ressources/background.png")).getImage();
        return image;
    }

    public static URL getSoundURL(String file) {
        return TowerMiner.class.getResource("/ressources/sounds/" + file + ".wav");
    }

    public static URL getURL(String file) {
        return TowerMiner.class.getResource(file);
    }

    public static URL getMap(String map) {
        return TowerMiner.class.getResource("/ressources/maps/" + map + ".mtmap");
    }

    public static File getDirectory() {
        String OS = System.getProperty("os.name").toUpperCase();
        if (OS.contains("WIN"))
            return new File(System.getenv("APPDATA"), "/.towerminer");
        else if (OS.contains("MAC"))
            return new File(System.getProperty("user.home") + "/Library/Application "
                    + "Support", "/.towerminer");
        else if (OS.contains("NUX"))
            return new File(System.getProperty("user.home"), "/.towerminer");
        return new File(System.getProperty("user.dir"), "/.towerminer");
    }

    public static File getMapsDirectory() {
        return new File(getDirectory(), "/maps");
    }

    public static File getLanguagesDirectory() {
        return new File(getDirectory(), "/languages");
    }

    public static File getSaveDirectory() {
        return new File(getDirectory(), "/internal");
    }
}
