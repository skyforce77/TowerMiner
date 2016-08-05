package fr.skyforce77.towerminer.achievements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Date;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.protocol.packets.Packet;
import fr.skyforce77.towerminer.protocol.packets.Packet3Action;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;
import fr.skyforce77.towerminer.save.DataBase;

public class Achievement extends Popup {

    private static final long serialVersionUID = -3665505443733804552L;
    public int id;

    public Achievement(int id) {
        super(getStaticText(id), 6000L);
        Achievements.achievements.put(id, this);
        this.id = id;
    }

    public Achievement(int id, String icon) {
        super(getStaticText(id), 6000L, icon);
        Achievements.achievements.put(id, this);
        this.id = id;
    }

    public boolean has() {
        return DataBase.hasPermission("achievement.has." + id);
    }

    public void give() {
        DataBase.addPermission("achievement.has." + id);
        TowerMiner.game.displayPopup(this);
        Packet3Action p = new Packet3Action("achievement");
        p.data = Packet.serialize(id);
        p.sendAllTCP();
    }

    public String getText() {
        return Achievement.getStaticText(id);
    }
    
    public String getDesc() {
        return LanguageManager.getText("achievement.desc." + id);
    }

    private static String getStaticText(int id) {
        return LanguageManager.getText("achievement.text." + id);
    }

    @Override
    public void draw(Graphics2D g2d, int CanvasY) {
        int difference = (int) ((time + displayed) - new Date().getTime());

        g2d.setFont(TowerMiner.getFont(22));
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int hgt = metrics.getHeight();
        int adv = metrics.stringWidth(text);
        if (metrics.stringWidth(LanguageManager.getText("achievement.text")) > adv)
            adv = metrics.stringWidth(LanguageManager.getText("achievement.text"));
        Dimension size = new Dimension(adv + 2, (hgt + 2) * 2);

        int x = (int) (TowerMiner.game.getWidth() - size.getWidth());
        int y = CanvasY;

        if (difference < size.getHeight() * 10) {
            y = y - ((int) size.getHeight() - difference / 10);
        } else if (displayed - difference < size.getHeight() * 10) {
            y = y - ((int) size.getHeight() - (int) ((displayed - difference) / 10));
        }

        g2d.setColor(new Color(0, 0, 0, 150));

        if (icon == null) {
            g2d.fillRect(x - 8, y, (int) size.getWidth() + 10, (int) size.getHeight());
        } else {
            g2d.fillRect((int) (x - 8 - size.getHeight()), y, (int) (size.getWidth() + 10 + size.getHeight()), (int) size.getHeight());
            g2d.setColor(new Color(255, 255, 255, 30));
            g2d.fillRect((int) (x - 8 - size.getHeight()), y, (int) size.getHeight(), (int) size.getHeight() - 1);
            g2d.setColor(Color.BLACK);
            g2d.drawRect((int) (x - 8 - size.getHeight()), y, (int) size.getHeight(), (int) size.getHeight() - 1);
            g2d.drawImage(icon, (int) (x - 8 - size.getHeight() + 10), y + 10, (int) size.getHeight() - 20, (int) size.getHeight() - 20, null);
        }

        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x - 4, y + (int) size.getHeight() - 4);

        g2d.setColor(Color.ORANGE);
        g2d.drawString(LanguageManager.getText("achievement.text"), x - 4, y + (int) size.getHeight() / 2 - 4);
    }
}
