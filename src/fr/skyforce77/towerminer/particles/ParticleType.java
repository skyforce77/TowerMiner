package fr.skyforce77.towerminer.particles;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.render.RenderHelper;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class ParticleType {
	
	public static ParticleType POTION_SPLASH;
	
	public static void createParticleTypes() {
		POTION_SPLASH = new ParticleType("potion_splash") {
			@Override
			public void draw(Graphics2D g2d, SinglePlayer sp, Particle particle) {
				int size = (int)(16*particle.getScale());
				int size2 = size/2;
				g2d.setClip((int)(particle.getX()-size2), (int)(particle.getY()-size2)+TowerMiner.game.CanvasY, size, size);
				float l1 = size2*particle.getTicksLived()/particle.getLiveTime();
				int data = (int)(l1);
				Color color = particle.getColor() == null ? Color.white : particle.getColor();
				g2d.drawImage(RenderHelper.getColoredImage(RessourcesManager.getTexture("splash"), color, 0.4f), (int)(particle.getX())-size2-(size*data), (int)(particle.getY())-size2+TowerMiner.game.CanvasY, (int)(128*particle.getScale()), size, null);
			}
		};
	}
	
	private String name;
	
	ParticleType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void onTick(Particle particle) {}
	
	public void draw(Graphics2D g2d, SinglePlayer sp, Particle particle) {}

}
