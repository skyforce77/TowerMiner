package fr.skyforce77.towerminer.achievements;

import java.util.HashMap;

public class Achievements {

    public static HashMap<Integer, Achievement> achievements = new HashMap<Integer, Achievement>();

    public static void initAchievements() {
        new Achievement(1, "steel_ingot");
        new Achievement(2, "steel_ingot");
        new Achievement(3, "steel_ingot");
        new Achievement(4, "potion");
        new Achievement(5, "flintandsteel");
        new Achievement(6, "poison");
        new Achievement(7, "skull_steve");
        new Achievement(8, "mushroomcow");
        new Achievement(9, "flower_allium");
    }

    public static void unlockAchievement(int id) {
        Achievement a = achievements.get(id);
        if (a != null && !a.has()) {
            a.give();
        }
    }

}
