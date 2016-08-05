package fr.skyforce77.towerminer.multiplayer;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.protocol.save.TMStorage;

public class MPInfos {

    public static boolean matchplaying;
    public static Connection connection;
    private static TMStorage serverProperties = new TMStorage();
    
    public static void remove() {
    	matchplaying = false;
        connection = null;
        serverProperties = new TMStorage();
    }
    
    public static TMStorage getProperties() {
    	return serverProperties;
    }
    
    public static void setProperties(TMStorage storage) {
    	serverProperties = storage;
    }

}
