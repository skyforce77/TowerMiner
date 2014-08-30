package fr.skyforce77.towerminer.menus;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.game.Game;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class AboutMenu extends Menu {

    public AboutMenu() {
        super();
    }

    public void drawMenu(Graphics2D g2d) {
        g2d.drawImage(RessourcesManager.getBackground(), 0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight(), null);
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
        g2d.fillRect(0, TowerMiner.game.getHeight() / 4, TowerMiner.game.getWidth(), TowerMiner.game.getHeight() - (TowerMiner.game.getHeight() / 2));
        g2d.setFont(TowerMiner.getFont(40));
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString(LanguageManager.getText("menu.about"), 10, 100 + (xmove / 5));

        g2d.setFont(TowerMiner.getFont(28));
        g2d.drawString(LanguageManager.getText("menu.about.music"), 10, 180);
        g2d.setFont(TowerMiner.getFont(22));
        g2d.drawString(LanguageManager.getText("menu.about.music.text"), 10, 230);

        g2d.setFont(TowerMiner.getFont(28));
        g2d.drawString(LanguageManager.getText("menu.about.textures"), 10, 300);
        g2d.setFont(TowerMiner.getFont(22));
        g2d.drawString(LanguageManager.getText("menu.about.textures.text"), 10, 350);
        super.drawMenu(g2d);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onUsed() {
        TowerMiner.game.resize(800, 500);
        TowerMiner.game.setTitle(LanguageManager.getText("towerminer") + " | " + Game.version);
    }
}
