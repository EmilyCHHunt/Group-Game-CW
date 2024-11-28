package com.example.cs230gamewithjavafx;

/**
 * Stores the player's profile data, where name is assumed to always be unique
 * @author Joseph Dawson
 * @version 1.0.0
 *
 */
public class PlayerProfile {
    /**
     * the players name
     */
    private String name;
    /**
     * the maximum level the player has unlocked and is able to play
     */
    private int levelUnlocked;

    /**
     *
     * @param name the users name
     * @param levelUnlocked the maximum level they have unlocked
     */
    public PlayerProfile(String name, int levelUnlocked) {
        this.name = name;
        this.levelUnlocked = levelUnlocked;
    }

    /**
     * @return the players name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the identifying factor for the profile
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the maximum level the player has unlocked
     */
    public int getLevelUnlocked() {
        return levelUnlocked;
    }

    /**
     * @param levelUnlocked set the level the player has unlocked
     */
    public void setLevelUnlocked(int levelUnlocked) {
        this.levelUnlocked = levelUnlocked;
    }

    /**
     * increase the level unlocked value by one
     */
    public void incrementLevelUnlocked() {
        this.levelUnlocked++;
    }

    /**
     * @return the player profile as a string
     */
    @Override
    public String toString() {
        return name  +
                ", level: " + levelUnlocked;
    }
}
