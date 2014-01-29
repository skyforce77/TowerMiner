package fr.skyforce77.towerminer.menus;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.additionals.ServerSelectionList;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

import java.awt.*;

public class MPSelectionMenu extends Menu {

    public MPSelectionMenu() {

        items = new MenuItem[3];

        items[0] = new MenuItem(0, LanguageManager.getText("menu.mp.create"), new Thread() {
            @Override
            public void run() {
                Menu.mapselmenu.setCible(Menu.mpservwait);
                TowerMiner.setMenu(Menu.mapselmenu);
            }
        });

        items[1] = new MenuItem(1, LanguageManager.getText("menu.mp.join"), new Thread() {
            @Override
            public void run() {
                TowerMiner.setMenu(Menu.mpjoinmenu);
            }
        });

        items[2] = new MenuItem(2, LanguageManager.getText("menu.mp.list"), new Thread() {
            @Override
            public void run() {
                TowerMiner.setMenu(Menu.mpjoinmenu);
                new ServerSelectionList();
            }
        });
    }

    public void drawMenu(Graphics2D g2d) {
        g2d.drawImage(RessourcesManager.getBackground(), 0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight(), null);
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
        g2d.fillRect(0, 0, TowerMiner.game.getWidth() / 2, TowerMiner.game.getHeight() * 2);
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 40));
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString(LanguageManager.getText("menu.multiplayer"), 10, 100 + (xmove / 5));
        super.drawMenu(g2d);
    }
}
