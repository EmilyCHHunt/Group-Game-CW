package com.example.cs230gamewithjavafx;

/**
  * Class to store the details of an individual highscore,
  * the players name, the score achieved, and what level it corresponds to.
 * @author Joseph Dawson
 * @version 1.0.0

 */
public class HighScore implements Comparable<HighScore> {
    /**
     * the name of the player who obtained the score
     */
    private final String name;
    /**
     * the value of the score achieved
     */
    private final int score;
    /**
     * the level the score was earnt on
     */
    private final int levelNum;

    /**
     * @param levelNum the level the score was achieved
     * @param name username to store
     * @param score the score acheived
     */
    public HighScore(int levelNum, String name, int score) {
        this.levelNum = levelNum;
        this.name = name;
        this.score = score;
    }

    /**
     * @return the score achievers name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the score achieved
     */
    public int getScore() {
        return score;
    }

    public int getLevelNum() {
        return levelNum;
    }

    /**
     * @return the highscore as a String
     */
    @Override
    public String toString() {
        return "Name: " + getName()
                + " Score: " + getScore()
                +" Level: " + getLevelNum();
    }


    /**
     * compares highscores to create a sort function, sorted by level number and then score
     * @param o another highscore object to compare
     * @return -1,0,1 based on if this object is less than, equal to, or greater than the specified object
     */
    @Override
    public int compareTo(HighScore o) {
        int compareLevel = this.levelNum - o.levelNum;
        if (compareLevel < 0) {
            return -1;
        } else if (compareLevel > 0) {
            return 1;
        } else {
            int compareScore = this.score - o.score;
            if (compareScore < 0) {
                return -1;
            } else if (compareScore > 0) {
                return 1;
            } else {
                return 0;
            }

        }

    }
}
