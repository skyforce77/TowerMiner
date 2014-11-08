package fr.skyforce77.towerminer.particles.types;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.Menu;
import fr.skyforce77.towerminer.particles.Particle;
import fr.skyforce77.towerminer.particles.ParticleType;
import fr.skyforce77.towerminer.render.RenderHelper;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class PTEightTextures extends ParticleType {

    private String texture;

    public PTEightTextures(String name, String texture) {
        super(name);
        this.texture = texture;
    }

    @Override
    public void draw(Graphics2D g2d, Menu m, Particle particle) {
        int size = (int) (16 * particle.getScale());
        int size2 = size / 2;
        g2d.setClip((int) (particle.getX() - size2), (int) (particle.getY() - size2) + TowerMiner.game.CanvasY, size, size);
        float l1 = size2 * particle.getTicksLived() / particle.getLiveTime();
        int data = (int) (size2 - l1);
        Color color = particle.getColor() == null ? new Color(0, 0, 0, 0) : particle.getColor();
        g2d.drawImage(RenderHelper.getColoredImage(RessourcesManager.getTexture(texture), color, color.getAlpha() == 255 ? 0.4f : color.getAlpha() / 255f), (int) (particle.getX()) - size2 - (size * data), (int) (particle.getY()) - size2 + TowerMiner.game.CanvasY, (int) (128 * particle.getScale()), size, null);
    }

}
