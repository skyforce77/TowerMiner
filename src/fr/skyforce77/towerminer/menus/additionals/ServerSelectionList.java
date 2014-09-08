package fr.skyforce77.towerminer.menus.additionals;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.MPJoinMenu;
import fr.skyforce77.towerminer.multiplayer.ServerInfos;
import fr.skyforce77.towerminer.protocol.Connect;
import fr.skyforce77.towerminer.protocol.packets.Packet14ServerPing;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerSelectionList extends JFrame {

    private static final long serialVersionUID = -5973702249949001012L;
    public static ServerSelectionList instance;
    public final DefaultListModel<ServerInfos> listm = new DefaultListModel<ServerInfos>();
    public final JList<ServerInfos> list = new JList<ServerInfos>(listm);

    public ServerSelectionList() {
        instance = this;
        setSize(400, 600);
        setVisible(true);
        setTitle(LanguageManager.getText("menu.mp.list"));
        setLocationRelativeTo(TowerMiner.game);

        list.setSize(new Dimension((int) getSize().getWidth() - 10, 120));

        update();

        JButton co = new JButton();
        co.setText(MPJoinMenu.mpjoinmenu.check.getText());
        co.setPreferredSize(new Dimension((int) getSize().getWidth() - 10, 40));
        co.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (list.getSelectedValue() != null) {
                    setVisible(false);
                    dispose();
                    MPJoinMenu.mpjoinmenu.adress.setText(((ServerInfos)list.getSelectedValue()).ip);
                    MPJoinMenu.mpjoinmenu.check.getActionListeners()[0].actionPerformed(arg0);
                }
            }
        });

        final JTextField name = new JTextField();
        name.setPreferredSize(new Dimension((int) getSize().getWidth() - 10, 40));
        final JTextField ip = new JTextField();
        ip.setPreferredSize(new Dimension((int) getSize().getWidth() - 10, 40));
        ip.setToolTipText(LanguageManager.getText("menu.mp.client.adress"));

        JButton ok = new JButton();
        ok.setText(LanguageManager.getText("menu.editor.create"));
        ok.setPreferredSize(new Dimension((int) getSize().getWidth() - 10, 40));
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                ServerInfos.infos.add(new ServerInfos(name.getText(), ip.getText()));
                ServerInfos.save();
                update();
            }
        });

        JButton del = new JButton();
        del.setText(LanguageManager.getText("menu.editor.delete"));
        del.setPreferredSize(new Dimension((int) getSize().getWidth() - 10, 40));
        del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                ServerInfos.infos.remove(list.getSelectedValue());
                ServerInfos.save();
                update();
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JScrollPane(list));
        panel.add(co);
        panel.add(name);
        panel.add(ip);
        panel.add(ok);
        panel.add(del);

        add(panel);

        Connect.initClient();
        new Thread("ServerListPing") {
            public void run() {
                while (isVisible()) {
                    for (ServerInfos inf : ServerInfos.infos) {
                        Connect.connect(inf.ip);
                        Packet14ServerPing ping = new Packet14ServerPing();
                        ping.name = inf.name;
                        ping.sendClientTCP();
                    }
                    try {
                        sleep(10000l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            ;
        }.start();
    }

    public void update() {
        listm.removeAllElements();
        int i = 0;
        for (ServerInfos b : ServerInfos.infos) {
            if (b != null) {
                listm.add(i, b);
                i++;
            }
        }
        list.setCellRenderer(new DefaultListCellRenderer() {
            private static final long serialVersionUID = -8142642427555971032L;

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                JLabel inf = new JLabel();
                JLabel ping = new JLabel();
                JPanel panel = new JPanel();
                ServerInfos info = (ServerInfos) value;
                label.setToolTipText(info.ip);
                if (info.online) {
                    label.setBackground(new Color(20, 120, 20, 50));
                    label.setText(info.name);
                    inf.setText(info.motd);
                    ping.setText(info.ping);
                    panel.add(ping);
                } else {
                    label.setBackground(new Color(120, 20, 20, 50));
                    label.setText(info.name);
                    inf.setText(info.motd);
                }
                panel.add(label);
                panel.add(inf);
                return panel;
            }
        });
    }

}
