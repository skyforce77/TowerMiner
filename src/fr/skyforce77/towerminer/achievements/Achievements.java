package fr.skyforce77.towerminer.achievements;

import java.util.HashMap;

public class Achievements {

	public static HashMap<Integer, Achievement> achievements = new HashMap<>();
	
	public static void initAchievements() {
		new Achievement(1, "steel_ingot");
		new Achievement(2, "steel_ingot");
		new Achievement(3, "steel_ingot");
	}
	
	public static void unlockAchievement(int id) {
		Achievement a = achievements.get(id);
		if(!a.has()) {
			a.give();
		}
	}

}
