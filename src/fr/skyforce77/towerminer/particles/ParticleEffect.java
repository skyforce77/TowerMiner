package fr.skyforce77.towerminer.particles;

import java.awt.Color;
import java.util.Random;

import javax.vecmath.Vector2d;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.protocol.packets.Packet18ParticleEffect;

public class ParticleEffect {

    public static void createEffect(int x, int y, int color, int type, int data) {
        if (type == 0) {
            createParticleSplash(ParticleType.POTION_SPLASH, ((SinglePlayer) TowerMiner.menu), x, y, new Color(color));
        } else if (type == 1) {
            createParticleAlea(ParticleType.SMOKE, ((SinglePlayer) TowerMiner.menu), x, y, data, new Color(color));
        } else if (type == 2) {
        	createParticleAlea(ParticleType.EXPLOSION, ((SinglePlayer) TowerMiner.menu), x, y, data, new Color(color));
        }
    }

    public static void createParticleSplash(ParticleType type, SinglePlayer sp, int x, int y, Color color) {
        if (sp instanceof MultiPlayer && ((MultiPlayer) sp).server) {
            new Packet18ParticleEffect(x, y, color.getRGB(), 0).sendAllTCP();
        }

        double d = 2 * Math.PI;
        while (d >= 0) {
            sp.particles.add(new Particle(type, x + (int) (Math.cos(d) * 10), y + (int) (Math.sin(d) * 10), new Vector2d(Math.cos(d), Math.sin(d)), 0.6f, color).setLiveTime(50));
            d -= 0.4f;
        }

        d = 2 * Math.PI;
        while (d >= 0) {
            sp.particles.add(new Particle(type, x + (int) (Math.cos(d) * 5), y + (int) (Math.sin(d) * 5), new Vector2d(Math.cos(d) * 0.7, Math.sin(d) * 0.7), 0.8f, color).setLiveTime(50));
            d -= 0.6f;
        }

        sp.particles.add(new Particle(type, x, y, null, color).setLiveTime(50));
    }
    
    public static void createParticleAlea(ParticleType type, SinglePlayer sp, int x, int y, int zone, Color color) {
        if (sp instanceof MultiPlayer && ((MultiPlayer) sp).server) {
        	if(type.equals(ParticleType.EXPLOSION)) {
        		new Packet18ParticleEffect(x, y, color.getRGB(), 2, zone).sendAllTCP();
        	} else {
        		new Packet18ParticleEffect(x, y, color.getRGB(), 1, zone).sendAllTCP();
        	}
        }
        
        int alpha = 254;
        int i = 10;
        zone = zone - 16;
        float scale = 1f;
        int time = 50;
        if(type.equals(ParticleType.EXPLOSION)) {
    		scale = 5f;
    		time = 20;
    		i = 20;
    	}

        while (i >= 0) {
        	if(type.equals(ParticleType.EXPLOSION)) {
        		alpha -= 5;
        	}
        	int xt = new Random().nextInt(2);
        	int yt = new Random().nextInt(2);
        	int x2 = 0;
        	int y2 = 0;
        	if(xt == 0) {
        		x2 = x + new Random().nextInt(zone+1);
        	} else {
        		x2 = x - new Random().nextInt(zone+1);
        	}
        	if(yt == 0) {
        		y2 = y + new Random().nextInt(zone+1);
        	} else {
        		y2 = y - new Random().nextInt(zone+1);
        	}
            sp.particles.add(new Particle(type, x2, y2, null, scale, new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha)).setLiveTime(time));
            i--;
        }
    }

}
