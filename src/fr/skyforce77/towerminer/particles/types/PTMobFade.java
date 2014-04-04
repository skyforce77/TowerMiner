package fr.skyforce77.towerminer.particles.types;

import java.awt.Graphics2D;
import java.awt.Image;

import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.particles.Particle;
import fr.skyforce77.towerminer.particles.ParticleType;

public class PTMobFade extends ParticleType {

	Image i;
	int x,y;
	
    public PTMobFade(String name, Image i, int x, int y) {
		super(name);
		this.i = i;
		this.x = x;
		this.y = y;
	}

	@Override
    public void draw(Graphics2D g2d, SinglePlayer sp, Particle particle) {
		g2d.setClip((int)particle.getX()+5*x, (int)particle.getY()+5*y, 5*(x+1), 5*(y+1));
        g2d.drawImage(i, (int)particle.getX(), (int)particle.getY(), 30, 30, null);
    }

}
