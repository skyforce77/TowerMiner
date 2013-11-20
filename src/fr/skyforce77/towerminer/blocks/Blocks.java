package fr.skyforce77.towerminer.blocks;

import java.awt.Image;
import java.io.File;
import java.io.Serializable;

import javax.swing.ImageIcon;

import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class Blocks implements Serializable{

	private static final long serialVersionUID = 2752810401397078248L;
	public static Blocks[] blocks;
	
	public static void createNativeBlocks() {
		blocks = new Blocks[2000];
		new Blocks(0,"unknown").setInvisible();
		new Blocks(1,"stone");
		new Blocks(2,"grass","podzol").setAdaptColor(0);
		new Blocks(3,"dirt");
		new Blocks(4,"cobblestone");
		new Blocks(5,"planks","planks1","planks2","planks3");
		//TODO pousses
		new Blocks(7,"bedrock");
		new Blocks(8,"water","water1","water2","water3","water4").setLiquid().setOverlay().setCantPlaceOn();
		new Blocks(9,"water").setLiquid().setInvisible();
		new Blocks(10,"lava").setLiquid().setCantPlaceOn().setInvisible();
		new Blocks(11,"lava").setLiquid().setCantPlaceOn();
		new Blocks(12,"sand","red_sand");
		new Blocks(13,"gravel");
		new Blocks(14,"gold_ore");
		new Blocks(15,"iron_ore");
		new Blocks(16,"coal_ore");
		new Blocks(17,"log","log1","log2","log3","log4","log5","log6","log7","log8");
		new Blocks(18,"leaves","leaves1","leaves2","leaves3").setAdaptColor().setOverlay();
		new Blocks(19,"sponge");
		new Blocks(20,"glass").setOverlay();
		new Blocks(21,"lapis_ore");
		new Blocks(22,"lapis_block");
		new Blocks(23,"dispenser","dispenser1");
		new Blocks(24,"sandstone");
		new Blocks(25,"noteblock");
		new Blocks(26,"bed","bed1","bed2","bed3","bed4","bed5","bed6","bed7");
		new Blocks(27,"rail_golden","rail_golden1","rail_golden2","rail_golden3").setOverlay();
		new Blocks(28,"rail_golden","rail_golden1","rail_golden2","rail_golden3").setOverlay();
		new Blocks(29,"spiston","piston1","piston2");
		//TODO plantes mortes
		new Blocks(33,"piston","piston1","piston2");
		new Blocks(35,"wool_colored_white","wool_colored_orange","wool_colored_magenta",
				"wool_colored_light_blue","wool_colored_yellow","wool_colored_lime","wool_colored_pink",
				"wool_colored_gray","wool_colored_silver","wool_colored_cyan","wool_colored_purple",
				"wool_colored_blue","wool_colored_brown","wool_colored_green","wool_colored_red",
				"wool_colored_black");
		//TODO plantes
		new Blocks(41,"gold_block");
		new Blocks(42,"iron_block");
		new Blocks(45,"brick");
		new Blocks(46,"tnt");
		new Blocks(48,"cobblestone_mossy");
		new Blocks(49,"obsidian");
		//TODO torches et feu
		new Blocks(52, "mob_spawner").setOverlay();
		//TODO un paquet!
		new Blocks(66, "rail","rail1","rail2","rail3","rail4","rail5").setOverlay();
		//TODO un paquet!
		new Blocks(79, "ice").setOverlay();
		new Blocks(80, "snow");
		new Blocks(81, "cactus","cactus1").setOverlay().setCantPlaceOn();
		new Blocks(82, "clay");
		//TODO cane à sucre
		new Blocks(84, "jukebox");
		//TODO barrières et citrouille
		new Blocks(87, "netherrack");
		new Blocks(88, "soul_sand");
		new Blocks(89, "glowstone");
		//TODO un paquet!
		new Blocks(98, "stonebrick","stonebrick_mossy","stonebrick_cracked","stonebrick_carved");
		//TODO un paquet!
		new Blocks(110, "mycelium");
		//TODO lilipad
		new Blocks(112, "nether_brick");
		//TODO un paquet!
		new Blocks(116,"enchtable");
		//TODO un paquet!
		new Blocks(121,"end_stone");
		//TODO ouef de dragon
		new Blocks(123,"redstonelamp","redstonelamp1");
		//TODO un paquet!
		new Blocks(152,"redstone_block");
		//TODO un paquet!
		new Blocks(155,"quartz","quartz1","quartz2","quartz3");
		//TODO un paquet!
		new Blocks(159,"hardened_clay_stained_white","hardened_clay_stained_orange","hardened_clay_stained_magenta",
				"hardened_clay_stained_light_blue","hardened_clay_stained_yellow","hardened_clay_stained_lime","hardened_clay_stained_pink",
				"hardened_clay_stained_gray","hardened_clay_stained_silver","hardened_clay_stained_cyan","hardened_clay_stained_purple",
				"hardened_clay_stained_blue","hardened_clay_stained_brown","hardened_clay_stained_green","hardened_clay_stained_red",
				"hardened_clay_stained_black");
		new Blocks(160,"hay_block","hay_block1");
		new Blocks(162,"hardened_clay");
		new Blocks(163,"coal_block");
		new Blocks(164,"ice_packed");
		
		new Blocks(1021,"death","death1");
		new Blocks(1022,"null").setInvisible().setOverlay();
		new Blocks(1023,"fleche","fleche1","fleche2","fleche3");
	}
	
	public static void addMapBlocks(Maps m) {
		createNativeBlocks();
		if(m.getCustomBlocks() != null) {
			for(Blocks b : m.getCustomBlocks()) {
				if(b != null) {
					blocks[b.getId()] = b;
				}
			}
		}
	}
	
	public static int getFirstVisibleBlock() {
		return getNextVisibleBlock(-1);
	}
	
	public static int getLatestVisibleBlock() {
		return getPreviousVisibleBlock(blocks.length);
	}
	
	public static int getNextVisibleBlock(int now) {
		int i = now+1;
		if(i == blocks.length) {
			return getFirstVisibleBlock();
		}
		while(blocks[i] == null || !blocks[i].isVisible()) {
			i++;
			if(i == blocks.length) {
				return getFirstVisibleBlock();
			}
		}
		return i;
	}
	
	public static int getPreviousVisibleBlock(int now) {
		int i = now-1;
		while(blocks[i] == null || !blocks[i].isVisible()) {
			i--;
			if(i == -1) {
				return getLatestVisibleBlock();
			}
		}
		return i;
	}
	
	int id = blocks.length-1;
	ImageIcon[] textures = new ImageIcon[16];
	boolean[] adapts = new boolean[16];
	boolean visible = true;
	boolean overlay = false;
	boolean liquid = false;
	boolean canplace = true;
	
	public Blocks() {
		visible = true;
		canplace = true;
	}
	
	public Blocks(int id, String texture) {
		this.id = id;
		this.textures[0] = RessourcesManager.getIconTexture(texture);
		setAdaptColor(false);
		blocks[id] = this;
	}
	
	public Blocks(int id, String... textures) {
		this.id = id;
		
		int i = 0;
		while(i < textures.length) {
			this.textures[i] = RessourcesManager.getIconTexture(textures[i]);
			i++;
		}
		
		setAdaptColor(false);
		blocks[id] = this;
	}
	
	public void resetTextures() {
		textures = new ImageIcon[16];
	}
	
	public Image getTexture() {
		return textures[0].getImage();
	}
	
	public ImageIcon getIcon() {
		return textures[0];
	}
	
	public Image getTexture(int data) {
		if(data < 0 || data > textures.length-1 || textures[data] == null) {
			return textures[0].getImage();
		}
		return textures[data].getImage();
	}
	
	public Image getRawTexture(int data) {
		if(textures[data] == null) {
			return null;
		}
		return textures[data].getImage();
	}
	
	public int getId() {
		return id;
	}
	
	public boolean isMapAdapted(int data) {
		return adapts[data];
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public boolean isOverlay() {
		return overlay;
	}
	
	public boolean isLiquid() {
		return liquid;
	}
	
	public boolean canPlaceTurretOn() {
		return canplace;
	}
	
	public Blocks setAdaptColor(boolean adapt) {
		int i = adapts.length-1;
		while(i >= 0) {
			adapts[i] = adapt;
			i--;
		}
		return this;
	}
	
	public Blocks setAdaptColor() {
		int i = adapts.length-1;
		while(i >= 0) {
			adapts[i] = true;
			i--;
		}
		return this;
	}
	
	public Blocks setAdaptColor(int data) {
		this.adapts[data] = true;
		return this;
	}
	
	public Blocks setInvisible() {
		this.visible = false;
		return this;
	}
	
	public Blocks setOverlay() {
		this.overlay = true;
		return this;
	}
	
	public Blocks setOverlay(boolean overlay) {
		this.overlay = overlay;
		return this;
	}
	
	public Blocks setLiquid() {
		this.liquid = true;
		return this;
	}
	
	public Blocks setLiquid(boolean liquid) {
		this.liquid = liquid;
		return this;
	}
	
	public Blocks setCantPlaceOn() {
		this.canplace = false;
		return this;
	}
	
	public Blocks setCanPlaceOn(boolean can) {
		this.canplace = can;
		return this;
	}
	
	public Blocks setId(int id) {
		this.id = id;
		return this;
	}
	
	public Blocks setTexture(int data, String texture) {
		textures[data] = RessourcesManager.getIconTexture(texture);
		return this;
	}
	
	public Blocks setTexture(int data, File f) {
		textures[data] = new ImageIcon(f.getAbsolutePath());
		return this;
	}

}