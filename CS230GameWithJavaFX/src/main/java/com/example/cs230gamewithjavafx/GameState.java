package com.example.cs230gamewithjavafx;

import java.util.ArrayList;

/**
 * A container class to store the state of an ongoing game at any point ready to save it,
 * or to store a recently loaded from txt game file ready for playing.
 * @author Joseph Dawson
 * @version 1.0.0

 */
public class GameState {
    /**
     * how much time is left for the level
     */
    private int timeLimit;
    /**
     * the level board, with all the tiles.
     */
    private Board theBoard;
    /**
     * the list of all the items on the board
     */
    private ArrayList<Item> items;
    /**
     * the list of all the NPCs on the board
     */
    private ArrayList<NPC> NPCs;
    /**
     * the player playing the game, including Profile
     */
    private Player thePlayer;
    /**
     * the level number being played
     */
    private int levelNum;

    /**
     *
     * @param timeLimit how much time left till the level ends
     * @param theBoard the array containing the board tiles
     * @param items the list of items on the map
     * @param NPCs the list of NPCs on the map
     * @param player the profile of the player playing this game
     */
    public GameState (int timeLimit, Board theBoard,
                      ArrayList<Item> items, ArrayList<NPC> NPCs, Player player, int levelNum) {
        this.timeLimit = timeLimit;
        this.theBoard = theBoard;
        this.items = items;
        this.NPCs = NPCs;
        this.thePlayer = player;
        this.levelNum = levelNum;
    }

    /**
     * @return timeLimit remaining for the level
     */
    public int getTimeLimit() {
        return timeLimit;
    }

    /**
     * @return the array containing the game board
     */
    public Board getTheBoard() {
        return theBoard;
    }

    /**
     * @return the array containing all the items on the board
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * @return the array containing all the NPCs on the board
     */
    public ArrayList<NPC> getNPCs() {
        return NPCs;
    }

    /**
     * @return the level number being played
     */
    public int getLevelNum() {
        return levelNum;
    }

    /**
     * @param levelNum set the level number being played
     */
    public void setLevelNum(int levelNum) {
        this.levelNum = levelNum;
    }

    /**
     * @param timeLimit how much time is left on the level
     */
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }


    /**
     * slowly counts down or up the time limit for the level.
     * @param increase boolean to specify adding or subtracting
     */
    public void incrementTimeLimit(boolean increase) {
        if (increase)
            this.timeLimit++;
        else {
            this.timeLimit--;
        }
    }

    /**
     * increases the timelimit by a specified amount, use a negative number to subtract
     * @param time how much to increase the time limit by
     */
    public void increaseTimeLimit(int time){
        timeLimit+=time;
    }

    /**
     * @param theBoard the data structure containing
     *                 all the tiles that make up the game map
     */
    public void setTheBoard(Board theBoard) {
        this.theBoard = theBoard;
    }

    /**
     * @param items the list of all items on the board
     */
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    /**
     * @param NPCs the list of all NPCs on the board
     */
    public void setNPCs(ArrayList<NPC> NPCs) {
        this.NPCs = NPCs;
    }

    /**
     * @return the Player on the board
     */
    public Player getThePlayer() {
        return thePlayer;
    }

    /**
     * removes an item from the list of Items
     * @param itemtoRemove item to remove from the list
     */
    public void removeItem(Item itemtoRemove){
        items.remove(itemtoRemove);
    }

    /**
     * removes an npc from the list of NPCs
     * @param npc the npc being removed from the list
     */
    public void removeNPC(NPC npc){
        NPCs.remove(npc);
    }

    /**
     * @return the gameState as a string for interpretting
     */
    @Override
    public String toString() {
        return "com.example.cs230gamewithjavafx.GameState{" +
                "timeLimit=" + timeLimit +
                ", theBoard=" + theBoard + "\n" +
                ", NPCs=" + items + "\n" +
                ", items=" + NPCs +  "\n" +
                ", playerProfile=" + thePlayer +
                '}';
    }
}
