package fr.skyforce77.towerminer.particles.types;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.particles.Particle;
import fr.skyforce77.towerminer.particles.ParticleType;
import fr.skyforce77.towerminer.render.RenderHelper;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

import java.awt.*;

public class PT16Textures extends ParticleType {

    private String texture;

    public PT16Textures(String name, String texture) {
        super(name);
        this.texture = texture;
    }

    @Override
    public void draw(Graphics2D g2d, SinglePlayer sp, Particle particle) {
        int size = (int) (16 * particle.getScale());
        int size2 = size / 2;
        g2d.setClip((int) (particle.getX() - size2), (int) (particle.getY() - size2) + TowerMiner.game.CanvasY, size, size);
        float l1 = size2 * particle.getTicksLived() / particle.getLiveTime();
        int data = (int) (size2 - l1);
        data = data/2;
        Color color = particle.getColor() == null ? new Color(0, 0, 0, 0) : particle.getColor();
        g2d.drawImage(RenderHelper.getColoredImage(RessourcesManager.getTexture(texture), color, color.getAlpha() == 255 ? 0.4f : color.getAlpha() / 255f), (int) (particle.getX()) - size2 - (size * data), (int) (particle.getY()) - size2 + TowerMiner.game.CanvasY, (int) (256 * particle.getScale()), size, null);
    }

}
