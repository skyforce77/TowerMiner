package fr.skyforce77.towerminer.ressources.language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.save.DataBase;

public class LanguageManager {

	static HashMap<String, Language> languages = new HashMap<String, Language>();
	static String def = "Français";
	
	public static String getText(String id) {
		if(getLanguage() == null) {
			if(getDefaultLanguage() != null) {
				return getDefaultLanguage().getText(id);
			} else {
				return id;
			}
		}
		return getLanguage().getText(id);
	}
	
	public static String getText(String id, String value) {
		return getLanguage().getText(id).replace("%", value);
	}
	
	public static String getText(String id, int value) {
		return getLanguage().getText(id).replace("%", ""+value);
	}
	
	public static Language getLanguage() {
		return languages.get((String)DataBase.getValue("language", def));
	}
	
	public static String getActualLanguage() {
		return (String)DataBase.getValue("language", def);
	}
	
	public static Language getLanguage(String id) {
		return languages.get(id);
	}
	
	public static void setLanguage(String id) {
		DataBase.setValue("language", id);
	}
	
	public static Language getDefaultLanguage() {
		return languages.get("Français");
	}
	
	public static Set<String> getLanguages() {
		return languages.keySet();
	}
	
	public static void initLanguages() {
		if(!RessourcesManager.getLanguagesDirectory().exists()) {
			RessourcesManager.getLanguagesDirectory().mkdirs();
		}
		
		initLanguage(RessourcesManager.getURL("/ressources/languages/default.txt"));
		initLanguage(RessourcesManager.getURL("/ressources/languages/english.txt"));
		
		for(File f : RessourcesManager.getLanguagesDirectory().listFiles()) {
			initLanguage(f);
		}
	}
	
	public static void initLanguage(File f) {
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String name = br.readLine();
			if(name.startsWith("language: ")) {
				name = name.replace("language: ", "");
				ArrayList<String> lines = new ArrayList<String>();
				while(true) {
					String line = br.readLine();
					if(line == null)
						break;
					lines.add(line);
				}
				languages.put(name, new Language(lines));
			}
			fr.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void initLanguage(URL url) {
		try {
			InputStreamReader fr = new InputStreamReader(url.openConnection().getInputStream());
			BufferedReader br = new BufferedReader(fr);
			String name = br.readLine();
			if(name.startsWith("language: ")) {
				name = name.replace("language: ", "");
				ArrayList<String> lines = new ArrayList<String>();
				while(true) {
					String line = br.readLine();
					if(line == null)
						break;
					lines.add(line);
				}
				languages.put(name, new Language(lines));
			}
			fr.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
