package fr.skyforce77.towerminer.menus.additionals;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class LanguageSelectionList extends JFrame{

	private static final long serialVersionUID = -5973702249949001012L;

	public LanguageSelectionList() {
		setSize(220, 230);
		setVisible(true);
		setTitle(LanguageManager.getText("menu.languages"));
		
		final LanguageSelectionList lsl = this;
		
		DefaultListModel<String> listm = new DefaultListModel<>();
		final JList<String> list = new JList<>(listm);
		int i = 0;
		for(String l : LanguageManager.getLanguages()) {
			listm.add(i, l);
			i++;
		}
		list.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -8142642427555971032L;

			@Override
		    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        label.setText((String)value);
		        return label;
		    }
		});
		list.setSelectedValue(LanguageManager.getActualLanguage(), true);
		list.addListSelectionListener(new ListSelectionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void valueChanged(ListSelectionEvent e) {
				LanguageManager.setLanguage((String)((JList<String>)e.getSource()).getSelectedValue());
				lsl.dispose();
				TowerMiner.game.terminated = true;
				TowerMiner.game.dispose();
				TowerMiner.main(new String[]{"-1", "ok"});
			}
		});
		
		JButton ok = new JButton();
		ok.setText(LanguageManager.getText("menu.quit"));
		ok.setPreferredSize(new Dimension(190,40));
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				dispose();
			}
		});
		
		JPanel panel = new JPanel();
		panel.add(new JScrollPane(list));
		panel.add(ok);
		
		add(panel);
	}

}
