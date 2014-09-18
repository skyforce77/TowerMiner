package fr.skyforce77.towerminer.ressources;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import fr.skyforce77.towerminer.TowerMiner;

public class RessourcesManager {

    private static HashMap<String, Image> imgurls = new HashMap<String, Image>();
    private static ArrayList<String> dl = new ArrayList<String>();
    private static int background = new Random().nextInt(2);

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
                new Thread("ImageDownload") {
                    @Override
                    public void run() {
                        dl.add(url);
                        try {
                            Image img = ImageIO.read(new URL(url));
                            imgurls.put(url, img);
                        } catch (Exception e) {}
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
    
    public static BufferedImage getBufferedTexture(String texture) {
        try {
			return ImageIO.read(TowerMiner.class.getResource("/ressources/textures/" + texture + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        return getBufferedTexture("unknown");
    }
    
    public static BufferedImage getDistantBufferedTexture(String url) {
        try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
        return getBufferedTexture("unknown");
    }

    public static ImageIcon getIconTexture(String texture) {
        return new ImageIcon(TowerMiner.class.getResource("/ressources/textures/" + texture + ".png"));
    }
    
    public static ImageIcon getGifIconTexture(String texture) {
        return new ImageIcon(TowerMiner.class.getResource("/ressources/textures/" + texture + ".gif"));
    }

    public static Image getIcon() {
        Image image = new ImageIcon(TowerMiner.class.getResource("/ressources/icon.png")).getImage();
        return image;
    }

    public static Image getBackground() {
        Image image = new ImageIcon(TowerMiner.class.getResource("/ressources/background"+background+".png")).getImage();
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
    
    public static File getPluginsDirectory() {
        return new File(getDirectory(), "/plugins");
    }
    
    public static File getServerPluginsDirectory() {
        return new File(getDirectory(), "/serverplugins");
    }

    public static File getLanguagesDirectory() {
        return new File(getDirectory(), "/languages");
    }

    public static File getSaveDirectory() {
        return new File(getDirectory(), "/internal");
    }
    
    public static File getTempDirectory() {
        return new File(getDirectory(), "/temp");
    }
}
