package fr.skyforce77.towerminer.ressources;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;

public class RessourcesManager {

	private static HashMap<String, Image> imgurls = new HashMap<String, Image>();
	private static ArrayList<String> dl = new ArrayList<String>();
	private static int background = new Random().nextInt(2);
	private static File directory = null;

	public RessourcesManager() {
		super();
	}

	public static Image getDistantImage(final String url, String defaultt) {
		return getDistantImage(url, getTexture(defaultt));
	}

	public static Image getDistantImage(final String url, Image defaultt) {
		if (imgurls.containsKey(url)) {
			return imgurls.get(url);
		} else {
			if (!dl.contains(url)) {
				new Thread("ImageDownload-"+url) {
					@Override
					public void run() {
						dl.add(url);
						try {
							Image img = ImageIO.read(new URL(url));
							imgurls.put(url, img);
						} catch (Exception e) {}
					}
				}.start();
			}
			return defaultt;
		}
	}

	public static Image getTexture(String texture) {
		Image image = new ImageIcon(TowerMiner.class.getResource("/ressources/textures/" + texture + ".png")).getImage();
		return image;
	}

	public static BufferedImage getBufferedTexture(String texture) {
		try {
			return ImageIO.read(TowerMiner.class.getResource("/ressources/textures/" + texture + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getBufferedTexture("unknown");
	}

	public static BufferedImage getDistantBufferedTexture(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getBufferedTexture("unknown");
	}

	public static ImageIcon getIconTexture(String texture) {
		return new ImageIcon(TowerMiner.class.getResource("/ressources/textures/" + texture + ".png"));
	}

	public static ImageIcon getGifIconTexture(String texture) {
		return new ImageIcon(TowerMiner.class.getResource("/ressources/textures/" + texture + ".gif"));
	}

	public static Image getIcon() {
		Image image = new ImageIcon(TowerMiner.class.getResource("/ressources/icon.png")).getImage();
		return image;
	}

	public static Image getBackground() {
		Image image = new ImageIcon(TowerMiner.class.getResource("/ressources/background"+background+".png")).getImage();
		return image;
	}

	public static URL getSoundURL(String file) {
		return TowerMiner.class.getResource("/ressources/sounds/" + file + ".wav");
	}

	public static URL getURL(String file) {
		return TowerMiner.class.getResource(file);
	}

	public static URL getMap(String map) {
		return TowerMiner.class.getResource("/ressources/maps/" + map + ".mtmap");
	}

	public static File getDirectory() {
		if(directory != null) {
			return directory;
		}

		File f = null;
		String OS = System.getProperty("os.name").toUpperCase();

		if (OS.contains("WIN"))
			f = new File(System.getenv("APPDATA"), "/.towerminer");
		else if (OS.contains("MAC"))
			f = new File(System.getProperty("user.home") + "/Library/Application Support", "/.towerminer");
		else if (OS.contains("NUX"))
			f = new File(System.getProperty("user.home"), "/.towerminer");
		else
			f = new File(System.getProperty("user.dir"), "/.towerminer");

		if(TowerMiner.dev) {
			f = new File(f.getAbsolutePath()+"-dev");
		}

		directory = f;
		return f;
	}

	public static File getMapsDirectory() {
		return new File(getDirectory(), "/maps");
	}

	public static File getPluginsDirectory() {
		return new File(getDirectory(), "/plugins");
	}

	public static File getLanguagesDirectory() {
		return new File(getDirectory(), "/languages");
	}

	public static File getSaveDirectory() {
		return new File(getDirectory(), "/internal");
	}

	public static File getTempDirectory() {
		return new File(getDirectory(), "/temp");
	}

	public static File getServerDirectory() {
		return new File(getDirectory(), "/servers");
	}

	public static File getServerDirectory(Connection c) {
		File f = new File(getServerDirectory(), "/"+c.getRemoteAddressTCP().getHostString());
		f.mkdirs();
		return f;
	}

	public static File getServerPluginsDirectory(Connection c) {
		File f = new File(getServerDirectory(c), "/plugins");
		f.mkdirs();
		return f;
	}

	public static File getServerCookies(Connection c) {
		return new File(getServerDirectory(c), "/cookies.yml");
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getServerCookiesAsMap(Connection c) {
		File cookies = getServerCookies(c);
		if(cookies.exists()) {
			Yaml yaml = new Yaml(new SafeConstructor());
			try {
				return (Map<String, Object>) yaml.load(new FileReader(cookies));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return new HashMap<String, Object>();
	}

	public static void addServerCookie(Connection c, String name, String value) {
		Yaml yaml = new Yaml(new SafeConstructor());
		Map<String, Object> map = getServerCookiesAsMap(c);
		map.put(name,value);
		try {
			File f = getServerCookies(c);
			if(!f.exists())
				f.createNewFile();
			yaml.dump(map, new FileWriter(f));
			TowerMiner.print("Added to server "+c.getRemoteAddressTCP().getHostString()+" \""+name+": "+value+"\"", "COOKIE");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void removeServerCookie(Connection c, String name) {
		Yaml yaml = new Yaml(new SafeConstructor());
		Map<String, Object> map = getServerCookiesAsMap(c);
		if(map.containsKey(name)) {
			map.remove(name);
			try {
				File f = getServerCookies(c);
				if(!f.exists())
					f.createNewFile();
				yaml.dump(map, new FileWriter(f));
				TowerMiner.print("Removed from server "+c.getRemoteAddressTCP().getHostString()+" \""+name, "COOKIE");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getServerCookie(Connection c, String name) {
		Map<String, Object> map = getServerCookiesAsMap(c);
		if(map.containsKey(name)) {
			return map.get(name).toString();
		} else {
			return "null";
		}
	}

	public static File copyTo(URL url, File outFile) {
		try {
			outFile.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (url != null) {
			InputStream in = null;
			try {
				in = url.openConnection().getInputStream();
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
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return outFile;
	}
}
