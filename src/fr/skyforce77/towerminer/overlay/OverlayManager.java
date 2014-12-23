package fr.skyforce77.towerminer.overlay;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import java.util.Set;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.protocol.save.TMStorage;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class OverlayManager {

	private static LinkedHashMap<String, TMStorage> components = new LinkedHashMap<>();

	public static void addComponent(String id, TMStorage component) {
		components.put(id, component);
		TowerMiner.print("Added component ["+id+"]: "+component.toString(), "Overlay");
	}

	public static void removeComponent(String id) {
		components.remove(id);
		TowerMiner.print("Removed component ["+id+"]", "Overlay");
	}

	public static TMStorage getComponent(String id) {
		return components.get(id);
	}

	public static Set<String> getComponents() {
		return components.keySet();
	}

	public static void clearComponents() {
		components.clear();
		TowerMiner.print("Cleared components", "Overlay");
	}

	public static void drawComponent(Graphics2D g2d, String id) {
		drawComponent(g2d, getComponent(id));
	}

	public static void drawComponent(Graphics2D g2d, TMStorage storage) {
		if(storage == null || storage.getEntry("type") == null) {
			return;
		}

		try {
			Integer type = storage.getInteger("type");
			Integer x = storage.getInteger("x");
			Integer y = storage.getInteger("y");
			
			Color c = new Color(storage.getInteger("color"));
			if(storage.getInteger("alpha") != 0) {
				c = new Color(c.getRed(), c.getGreen(), c.getBlue(), storage.getInteger("alpha"));
			}
			g2d.setColor(c);
			
			if(storage.getInteger("font_size") != 0) {
				g2d.setFont(TowerMiner.getFont(storage.getInteger("font_size")));
			} else {
				g2d.setFont(TowerMiner.getFont(12));
			}
			
			if(type == OverlayType.RECTANGLE) {
				g2d.fillRect(x, y, storage.getInteger("width"), storage.getInteger("height"));
			} else if(type == OverlayType.TEXT) {
				g2d.drawString(storage.getString("text"), x, y);
			} else if(type == OverlayType.IMAGE) {
				g2d.drawImage(RessourcesManager.getDistantImage(storage.getString("url"), "unknown")
						, x, y, storage.getInteger("width"), storage.getInteger("height"), null);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}