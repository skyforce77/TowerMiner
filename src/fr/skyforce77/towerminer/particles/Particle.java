package fr.skyforce77.towerminer.particles;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.vecmath.Vector2d;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.SinglePlayer;

public class Particle {

	private int lived = 0;
	private int livetime = 100;
	private double x, y;
	private Vector2d direction;
	private ParticleType type;
	private Color color = null;
	private float scale = 1.0f;

	public Particle(ParticleType type, int x, int y, Vector2d direction) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.direction = direction;
	}
	
	public Particle(ParticleType type, int x, int y, Vector2d direction, Color color) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.color = color;
	}
	
	public Particle(ParticleType type, int x, int y, Vector2d direction, float scale) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.scale = scale;
	}
	
	public Particle(ParticleType type, int x, int y, Vector2d direction, float scale, Color color) {
		this.type = type;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.scale = scale;
		this.color = color;
	}

	public int getTicksLived() {
		return lived;
	}

	public int getLiveTime() {
		return livetime;
	}
	
	public Particle setLiveTime(int livetime) {
		this.livetime = livetime;
		return this;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public float getScale() {
		return scale;
	}
	
	public Color getColor() {
		return color;
	}

	public Vector2d getDirection() {
		return direction;
	}

	public ParticleType getType() {
		return type;
	}

	public void onTick() {
		lived++;

		if(direction != null) {
			x = x+direction.x;
			y = y+direction.y;
			direction.scale(0.98);
		}
		type.onTick(this);

		if(lived >= livetime) {
			((SinglePlayer)TowerMiner.menu).particles.remove(this);
		}
	}

	public void draw(Graphics2D g2d, SinglePlayer sp) {
		type.draw(g2d, sp, this);
	}

}
