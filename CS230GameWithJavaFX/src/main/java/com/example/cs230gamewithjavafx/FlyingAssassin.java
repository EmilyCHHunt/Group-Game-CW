package com.example.cs230gamewithjavafx;

import java.util.ArrayList;

/**
  * FlyingAssassin is a subclass of NPC. It moves in a straight line in the direction it
   * is facing and moves 180 degree when reached the edge of the board.
   * If the player connects with this NPC then the player looses.
   * If another NPC connects with flying assassin then that NPC is removed.
 * @author chinmayee
 * @version 0.5.0
 */
public class FlyingAssassin extends NPC {

    /**
     * @param xCoord    is the x coordinate of the flying assassin.
     * @param yCoord    is the y coordinate of the flying assassin.
     * @param direction is the direction in which the flying assassin will be facing.
     */
    public FlyingAssassin(int xCoord, int yCoord, String direction) {
        super("FlyingAssassin", xCoord, yCoord, direction);
    }

    /**
     * Nothing as dont need that specific input types
     * @param directionFace the direction the NPC is facing
     * @param theBoard the backend representation of the board
     * @param theBoardUI the front end representation of the board
     * @param itemArrayList the list of items on the board for an NPC to interact with
     */
    @Override
    public void move(String directionFace, Board theBoard, Tile[][] theBoardUI, ArrayList<Item> itemArrayList) {

    }

    /**
     * determines where the FA moves and how
     * @param directionFaced the direction the NPC is facing
     * @param theBoard the backend representation of the board
     * @param theBoardUI the front end representation of the board
     * @param curGameState the container class that holds the games data
     */
    @Override
    public void move(String directionFaced, Board theBoard, Tile[][] theBoardUI, GameState curGameState) {
        theBoard.getTile(getxCoord(),getyCoord()).setOccupyingCharacter(null);
        switch (directionFaced) {
            case "n":
                if (getyCoord() <= 0) {
                    this.setDirection("s");
                    incrementY(1);
                } else {
                    incrementY(-1);
                }
                this.getImageView().setY(theBoardUI[0][0].getTranslateY() + getyCoord() * 50);
                break;
            case "s":
                if (getyCoord() >= theBoard.getBoardMaxY() - 1) {
                    this.setDirection("n");
                    incrementY(-1);
                } else {
                    incrementY(1);
                }
                this.getImageView().setY(theBoardUI[0][0].getTranslateY() + getyCoord() * 50);
                break;
            case "e":
                if (getxCoord() >= theBoard.getBoardMaxX()-1) {
                    this.setDirection("w");
                    incrementX(-1);
                } else {
                    incrementX(1);
                }
                this.getImageView().setX(theBoardUI[0][0].getTranslateX() + getxCoord() * 50);

                break;
            case "w":
                if (getxCoord() <= 0) {
                    this.setDirection("e");
                    incrementX(1);
                } else {
                    incrementX(-1);
                }
                this.getImageView().setX(theBoardUI[0][0].getTranslateX() + getxCoord() * 50);
                break;
            default:
                System.out.println("not valid");
                break;
        }
        theBoard.getTile(getxCoord(),getyCoord()).setOccupyingCharacter(this);
    }


    /**
     * a method to print that the player should be killed
     */
    @Deprecated
    public void kill() {
        System.out.println("die!");
    }

    /**
     * @return the flying assassin as a string for txt file saving
     */
    @Override
    public String getStringForFile() {
        return "FlyingAssassin " + getxCoord() + "," + getyCoord() + " " + getDirection();
    }

    /**
     * @return a string for NPC type comparisons
     */
    @Override
    public String getNPCType() {
        return "FlyingAssassin";
    }

}
