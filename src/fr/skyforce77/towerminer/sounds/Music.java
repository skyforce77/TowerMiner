package fr.skyforce77.towerminer.sounds;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Popup;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;
import fr.skyforce77.towerminer.save.DataBase;

public class Music {

    private static Music[] playing = new Music[10];

    public static void play(final int channel, String file) {
        stop(channel);
        playing[channel] = new Music(new File(file));
        new Thread() {
            public void run() {
                playing[channel].run();
            }

            ;
        }.start();
    }


    public static void stop(int channel) {
        if (playing[channel] != null) {
            playing[channel].stop();
        }
    }

    public static void playSound(String name) {
        playURL(1, RessourcesManager.getSoundURL(name), false);
    }

    public static void playMusic(String name) {
        try {
            playURL(0, new URL("http://dl.dropboxusercontent.com/u/38885163/TowerMiner/music/" + name + ".wav"), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playURL(final int channel, final URL url, final boolean display) {
        new Thread() {
            @Override
            public void run() {
                String chemin = RessourcesManager.getDirectory().getPath() + "/sounds";
                InputStream input = null;
                FileOutputStream writeFile = null;
                chemin.substring(0, chemin.lastIndexOf("/"));
                try {
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
                    String fileName = url.getFile().substring(url.getFile().lastIndexOf('/') + 1);

                    if (new File(chemin + "/" + fileName).exists()) {
                        play(channel, chemin + "/" + fileName);
                        return;
                    } else {
                        if (display)
                            TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("music.downloading", fileName), 2000, "music_disc_gold"));
                    }

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
                    }
                }
            }
        }.start();
    }


    AudioInputStream audioInputStream = null;
    SourceDataLine line;
    File fichier;
    boolean stopped = false;

    public Music(File f) {
        fichier = f;
    }

    public void run() {
        try {
            @SuppressWarnings("unused")
            AudioFileFormat format = AudioSystem.getAudioFileFormat(fichier);
        } catch (UnsupportedAudioFileException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try {
            audioInputStream = AudioSystem.getAudioInputStream(fichier);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        line.start();
        try {
            byte bytes[] = new byte[1024];
            int bytesRead = 0;
            while (!stopped && (bytesRead = audioInputStream.read(bytes, 0, bytes.length)) != -1) {
                line.write(bytes, 0, bytesRead);

                if (line.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl control = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                    control.setValue((Float) DataBase.getValue("volume", -10F) * 0.8F);
                }
            }
        } catch (IOException io) {
            io.printStackTrace();
            return;
        }
    }

    public void stop() {
        stopped = true;
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

            // previous code: destination.transferFrom(source, 0, source.size());
            // to avoid infinite loops, should be:
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
}
