package fr.skyforce77.towerminer.entity;

import java.awt.Image;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class EntityTypes {

	public static EntityTypes UNKNOWN =  new EntityTypes(0, RessourcesManager.getIconTexture("unknown"), 0);
	
	public static EntityTypes CHICKEN = new EntityTypes(1, RessourcesManager.getIconTexture("chicken"), 1, 3, 1);
	public static EntityTypes OCELOT = new EntityTypes(2, RessourcesManager.getIconTexture("ocelot"), 4, 5, 20);
	public static EntityTypes BAT = new EntityTypes(3, RessourcesManager.getIconTexture("bat"), 8, 3, 15);
	public static EntityTypes COW = new EntityTypes(4, RessourcesManager.getIconTexture("Cow"), 10, 2, 40);
	public static EntityTypes PIG = new EntityTypes(5, RessourcesManager.getIconTexture("pig"), 10, 4, 60);
	public static EntityTypes SHEEP = new EntityTypes(6, RessourcesManager.getIconTexture("sheep"), 20, 3, 90);
	public static EntityTypes HORSE = new EntityTypes(7, RessourcesManager.getIconTexture("horse"), 30, 4, 130);
	public static EntityTypes BABYMUSHCOW = new EntityTypes(8, RessourcesManager.getIconTexture("minimushroomcow"), 100, 1, 350);
	public static EntityTypes MUSHCOW = new EntityTypes(9, RessourcesManager.getIconTexture("mushroomcow"), 200, 1, 1000);
	
	public static EntityTypes SKELETON = new EntityTypes(50, RessourcesManager.getIconTexture("skeleton"), -90);
	public static EntityTypes BLAZE = new EntityTypes(51, RessourcesManager.getIconTexture("blaze"), 50, 90, Blaze.class);
	public static EntityTypes SPIDER = new EntityTypes(52, RessourcesManager.getIconTexture("Spider"), 50, -90, Spider.class);
	public static EntityTypes WITCH = new EntityTypes(53, RessourcesManager.getIconTexture("witch"), 70, -90, Witch.class);
	public static EntityTypes CREEPER = new EntityTypes(54, RessourcesManager.getIconTexture("creeper"), 200, -90, Creeper.class);
	public static EntityTypes WITHER_SKELETON = new EntityTypes(55, RessourcesManager.getIconTexture("wither_skeleton"), 60, -90, WitherSkeleton.class);
	public static EntityTypes SNOW_GOLEM = new EntityTypes(56, RessourcesManager.getIconTexture("pumpkin"), 300, -90, SnowGolem.class);
	public static EntityTypes GUARDIAN = new EntityTypes(57, RessourcesManager.getIconTexture("guardian"), 100, -90, Guardian.class);
	
	public static EntityTypes ARROW = new EntityTypes(100, RessourcesManager.getIconTexture("arrow"), true, 45);
	public static EntityTypes FIREBALL = new EntityTypes(101, RessourcesManager.getIconTexture("fireball"), false, 0);
	public static EntityTypes POISON_POTION = new EntityTypes(102, RessourcesManager.getIconTexture("poison"), false, 0);
	public static EntityTypes WEAKNESS_POTION = new EntityTypes(103, RessourcesManager.getIconTexture("weakness"), false, 0);
	public static EntityTypes SNOWBALL = new EntityTypes(104, RessourcesManager.getIconTexture("snowball"), false, 0);

    public static ArrayList<EntityTypes> turrets = new ArrayList<EntityTypes>();
    public static ArrayList<EntityTypes> mobs = new ArrayList<EntityTypes>();
    public static ArrayList<EntityTypes> values = new ArrayList<EntityTypes>();

    public static void createTurrets() {
    	registerTurret(SKELETON);
    	registerTurret(BLAZE);
    	registerTurret(SPIDER);
    	registerTurret(WITHER_SKELETON);
    	registerTurret(WITCH);
    	registerTurret(GUARDIAN);
    	registerTurret(CREEPER);
    	registerTurret(SNOW_GOLEM);
        
    	registerMob(CHICKEN);
    	registerMob(OCELOT);
    	registerMob(BAT);
    	registerMob(COW);
    	registerMob(PIG);
    	registerMob(SHEEP);
    	registerMob(HORSE);
    	registerMob(BABYMUSHCOW);
    	registerMob(MUSHCOW);
        
    	registerEntity(ARROW);
    	registerEntity(FIREBALL);
    	registerEntity(POISON_POTION);
    	registerEntity(WEAKNESS_POTION);
    	registerEntity(SNOWBALL);
    }
    
    public static void registerMob(EntityTypes mob) {
    	mobs.add(mob);
    	values.add(mob);
    }
    
    public static void registerTurret(EntityTypes turret) {
    	turrets.add(turret);
    	values.add(turret);
    }
    
    public static void registerEntity(EntityTypes entity) {
    	values.add(entity);
    }

    public static int getNextVisibleTurret(int now) {
        int i = now + 1;
        if (i == turrets.size()) {
            return 0;
        }
        return i;
    }

    public static int getPreviousVisibleTurret(int now) {
        int i = now - 1;
        if (i == -1) {
            return turrets.size() - 1;
        }
        return i;
    }

    public static EntityTypes getType(int id) {
        for (EntityTypes type : values) {
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
    private int id;
    Class<? extends Entity> classe;

    public EntityTypes(int id, ImageIcon texture, int rotation) {
        this.textures[0] = texture;
        this.rotation = rotation;
        classe = Turret.class;
        this.id = id;
    }

    public EntityTypes(int id, ImageIcon texture, int price, int rotation, Class<? extends Entity> classe) {
        this.textures[0] = texture;
        this.classe = classe;
        this.price = price;
        this.rotation = rotation;
        this.id = id;
    }

    public EntityTypes(int id, ImageIcon texture, int life, int speed, int level) {
        this.textures[0] = texture;
        this.life = life;
        this.speed = speed;
        this.level = level;
        classe = Mob.class;
        this.id = id;
    }

    public EntityTypes(int id, ImageIcon texture, boolean canrotate, int rotation) {
        this.textures[0] = texture;
        this.level = canrotate ? 1 : 0;
        this.rotation = rotation;
        classe = EntityProjectile.class;
        this.id = id;
    }

    public Image getTexture(int data) {
        return textures[data].getImage();
    }
    
    public int getId() {
    	return id;
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
