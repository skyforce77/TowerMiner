package fr.skyforce77.towerminer.maps;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URL;
import java.util.HashMap;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class Maps implements Serializable {

	private static final long serialVersionUID = -692155342013021355L;
	public static Maps[] maps = new Maps[1024];
	private static int actual = 0;

	public static void createMaps() {
		RessourcesManager.getMapsDirectory().mkdirs();
		maps[0] = getOrCreateMap("Plage");
		maps[1] = getOrCreateMap("Hiver");
		maps[2] = getOrCreateMap("Nether");
		maps[3] = getOrCreateMap("Portal");
		maps[3] = getOrCreateMap("Pokemon");
		maps[1023] = new Maps("Sans Nom", 0, 0, 0, new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
		, new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
		, new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, new double[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
	}

	public static Maps getActualMap() {
		return maps[actual];
	}

	@SuppressWarnings("deprecation")
	public static void resize() {
		TowerMiner.game.resize(Maps.getActualMap().getXMax() * 48 + TowerMiner.game.CanvasX, Maps.getActualMap().getYMax() * 48 + TowerMiner.game.CanvasY + 30);
	}

	public static void setActualMap(int id) {
		actual = id;
		resize();
		if (TowerMiner.menu != null) {
			TowerMiner.menu.onMapChanged();
		}
		Blocks.addMapBlocks(getActualMap());
	}

	String name;
	int x;
	int y;
	int xdepart;
	int ydepart;
	int rotation;
	MapLine[] maplines;
	MapLine[] overlaylines;
	Blocks[] blocks = new Blocks[0];
	HashMap<String, Object> storage = new HashMap<String, Object>();
	Color modifier = new Color(42, 97, 14);
	public Point[] deaths = new Point[2];
	public HashMap<Integer, String> blockids = new HashMap<>();
	public HashMap<Integer, Integer> vanilla = new HashMap<>();

	public Maps() {
	}

	public Maps(String name, int xdepart, int ydepart, int rotation, double[]... maplines) {
		this.name = name;
		this.x = maplines[0].length;
		this.y = maplines.length;
		this.xdepart = xdepart;
		this.ydepart = ydepart;
		this.rotation = rotation;
		deaths[0] = new Point(0, 1);
		deaths[1] = new Point(0, 2);

		this.maplines = new MapLine[y];
		this.overlaylines = new MapLine[y];
		int i = 0;
		while (i < y) {
			int[] lid = new int[maplines[0].length];
			int[] ldata = new int[maplines[0].length];
			int[] oid = new int[maplines[0].length];
			int[] odata = new int[maplines[0].length];
			int n = 0;
			while (n < x) {
				lid[n] = (int) (maplines[i][n]);
				ldata[n] = (int) (BigDecimal.valueOf(maplines[i][n]).divideAndRemainder(BigDecimal.ONE)[1].floatValue() * 10);
				oid[n] = 1022;
				odata[n] = 0;
				n++;
			}
			this.maplines[i] = new MapLine(lid, ldata);
			this.overlaylines[i] = new MapLine(oid, odata);
			i++;
		}
	}

	public void addBlock(Blocks b) {
		if (blocks.length < b.getId()) {
			Blocks[] newb = new Blocks[b.getId() + 1];
			for (Blocks bl : blocks) {
				if (bl != null)
					newb[bl.getId()] = bl;
			}
			blocks = newb;
		}
		blocks[b.getId()] = b;
	}

	public void removeBlock(Blocks b) {
		if (blocks.length < b.getId()) {
			return;
		}
		blocks[b.getId()] = null;
	}

	public Maps setColorModifier(Color modifier) {
		this.modifier = modifier;
		return this;
	}

	public Point[] getDeathPoints() {
		if (deaths != null) {
			return deaths;
		} else {
			return new Point[]{new Point(0, 1), new Point(0, 2)};
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getXMax() {
		return x;
	}

	public void setXMax(int x) {
		this.x = x;
		extend(x, y);
	}

	public int getXDepart() {
		return xdepart;
	}

	public void setXDepart(int xdepart) {
		this.xdepart = xdepart;
	}

	public int getYMax() {
		return y;
	}

	public Blocks[] getCustomBlocks() {
		return blocks;
	}

	public void setYMax(int y) {
		this.y = y;
		extend(x, y);
	}

	public int getYDepart() {
		return ydepart;
	}

	public void setYDepart(int ydepart) {
		this.ydepart = ydepart;
	}

	public double getDepartRotation() {
		switch (rotation) {
		case 0:
			return Math.toRadians(0);
		case 1:
			return Math.toRadians(90);
		case 2:
			return Math.toRadians(180);
		case 3:
			return Math.toRadians(270);
		}
		return 0;
	}

	public int getRawRotation() {
		return rotation;
	}

	public void setRawRotation(int rotation) {
		this.rotation = rotation;
	}

	public Color getColorModifier() {
		return modifier;
	}

	public int getBlockId(int x, int y) {
		return maplines[y].getId(x);
	}

	public int getOverlayId(int x, int y) {
		return overlaylines[y].getId(x);
	}

	public int getBlockPathId() {
		return maplines[ydepart].getId(xdepart);
	}

	public int getOverlayPathId() {
		return overlaylines[ydepart].getId(xdepart);
	}

	public void setBlockId(int x, int y, int id) {
		maplines[y].setId(x, id);
	}

	public void setBlockIdAndData(int x, int y, int id, int data) {
		maplines[y].setId(x, id);
		maplines[y].setData(x, data);
	}

	public void setOverlayIdAndData(int x, int y, int id, int data) {
		overlaylines[y].setId(x, id);
		overlaylines[y].setData(x, data);
	}

	public void setBlockStorage(int x, int y, Object st) {
		storage.put(x + "," + y, st);
	}

	public void setOverlayStorage(int x, int y, Object st) {
		storage.put(x + "," + y + ",o", st);
	}

	public Blocks getBlock(int x, int y) {
		return Blocks.byId(getBlockId(x, y));
	}

	public Object getBlockStorage(int x, int y) {
		if (storage.containsKey(x + "," + y)) {
			return storage.get(x + "," + y);
		} else {
			return null;
		}
	}

	public Object getOverlayStorage(int x, int y) {
		if (storage.containsKey(x + "," + y + ",o")) {
			return storage.get(x + "," + y + ",o");
		} else {
			return null;
		}
	}

	public Blocks getOverlayBlock(int x, int y) {
		return Blocks.byId(getOverlayId(x, y));
	}

	public Blocks getBlockPath() {
		return Blocks.byId(getBlockPathId());
	}

	public Blocks getOverlayPath() {
		return Blocks.byId(getOverlayPathId());
	}

	public Image getBlockTexture(int x, int y) {
		return Blocks.byId(getBlockId(x, y)).getTexture(getBlockData(x, y));
	}

	public Image getOverlayTexture(int x, int y) {
		return Blocks.byId(getOverlayId(x, y)).getTexture(getBlockData(x, y));
	}

	public int getBlockData(int x, int y) {
		return maplines[y].getData(x);
	}

	public int getOverlayData(int x, int y) {
		return overlaylines[y].getData(x);
	}

	public void setBlockData(int x, int y, int data) {
		maplines[y].setData(x, data);
	}

	public void setOverlayData(int x, int y, int data) {
		overlaylines[y].setData(x, data);
	}

	public int getBlockPathData() {
		return maplines[ydepart].getData(xdepart);
	}

	public void extend(int newx, int newy) {
		MapLine[] lines = new MapLine[newy];
		MapLine[] overlines = new MapLine[newy];
		int c = 0;
		for (MapLine d : maplines) {
			if (c >= lines.length - 1) {
				return;
			}
			d.extend(newx);
			lines[c] = d;
			c++;
		}
		c = 0;
		for (MapLine d : overlaylines) {
			if (c >= lines.length - 1) {
				return;
			}
			d.extend(newx);
			overlines[c] = d;
			c++;
		}
		while (c < newy) {
			lines[c] = new MapLine(newx);
			overlines[c] = new MapLine(newx);
			c++;
		}
		maplines = lines;
		overlaylines = overlines;
	}

	public boolean hasPoint(Point p) {
		if (p.getX() >= getXMax() || p.getX() < 0 || p.getY() >= getYMax() || p.getY() < 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean canPlaceTurret(int turret, int x, int y) {
		EntityTypes types = EntityTypes.turrets.get(turret);
		if(types == null) {
			return false;
		}
		Class<? extends Entity> c = types.getEntityClass();
		try {
			Method method = c.getMethod("canPlace", int.class, int.class);
			Object o = method.invoke(null, x, y);
			if(o instanceof Boolean) {
				return (Boolean)o;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Boolean serialize(File file) {
		try {
			FileOutputStream fichier = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			for(String s : Blocks.getBlockIds().keySet()) {
				int i = Blocks.getBlockIds().get(s);
				if(!blockids.containsKey(i) || !blockids.get(i).equals(s)) {
					if(Blocks.getCustomBlock(s) != null) {
						blockids.put(i, s);
						vanilla.put(i, Blocks.getCustomBlock(s).getFallback());
					}
				}
			}
			oos.writeObject(this);
			oos.flush();
			oos.close();
			return true;
		} catch (java.io.IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static Maps deserialize(File file) {
		try {
			FileInputStream fichier = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fichier);
			Maps o = (Maps) ois.readObject();
			ois.close();
			return o;
		} catch (Exception e) {
			return maps[1023];
		}
	}

	public static Maps deserialize(URL url) {
		try {
			FileInputStream fichier = new FileInputStream(url.getFile());
			ObjectInputStream ois = new ObjectInputStream(fichier);
			Maps o = (Maps) ois.readObject();
			ois.close();
			return o;
		} catch (Exception e) {
			return maps[1023];
		}
	}

	public static Maps getOrCreateMap(String map) {
		File outFile = new File(RessourcesManager.getMapsDirectory(), map + ".mtmap");
		try {
			outFile.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		URL mapurl = RessourcesManager.getMap(map);
		if (mapurl != null) {
			InputStream in = null;
			try {
				in = mapurl.openConnection().getInputStream();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				if (in != null) {
					OutputStream out = new FileOutputStream(outFile);
					byte[] buf = new byte[1024];
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					out.close();
					in.close();
					return deserialize(outFile);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return maps[1023];

	}
}
