package fr.skyforce77.towerminer.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class DataBase implements Serializable {

    private static final long serialVersionUID = 4364142227662752242L;
    private static HashMap<String, Object> map = new HashMap<String, Object>();
    private static ArrayList<String> perm = new ArrayList<String>();

    public static Object getValue(String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return null;
        }
    }

    public static Object getValue(String key, Object def) {
        if (map.containsKey(key)) {
            return map.get(key);
        } else {
            return def;
        }
    }

    public static void setValue(String key, Object value) {
        map.put(key, value);
        save();
    }

    public static boolean hasPermission(String key) {
        return perm.contains(key);
    }

    public static void addPermission(String key) {
        if (!hasPermission(key)) {
            perm.add(key);
            save();
        }
    }

    public static void removePermission(String key) {
        perm.remove(key);
        save();
    }

    public static void save() {
        try {
            FileOutputStream bos = new FileOutputStream(new File(RessourcesManager.getSaveDirectory(), "database"));
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(map);
            out.writeObject(perm);
            bos.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void load() {
        File f = new File(RessourcesManager.getSaveDirectory(), "database");
        try {
            if (!f.exists()) {
                f.createNewFile();
                save();
                return;
            }
            FileInputStream fis = new FileInputStream(f);
            ObjectInput in = new ObjectInputStream(fis);
            map = (HashMap<String, Object>) in.readObject();
            try {
                perm = (ArrayList<String>) in.readObject();
            } catch (Exception e) {
            }
            fis.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
