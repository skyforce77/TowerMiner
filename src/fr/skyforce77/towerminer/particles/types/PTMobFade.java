package fr.skyforce77.towerminer.particles.types;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;

import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.menus.Menu;
import fr.skyforce77.towerminer.particles.Particle;
import fr.skyforce77.towerminer.particles.ParticleType;

public class PTMobFade extends ParticleType {

	Image i;
	int x,y;
    boolean translucent = false;

    public PTMobFade(String name, Image i, int x, int y, int translucent) {
        super(name);
		this.i = i;
		this.x = x;
		this.y = y;
        this.translucent = translucent == 1;
    }

	@Override
    public void draw(Graphics2D g2d, Menu m, Particle particle) {
        if (translucent) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }

		if(i.equals(EntityTypes.SNOWBALL.getTexture(0))) {
			g2d.setClip((int)particle.getX()+2*x, (int)particle.getY()+2*y, 2*(x+1), 2*(y+1));
			g2d.drawImage(i, (int)particle.getX(), (int)particle.getY(), 10, 10, null);
		} else {
			g2d.setClip((int)particle.getX()+5*x, (int)particle.getY()+5*y, 5*(x+1), 5*(y+1));
			g2d.drawImage(i, (int)particle.getX(), (int)particle.getY(), 30, 30, null);
		}
    }

}
