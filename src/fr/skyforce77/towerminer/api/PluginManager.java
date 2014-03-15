package fr.skyforce77.towerminer.api;

import java.awt.Event;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import fr.skyforce77.towerminer.multiplayer.MPInfos;
import fr.skyforce77.towerminer.protocol.BigSending;
import fr.skyforce77.towerminer.protocol.ObjectReceiver;
import fr.skyforce77.towerminer.protocol.packets.Packet21LoadPlugin;
import fr.skyforce77.towerminer.ressources.FileContainer;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class PluginManager {

	private static ArrayList<Plugin> plugins = new ArrayList<>();
	private static ArrayList<File> pluginfiles = new ArrayList<>();
	private static ArrayList<String> pluginlist = new ArrayList<>();

	public static void initPlugins() {
		RessourcesManager.getPluginsDirectory().mkdirs();
		for(File f : RessourcesManager.getPluginsDirectory().listFiles()) {
			if(f.getName().contains(".jar")) {
				try {
					URL[] urls = new URL[]{f.toURI().toURL()};
					@SuppressWarnings("resource")
					URLClassLoader loader = new URLClassLoader(urls);
					Class<?> cls = loader.loadClass("TMPlugin");
					Plugin p = (Plugin)cls.newInstance();
					loadPlugin(p, f);
				} catch (Exception e) {
					System.out.println("Can't launch "+f.getName()+" plugin.");
					e.printStackTrace();
				}
			}
		}
	}

	public static void loadPlugin(Plugin p, File f) {
		try {
			plugins.add(p);
			pluginlist.add(p.getName()+"("+p.getVersion()+")");
			pluginfiles.add(f);
			System.out.println("Enabling "+p.getName()+" plugin");
			p.onEnable();
			System.out.println("Successfully enabled "+p.getName()+" plugin");
		} catch (Exception e) {
			System.out.println("Can't launch "+p.getName()+" plugin.");
			e.printStackTrace();
		}
	}

	public static void loadPlugin(FileContainer fc) {
		try {
			File fl = fc.recreate(RessourcesManager.getTempDirectory());
			RessourcesManager.getServerPluginsDirectory().mkdir();
			File f = new File(RessourcesManager.getServerPluginsDirectory(), fl.getName());
			f.createNewFile();
			move(fl, f);
			if(f.getName().contains(".jar")) {
				try {
					URL[] urls = new URL[]{f.toURI().toURL()};
					@SuppressWarnings("resource")
					URLClassLoader loader = new URLClassLoader(urls);
					Class<?> cls = loader.loadClass("TMPlugin");
					Plugin p = (Plugin)cls.newInstance();
					loadPlugin(p, f);
				} catch (Exception e) {
					System.out.println("Can't launch "+f.getName()+" plugin.");
					e.printStackTrace();
				}
			}
			fl.delete();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static ArrayList<String> getPlugins() {
		return pluginlist;
	}

	public static boolean canConnect(ArrayList<String> pl) {
		boolean can = true;
		for(Plugin p : plugins) {
			if(p.isPluginNeededByClient()) {
				if(!pl.contains(pluginlist.get(plugins.indexOf(p)))) {
					can = false;
				}
			}
		}
		return can;
	}

	public static String getNeededPlugins(ArrayList<String> pl) {
		String s = "";
		for(Plugin p : plugins) {
			if(p.isPluginNeededByClient()) {
				if(!pl.contains(pluginlist.get(plugins.indexOf(p)))) {
					s = s+" "+pluginlist.get(plugins.indexOf(p));
				}
			}
		}
		return s;
	}

	public static void sendNeededPlugins(ArrayList<String> pl) {
		for(final Plugin p : plugins) {
			if(p.isPluginNeededByClient()) {
				if(!pl.contains(pluginlist.get(plugins.indexOf(p)))) {
					try {
						BigSending.sendBigObject(new FileContainer(pluginfiles.get(plugins.indexOf(p)), p.getName()+"("+p.getVersion()+").jar"), MPInfos.connection, new ObjectReceiver.ReceivingThread() {
							@Override
							public void run(int objectid) {
								Packet21LoadPlugin pe = new Packet21LoadPlugin();
								pe.eid = objectid;
								pe.sendAllTCP();
							}
						});
						Thread.sleep(1000l);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@SuppressWarnings("resource")
	public static void move(File source, File destination) {
		try {
			if(!destination.exists()) {
				destination.createNewFile();
			}

			FileChannel sourcec = null;
			FileChannel destinationc = null;
			try {
				sourcec = new FileInputStream(source).getChannel();
				destinationc = new FileOutputStream(destination).getChannel();
				long count = 0;
				long size = sourcec.size();              
				while((count += destinationc.transferFrom(sourcec, count, size-count))<size);
				source.delete();
			}
			finally {
				if(sourcec != null) {
					sourcec.close();
				}
				if(destinationc != null) {
					destinationc.close();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static List<Class<?>> handlers = new ArrayList<Class<?>>();

    public static void registerHandler(Class<?> handler) {
        handlers.add(handler);
    }

    public static List<Class<?>> getHandlers() {
        return handlers;
    }
    
    public static void callEvent(final Event event) {
        new Thread() {
            @Override
            public void run() {
                callCompleteEvent(event);
            }
        }.start();
    }

    private static void callCompleteEvent(final Event event) {
        for (Class<?> handler : getHandlers()) {
            Method[] methods = handler.getMethods();

            for (int i = 0; i < methods.length; ++i) {
                EventHandler eventHandler = methods[i].getAnnotation(EventHandler.class);
                if (eventHandler != null) {
                    Class<?>[] methodParams = methods[i].getParameterTypes();

                    if (methodParams.length < 1)
                        continue;

                    if (!event.getClass().getSimpleName()
                            .equals(methodParams[0].getSimpleName()))
                        continue;

                    try {
                        methods[i].invoke(handler.newInstance(), event);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            }
        }
    }

}
