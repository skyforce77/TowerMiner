package fr.skyforce77.towerminer.menus;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;
import fr.skyforce77.towerminer.save.DataBase;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class VolumeControlMenu extends Menu {

    public JPanel bpanel;
    public JSlider check;

    public VolumeControlMenu() {

        bpanel = new JPanel();
        bpanel.setOpaque(false);
        bpanel.setVisible(false);

        check = new JSlider(0, 100);
        check.setVisible(false);
        check.setFont(new Font("TimesRoman", Font.BOLD, 16));
        check.setValue((int) (100 + (Float) DataBase.getValue("volume", -10F)));

        check.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                DataBase.setValue("volume", -(float) (100 - check.getValue()));
            }
        });

        check.setPreferredSize(new Dimension(TowerMiner.game.getWidth(), TowerMiner.game.getHeight() / 14));

        bpanel.add(check);

        TowerMiner.game.add(bpanel, BorderLayout.CENTER);
    }

    public void drawMenu(Graphics2D g2d) {
        g2d.drawImage(RessourcesManager.getBackground(), 0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight(), null);
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
        g2d.fillRect(0, TowerMiner.game.getHeight() / 4, TowerMiner.game.getWidth(), TowerMiner.game.getHeight() - (TowerMiner.game.getHeight() / 2));
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 50));
        g2d.setColor(Color.LIGHT_GRAY);
        int volume = (int) (100 + (Float) DataBase.getValue("volume"));
        g2d.drawString(LanguageManager.getText("menu.about.music") + ": " + volume + "%", 10, 240 + (xmove / 5));
        super.drawMenu(g2d);
    }

    @Override
    public void onUnused() {
        check.setVisible(false);
        bpanel.setVisible(false);
    }

    @Override
    public void onUsed() {
        check.setVisible(true);
        bpanel.setVisible(true);
    }
}
