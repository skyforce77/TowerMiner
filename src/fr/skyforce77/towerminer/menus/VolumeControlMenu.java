package fr.skyforce77.towerminer.menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sun.corba.se.pept.transport.EventHandler;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.api.PluginManager;
import fr.skyforce77.towerminer.api.events.menu.MenuVolumeChangedEvent;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;
import fr.skyforce77.towerminer.save.DataBase;
import fr.skyforce77.towerminer.sounds.Music;

public class VolumeControlMenu extends Menu {

    public JPanel bpanel;
    public JSlider volumeSlider;

    public VolumeControlMenu() {

        bpanel = new JPanel();
        bpanel.setOpaque(false);
        bpanel.setVisible(false);

        volumeSlider = new JSlider(0, 100);
        volumeSlider.setVisible(false);
        volumeSlider.setFont(TowerMiner.getFont(16));
        volumeSlider.setValue((int)(float)DataBase.getValue("volume", 65536F));

        final Menu menu = this;
        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                DataBase.setValue("volume", (float)volumeSlider.getValue());
                PluginManager.callAsyncEvent(new MenuVolumeChangedEvent(menu, (float)volumeSlider.getValue()));
                Music.volumeChange((float)volumeSlider.getValue());
            }
        });

        volumeSlider.setPreferredSize(new Dimension(TowerMiner.game.getWidth(), TowerMiner.game.getHeight() / 14));

        bpanel.add(volumeSlider);

        TowerMiner.game.add(bpanel, BorderLayout.CENTER);
    }

    public void drawMenu(Graphics2D g2d) {
        g2d.drawImage(RessourcesManager.getBackground(), 0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight(), null);
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
        g2d.fillRect(0, TowerMiner.game.getHeight() / 4, TowerMiner.game.getWidth(), TowerMiner.game.getHeight() - (TowerMiner.game.getHeight() / 2));
        g2d.setFont(TowerMiner.getFont(50));
        g2d.setColor(Color.LIGHT_GRAY);
        if(DataBase.getValue("volume") == null || (float)DataBase.getValue("volume") < 0) {
        	DataBase.setValue("volume", 65536F);
        }
        float value = (Float) DataBase.getValue("volume");
        //int volume = (int) (value/65536F*100F);
        g2d.drawString(LanguageManager.getText("menu.about.music") + ": " + value + "%", 10, 240 + (xmove / 5));
        super.drawMenu(g2d);
    }

    @Override
    public void onUnused() {
    	volumeSlider.setVisible(false);
        bpanel.setVisible(false);
    }

    @Override
    public void onUsed() {
    	volumeSlider.setVisible(true);
        bpanel.setVisible(true);
    }
}
