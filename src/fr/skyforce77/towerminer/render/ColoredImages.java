package fr.skyforce77.towerminer.render;

import java.awt.Color;
import java.awt.Image;
import java.util.HashMap;

public class ColoredImages {

    private static HashMap<ColoredImages, Image> images = new HashMap<ColoredImages, Image>();

    public static void add(ColoredImages infos, Image colored) {
        images.put(infos, colored);
    }

    public static boolean exists(ColoredImages infos) {
        return images.containsKey(infos);
    }

    public static Image getImage(ColoredImages infos) {
        return images.get(infos);
    }

    public Color color;
    public Image image;
    public float alpha;

    public ColoredImages(Image image, Color color, float alpha) {
        this.color = color;
        this.image = image;
        this.alpha = alpha;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(alpha);
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + ((image == null) ? 0 : image.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ColoredImages other = (ColoredImages) obj;
        if (Float.floatToIntBits(alpha) != Float.floatToIntBits(other.alpha))
            return false;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (image == null) {
            if (other.image != null)
                return false;
        } else if (!image.equals(other.image))
            return false;
        return true;
    }

}
