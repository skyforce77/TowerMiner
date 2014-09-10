package fr.skyforce77.towerminer.api;

import com.esotericsoftware.kryonet.Connection;
import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.api.events.TMEvent;
import fr.skyforce77.towerminer.menus.Menu;
import fr.skyforce77.towerminer.multiplayer.MPInfos;
import fr.skyforce77.towerminer.protocol.BigSending;
import fr.skyforce77.towerminer.protocol.ObjectReceiver;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.chat.ChatModel;
import fr.skyforce77.towerminer.protocol.chat.MessageModel;
import fr.skyforce77.towerminer.protocol.containers.FileContainer;
import fr.skyforce77.towerminer.protocol.packets.Packet21LoadPlugin;
import fr.skyforce77.towerminer.protocol.packets.Packet22PluginMessage;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.awt.*;
import java.beans.IntrospectionException;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager {

    private static ArrayList<Plugin> plugins = new ArrayList<Plugin>();
    private static ArrayList<File> pluginfiles = new ArrayList<File>();
    private static ArrayList<String> pluginlist = new ArrayList<String>();
    private static List<TMListener> handlers = new ArrayList<TMListener>();
    public static boolean responsereceived = false;
    public static boolean pluginsreceived = false;

    public static void initPlugins() {
        RessourcesManager.getPluginsDirectory().mkdirs();
        for (File f : RessourcesManager.getPluginsDirectory().listFiles()) {
            if (f.getName().contains(".jar")) {
                loadPlugin(f);
            }
        }
    }

    public static void loadPlugin(Plugin p, File f) {
        try {
            plugins.add(p);
            pluginlist.add(p.getName() + "(" + p.getVersion() + ")");
            ChatModel plu = new ChatModel(p.getName());
            plu.setForegroundColor(Color.CYAN);
            plu.setMouseModel(new MessageModel("Version: " + p.getVersion()));
            pluginfiles.add(f);
            TowerMiner.printInfo("Enabling " + p.getName() + " plugin");
            p.onEnable();
            TowerMiner.printInfo("Successfully enabled " + p.getName() + " plugin");
            TowerMiner.menu.chat.onMessageReceived(new ChatMessage(new ChatModel("Plugin "), plu, new ChatModel(" enabled")));
        } catch (Exception e) {
            TowerMiner.printError("Can't enable " + p.getName() + " plugin.");
            e.printStackTrace();
        }
    }

    public static void loadPlugin(File f) {
        if (f.getName().contains(".jar")) {
            try {
                addURLToSystemClassLoader(f.toURI().toURL());
                @SuppressWarnings("resource")
                JarFile jar = new JarFile(f.getPath());
                JarEntry entry = jar.getJarEntry("plugin.yml");
                if (entry == null) {
                    throw new FileNotFoundException("Jar does not contain plugin.yml");
                }
                InputStream stream = jar.getInputStream(entry);
                Yaml yaml = new Yaml(new SafeConstructor());
                Map<?, ?> map = asMap(yaml.load(stream));
                Class<?> cls = ClassLoader.getSystemClassLoader().loadClass(map.get("main").toString());
                Plugin p = (Plugin) cls.newInstance();
                p.setPluginInfos(new PluginInfo(map));
                loadPlugin(p, f);
            } catch (Exception e) {
                TowerMiner.printError("Can't launch " + f.getName() + " plugin.");
                //e.printStackTrace();
            }
        }
    }

    public static void loadPlugin(FileContainer fc) {
        try {
            File fl = fc.recreate(RessourcesManager.getTempDirectory());
            RessourcesManager.getServerPluginsDirectory().mkdir();
            File f = new File(RessourcesManager.getServerPluginsDirectory(), fl.getName());
            f.createNewFile();
            move(fl, f);
            loadPlugin(f);
            fl.delete();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void addURLToSystemClassLoader(URL url) throws IntrospectionException {
        URLClassLoader systemClassLoader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class<URLClassLoader> classLoaderClass = URLClassLoader.class;
        try {
            Method method = classLoaderClass.getDeclaredMethod("addURL", new Class[]{URL.class});
            method.setAccessible(true);
            method.invoke(systemClassLoader, new Object[]{url});
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IntrospectionException("Error when adding url to system ClassLoader ");
        }
    }

    public static ArrayList<String> getPlugins() {
        return pluginlist;
    }

    public static boolean canConnect(ArrayList<String> pl) {
        boolean can = true;
        for (Plugin p : plugins) {
            if (p.isPluginNeededByClient()) {
                if (!pl.contains(pluginlist.get(plugins.indexOf(p)))) {
                    can = false;
                }
            }
        }
        return can;
    }

    public static String getNeededPlugins(ArrayList<String> pl) {
        String s = "";
        for (Plugin p : plugins) {
            if (p.isPluginNeededByClient()) {
                if (!pl.contains(pluginlist.get(plugins.indexOf(p)))) {
                    s = s + " " + pluginlist.get(plugins.indexOf(p));
                }
            }
        }
        return s;
    }

    public static boolean sendNeededPlugins(ArrayList<String> pl) {
        for (final Plugin p : plugins) {
            if (p.isPluginNeededByClient()) {
                if (!pl.contains(pluginlist.get(plugins.indexOf(p)))) {
                    try {
                        BigSending.sendBigObject(new FileContainer(pluginfiles.get(plugins.indexOf(p)), p.getName() + "(" + p.getVersion() + ").jar"), MPInfos.connection, new ObjectReceiver.ReceivingThread() {
                            @Override
                            public void run(int objectid) {
                                Packet21LoadPlugin pe = new Packet21LoadPlugin();
                                pe.eid = objectid;
                                pe.sendAllTCP();
                            }
                        });
                        while(!responsereceived) {
                        	Thread.sleep(100l);
                        }
                        responsereceived = false;
                        if(pluginsreceived) {
                        	pluginsreceived = false;
                        	return true;
                        } else {
                            TowerMiner.returnMenu(Menu.mainmenu);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    @SuppressWarnings("resource")
    public static void move(File source, File destination) {
        try {
            if (!destination.exists()) {
                destination.createNewFile();
            }

            FileChannel sourcec = null;
            FileChannel destinationc = null;
            try {
                sourcec = new FileInputStream(source).getChannel();
                destinationc = new FileOutputStream(destination).getChannel();
                long count = 0;
                long size = sourcec.size();
                while ((count += destinationc.transferFrom(sourcec, count, size - count)) < size) ;
                source.delete();
            } finally {
                if (sourcec != null) {
                    sourcec.close();
                }
                if (destinationc != null) {
                    destinationc.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerListener(TMListener listener) {
        handlers.add(listener);
        TowerMiner.printInfo("Registered listener " + listener.getClass().getSimpleName());
    }

    public static List<TMListener> getListeners() {
        return handlers;
    }

    public static void callSyncEvent(final TMEvent event) {
        for (EventPriority ep : EventPriority.values())
            callCompleteEvent(event, ep);
    }

    public static void callAsyncEvent(final TMEvent event) {
        new Thread("AsyncCallEvent-" + event.getEventType()) {
            @Override
            public void run() {
                for (EventPriority ep : EventPriority.values())
                    callCompleteEvent(event, ep);
            }
        }.start();
    }

    private static void callCompleteEvent(final TMEvent event, EventPriority priority) {
        for (TMListener listener : getListeners()) {
            Class<?> handler = listener.getClass();
            Method[] methods = handler.getMethods();

            for (int i = 0; i < methods.length; ++i) {
                EventHandler eventHandler = methods[i].getAnnotation(EventHandler.class);
                if (eventHandler != null && eventHandler.priority().equals(priority)) {
                    Class<?>[] methodParams = methods[i].getParameterTypes();

                    if (methodParams.length < 1)
                        continue;

                    if (!event.getClass().getSimpleName()
                            .equals(methodParams[0].getSimpleName()))
                        continue;

                    try {
                        methods[i].invoke(listener, event);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            }
        }
    }

    public static void sendPluginMessage(Plugin p, int id, Serializable data) {
        Packet22PluginMessage ppm = new Packet22PluginMessage(p.getName(), p.getVersion(), id, data);
        ppm.sendAllTCP();
    }

    public static void sendPluginMessage(Connection c, Plugin p, int id, Serializable data) {
        Packet22PluginMessage ppm = new Packet22PluginMessage(p.getName(), p.getVersion(), id, data);
        ppm.sendConnectionTCP(c);
    }

    private static Map<?, ?> asMap(Object object) {
        if (object instanceof Map) {
            return (Map<?, ?>) object;
        }
        return null;
    }

}
