package fr.skyforce77.towerminer.menus.additionals;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import fr.skyforce77.towerminer.maps.Maps;

public class ColorSelection extends JFrame implements ChangeListener{

	private static final long serialVersionUID = 84545256561L;

	public JColorChooser chooser;

	public ColorSelection() {
		setSize(700, 500);
		setVisible(true);

		chooser = new JColorChooser(Maps.getActualMap().getColorModifier());
		chooser.getSelectionModel().addChangeListener(this);

		add(chooser);
	}

	public void stateChanged(ChangeEvent e) {
		Maps.getActualMap().setColorModifier(chooser.getColor());
	}
}
