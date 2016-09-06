package fr.skyforce77.towerminer.sounds;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.save.DataBase;

public class Track {
	
	   
    private AudioInputStream audioInputStream = null;
    private SourceDataLine line;
    private File fichier;
    private boolean stopped = false;
	

    protected Track(File f) {
        fichier = f;
       
    }

    protected void run() {
        try {
            @SuppressWarnings("unused")
            AudioFileFormat format = AudioSystem.getAudioFileFormat(fichier);
        } catch (Exception e1) {
        }

        try {
            audioInputStream = AudioSystem.getAudioInputStream(fichier);
        } catch (Exception e) {
        }

        AudioFormat audioFormat = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return;
        }

        try {
            if (line.isOpen()) {
                line.close();
            }
            line.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return;
        }
        
        applyVolume((float)DataBase.getValue("volume", 65536F));
        
        line.start();
        
       try {
            byte bytes[] = new byte[1024];
            int bytesRead = 0;
            while (!stopped && (bytesRead = audioInputStream.read(bytes, 0, bytes.length)) != -1) {
                line.write(bytes, 0, bytesRead);
            }
        } catch (IOException io) {
            io.printStackTrace();
            return;
        }
    }

    protected void stop() {
        stopped = true;
    }
    
    protected void applyVolume(float volume){
    	
    	try {
    		
             FloatControl control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
               
             // conversion du volume en % en valeur
             float minControl = control.getMinimum();
             float maxControl = control.getMaximum();          
             float gain =  minControl + ((maxControl - minControl) * volume/100);
             
             control.setValue(gain);
             
             TowerMiner.printInfo("Application du volume "  +control.getValue());
             
        } catch (Exception e) {
        	TowerMiner.printError(e.getMessage()); 
             return;
        }
    
    }
    

}
