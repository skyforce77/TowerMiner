package fr.skyforce77.towerminer.particles;

import java.awt.Color;

import javax.vecmath.Vector2d;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.protocol.packets.Packet18ParticleEffect;

public class ParticleEffect {
	
	public static void createEffect(int x, int y, int color, int type, int data) {
		if(type == 0) {
			createPotionSplashEffect(((SinglePlayer)TowerMiner.menu), x,  y, new Color(color));
		}
	}

	public static void createPotionSplashEffect(SinglePlayer sp, int x, int y, Color color) {
		if(sp instanceof MultiPlayer && ((MultiPlayer)sp).server) {
			new Packet18ParticleEffect(x, y, color.getRGB(), 0).sendAllTCP();
		}
		
		double d = 2*Math.PI;
		while(d >= 0) {
			sp.particles.add(new Particle(ParticleType.POTION_SPLASH, x+(int)(Math.cos(d)*10), y+(int)(Math.sin(d)*10), new Vector2d(Math.cos(d), Math.sin(d)), 0.6f, color).setLiveTime(50));
			d -= 0.4f;
		}
		
		d = 2*Math.PI;
		while(d >= 0) {
			sp.particles.add(new Particle(ParticleType.POTION_SPLASH, x+(int)(Math.cos(d)*5), y+(int)(Math.sin(d)*5), new Vector2d(Math.cos(d)*0.7, Math.sin(d)*0.7), 0.8f, color).setLiveTime(50));
			d -= 0.6f;
		}
		
		sp.particles.add(new Particle(ParticleType.POTION_SPLASH, x, y, null, color).setLiveTime(50));
	}

}
