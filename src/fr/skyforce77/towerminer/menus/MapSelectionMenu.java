package fr.skyforce77.towerminer.menus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.File;

import javax.swing.JFileChooser;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class MapSelectionMenu extends Menu {

    public Menu cible = Menu.singleplayer;

    public MapSelectionMenu() {

        items = new MenuItem[4];

        items[0] = new MenuItem(0, Maps.maps[0].getName(), new Thread() {
            @Override
            public void run() {
                Maps.setActualMap(0);
                TowerMiner.setMenu(cible);
            }
        });

        items[1] = new MenuItem(1, Maps.maps[1].getName(), new Thread() {
            @Override
            public void run() {
                Maps.setActualMap(1);
                TowerMiner.setMenu(cible);
            }
        });

        items[2] = new MenuItem(2, Maps.maps[2].getName(), new Thread() {
            @Override
            public void run() {
                Maps.setActualMap(2);
                TowerMiner.setMenu(cible);
            }
        });

        items[3] = new MenuItem(3, LanguageManager.getText("menu.editor.load"), new Thread() {
            @Override
            public void run() {
                JFileChooser fc = new JFileChooser(RessourcesManager.getMapsDirectory());
                fc.setSelectedFile(new File(RessourcesManager.getMapsDirectory(), Maps.getActualMap().getName() + ".mtmap"));
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int access = fc.showOpenDialog(TowerMiner.game);
                if (access == JFileChooser.APPROVE_OPTION) {
                    File f = fc.getSelectedFile();
                    if (f.getPath().endsWith(".mtmap")) {
                        Maps.maps[1023] = Maps.deserialize(f);
                        Maps.setActualMap(1023);
                        TowerMiner.setMenu(cible);
                    }
                }
            }
        });
    }

    public void setCible(Menu m) {
        cible = m;
    }

    public void drawMenu(Graphics2D g2d) {
        g2d.drawImage(RessourcesManager.getBackground(), 0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight(), null);
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
        g2d.fillRect(0, 0, TowerMiner.game.getWidth() / 2, TowerMiner.game.getHeight() * 2);
        g2d.setFont(TowerMiner.getFont(40));
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString(LanguageManager.getText("menu.maps"), 10, 100 + (xmove / 5));
        super.drawMenu(g2d);
    }
}
