package fr.skyforce77.towerminer.menus;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.game.Game;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

import java.awt.*;

public class MoreMenu extends Menu {

    public MoreMenu() {
        super();

        items = new MenuItem[4];

        items[0] = new MenuItem(0, LanguageManager.getText("menu.options"), new Thread() {
            @Override
            public void run() {
                TowerMiner.setMenu(Menu.options);
            }
        });

        items[1] = new MenuItem(1, LanguageManager.getText("menu.about"), new Thread() {
            @Override
            public void run() {
                TowerMiner.setMenu(Menu.about);
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
        g2d.drawString(LanguageManager.getText("menu.more"), 10, 100 + (xmove / 5));
        super.drawMenu(g2d);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onUsed() {
        TowerMiner.game.resize(800, 500);
        TowerMiner.game.setTitle(LanguageManager.getText("towerminer") + " | " + Game.version);
    }
}
