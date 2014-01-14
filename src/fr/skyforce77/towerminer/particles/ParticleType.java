package fr.skyforce77.towerminer.particles;

import java.awt.Graphics2D;

import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.particles.types.PTEightTextures;
import fr.skyforce77.towerminer.particles.types.PTScaleFade;

public class ParticleType {
	
	public static ParticleType POTION_SPLASH = new PTEightTextures("potion_splash", "splash");
	public static ParticleType SMOKE = new PTEightTextures("smoke", "smoke");
	public static ParticleType CRITICAL = new PTEightTextures("critical", "crit");
	public static ParticleType FIREWORK = new PTEightTextures("firework", "fw");
	public static ParticleType FLAME = new PTScaleFade("flame", "flame");
	
	private String name;
	
	public ParticleType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void onTick(Particle particle) {}
	
	public void draw(Graphics2D g2d, SinglePlayer sp, Particle particle) {}

}
