package fr.skyforce77.towerminer.particles.types;

import java.awt.Graphics2D;

import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.particles.Particle;
import fr.skyforce77.towerminer.particles.ParticleType;

public class PTBlockBreak extends ParticleType {

	Blocks b;
	int data,x,y;
	
    public PTBlockBreak(String name, Blocks b, int data, int x, int y) {
		super(name);
		this.b = b;
		this.data = data;
		this.x = x;
		this.y = y;
	}

	@Override
    public void draw(Graphics2D g2d, SinglePlayer sp, Particle particle) {
		g2d.setClip((int)particle.getX()+5*x, (int)particle.getY()+5*y, 5*(x+1), 5*(y+1));
		b.getRender().onBlockRender(b, g2d, data, (int)particle.getX(), (int)particle.getY(), 30);
    }

}
