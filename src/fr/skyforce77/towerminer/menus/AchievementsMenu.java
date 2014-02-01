package fr.skyforce77.towerminer.menus;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Achievement;
import fr.skyforce77.towerminer.achievements.Achievements;
import fr.skyforce77.towerminer.render.RenderHelper;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class AchievementsMenu extends Menu {

    public AchievementsMenu() {}

    public void drawMenu(Graphics2D g2d) {
        g2d.drawImage(RessourcesManager.getBackground(), 0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight(), null);
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
        
        int x = 0;
        int y = 0;
        Image im = null;
        for(Integer i : Achievements.achievements.keySet()) {
        	Achievement a = Achievements.achievements.get(i);
        	if(!a.has()) {
        		g2d.setColor(Color.LIGHT_GRAY);
        		im = RenderHelper.getColoredImage(a.icon, Color.LIGHT_GRAY, 0.8f);
        	} else {
        		g2d.setColor(Color.YELLOW);
        		im = a.icon;
        	}
        	
        	if(x <= 2) {
        		x++;
        	} else {
        		y++;
        		x = 1;
        	}
        	
        	g2d.setColor(new Color(g2d.getColor().getRed(), g2d.getColor().getGreen(), g2d.getColor().getBlue(), 20));
        	g2d.fillRect(x*200-60, y*120+60, 60, 60);
        	g2d.setColor(new Color(g2d.getColor().getRed(), g2d.getColor().getGreen(), g2d.getColor().getBlue(), 250));
        	g2d.drawRect(x*200-60, y*120+60, 60, 60);
        	g2d.drawImage(im, x*200+10-60, y*120+10+60, 60-20, 60-20, null);
        	g2d.drawString(a.getText(), x*200-60, y*120+140);
        	g2d.drawString(a.getDesc(), x*200-60, y*120+160);
        }
        
        
        super.drawMenu(g2d);
    }
}
