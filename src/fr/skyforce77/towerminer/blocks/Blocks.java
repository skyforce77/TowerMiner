package fr.skyforce77.towerminer.blocks;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.renders.BlockRender;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.particles.Particle;
import fr.skyforce77.towerminer.particles.ParticleType;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.Serializable;
import java.util.Random;

public class Blocks implements Serializable {

    private static final long serialVersionUID = 2752810401397078248L;
    private static Blocks[] blocks;

    public static void createNativeBlocks() {
        blocks = new Blocks[5000];
        new Blocks(0, "unknown").setInvisible();
        new Blocks(1, "stone", "stone_granite", "stone_granite_smooth", "stone_diorite", "stone_diorite_smooth", "stone_andesite", "stone_andesite_smooth");
        new Blocks(2, "grass", "podzol").setAdaptColor(0);
        new Blocks(3, "dirt");
        new Blocks(4, "cobblestone");
        new Blocks(5, "planks", "planks1", "planks2", "planks3", "planks_acacia", "planks_big_oak");
        new Blocks(6, "sapling_oak", "sapling_spruce", "sapling_birch", "sapling_jungle", "sapling_acacia", "sapling_roofed_oak").setOverlay().setRender(10);
        new Blocks(7, "bedrock");
        new Blocks(8, "water_still", "water").setLiquid().setOverlay().setCantPlaceOn().setRender(1);
        new Blocks(9, "water_flow", "water").setLiquid().setOverlay().setCantPlaceOn().setRender(2);
        new Blocks(10, "lava_still", "lava").setLiquid().setCantPlaceOn().setRender(1);
        new Blocks(11, "lava_flow", "lava").setLiquid().setCantPlaceOn().setRender(2);
        new Blocks(12, "sand", "red_sand");
        new Blocks(13, "gravel");
        new Blocks(14, "gold_ore");
        new Blocks(15, "iron_ore");
        new Blocks(16, "coal_ore");
        new Blocks(17, "log", "log1", "log2", "log3", "log4", "log5", "log6", "log7", "log8", "log9", "log10", "log11");
        new Blocks(18, "leaves", "leaves1", "leaves2", "leaves3").setAdaptColor().setOverlay();
        new Blocks(19, "sponge");
        new Blocks(20, "glass").setOverlay();
        new Blocks(21, "lapis_ore");
        new Blocks(22, "lapis_block");
        new Blocks(23, "dispenser", "dispenser1");
        new Blocks(24, "sandstone");
        new Blocks(25, "noteblock");
        new Blocks(26, "bed", "bed1", "bed2", "bed3", "bed4", "bed5", "bed6", "bed7");
        new Blocks(27, "rail_golden", "rail_golden1", "rail_golden2", "rail_golden3").setOverlay();
        new Blocks(28, "rail_detector", "rail_detector1", "rail_detector2", "rail_detector3").setOverlay();
        new Blocks(29, "spiston", "piston1", "piston2");
        //TODO plantes mortes
        new Blocks(33, "piston", "piston1", "piston2");
        new Blocks(35, "wool_colored_white", "wool_colored_orange", "wool_colored_magenta",
                "wool_colored_light_blue", "wool_colored_yellow", "wool_colored_lime", "wool_colored_pink",
                "wool_colored_gray", "wool_colored_silver", "wool_colored_cyan", "wool_colored_purple",
                "wool_colored_blue", "wool_colored_brown", "wool_colored_green", "wool_colored_red",
                "wool_colored_black");
        //TODO plantes
        new Blocks(41, "gold_block");
        new Blocks(42, "iron_block");
        new Blocks(45, "brick");
        new Blocks(46, "tnt");
        new Blocks(48, "cobblestone_mossy");
        new Blocks(49, "obsidian");
        //TODO torches et feu
        new Blocks(50, "torch_on", "torch_off") {
            private static final long serialVersionUID = -4676064980975742683L;

            public void onDraw(Graphics2D g2d, int data, int x, int y, int m, int n) {
                if (data == 0 && TowerMiner.menu instanceof SinglePlayer && new Random().nextInt(200) <= 1)
                    ((SinglePlayer) TowerMiner.menu).particles.add(new Particle(ParticleType.FLAME, x + 24, y + 24, null, 1.2f));
            }

            ;
        }.setOverlay();
        new Blocks(51, "fire_layer_0", "fire_layer_0", "fire").setOverlay().setRender(4);
        new Blocks(52, "mob_spawner") {
            private static final long serialVersionUID = -46760649095742683L;

            public void onDraw(Graphics2D g2d, int data, int x, int y, int m, int n) {
                if (TowerMiner.menu instanceof SinglePlayer && new Random().nextInt(200) <= 1) {
                    int i = 10;
                    while (i >= 0) {
                        int ix = new Random().nextInt(2) == 0 ? 1 : -1;
                        int iy = new Random().nextInt(2) == 0 ? +1 : -1;
                        int is = new Random().nextInt(2) == 0 ? +1 : -1;
                        ((SinglePlayer) TowerMiner.menu).particles.add(new Particle(ParticleType.FLAME, x + 24 + new Random().nextInt(30) * ix, y + 24 + new Random().nextInt(30) * iy, null, 1f + (new Random().nextInt(20) / 40) * is));
                        i--;
                    }
                }
            }

            ;
        }.setOverlay();
        new Blocks(55, "redstone_dust", "redstone_dust_cross", "redstone_dust_line") {
            private static final long serialVersionUID = -467606498095742683L;

            public void onDraw(Graphics2D g2d, int data, int x, int y, int m, int n) {
                if (data == 0 && TowerMiner.menu instanceof SinglePlayer && new Random().nextInt(200) <= 1) {
                    int ix = new Random().nextInt(2) == 0 ? 1 : -1;
                    int iy = new Random().nextInt(2) == 0 ? +1 : -1;
                    ((SinglePlayer) TowerMiner.menu).particles.add(new Particle(ParticleType.SMOKE, x + 24 + new Random().nextInt(15) * ix, y + 24 + new Random().nextInt(15) * iy, null, 1f + -0.2f, new Color(255, 0, 0, 254)));
                }
            }

            ;
        }.setOverlay().setRender(3);
        //TODO un paquet!
        //TODO finir le render new Blocks(63,"planks","planks1","planks2","planks3", "planks_acacia", "planks_big_oak").setOverlay().setRender(8);
        new Blocks(66, "rail", "rail1", "rail2", "rail3", "rail4", "rail5").setOverlay();
        //TODO un paquet!
        new Blocks(76, "torch_redstone_on", "torch_off") {
            private static final long serialVersionUID = -467606498095742683L;

            public void onDraw(Graphics2D g2d, int data, int x, int y, int m, int n) {
                if (data == 0 && TowerMiner.menu instanceof SinglePlayer && new Random().nextInt(200) <= 1) {
                    int ix = new Random().nextInt(2) == 0 ? 1 : -1;
                    int iy = new Random().nextInt(2) == 0 ? +1 : -1;
                    ((SinglePlayer) TowerMiner.menu).particles.add(new Particle(ParticleType.SMOKE, x + 24 + new Random().nextInt(15) * ix, y + 24 + new Random().nextInt(15) * iy, null, 1f + -0.2f, new Color(255, 0, 0, 254)));
                }
            }

            ;
        }.setOverlay();
        new Blocks(79, "ice").setOverlay();
        new Blocks(80, "snow");
        new Blocks(81, "cactus", "cactus1").setOverlay().setCantPlaceOn();
        new Blocks(82, "clay");
        //TODO cane à sucre
        new Blocks(84, "jukebox");
        new Blocks(85, "planks", "planks1", "planks2", "planks3", "planks_acacia", "planks_big_oak").setOverlay().setRender(5);
        //TODO barrières et citrouille
        new Blocks(87, "netherrack");
        new Blocks(88, "soul_sand");
        new Blocks(89, "glowstone");
        new Blocks(90, "portal", "water").setOverlay().setCantPlaceOn().setRender(1);
        //TODO un paquet!
        new Blocks(95, "glass_white", "glass_orange", "glass_magenta",
                "glass_light_blue", "glass_yellow", "glass_lime", "glass_pink",
                "glass_gray", "glass_silver", "glass_cyan", "glass_purple",
                "glass_blue", "glass_brown", "glass_green", "glass_red",
                "glass_black").setOverlay();
        new Blocks(98, "stonebrick", "stonebrick_mossy", "stonebrick_cracked", "stonebrick_carved");
        //TODO un paquet!
        new Blocks(110, "mycelium");
        //TODO lilipad
        new Blocks(112, "nether_brick");
        new Blocks(113, "nether_brick").setOverlay().setRender(5);
        //TODO un paquet!
        new Blocks(116, "enchtable");
        //TODO un paquet!
        new Blocks(121, "end_stone");
        //TODO ouef de dragon
        new Blocks(123, "redstonelamp", "redstonelamp1");
        new Blocks(137, "command_block");
        new Blocks(139, "cobblestone", "cobblestone_mossy").setOverlay().setRender(6);
        //TODO un paquet!
        new Blocks(152, "redstone_block");
        //TODO un paquet!
        new Blocks(155, "quartz", "quartz1", "quartz2", "quartz3");
        //TODO un paquet!
        new Blocks(159, "hardened_clay_stained_white", "hardened_clay_stained_orange", "hardened_clay_stained_magenta",
                "hardened_clay_stained_light_blue", "hardened_clay_stained_yellow", "hardened_clay_stained_lime", "hardened_clay_stained_pink",
                "hardened_clay_stained_gray", "hardened_clay_stained_silver", "hardened_clay_stained_cyan", "hardened_clay_stained_purple",
                "hardened_clay_stained_blue", "hardened_clay_stained_brown", "hardened_clay_stained_green", "hardened_clay_stained_red",
                "hardened_clay_stained_black");
        new Blocks(160, "hay_block", "hay_block1");
        new Blocks(161, "log_acacia_top", "log_acacia", "log_acacia2", "log_big_oak_top", "log_big_oak", "log_big_oak2");
        new Blocks(162, "hardened_clay");
        new Blocks(163, "coal_block");
        new Blocks(164, "ice_packed");
        new Blocks(165, "slime").setOverlay();

        new Blocks(996, "mskeleton").setOverlay().setRender(7);
        new Blocks(997, "wither_skeleton").setOverlay().setRender(7);
        new Blocks(998, "zombie").setOverlay().setRender(7);
        new Blocks(999, "steve").setOverlay().setRender(7);
        new Blocks(1000, "creeper").setOverlay().setRender(7);

        new Blocks(1021, "death", "death1");
        new Blocks(1022, "null").setInvisible().setOverlay();
        new Blocks(1023, "fleche", "fleche1", "fleche2", "fleche3");
        new Blocks(1024, "unknown").setInvisible().setRender(9).setCantPlaceOn();

        BlockRender.createRenders();
    }

    public static void addMapBlocks(Maps m) {
        createNativeBlocks();
        if (m.getCustomBlocks() != null) {
            for (Blocks b : m.getCustomBlocks()) {
                if (b != null) {
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
        int i = now + 1;
        if (i == blocks.length) {
            return getFirstVisibleBlock();
        }
        while (blocks[i] == null || !blocks[i].isVisible()) {
            i++;
            if (i == blocks.length) {
                return getFirstVisibleBlock();
            }
        }
        return i;
    }

    public static int getPreviousVisibleBlock(int now) {
        int i = now - 1;
        while (blocks[i] == null || !blocks[i].isVisible()) {
            i--;
            if (i == -1) {
                return getLatestVisibleBlock();
            }
        }
        return i;
    }

    public static Blocks byId(int id) {
        if(id < blocks.length && blocks[id] != null) {
            return blocks[id];
        }
        return blocks[1024];
    }

    public static Blocks[] getList() {
        return blocks;
    }

    int id = blocks.length - 1;
    ImageIcon[] textures = new ImageIcon[16];
    boolean[] adapts = new boolean[16];
    boolean visible = true;
    boolean overlay = false;
    boolean liquid = false;
    boolean canplace = true;
    int render = 0;

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
        while (i < textures.length) {
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

    public ImageIcon getIcon(int data) {
        return textures[data];
    }

    public int dataNumber() {
        return getRender().dataNumber(this);
    }

    public Image getTexture(int data) {
        if (data < 0 || data > textures.length - 1 || textures[data] == null) {
            return textures[0].getImage();
        }
        return textures[data].getImage();
    }

    public Image getRawTexture(int data) {
        if (textures[data] == null) {
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

    public BlockRender getRender() {
        return render == -1 ? BlockRender.renders[0] : BlockRender.renders[render];
    }

    public Blocks setRender(int id) {
        render = id;
        return this;
    }

    public Blocks setAdaptColor(boolean adapt) {
        int i = adapts.length - 1;
        while (i >= 0) {
            adapts[i] = adapt;
            i--;
        }
        return this;
    }

    public Blocks setAdaptColor() {
        int i = adapts.length - 1;
        while (i >= 0) {
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

    public void onDraw(Graphics2D g2d, int data, int x, int y, int m, int n) {
    }

}
