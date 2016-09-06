package fr.skyforce77.towerminer.sounds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Popup;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class Music {

	// ameliration : on peut gerer les channels de maniere masqué
	// rendre private la méthode playURL
	// comment s'abonner au evenement?? 
	
	private final static int MUSIC_CHANNEL = 0;
	private final static int SOUND_CHANNEL = 1;
	private final static int CHANNEL_COUNT = 10;

    private static Track[] tracks = new Track[CHANNEL_COUNT];
    
    public static void stopAll() {
    	
    	for(int channel = 0;channel<CHANNEL_COUNT;channel++)
    	{
	        if (tracks[channel] != null) {
	        	tracks[channel].stop();
	        }
    	}
    }

    public static void volumeChange(float volume){
    	for(int channel = 0;channel<CHANNEL_COUNT;channel++)
    	{
	        if (tracks[channel] != null) {
	        	tracks[channel].applyVolume(volume);
	        }
    	}
    }
   
    public static void playSound(String name) {
    	try {
    		playURL(SOUND_CHANNEL, RessourcesManager.getSoundURL(name), false);
    	} 
        catch (Exception e) {
        	TowerMiner.printError("playSound : " + e.getMessage());
        }
    }

    public static void playMusic(String name) {
        try {
            playURL(MUSIC_CHANNEL, new URL("https://dl.dropboxusercontent.com/u/38885163/TowerMiner/music/" + name + ".wav"), false);
        } 
        catch (Exception e) {
        	TowerMiner.printError("playMusic : " + e.getMessage());
        }
    }
    
    public static void playSound(URL cible) {
        try {
            playURL(SOUND_CHANNEL, cible, false);
        } 
        catch (Exception e) {
        	TowerMiner.printError("playSound : " + e.getMessage());
        }
    }

    public static void playURL(final int channel, final URL url, final boolean display) {
        new Thread("MusicURLDownload") {
            @Override
            public void run() {
                String chemin = RessourcesManager.getDirectory().getPath() + "/sounds";
                InputStream input = null;
                FileOutputStream writeFile = null;
                chemin.substring(0, chemin.lastIndexOf("/"));
                try {
                	String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);

                	TowerMiner.printInfo("Playing sound : " + fileName);
                	
                    if (!new File(chemin + "/" + fileName).exists()) {
                    	
                    	URLConnection connection = url.openConnection();
                    	int fileLength = connection.getContentLength();
                    	
                    	if (!new File(chemin).exists()) {
                    		new File(chemin).mkdirs();
                    	}

                    	if (fileLength == -1) {
                    		if (display)
                    			TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("music.error"), 2000, "music_disc_gold"));
                    		TowerMiner.print("Invalid URL or File", "music");
                    		return;
                    	}
                    
                    	input = connection.getInputStream();
                    

	                    File temp = new File(RessourcesManager.getDirectory().getPath() + "/temp");
	                    temp.mkdirs();
	                    File temploc = new File(temp.getPath() + "/" + fileName);
	                    temploc.deleteOnExit();
	                    writeFile = new FileOutputStream(temploc);
	                    byte[] buffer = new byte[1024];
	                    int read;

	                    while ((read = input.read(buffer)) > 0) {
	                        writeFile.write(buffer, 0, read);
	                    }
	                    writeFile.flush();
	
	                    copyFile(temploc, new File(chemin + "/" + fileName));
                    }

                    if (display)
                        TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("music.finished"), 2000, "music_disc_gold"));
                    
                    play(channel, chemin + "/" + fileName);

                } catch (IOException e) {
                    TowerMiner.print("Error while trying to download the file.", "music");
                    e.printStackTrace();
                    return;
                } finally {
                    try {
                        if (writeFile != null && input != null) {
                            writeFile.close();
                            input.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }.start();
    }

    @SuppressWarnings({"resource"})
    private static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();

            long count = 0;
            long size = source.size();
            while ((count += destination.transferFrom(source, count, size - count)) < size) ;
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    private static void play(final int channel, String file) {
    	if(tracks[channel] != null)
    		tracks[channel].stop();
    	
    	tracks[channel] = new Track(new File(file));
        
        new Thread("MusicPlaying") {
            public void run() {
                try {
                	tracks[channel].run();
                } catch (Exception e) {
                }
            }
        }.start();
    }

 
}
