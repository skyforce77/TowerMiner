package fr.skyforce77.towerminer;

import java.applet.Applet;
import java.util.Random;
import java.util.UUID;

import javax.swing.GroupLayout;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

public class TMApplet extends Applet{

	private static final long serialVersionUID = 501814398457129420L;
	
	private JEditorPane pane;
	public static TMApplet instance;

	@Override
	public void init() {
		instance = this;
		TowerMiner.version = "A"+TowerMiner.version;
		pane = new JEditorPane();
		pane.setText("Starting TMApplet...");
		pane.setEditable(false);
		JScrollPane scroll = new JScrollPane(pane);
		add(scroll);
		TowerMiner.startGame(-1, "ok", "windows", "AppletPlayer"+new Random().nextInt(), UUID.randomUUID(), 0);
		
		GroupLayout layout = new GroupLayout(this);
		setLayout(layout);
		
		layout.setHorizontalGroup(layout.createParallelGroup().addComponent(scroll));
		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(scroll));
	}
	
	public void print(String print) {
		pane.setText(pane.getText()+"\n"+print);
	}
}