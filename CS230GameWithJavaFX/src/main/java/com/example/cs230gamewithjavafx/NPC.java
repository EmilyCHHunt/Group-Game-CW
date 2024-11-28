package com.example.cs230gamewithjavafx;

import java.util.ArrayList;

/**
 * Character is a super class of all the NPC involved in the game.
 * @author Chinmayee Malvankar and Emily Hunt and Joseph Dawson
 * @version 1.0.0
 *
 */

public abstract class NPC extends Entity {
    /**
     * the direction the NPC is facing
     */
    private String direction;
    /**
     * @param entityName the name of the NPC type
     * @param xCoord is the X coordinate of NPC on the Board.
     * @param yCoord is the Y coordinate of NPC on the Board.
     * @param direction the direction the NPC is facing
     */
    public NPC(String entityName, int xCoord, int yCoord, String direction){
        super(entityName, xCoord, yCoord);
        this.direction = direction;
    }

    /**
     * @param direction sets the direction of the NPC.
     */
    public void setDirection(String direction){
        this.direction = direction;
    }

    /**
     * @return the direction of the NPC.
     */
    public String getDirection(){
        return direction;
    }

    /**
     * used to create the movement technique of various NPCs
     * @param directionFace the direction the NPC is facing
     * @param theBoard the backend representation of the board
     * @param theBoardUI the front end representation of the board
     * @param itemArrayList the list of items on the board for an NPC to interact with
     */
    public abstract void move(String directionFace, Board theBoard, Tile[][] theBoardUI, ArrayList<Item> itemArrayList);

    /**
     * used to create the movement technique of various NPCs using different data inputs
     * @param directionFace the direction the NPC is facing
     * @param theBoard the backend representation of the board
     * @param theBoardUI the front end representation of the board
     * @param curGameState the container class that holds the games data
     */
    public abstract void move(String directionFace, Board theBoard, Tile[][] theBoardUI, GameState curGameState);

    /**
     * @return the NPCs string of data for converting to a txt file
     */
    public abstract String getStringForFile();

    /**
     * @return the type of the NPC for comparison
     */
    public abstract String getNPCType();

    /**
     * removes an NPC from the game board, deleting it.
     * @param theBoard the backend representation of the board
     * @param curGameState the container class that holds the games data
     */
    public void removeFromBoard(Board theBoard, GameState curGameState) {
        theBoard.getTile(getxCoord(),getyCoord()).setOccupyingCharacter(null);
        curGameState.removeNPC(this);
        getImageView().setVisible(false);
    }
}
