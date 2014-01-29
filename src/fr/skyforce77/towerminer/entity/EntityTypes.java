package fr.skyforce77.towerminer.entity;

import fr.skyforce77.towerminer.ressources.RessourcesManager;

import javax.swing.*;
import java.awt.*;

public enum EntityTypes {

    UNKNOWN("unknown", 0),
    CHICKEN("chicken", 1, 3, 1),
    OCELOT("ocelot", 4, 5, 20),
    BAT("bat", 8, 3, 15),
    COW("Cow", 10, 2, 40),
    PIG("pig", 10, 4, 60),
    SHEEP("sheep", 20, 3, 90),
    HORSE("horse", 30, 4, 130),
    BABYMUSHCOW("minimushroomcow", 100, 1, 350),
    MUSHCOW("mushroomcow", 200, 1, 1000),
    SKELETON("skeleton", -90),
    BLAZE("blaze", 50, 90, Blaze.class),
    SPIDER("Spider", 50, -90, Spider.class),
    WITCH("witch", 70, -90, Witch.class),
    ARROW("arrow", true, 45),
    FIREBALL("fireball", false, 0),
    POISON_POTION("poison", false, 0),
    WEAKNESS_POTION("weakness", false, 0);

    public static EntityTypes[] turrets;
    public static EntityTypes[] mobs;
    public static int count = 0;

    public static void createTurrets() {
        turrets = new EntityTypes[]{SKELETON, BLAZE, SPIDER, WITCH};
        mobs = new EntityTypes[]{CHICKEN, OCELOT, BAT, COW, PIG, SHEEP, HORSE, BABYMUSHCOW, MUSHCOW};

        for (EntityTypes type : values()) {
            type.id = count;
            count++;
        }
    }

    public static int getNextVisibleTurret(int now) {
        int i = now + 1;
        if (i == turrets.length) {
            return 0;
        }
        while (turrets[i] == null) {
            i++;
            if (i == turrets.length) {
                return 0;
            }
        }
        return i;
    }

    public static int getPreviousVisibleTurret(int now) {
        int i = now - 1;
        if (i == -1) {
            return turrets.length - 1;
        }
        while (turrets[i] == null) {
            i--;
            if (i == -1) {
                return turrets.length - 1;
            }
        }
        return i;
    }

    public static EntityTypes getType(int id) {
        for (EntityTypes type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        return null;
    }

    ImageIcon[] textures = new ImageIcon[16];
    int life = 1;
    int speed = 1;
    int price = 30;
    int level = 1;
    int rotation = 0;
    public int id;
    Class<? extends Entity> classe;

    EntityTypes(String texture, int rotation) {
        this.textures[0] = RessourcesManager.getIconTexture(texture);
        this.rotation = rotation;
        classe = Turret.class;
    }

    EntityTypes(String texture, int price, int rotation, Class<? extends Entity> classe) {
        this.textures[0] = RessourcesManager.getIconTexture(texture);
        this.classe = classe;
        this.price = price;
        this.rotation = rotation;
    }

    EntityTypes(String texture, int life, int speed, int level) {
        this.textures[0] = RessourcesManager.getIconTexture(texture);
        this.life = life;
        this.speed = speed;
        this.level = level;
        classe = Mob.class;
    }

    EntityTypes(String texture, boolean canrotate, int rotation) {
        this.textures[0] = RessourcesManager.getIconTexture(texture);
        this.level = canrotate ? 1 : 0;
        this.rotation = rotation;
        classe = EntityProjectile.class;
    }

    public Image getTexture(int data) {
        return textures[data].getImage();
    }

    public int getMaxLife() {
        return life;
    }

    public Class<? extends Entity> getEntityClass() {
        return classe;
    }

    public int getLevel() {
        return level;
    }

    public int getPrice() {
        return price;
    }

    public int getSpeed() {
        return speed;
    }

    public int getRotation() {
        return rotation;
    }

    public EntityTypes setTexture(int data, String texture) {
        textures[data] = RessourcesManager.getIconTexture(texture);
        return this;
    }

}
