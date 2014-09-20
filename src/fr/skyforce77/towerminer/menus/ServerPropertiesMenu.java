package fr.skyforce77.towerminer.menus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;
import fr.skyforce77.towerminer.save.DataBase;

public class ServerPropertiesMenu extends Menu {

    public JPanel bpanel;
    public JTextField name;
    public JTextArea desc;
    public JButton check;

    public ServerPropertiesMenu() {

        bpanel = new JPanel();
        bpanel.setOpaque(false);
        GroupLayout layout = new GroupLayout(bpanel);
        bpanel.setLayout(layout);
        bpanel.setVisible(false);

        name = new JTextField((String)DataBase.getValue("server_name", "%p's server"));
        name.setFont(TowerMiner.getFont(16));
        
        desc = new JTextArea((String)DataBase.getValue("server_desc","Welcome to my local server!\nHave a good time"));
        desc.setFont(TowerMiner.getFont(16));
        
        check = new JButton(LanguageManager.getText("menu.editor.edit"));
        check.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				DataBase.setValue("server_name", name.getText());
				DataBase.setValue("server_desc", desc.getText());
			}
		});

        name.setPreferredSize(new Dimension(TowerMiner.game.getWidth()-100, TowerMiner.game.getHeight() / 14));
        desc.setPreferredSize(new Dimension(TowerMiner.game.getWidth()-100, TowerMiner.game.getHeight() / 4));
        check.setPreferredSize(new Dimension(TowerMiner.game.getWidth()-100, TowerMiner.game.getHeight() / 14));
        
        layout.setVerticalGroup(layout.createSequentialGroup().addGap(TowerMiner.game.getHeight() / 4)
        		.addComponent(name).addComponent(desc).addComponent(check));
		layout.setHorizontalGroup(layout.createParallelGroup()
				.addComponent(name).addComponent(desc)
				.addComponent(check, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));

        bpanel.add(name);
        bpanel.add(desc);
        bpanel.add(check);

        TowerMiner.game.add(bpanel, BorderLayout.CENTER);
    }

    public void drawMenu(Graphics2D g2d) {
        g2d.drawImage(RessourcesManager.getBackground(), 0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight(), null);
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
        g2d.fillRect(0, TowerMiner.game.getHeight() / 4, TowerMiner.game.getWidth(), TowerMiner.game.getHeight() - (TowerMiner.game.getHeight() / 2));
        g2d.setFont(TowerMiner.getFont(50));
        super.drawMenu(g2d);
    }

    @Override
    public void onUnused() {
        bpanel.setVisible(false);
    }

    @Override
    public void onUsed() {
        bpanel.setVisible(true);
    }
}
