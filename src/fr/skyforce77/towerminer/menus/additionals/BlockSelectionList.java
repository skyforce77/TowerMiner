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

import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class BlockSelectionList extends JFrame{

	private static final long serialVersionUID = -5973702249949001012L;

	public BlockSelectionList() {
		setSize(200, 300);
		setVisible(true);
		setTitle(LanguageManager.getText("menu.editor.edit.block"));
		
		DefaultListModel<Blocks> listm = new DefaultListModel<Blocks>();
		final JList<Blocks> list = new JList<Blocks>(listm);
		int i = 0;
		for(Blocks b : Blocks.blocks) {
			if(b != null && b.isVisible() && !(b.getId() == 1023 || b.getId() == 1021)) {
				listm.add(i, b);
				i++;
			}
		}
		list.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = -8142642427555971032L;

			@Override
		    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        label.setText(((Blocks)value).getId()+"");
		        label.setIcon(((Blocks)value).getRender().getListIcon((Blocks)value));
		        return label;
		    }
		});
		
		JButton ok = new JButton();
		ok.setText(LanguageManager.getText("menu.editor.edit"));
		ok.setPreferredSize(new Dimension(190,40));
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new BlockCreator(Maps.getActualMap(), list.getSelectedValue());
				setVisible(false);
				dispose();
			}
		});
		
		JButton del = new JButton();
		del.setText(LanguageManager.getText("menu.editor.delete"));
		del.setPreferredSize(new Dimension(190,40));
		del.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Maps.getActualMap().removeBlock(list.getSelectedValue());
				Blocks.addMapBlocks(Maps.getActualMap());
				setVisible(false);
				dispose();
			}
		});
		
		JPanel panel = new JPanel();
		panel.add(new JScrollPane(list));
		panel.add(ok);
		panel.add(del);
		
		add(panel);
	}

}
