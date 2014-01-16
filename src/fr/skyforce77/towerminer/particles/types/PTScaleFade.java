package fr.skyforce77.towerminer.particles.types;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.particles.Particle;
import fr.skyforce77.towerminer.particles.ParticleType;
import fr.skyforce77.towerminer.render.RenderHelper;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class PTScaleFade extends ParticleType{
	
	private String texture;
	
	public PTScaleFade(String name, String texture) {
		super(name);
		this.texture = texture;
	}
	
	@Override
	public void draw(Graphics2D g2d, SinglePlayer sp, Particle particle) {
		float l1 = 100*particle.getTicksLived()/particle.getLiveTime();
		float scale = particle.getScale()*(1-(l1/100));
		int size = (int)(16*scale);
		int size2 = size/2;
		Color color = particle.getColor() == null ? new Color(0,0,0,0) : particle.getColor();
		g2d.drawImage(RenderHelper.getColoredImage(RessourcesManager.getTexture(texture), color, color.getAlpha() == 0 ? 0.4f : color.getAlpha()/255f), (int)(particle.getX())-size2, (int)(particle.getY())-size2+TowerMiner.game.CanvasY, size, size, null);
	}

}
