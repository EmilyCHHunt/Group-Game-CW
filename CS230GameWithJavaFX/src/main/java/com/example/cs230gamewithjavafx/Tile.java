package com.example.cs230gamewithjavafx;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Tile represents each tile in the loaded level
   * Holds the colour of the tile and the item and character on the tile
 * @author Emily Hunt
 * @version 1.0.0
 */
public class Tile extends StackPane {
    /**
     * the Tiles 4 colours
     */
    private String colour;
    /**
     * the item occupying the location
     */
    private Item occupyingItem;
    /**
     * the npc occupying the location
     */
    private NPC occupyingNPC;
    /**
     * if a player is on the tile or not
     */
    private boolean holdsPlayer;

    /**
     * Constructs the Tile and its UI
     * @param colour 4 Char string representation of tile colours
     * @param x Y Coordinate in board
     * @param y Y Coordinate in board
     * @param width Pixel width of Tile UI
     */
    public Tile(String colour, double x, double y, double width) {
        this.colour = colour;
        holdsPlayer = false;

        double height = width;
        String[][] colourList = new String[2][2];
        colourList[0] = colour.substring(0, 2).split("");
        colourList[1] = colour.substring(2, 4).split("");


        // create square border
        Rectangle rectangle = new Rectangle(width, height);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(5);

        // set position
        setTranslateX(x);
        setTranslateY(y);

        getChildren().add(rectangle);

        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                Rectangle subRectangle = new Rectangle((width / 2), (height / 2));
                subRectangle.setTranslateX(j * (width / 4));
                subRectangle.setTranslateY(i * (height / 4));

                switch (colourList[(int) (i / 2.0 + 0.5)][(int) (j / 2.0 + 0.5)]) {
                    case "R" -> subRectangle.setFill(Color.MAROON);
                    case "G" -> subRectangle.setFill(Color.LAWNGREEN);
                    case "Y" -> subRectangle.setFill(Color.DARKORANGE);
                    case "B" -> subRectangle.setFill(Color.CADETBLUE);
                    case "C" -> subRectangle.setFill(Color.TEAL);
                    case "M" -> subRectangle.setFill(Color.HOTPINK);
                    default -> subRectangle.setFill(Color.SADDLEBROWN);
                }
                getChildren().add(subRectangle);
            }
        }
    }

    /**
     * @return The colour of the Tile
     */
    public String getColour() {
        return colour;
    }

    /**
     * @param colour The colour code to give the Tile
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * @return The Item currently on the Tile
     */
    public Item getOccupyingItem() {
        return occupyingItem;
    }

    /**
     * @param occupyingItem The Item currently on the Tile
     */
    public void setOccupyingItem(Item occupyingItem) {
        this.occupyingItem = occupyingItem;
    }

    /**
     * @return The Character currently on the Tile
     */
    public NPC getOccupyingCharacter() {
        return occupyingNPC;
    }

    /**
     * @param occupyingNPC The Character currently on the Tile
     */
    public void setOccupyingCharacter(NPC occupyingNPC) {
        this.occupyingNPC = occupyingNPC;
    }

    /**
     * @return The tile as a formatted string
     */
    @Override
    public String toString() {
        String outputString = "Colour code: " + colour;
        if (occupyingNPC != null) {
            outputString += "\nOccupying character: " + occupyingNPC;
        }
        if (occupyingItem != null) {
            outputString += "\nOccupying Item: " + occupyingItem;
        }
        return outputString;
    }

    /**
     * @return Player is on Tile
     */
    public boolean doesHoldPlayer() {
        return holdsPlayer;
    }

    /**
     * @param holdsPlayer Whether Player Is On Tile
     */
    public void holdsPlayer(boolean holdsPlayer) {
        this.holdsPlayer = holdsPlayer;
    }
}




