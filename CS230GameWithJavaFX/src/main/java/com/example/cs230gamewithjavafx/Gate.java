package com.example.cs230gamewithjavafx;

import javafx.scene.image.Image;
/**
 *  A player and a gate cannot be on the same tile
   * When the corresponding lever is collected,
   * gate is removed. Player can then occupy that tile.
 * @author Euan Phillips
 * @version 1.0
 * Subclass of item

 */

public class Gate extends Item {
    /**
     * the colour the gate represents and is locked behind
     */
    String colour;
    /**
     * Whether the gate is open or not
     */
    boolean isOpen;

    /** Creates a gate
     * @param xCoord   place on X-axis of board
     * @param yCoord   place on Y-axis of board
     * @param isOpen whether the gate is open or closed
     * @param colour the colour the gate represents
     */
    public Gate(int xCoord, int yCoord, boolean isOpen, String colour) {
        super("Gate",xCoord, yCoord);
        this.colour = colour;
        this.isOpen = isOpen;
        setImageItem(colour);
    }

    /**
     * sets the imageview for the gate based on what colour it is
     * @param colour the colour of the gate
     */
    @Override
    protected void setImageItem(String colour){
        this.getImageView().setVisible(false); //Old default image
        this.getImageView().setImage(new Image("file:CS230GameWithJavaFX/src/main/resources/Items/Gate"  + colour + ".png")); //New image
        this.getImageView().setVisible(true); //Made visible
    }

    /**
     * Gets colour of gate as a string
     * @return colour of gate
     */
    public String getColour() {
        return colour;
    }

    /**
     * Sets colour
     * @param colour of gate
     */
    public void setColour(String colour) {
        this.colour = colour;
    }

    /**
     * Checks if the gate is open
     * @return true if open, else false
     */
    public boolean isOpen() {
        return isOpen;
    }

    /**
     * String information of gate
     * @return String
     */
    @Override
    public String getStringForFile() {
        return "Gate " + getxCoord() + "," + getyCoord()
                + " " + isOpen() + " " + getColour();
    }

    /**
     * @return the type of the item, used for comparisons
     */
    @Override
    public String getItemType() {
        return "Gate";
    }
}
