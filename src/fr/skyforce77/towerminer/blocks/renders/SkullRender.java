package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MapEditor;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class SkullRender extends BlockRender {

	private static final long serialVersionUID = 3528216491928762967L;

	public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
        int lpix = pix;
        int lx = x;
        int ly = y;
        float po = 0.5f;
        pix = (int) (po * pix);
        x += (lpix - pix) / 2;
        y += (lpix - pix) / 2;
        Image image = b.getTexture(0);
        String s = "";
        if (b.getId() == 999) {
            if (TowerMiner.menu instanceof MapEditor && lpix != 48) {
                MapEditor me = (MapEditor) TowerMiner.menu;
                s = me.bdata.getText();
                image = RessourcesManager.getDistantImage("http://s3.amazonaws.com/MinecraftSkins/" + me.bdata.getText() + ".png", "steve");
            } else {
                Object storage = Maps.getActualMap().getOverlayStorage(x / 48, (y - TowerMiner.game.CanvasY) / 48);
                if (storage != null) {
                    ;
                    s = (String) storage;
                    image = RessourcesManager.getDistantImage("http://s3.amazonaws.com/MinecraftSkins/" + s + ".png", "steve");
                }
            }
        }
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        if (data == 0) {
            if(s.equals("deadmau5")) {
            	int npix = pix;
            	pix = (int)(0.8*pix);
            	g2d.setClip(new Rectangle2D.Float(lx, ly, pix, pix));
            	g2d.drawImage(image, lx-5*pix, ly, width/8*pix, height/8*pix, null);
            	
            	g2d = (Graphics2D)g2d.create();
            	g2d.setClip(new Rectangle2D.Float(lx+lpix-pix, ly, pix, pix));
            	g2d.drawImage(image, lx+lpix-pix-5*pix, ly, width/8*pix, height/8*pix, null);
                
            	pix = npix;
            	g2d = (Graphics2D)g2d.create();
            	g2d.setClip(new Rectangle2D.Float(x, y, pix, pix));
                g2d.drawImage(image, x - pix, y - pix, width / 8 * pix, height / 8 * pix, null);
                g2d.drawImage(image, x-5*pix, y-pix, width/8*pix, height/8*pix, null);
            } else {
            	g2d.setClip(new Rectangle2D.Float(x, y, pix, pix));
                g2d.drawImage(image, x - pix, y - pix, width / 8 * pix, height / 8 * pix, null);
            }
            //g2d.drawImage(image, x-5*pix, y-pix, width/8*pix, height/8*pix, null);
        } else {
            g2d.rotate(Math.toRadians((45f) * data), x + pix / 2, y + pix / 2);
            g2d.setClip(new Rectangle2D.Float(x, y, pix, pix));
            g2d.drawImage(image, x - pix, y, width / 8 * pix, height / 8 * pix, null);
            //g2d.drawImage(image, x-5*pix, y, width/8*pix, height/8*pix, null);
        }
    }
	
	@Override
	public ImageIcon getListIcon(Blocks b) {
		return b.getIcon(1);
	}

    @Override
    public boolean overrideNormalRender() {
        return true;
    }

    @Override
    public boolean needNBT(int id) {
        return id == 999 ? true : false;
    }

    @Override
    public int dataNumber(Blocks b) {
        return 9;
    }

    @Override
    public void onPlaced(Blocks b, Maps m, int x, int y, int data, String st) {
        if (b.getId() == 999 && !st.equals(""))
            m.setOverlayStorage(x, y, st);
    }
}
