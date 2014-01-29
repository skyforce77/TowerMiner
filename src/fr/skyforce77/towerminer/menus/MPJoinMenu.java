package fr.skyforce77.towerminer.menus;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MPJoinMenu extends Menu {

    public JPanel panel;
    public JPanel bpanel;
    public JButton check;
    public JTextField adress;

    public MPJoinMenu() {

        panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setVisible(false);

        bpanel = new JPanel();
        bpanel.setOpaque(false);
        bpanel.setVisible(false);

        check = new JButton(LanguageManager.getText("menu.mp.client.join"));
        check.setVisible(false);
        check.setFont(new Font("TimesRoman", Font.BOLD, 16));

        adress = new JTextField();
        adress.setVisible(false);
        adress.setToolTipText(LanguageManager.getText("menu.mp.client.adress"));
        adress.setText("localhost");
        adress.setFont(new Font("TimesRoman", Font.BOLD, 28));

        check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Menu.mpclientwait.ip = adress.getText();
                TowerMiner.setMenu(Menu.mpclientwait);
            }
        });

        adress.setPreferredSize(new Dimension((int) (TowerMiner.game.getWidth() / 1.5), TowerMiner.game.getHeight() / 7));
        check.setPreferredSize(new Dimension(TowerMiner.game.getWidth() / 4, TowerMiner.game.getHeight() / 7));

        panel.setBorder(BorderFactory.createEmptyBorder((int) (TowerMiner.game.getHeight() / 2 - adress.getPreferredSize().getHeight()), 0, (int) (TowerMiner.game.getHeight() / 2 - adress.getPreferredSize().getHeight()), 0));

        bpanel.add(adress);
        bpanel.add(check);

        panel.add(bpanel);
        TowerMiner.game.add(panel, BorderLayout.CENTER);
    }

    public void drawMenu(Graphics2D g2d) {
        g2d.drawImage(RessourcesManager.getBackground(), 0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight(), null);
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
        g2d.fillRect(0, TowerMiner.game.getHeight() / 4, TowerMiner.game.getWidth(), TowerMiner.game.getHeight() - (TowerMiner.game.getHeight() / 2));
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 40));
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString(LanguageManager.getText("menu.multiplayer"), 10, 90 + (xmove / 5));
        super.drawMenu(g2d);
    }

    @Override
    public void onUnused() {
        adress.setVisible(false);
        check.setVisible(false);
        bpanel.setVisible(false);
        panel.setVisible(false);
    }

    @Override
    public void onUsed() {
        adress.setVisible(true);
        check.setVisible(true);
        panel.setVisible(true);
        bpanel.setVisible(true);
    }
}
