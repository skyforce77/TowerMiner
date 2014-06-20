package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Graphics2D;
import java.io.Serializable;

import javax.swing.ImageIcon;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;

public class BlockRender implements Serializable {

    private static final long serialVersionUID = -5745290778062774752L;
    public static BlockRender[] renders = new BlockRender[1000];

    public static void createRenders() {
        renders[0] = new BlockRender();
        renders[1] = new MovingRender();
        renders[2] = new FlowingRender();
        renders[3] = new RedstoneRender();
        renders[4] = new FireRender();
        renders[5] = new FenceRender();
        renders[6] = new WallRender();
        renders[7] = new SkullRender();
        renders[8] = new SignRender();
        renders[9] = new UnknownBlockRender();
        renders[10] = new SaplingRender();
        renders[11] = new ColorBlockRender();
        renders[12] = new NumberBlockRender();
    }

    public static Object getOverlayStorage(int x, int y) {
        return Maps.getActualMap().getOverlayStorage(x / 48, (y - TowerMiner.game.CanvasY) / 48);
    }

    ;

    public static Object getBlockStorage(int x, int y) {
        return Maps.getActualMap().getBlockStorage(x / 48, (y - TowerMiner.game.CanvasY) / 48);
    }

    ;

    public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
    }

    public void onGameTick() {
    }

    public boolean overrideNormalRender() {
        return false;
    }

    public boolean needNBT(int id) {
        return false;
    }
    
    public String formatNBT(int id) {
        return "";
    }

    public int dataNumber(Blocks b) {
        int i = 1;
        while (!b.getTexture(i).equals(b.getTexture(0))) {
            i++;
        }
        return i;
    }

    public ImageIcon getListIcon(Blocks b) {
        return b.getIcon(0);
    }

    public void onPlaced(Blocks b, Maps m, int x, int y, int data, String st) {
    }

}
