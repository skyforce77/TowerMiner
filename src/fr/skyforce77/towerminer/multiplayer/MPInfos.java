package fr.skyforce77.towerminer.multiplayer;

import com.esotericsoftware.kryonet.Connection;

public class MPInfos {

    public static boolean matchplaying;
    public static Connection connection;
    
    public static void remove() {
    	matchplaying = false;
        connection = null;
    }

}
