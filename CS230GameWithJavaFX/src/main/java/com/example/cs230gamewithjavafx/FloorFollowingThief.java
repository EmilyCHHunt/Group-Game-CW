package com.example.cs230gamewithjavafx;

import java.util.ArrayList;

/**
 * * FloorFollowingThief is a subclass of NPC. It moves on the colour of the tile they are assigned to
 *  * and follows the edge of the board. It starts in the direction they are facing
 *  * and to moves to left-hand side.
 * @author Emily Hunt
 * @version 0.5.0
 */

public class FloorFollowingThief extends NPC {

    private String colourOfTileAssigned;
    private boolean waiting;

    /**
     * @param xCoord               is the X coordinate of floor following thief.
     * @param yCoord               is the Y coordinate of floor following thief.
     * @param direction            is the direction in which the floor following thief will be facing.
     * @param colourOfTileAssigned is the colour of the tile the thief is assigned to.
     */
    public FloorFollowingThief(int xCoord, int yCoord, String direction, String colourOfTileAssigned) {

        super("FloorFollowingThief", xCoord, yCoord, direction);
        this.colourOfTileAssigned = colourOfTileAssigned;
        this.waiting = false;
    }

    /**
     * @param colourOfTileAssigned is the colour of the tile assigned to the thief.
     */
    public void setColourOfTileAssigned(String colourOfTileAssigned) {
        this.colourOfTileAssigned = colourOfTileAssigned;
    }

    /**
     * @return the colour of the tile.
     */
    public String getColourOfTileAssigned() {
        return colourOfTileAssigned;
    }

    @Override
    public void move(String directionFace, Board theBoard, Tile[][] theBoardUI, ArrayList<Item> itemArrayList) {

    }

    /**
     * Checks Tiles in the order left, straight, right moving to the tile if it contains the correct colour
     * Resets search after finding first valid move
     *
     * @param directionFaced the direction the NPC is facing
     * @param theBoard       the backend representation of the board
     * @param theBoardUI     the front end representation of the board
     * @param curGameState   the container class that holds the games data
     */
    @Override
    public void move(String directionFaced, Board theBoard, Tile[][] theBoardUI, GameState curGameState) {
        theBoard.getTile(getxCoord(), getyCoord()).setOccupyingCharacter(null);
        switch (directionFaced) {
            case "n":
                //if check west
                if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord() - 1, getyCoord())) {
                    setDirection("w");
                    incrementX(-1);
                    this.getImageView().setRotate(180);
                } else if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord(), getyCoord() - 1)) { //else check north
                    incrementY(-1);
                    this.getImageView().setRotate(270);
                } else if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord() + 1, getyCoord())) { //else check east
                    setDirection("e");
                    incrementX(1);
                    this.getImageView().setRotate(0);
                } else if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord(), getyCoord() + 1)) { //else check south
                    setDirection("s");
                    incrementY(+1);
                    this.getImageView().setRotate(90);
                }
                this.getImageView().setX(theBoardUI[0][0].getTranslateX() + getxCoord() * 50);
                this.getImageView().setY(theBoardUI[0][0].getTranslateY() + getyCoord() * 50);
                break;
            case "s":
                //if check east
                if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord() + 1, getyCoord())) {
                    setDirection("e");
                    incrementX(1);
                    this.getImageView().setRotate(0);
                } else if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord(), getyCoord() + 1)) { //else check south
                    incrementY(1);
                    this.getImageView().setRotate(90);
                } else if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord() - 1, getyCoord())) { //else check west
                    setDirection("w");
                    incrementX(-1);
                    this.getImageView().setRotate(180);
                } else if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord(), getyCoord() - 1)) { //else check north
                    setDirection("n");
                    incrementY(-1);
                    this.getImageView().setRotate(270);
                }
                this.getImageView().setX(theBoardUI[0][0].getTranslateX() + getxCoord() * 50);
                this.getImageView().setY(theBoardUI[0][0].getTranslateY() + getyCoord() * 50);
                break;
            case "e":
                //if check north
                if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord(), getyCoord() - 1)) {
                    setDirection("n");
                    incrementY(-1);
                    this.getImageView().setRotate(270);
                } else if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord() + 1, getyCoord())) { //else check east
                    incrementX(1);
                    this.getImageView().setRotate(0);
                } else if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord(), getyCoord() + 1)) { //else check south
                    setDirection("s");
                    incrementY(1);
                    this.getImageView().setRotate(90);
                } else if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord() - 1, getyCoord())) { //else check west
                    setDirection("w");
                    incrementX(-1);
                    this.getImageView().setRotate(180);
                }
                this.getImageView().setX(theBoardUI[0][0].getTranslateX() + getxCoord() * 50);
                this.getImageView().setY(theBoardUI[0][0].getTranslateY() + getyCoord() * 50);
                break;
            case "w":
                //if check south
                if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord(), getyCoord() + 1)) {
                    setDirection("s");
                    incrementY(1);
                    this.getImageView().setRotate(90);
                } else if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord() - 1, getyCoord())) { //else check west
                    incrementX(-1);
                    this.getImageView().setRotate(180);
                } else if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord(), getyCoord() - 1)) { //else check north
                    setDirection("n");
                    incrementY(-1);
                    this.getImageView().setRotate(270);
                } else if (checkFloorFollowingThiefMoveColour(theBoard, getxCoord() + 1, getyCoord())) { //else check east
                    setDirection("e");
                    incrementX(+1);
                    this.getImageView().setRotate(0);
                }
                this.getImageView().setX(theBoardUI[0][0].getTranslateX() + getxCoord() * 50);
                this.getImageView().setY(theBoardUI[0][0].getTranslateY() + getyCoord() * 50);
                break;
            default:
                System.out.println("Not valid direction: " + directionFaced);
                break;
        }
        theBoard.getTile(getxCoord(), getyCoord()).setOccupyingCharacter(this);
    }

    /**
     * Checks whether moving to the given Tile would be valid and if there's a collectable item interact with it
     *
     * @param theBoard the backend representation of the board
     * @param checkX Y coordinate of Tile to compare to
     * @param checkY Y coordinate of Tile to compare to
     * @return Whether the move would be valid
     */
    private boolean checkFloorFollowingThiefMoveColour(Board theBoard, int checkX, int checkY) {
        try {
            if (theBoard.getTile(checkX, checkY).getOccupyingCharacter() != null
                    || theBoard.getTile(checkX, checkY).doesHoldPlayer()) {
                return false;
            }
            Item checkTileItem = theBoard.getTile(checkX, checkY).getOccupyingItem();
            if (checkTileItem != null) {
                if (!(checkTileItem.getItemType().equals("Loot") || checkTileItem.getItemType().equals("Clock")
                        || checkTileItem.getItemType().equals("Lever"))) {
                    return false;
                } else if (checkTileItem.getItemType().equals("Door")) {
                    if (!((Door) checkTileItem).isOpen()) {
                        return false;
                    }
                }
            }
            String[] nextTileColours = theBoard.getTile(checkX, checkY).getColour().split("");
            for (String nextColour : nextTileColours) {
                if (colourOfTileAssigned.equals(nextColour)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * String information of door to write to file
     * @return string that can be used to write to txt file
     */
    @Override
    public String getStringForFile() {
        return "FloorFollowingThief " + getxCoord() + "," + getyCoord() + " " + getDirection() +
                " " + getColourOfTileAssigned();
    }

    /**
     * String information of the FloorFollowingThief
     * @return string that identifies all floor following thieves
     */
    @Override
    public String getNPCType() {
        return "FloorFollowingThief";
    }
}

