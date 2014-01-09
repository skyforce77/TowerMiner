package fr.skyforce77.towerminer.ressources.language;

import java.util.ArrayList;
import java.util.HashMap;

public class Language {

	HashMap<String, String> sentences = new HashMap<String, String>();
	
	public Language(ArrayList<String> translations) {
		for(String s : translations) {
			if(s.contains(": ") && !s.startsWith("//")) {
				String[] strings = s.split(": ");
				if(strings.length > 2) {
					String s2 = strings[1];
					int i = 2;
					while(i < strings.length) {
						s2 = s2+": "+strings[i];
						i++;
					}
					sentences.put(strings[0], s2);
				} else {
					sentences.put(strings[0], strings[1]);
				}
			}
		}
	}
	
	public boolean hasText(String id) {
		return sentences.containsKey(id);
	}
	
	public String getText(String id) {
		if(hasText(id)) {
			return sentences.get(id);
		} else if(LanguageManager.getDefaultLanguage().hasText(id)) {
			return LanguageManager.getDefaultLanguage().getText(id);
		} else {
			return id;
		}
	}

}
